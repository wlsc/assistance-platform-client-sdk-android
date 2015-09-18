package de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.contentobserver;

import android.content.Context;
import android.net.Uri;
import android.provider.Browser;

import java.lang.reflect.Method;

import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.ESensorType;
import de.tudarmstadt.informatik.tk.android.kraken.model.db.sensors.abstract_sensors.AbstractContentObserverSensor;


/**
 * @author Karsten Planz
 */
public class BrowserHistorySensor extends AbstractContentObserverSensor {

    protected static final Uri URI_BROWSER_HISTORY = Browser.BOOKMARKS_URI;
    protected static final Uri URI_CHROME_HISTORY = Uri.parse("content://com.android.chrome.browser/bookmarks");

    private Method m_methodForGetAllExistingHistory;
    private boolean m_bFlushToServer;
    private Method m_checkDifferenceMethodForHistoryChange;
    private Method m_getKeyMethodForSensorContact;

    public BrowserHistorySensor(Context context) {
        super(context);
    }

    @Override
    protected void dumpData() {

    }

    @Override
    protected void syncData() {
//        ContentResolver cr = context.getContentResolver();
//        Cursor cur = cr
//                .query(URI_BROWSER_HISTORY, null, null, null, null);
//
//        Log.d("kraken", "BrowserHistorySensor syncData");
//
//        if (cur == null)
//            return;
//
//        HashMap<Long, SensorBrowserHistory> allExistingHistory;
//        try {
//            allExistingHistory = getAllExistingHistory();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//
//        while (cur.moveToNext() && isRunning) {
//            long id = getLongByColumnName(cur, Browser.BookmarkColumns._ID);
//            String title = getStringByColumnName(cur, Browser.BookmarkColumns.TITLE);
//            String url = getStringByColumnName(cur, Browser.BookmarkColumns.URL);
//            boolean bookmark = getBoolByColumnName(cur, Browser.BookmarkColumns.BOOKMARK);
//            long created = getLongByColumnName(cur, Browser.BookmarkColumns.CREATED);
//            long lastVisited = getLongByColumnName(cur, Browser.BookmarkColumns.DATE);
//            int visits = getIntByColumnName(cur, Browser.BookmarkColumns.VISITS);
//            //byte[] favicon = cur.getBlob(cur.getColumnIndex(Browser.BookmarkColumns.FAVICON));
//
//            try {
//                new URL(url);
//            } catch (MalformedURLException e) {
//                continue;
//            }
//
//            SensorBrowserHistory browserHistory = new SensorBrowserHistory(null,
//                    id, url, title, lastVisited, visits, bookmark, created, true, false, false);
//
//            //Log.d("kraken", "BrowserHistorySensor new Item");
//
//            try {
//                if (checkForHistoryChange(allExistingHistory, browserHistory))
//                {
//                    //Log.d("kraken", "BrowserHistorySensor checkForHistoryChange true");
//                    //Log.d("kraken", "BrowserHistorySensor: " + title + ", " + url + ", " + bookmark + ", " + created + ", " + lastVisited + ", " + visits);
//                    handleDBEntry(browserHistory, !browserHistory.getIsNew(), false, true);
//                    m_bFlushToServer = true;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            //int faviconSize = favicon == null? 0 : favicon.length;
//            //Log.d("kraken", title + ", " + url + ", " + bookmark + ", " + created + ", " + lastVisited + ", " + visits + ", " + faviconSize);
//        }
//
//        if (m_bFlushToServer) {
//            String strFullqualifiedDatabaseClassName = getSensorType().getFullqualifiedDatabaseClassName();
//
//            //SensorData dataBrowserHistory = flushData(mDaoSession, strFullqualifiedDatabaseClassName);
//            ApiMessage.DataWrapper dataBrowserHistory = flushDataRetro(strFullqualifiedDatabaseClassName);
//            //ServerPushManager.getInstance(context).flushManually(dataBrowserHistory);
//            RetroServerPushManager.getInstance(context).flushManually(getPushType(), dataBrowserHistory);
//            //Log.d("kraken", "BrowserHistorySensor FLUSH!");
//        }
//
//        cur.close();
    }

//    private HashMap<Long, SensorBrowserHistory> getAllExistingHistory() throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//
//        if (m_methodForGetAllExistingHistory == null)
//        {
//            m_methodForGetAllExistingHistory = SensorBrowserHistory.class.getDeclaredMethod("getHistoryId");
//            m_methodForGetAllExistingHistory.setAccessible(true);
//        }
//        return getAllExistingEntries(SensorBrowserHistory.class, m_methodForGetAllExistingHistory);
//    }

//    private boolean checkForHistoryChange(HashMap<Long, SensorBrowserHistory> map, SensorBrowserHistory newSensorBrowserHistory) throws Exception {
//        try {
//            long id = newSensorBrowserHistory.getHistoryId();
//            SensorBrowserHistory existingReminder = map.get(id);
//
//            if (m_checkDifferenceMethodForHistoryChange == null || m_getKeyMethodForSensorContact == null)
//            {
//                m_getKeyMethodForSensorContact = SensorBrowserHistory.class.getDeclaredMethod("getHistoryId");
//                m_getKeyMethodForSensorContact.setAccessible(true);
//                m_checkDifferenceMethodForHistoryChange = getClass().getDeclaredMethod("hasHistoryDifference", new Class[]{SensorBrowserHistory.class, SensorBrowserHistory.class});
//                m_checkDifferenceMethodForHistoryChange.setAccessible(true);
//            }
//            boolean result = checkForChange(map, newSensorBrowserHistory, m_getKeyMethodForSensorContact, m_checkDifferenceMethodForHistoryChange);
//            if (!result)
//            {
//                newSensorBrowserHistory.setId(existingReminder.getId());
//            }
//            return result;
//        } catch (Exception e) {
//            throw new Exception(e);
//        }
//    }

//    private boolean hasHistoryDifference(SensorBrowserHistory existingHistory, SensorBrowserHistory newHistory) {
//        if (checkForDifference(existingHistory.getTitle(), newHistory.getTitle()))
//            return true;
//        if (checkForDifference(existingHistory.getUrl(), newHistory.getUrl()))
//            return true;
//        if (checkForDifference(existingHistory.getLastVisited(), newHistory.getLastVisited()))
//            return true;
//        if (checkForDifference(existingHistory.getVisits(), newHistory.getVisits()))
//            return true;
//        return false;
//    }

    @Override
    public void startSensor() {
        isRunning = true;
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                syncData();
                context.getContentResolver().registerContentObserver(URI_BROWSER_HISTORY, true, m_observer);
            }
        });
        thread.setName("BrowserHistorySensorThread");
        thread.start();
    }

    @Override
    public ESensorType getSensorType() {
        return ESensorType.SENSOR_BROWSER_HISTORY;
    }

    @Override
    public void reset() {

    }
}
