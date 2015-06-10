package de.tudarmstadt.informatik.tk.kraken.android.sdk.communication;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.greenrobot.dao.AbstractDao;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.api.RetroApiManager;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.api.entities.ApiMessage;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.api.entities.ApiResponse;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.interfaces.IDbSensor;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.interfaces.IDbUpdatableSensor;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.sensors.interfaces.ISensor;
import de.tudarmstadt.informatik.tk.kraken.android.sdk.services.KrakenService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RetroServerPushManager {

    private static final String TAG = "RetroServerPushManager";

	private static final int PERIODIC_PUSH_DELAY_IN_MIN = 0;
	private static final int PERIODIC_PUSH_PERIOD_IN_MIN = 1;

    private static RetroServerPushManager mInstance;

	private HashSet<ISensor> mSensorsImmediate = new HashSet<ISensor>();
    private HashMap<ISensor, Long> mSensorsPeriodic = new HashMap<ISensor, Long>();
	private HashSet<ISensor> mSensorsWlan = new HashSet<ISensor>();
	private List<ApiMessage.DataWrapper> mCache = new LinkedList<>();
	private List<ApiMessage.DataWrapper> mCacheWlan = new LinkedList<>();
	private boolean mIsWlanConnected = false;

	private static Future<?> mFuture;
	protected ScheduledExecutorService mScheduledTaskExecutor;
	private static Context mContext;

	public static RetroServerPushManager getInstance(Context ctx) {
		if (mInstance == null) {
			mInstance = new RetroServerPushManager();
		}

		if (mFuture == null)
			mInstance.startPeriodicPush();

		mInstance.mContext = ctx.getApplicationContext();
		return mInstance;
	}

	private RetroServerPushManager() {
		mScheduledTaskExecutor = Executors.newSingleThreadScheduledExecutor();
	}

	public void setWlanConnected(boolean bConnected) {
		if (bConnected) {
			mIsWlanConnected = true;
			stopPeriodicPush();
            flushAll();
		} else {
			mIsWlanConnected = false;
			startPeriodicPush();
		}
	}

	private void startPeriodicPush() {
        Log.d(TAG, "startPeriodicPush");
		if (mFuture == null) {
			mFuture = mScheduledTaskExecutor.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
                    Log.d(TAG, "periodic flush");
					flushData(EPushType.PERIODIC);
				}
			}, PERIODIC_PUSH_DELAY_IN_MIN, PERIODIC_PUSH_PERIOD_IN_MIN, TimeUnit.MINUTES);
		}
	}

	public static void stopPeriodicPush() {
        Log.d(TAG, "startPeriodicPush");
		if (mFuture != null)
		{
			mFuture.cancel(true);
			mFuture = null;
		}
	}

    // TODO Wäre ein if (m_setImmediate.contains(sensor) || m_bIsWlanConnected) hier sinnvoller?
    // Es müsste dann nicht bei jedem inform jeder Sensor abgefragt werden. (Einige Queries werden gespart)

    // TODO: Was passiert, wenn ein MANUAL PushSensor hier informt?
	public void inform(ISensor sensor) {
        Log.d(TAG, "inform: " + sensor.getSensorType().getSensorName());
		if (mSensorsImmediate.contains(sensor)) {
			flushData(sensor);
		} else if (mIsWlanConnected) {
			flushAll();
		}
	}

	@SuppressWarnings("incomplete-switch")
	public void setPushType(ISensor sensor, EPushType type) {
		switch (type) {
		case IMMEDIATE:
			mSensorsPeriodic.remove(sensor);
			mSensorsWlan.remove(sensor);
			mSensorsImmediate.add(sensor);
			break;
		case PERIODIC:
			mSensorsImmediate.remove(sensor);
			mSensorsWlan.remove(sensor);
			mSensorsPeriodic.put(sensor, null);
			break;
		case WLAN_ONLY:
			mSensorsImmediate.remove(sensor);
			mSensorsPeriodic.remove(sensor);
			mSensorsWlan.add(sensor);
			break;
		}
	}

	private void flushData(ISensor sensor) {
        Log.d(TAG, "flushData: " + sensor.getSensorType().getSensorName());

        String kroken = SdkAuthentication.getInstance(mContext).getKroken();
        if (kroken == null)
			return;

        List<ApiMessage.DataWrapper> dataWrappers = new LinkedList<>();
        ApiMessage.DataWrapper dataWrapper = sensor.flushDataRetro();
        if(dataWrapper != null) {
            dataWrappers.add(dataWrapper);
            sendSensorData(dataWrappers);
        }
    }

	public void flushAll() {
		flushData(EPushType.ALL);
	}

	private void flushData(EPushType type) {
        Log.d(TAG, "flushData: " + type.name());
        String kroken = SdkAuthentication.getInstance(mContext).getKroken();
        if (kroken == null)
            return;

        List<ApiMessage.DataWrapper> dataWrappers = new LinkedList<>();
		buildSensorsDataArray(type, dataWrappers);
		sendSensorData(dataWrappers);
	}

	private void sendSensorData(final List<ApiMessage.DataWrapper> dataWrappers) {
        if (dataWrappers == null) {
            return;
        }

        // add cached data!
        addCachedDataToArray(dataWrappers);

        if(dataWrappers.size() == 0) {
            return;
        }

        RetroApiManager.getInstance(mContext).postData(dataWrappers, new Callback<ApiResponse.BundleApiResponse>() {
            @Override
            public void success(ApiResponse.BundleApiResponse apiResponse, Response response) {
                if(apiResponse.succ) {
                    int i = 0;
                    for (ApiResponse.SingleApiResponse singleApiResponse : apiResponse.data.data) {
                        if(singleApiResponse.succ) {
                            // remove from DB
                            ApiMessage.DataWrapper wrapper = dataWrappers.get(i);
                            if(wrapper != null && wrapper.objs != null && !wrapper.objs.isEmpty()) {
                                removeDataFromDb(wrapper.objs, wrapper.databaseClassName);
                            }
                        }
                        else {
                            if(singleApiResponse.error != null) {
                                Log.d("RetroServerPushManager", singleApiResponse.error.cause + " " + singleApiResponse.error.msg);
                            }
                            //addToCache();
                        }
                        i++;
                    }
                }
                else {
                    if(apiResponse.error != null) {
                        Log.d("RetroServerPushManager", apiResponse.error.cause + " " + apiResponse.error.msg);
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d("RetroServerPushManager", error.toString());
            }
        });

    }

    @SuppressWarnings("incomplete-switch")
	private void buildSensorsDataArray(EPushType type, List<ApiMessage.DataWrapper> dataWrappers) {
		switch (type) {
            case IMMEDIATE:
                addSensorsToArray(mSensorsImmediate, dataWrappers);
                break;
            case PERIODIC:
                addPeriodicPushingSensorsToArray(mSensorsPeriodic, dataWrappers);
                break;
            case WLAN_ONLY:
                addSensorsToArray(mSensorsWlan, dataWrappers);
                break;
            case ALL: {
                addSensorsToArray(mSensorsImmediate, dataWrappers);
                addSensorsToArray(mSensorsPeriodic.keySet(), dataWrappers);
                addSensorsToArray(mSensorsWlan, dataWrappers);
                break;
            }
		}
	}

	private void addSensorsToArray(Set<ISensor> set, List<ApiMessage.DataWrapper> dataWrappers) {
		for (ISensor sensor : set) {
            ApiMessage.DataWrapper data = sensor.flushDataRetro();
            if (data != null) {
                dataWrappers.add(data);
            }
        }
	}

	private void addPeriodicPushingSensorsToArray(HashMap<ISensor, Long> map, List<ApiMessage.DataWrapper> dataWrappers) {

		long longCurrentTimestamp = Calendar.getInstance().getTimeInMillis();

        for (Map.Entry<ISensor, Long> entry : map.entrySet()) {
            ISensor sensor = entry.getKey();
            Long lastSensorPush = entry.getValue();
            if (lastSensorPush == null) {
                entry.setValue(longCurrentTimestamp);
                continue;
            }

            int intPushIntervall = sensor.getPushIntervalInMin();
            long longPushIntervall = intPushIntervall * 60 * 1000;

            if (lastSensorPush + longPushIntervall < longCurrentTimestamp) {
                ApiMessage.DataWrapper data = sensor.flushDataRetro();
                if (data != null) {
                    dataWrappers.add(data);
                }
                entry.setValue(longCurrentTimestamp);
            }
        }
        Log.d(TAG, "addPeriodicPushingSensorsToArray: " + dataWrappers.size());
	}

	private void addCachedDataToArray(List<ApiMessage.DataWrapper> dataWrappers) {
        Log.d(TAG, "addCachedDataToArray before: " + dataWrappers.size());
		for (ApiMessage.DataWrapper data : mCache) {
			dataWrappers.add(data);
		}
		mCache.clear();

		if (mIsWlanConnected) {
			for (ApiMessage.DataWrapper data : mCacheWlan) {
				dataWrappers.add(data);
			}
			mCacheWlan.clear();
		}
        Log.d(TAG, "addCachedDataToArray after:  " + dataWrappers.size());
	}

    /**
     * This method is invoked by Sensors which handle the sending of items by their own.
     * These are sensors with more than one database table. (e.g. calendar or contacts sensor)
     *
     * @param dataWrappers
     */
	public void flushManually(EPushType pushType, ApiMessage.DataWrapper... dataWrappers) {
        Log.d(TAG, "flushManually: " + dataWrappers.length);
        List<ApiMessage.DataWrapper> listDataWrappers = new LinkedList<>();
		if (dataWrappers.length == 0)
			return;
        for (ApiMessage.DataWrapper data : dataWrappers) {
            if (data == null)
                continue;
            if (!mIsWlanConnected && (EPushType.MANUALLY_WLAN_ONLY.equals(pushType) || EPushType.WLAN_ONLY.equals(pushType))) {
                addToCache(pushType, dataWrappers);
                return;
            }
            listDataWrappers.add(data);
        }
		//sendSensorData(Arrays.asList(dataWrappers));
		sendSensorData(listDataWrappers);
	}

	public void addToCache(EPushType pushType, ApiMessage.DataWrapper... dataWrappers) {
        Log.d(TAG, "addToCache: " + dataWrappers.length);
        for (ApiMessage.DataWrapper data : dataWrappers) {
            if (pushType.equals(EPushType.MANUALLY_IMMEDIATE) || pushType.equals(EPushType.IMMEDIATE) || pushType.equals(EPushType.PERIODIC)) {
                mCache.add(data);
            } else if (pushType.equals(EPushType.MANUALLY_WLAN_ONLY) || pushType.equals(EPushType.WLAN_ONLY)) {
                mCacheWlan.add(data);
            }
        }
	}

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void removeDataFromDb(List<? extends IDbSensor> liSensorData, String strFullqualifiedSensorClassName) {
        Log.d(TAG, "removeDataFromDb: " + liSensorData.size() + ", " + strFullqualifiedSensorClassName);

        try {

            Class<? extends IDbSensor> dbClass = (Class<? extends
                    IDbSensor>) Class.forName(strFullqualifiedSensorClassName);
            AbstractDao<? extends IDbSensor, Long> dao = (AbstractDao<?
                    extends IDbSensor, Long>) KrakenService.getInstance()
                    .getDaoSession().getDao(dbClass);

            // We assume that every entry in this list is of the same type!
            if (liSensorData != null && liSensorData.size() > 0) {
                // UpdatableSensors won't be simply deleted from database. May
                // be we have to update the flags!
                if (liSensorData.get(0) instanceof IDbUpdatableSensor) {
                    List<IDbUpdatableSensor> liDelete = new LinkedList<>();
                    List<IDbUpdatableSensor> liUpdate = new LinkedList<>();
                    for (IDbSensor data : liSensorData) {
                        IDbUpdatableSensor event = (IDbUpdatableSensor) data;
                        if (event.getIsDeleted())
                            liDelete.add(event);
                        else {
                            event.setIsNew(false);
                            event.setIsUpdated(false);
                            liUpdate.add(event);
                        }
                    }
                    dao.updateInTx((Iterable) liUpdate);
                    dao.deleteInTx((Iterable) liDelete);
                } else {
                    dao.deleteInTx((Iterable) liSensorData);
                }
            }

        } catch (ClassNotFoundException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
