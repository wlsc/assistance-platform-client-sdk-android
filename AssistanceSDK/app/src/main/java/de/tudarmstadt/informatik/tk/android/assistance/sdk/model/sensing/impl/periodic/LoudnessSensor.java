package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.periodic;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.db.DbLoudnessEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.dto.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractPeriodicEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.DateUtils;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;

/**
 * @author Unknown
 * @edited by Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 24.11.2015
 */
public class LoudnessSensor extends AbstractPeriodicEvent implements Callback {

    private static final String TAG = LoudnessSensor.class.getSimpleName();

    private static final int INIT_DATA_INTERVAL = 120;
    public static final int AUDIO_BLOCK = 0;
    public static final int PHONE_STATUS = 1;
    public static final int COMMON_AUDIO_FREQUENCY = 44100;

    private PhoneListener mPhoneListener;

    private AudioRecorder mAudioRecorder;
    private CalcLeq mLeqCalc = new CalcLeq();

    private boolean isPaused;

    private float currentValue;

    public class LeqValue {

        public float value = 0;
        public int samples = 0;

    }

    public class RMSValue {

        public float value = 0;
        public int samples = 0;
        public long summedSquaredSamples = 0;

    }

    public class CalcLeq {

        private List<RMSValue> sampleValues;

        public CalcLeq() {
            sampleValues = new ArrayList<>();
        }

        public void addRMS(RMSValue rms) {
            synchronized (sampleValues) {
                sampleValues.add(rms);
            }
        }

        public void resetValues() {
            synchronized (sampleValues) {
                sampleValues = new ArrayList<>();
            }
        }

        public LeqValue calcLeq() {

            LeqValue leq = new LeqValue();

            long sum = 0;
            int sumSamples = 0;

            synchronized (sampleValues) {

                if (sampleValues.size() > 0) {

                    for (RMSValue value : sampleValues) {

                        sum += value.summedSquaredSamples;
                        sumSamples += value.samples;
                    }

                    leq.value = (float) Math.sqrt((float) sum / sumSamples);
                    leq.samples = sumSamples;
                }
            }

            return leq;
        }
    }

    public LoudnessSensor(Context context) {
        super(context);

        setDataIntervalInSec(INIT_DATA_INTERVAL);
        TelephonyManager tManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        mPhoneListener = new PhoneListener(this);

        if (tManager != null) {
            tManager.listen(mPhoneListener, PhoneListener.LISTEN_CALL_STATE);
        }
    }

    @Override
    public void dumpData() {

        DbLoudnessEvent loudnessSensor = new DbLoudnessEvent();

        loudnessSensor.setLoudness(currentValue);
        loudnessSensor.setCreated(DateUtils.dateToISO8601String(new Date(), Locale.getDefault()));

        Log.d(TAG, "Insert entry");

        daoProvider.getLoudnessEventDao().insert(loudnessSensor);

        Log.d(TAG, "Finished");
    }

    @Override
    public boolean handleMessage(Message msg) {

        Bundle data = msg.getData();
        if (data.containsKey("type")) {
            if (data.getInt("type") == AUDIO_BLOCK) {
                getAudioBlock(data);
                return false;
            } else {
                if (data.getInt("type") == PHONE_STATUS) {
                    phoneStateChanged(data);
                    return false;
                }
            }
        }
        return false;
    }

    private void getAudioBlock(Bundle data) {

        if (data == null) {
            return;
        }

        short[] audioData = data.getShortArray("audioBlock");

        RMSValue rms = calcRMS(audioData);

        if (rms != null) {

            // add RMS to LEQ calculation
            synchronized (mLeqCalc) {
                mLeqCalc.addRMS(rms);
            }
        }
    }

    private void phoneStateChanged(Bundle data) {

        int status = data.getInt("status");

        if ((status == TelephonyManager.CALL_STATE_RINGING ||
                status == TelephonyManager.CALL_STATE_OFFHOOK) &&
                !isPaused) {

            isPaused = true;
            mAudioRecorder.killThread();

        } else {
            if (status == TelephonyManager.CALL_STATE_IDLE && isPaused) {

                isPaused = false;
                mAudioRecorder = new AudioRecorder(this);
                mAudioRecorder.setName("AudioRecorderThread");
                mAudioRecorder.start();
            }
        }
    }

    @Override
    public int getType() {
        return -1;
    }

    @Override
    public void reset() {

        this.currentValue = 0f;
    }

    @Override
    public void startSensor() {

        try {

            isPaused = true;
            mAudioRecorder = new AudioRecorder(this);
            mAudioRecorder.start();

            super.startSensor();

        } catch (Exception e) {
            Log.e(TAG, "Some error:", e);
        }
    }

    @Override
    public void stopSensor() {

        isPaused = false;
        super.stopSensor();
    }

    public RMSValue calcRMS(short[] data) {

        if (data == null) {
            return null;
        }

        long rms = 0;

        for (short value : data) {
            rms += value * value;
        }

        RMSValue rmsValue = new RMSValue();
        rmsValue.summedSquaredSamples = rms;
        rmsValue.value = (float) Math.sqrt((float) rms / data.length);
        rmsValue.samples = data.length;

        return rmsValue;
    }

    public class AudioRecorder extends Thread {

        private LoudnessSensor mSensor;
        private AudioRecord audioInput = null;
        private int bufferSize = (int) (COMMON_AUDIO_FREQUENCY * (float) 0.5);
        private boolean threadKilled = false;

        public AudioRecorder(LoudnessSensor sensor) {

            this.mSensor = sensor;

            int channel = AudioFormat.CHANNEL_IN_MONO;
            int mic = MediaRecorder.AudioSource.MIC;

            // Berechne den Puffer
            int minAudioBuffer = AudioRecord.getMinBufferSize(
                    COMMON_AUDIO_FREQUENCY,
                    channel,
                    AudioFormat.ENCODING_PCM_16BIT);
            int audioBuffer = minAudioBuffer * 6;

            // Erstelle den Recorder
            audioInput = new AudioRecord(
                    mic,
                    COMMON_AUDIO_FREQUENCY,
                    channel,
                    AudioFormat.ENCODING_PCM_16BIT,
                    audioBuffer);
        }

        @Override
        public void run() {

            // Warten bis Recorder bereit ist.
            int maxTimeout = 2000;
            int checkTime = 100;

            try {
                while (audioInput.getState() != AudioRecord.STATE_INITIALIZED && maxTimeout > 0) {
                    Thread.sleep(checkTime);
                    maxTimeout -= checkTime;
                }
            } catch (InterruptedException e) {
                Log.e(TAG, "Thread was interrupted: ", e);
            }

            // Prüfen, ob Recorder bereit ist
            if (audioInput.getState() != AudioRecord.STATE_INITIALIZED) {
                return;
            }

            try {
                short[] audioData = new short[bufferSize];

                int offset = 0;
                int read = 0;

                boolean flushBuffer = false;

                audioInput.startRecording();

                while (!threadKilled) {

                    // Lese Audiodaten
                    synchronized (audioData) {
                        read = audioInput.read(audioData, offset, bufferSize - offset);
                    }

                    // Fehler beim Lesen der Audiodaten
                    if (read < 0) {
                        // ERROR!
                        return;

                        // Buffer wurde ein Stück weiter vollgeschrieben
                    } else {
                        if (read + offset < bufferSize) {
                            offset += read;

                            // Buffer ist voll!
                        } else {
                            offset = 0;
                            flushBuffer = true;
                        }
                    }

                    // Notify Listener!
                    if (flushBuffer) {
                        flushBuffer = false;
                        Bundle bundle = new Bundle(2);
                        synchronized (audioData) {
                            bundle.putInt("type", LoudnessSensor.AUDIO_BLOCK);
                            bundle.putShortArray("audioBlock", audioData.clone());
                        }

                        mSensor.handleData(bundle);
                    }
                }

            } catch (Exception e) {
                Log.e(TAG, "Some error: ", e);
            } finally {
                audioInput.stop();
            }
        }

        public void killThread() {
            threadKilled = true;
        }
    }

    @Override
    protected void getData() {

        if (!isPaused) {

            LeqValue leq = new LeqValue();
            leq = mLeqCalc.calcLeq();
            mLeqCalc.resetValues();

            if (leq.samples > 0) {

                currentValue = calcDB(leq.value);

                dumpData();
            }
        }
    }

    public static float calcDB(float input) {

        if (input == 0) {
            return 0;
        } else {
            return 20 * (float) Math.log10((input / 32767));
        }
    }

    public class PhoneListener extends PhoneStateListener {

        private LoudnessSensor sensor;

        public PhoneListener(LoudnessSensor sensor) {
            this.sensor = sensor;
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (sensor == null) {
                return;
            }

            Bundle bundle = new Bundle(2);
            bundle.putInt("type", LoudnessSensor.PHONE_STATUS);
            bundle.putInt("status", state);

            sensor.handleData(bundle);
        }
    }

    public void handleData(Bundle data) {

        if (data.containsKey("type")) {
            if (data.getInt("type") == AUDIO_BLOCK) {
                getAudioBlock(data);
            } else if (data.getInt("type") == PHONE_STATUS) {
                phoneStateChanged(data);
            }
        }
    }
}
