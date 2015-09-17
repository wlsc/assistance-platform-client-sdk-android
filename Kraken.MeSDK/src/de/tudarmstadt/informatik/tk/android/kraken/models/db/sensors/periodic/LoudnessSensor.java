package de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.periodic;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.util.Calendar;
import java.util.LinkedList;

import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.android.kraken.models.db.sensors.abstract_sensors.AbstractPeriodicSensor;


public class LoudnessSensor extends AbstractPeriodicSensor implements Callback {

    private static final int INIT_DATA_INTERVALL = 120;
    public static final int AUDIO_BLOCK = 0;
    public static final int PHONE_STATUS = 1;

    private PhoneListener m_phoneListener;

    private AudioRecorder m_audioRecorder;
    private CalcLeq m_leqCalc = new CalcLeq();

    private boolean m_isPaused;

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

        private LinkedList<RMSValue> sampleValues;
        public long m_startTimestamp;

        public CalcLeq() {
            this.sampleValues = new LinkedList<RMSValue>();
            m_startTimestamp = Calendar.getInstance().getTimeInMillis();
        }

        public void addRMS(RMSValue rms) {
            synchronized (sampleValues) {
                sampleValues.add(rms);
            }
        }

        public void resetValues() {
            m_startTimestamp = Calendar.getInstance().getTimeInMillis();
            synchronized (sampleValues) {
                sampleValues = new LinkedList<RMSValue>();
            }
        }

        public LeqValue calcLeq() {

            LeqValue leq = new LeqValue();

            long sum = 0;
            int sumSamples = 0;

            synchronized (sampleValues) {
                if (sampleValues.size() > 0) {
                    RMSValue rms;
                    for (int i = 0; i < sampleValues.size(); i++) {
                        rms = sampleValues.get(i);
                        sum += rms.summedSquaredSamples;
                        sumSamples += rms.samples;
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
        setDataIntervallInSec(INIT_DATA_INTERVALL);
        TelephonyManager tManager = (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);
        m_phoneListener = new PhoneListener(this);
        tManager.listen(m_phoneListener, PhoneListener.LISTEN_CALL_STATE);
    }

    @Override
    protected void dumpData() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        Bundle data = msg.getData();
        if (data.containsKey("type")) {
            if (data.getInt("type") == AUDIO_BLOCK) {
                getAudioBlock(data);
                return false;
            } else if (data.getInt("type") == PHONE_STATUS) {
                phoneStateChanged(data);
                return false;
            }
        }
        return false;
    }

    private void getAudioBlock(Bundle data) {

        short[] audioData = data.getShortArray("audioBlock");

        RMSValue rms = calcRMS(audioData);

        // add RMS to LEQ calculation
        synchronized (m_leqCalc) {
            m_leqCalc.addRMS(rms);
        }

    }

    private void phoneStateChanged(Bundle data) {
        int status = data.getInt("status");

        if ((status == TelephonyManager.CALL_STATE_RINGING || status == TelephonyManager.CALL_STATE_OFFHOOK) && !m_isPaused) {
            m_isPaused = true;
            m_audioRecorder.killThread();
        } else if (status == TelephonyManager.CALL_STATE_IDLE && m_isPaused) {
            m_isPaused = false;
            m_audioRecorder = new AudioRecorder(this);
            m_audioRecorder.setName("AudioRecorderThread");
            m_audioRecorder.start();
        }
    }

    @Override
    public ESensorType getSensorType() {
        return ESensorType.SENSOR_LOUDNESS;
    }

    @Override
    public void reset() {

    }

    @Override
    public void startSensor() {
        m_isPaused = true;
        m_audioRecorder = new AudioRecorder(this);
        m_audioRecorder.start();
        super.startSensor();
    }

    @Override
    public void stopSensor() {
        m_isPaused = false;
        super.stopSensor();
    }

    public RMSValue calcRMS(short[] data) {

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

        private LoudnessSensor m_sensor;
        private AudioRecord audioInput = null;
        private int bufferSize = (int) (44100 * (float) 0.5);
        private boolean threadKilled = false;

        public AudioRecorder(LoudnessSensor sensor) {

            this.m_sensor = sensor;

            int channel = AudioFormat.CHANNEL_IN_MONO;
            int mic = MediaRecorder.AudioSource.MIC;

            // Berechne den Puffer
            int minAudioBuffer = AudioRecord.getMinBufferSize(44100, channel, AudioFormat.ENCODING_PCM_16BIT);
            int audioBuffer = minAudioBuffer * 6;

            // Erstelle den Recorder
            audioInput = new AudioRecord(mic, 44100, channel, AudioFormat.ENCODING_PCM_16BIT, audioBuffer);
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
                e.printStackTrace();
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
                    } else if (read + offset < bufferSize) {
                        offset += read;

                        // Buffer ist voll!
                    } else {
                        offset = 0;
                        flushBuffer = true;
                    }

                    // Notify Listener!
                    if (flushBuffer) {
                        flushBuffer = false;
                        Bundle bundle = new Bundle(2);
                        synchronized (audioData) {
                            bundle.putInt("type", LoudnessSensor.AUDIO_BLOCK);
                            bundle.putShortArray("audioBlock", audioData.clone());
                        }
                        m_sensor.handleData(bundle);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
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
        if (!m_isPaused) {
            LeqValue leq = new LeqValue();
            leq = m_leqCalc.calcLeq();
            long startTimestamp = m_leqCalc.m_startTimestamp;
            m_leqCalc.resetValues();

            if (leq.samples > 0) {
//				SensorLoudness sensorLoudness = new SensorLoudness();
//				sensorLoudness.setStartTimestamp(startTimestamp);
//				sensorLoudness.setLoudness(calcDB(leq.value));
//				handleDBEntry(sensorLoudness);
            }
        }
    }

    public static float calcDB(float input) {
        if (input == 0)
            return 0;
        else {
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
