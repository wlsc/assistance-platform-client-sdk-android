package de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.impl.contentobserver;

import android.content.Context;
import android.net.Uri;

import java.lang.reflect.Method;

import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.api.DtoType;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.model.sensing.AbstractContentObserverEvent;
import de.tudarmstadt.informatik.tk.android.assistance.sdk.util.logger.Log;


/**
 * @author Karsten Planz
 */
public class BrowserHistoryEvent extends AbstractContentObserverEvent {

    private static final String TAG = BrowserHistoryEvent.class.getSimpleName();

    private static BrowserHistoryEvent INSTANCE;

    // this one was deleted in SDK 23
    //        protected static final Uri URI_BROWSER_HISTORY = Browser.BOOKMARKS_URI;
    private static final Uri URI_CHROME_BOOKMARKS = Uri.parse("content://com.android.chrome.browser/bookmarks");

    // 0 = history, 1 = bookmark
//    private static final String WHAT_TO_SELECT = Browser.BookmarkColumns.BOOKMARK + " = 0";

    private Method mMethodForGetAllExistingHistory;
    private boolean m_bFlushToServer;
    private Method m_checkDifferenceMethodForHistoryChange;
    private Method m_getKeyMethodForSensorContact;

    private BrowserHistoryEvent(Context context) {
        super(context);
    }

    /**
     * Gives singleton of this class
     *
     * @param context
     * @return
     */
    public static BrowserHistoryEvent getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = new BrowserHistoryEvent(context);
        }

        return INSTANCE;
    }

    @Override
    public void dumpData() {

    }

    @Override
    protected void syncData() {

        Log.d(TAG, "Syncing data...");

//        ContentResolver cr = context.getContentResolver();
//        Cursor cur = cr.query(URI_CHROME_BOOKMARKS, null, null, null, null);
//
//        if (cur == null) {
//            Log.d(TAG, "Cursor is null. Aborting...");
//            return;
//        }
//
//        Map<Long, DbBrowserHistoryEvent> allExistingHistory;
//        try {
//            allExistingHistory = getAllExistingHistory();
//        } catch (Exception e) {
//            Log.e(TAG, "Cannot get all history", e);
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
//                if (checkForHistoryChange(allExistingHistory, browserHistory)) {
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
//            String strFullqualifiedDatabaseClassName = getType().getFullqualifiedDatabaseClassName();
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

//    private Map<Long, DbBrowserHistoryEvent> getAllExistingHistory() throws
//            NoSuchMethodException,
//            NoSuchFieldException,
//            IllegalAccessException,
//            IllegalArgumentException,
//            InvocationTargetException {

//        if (mMethodForGetAllExistingHistory == null) {
//            mMethodForGetAllExistingHistory = DbBrowserHistoryEvent.class.getDeclaredMethod("getHistoryId");
//            mMethodForGetAllExistingHistory.setAccessible(true);
//        }
//        return getAllExistingEntries(SensorBrowserHistory.class, mMethodForGetAllExistingHistory);
//    }

//    private boolean checkForHistoryChange(HashMap<Long, SensorBrowserHistory> map, SensorBrowserHistory newSensorBrowserHistory) throws Exception {
//        try {
//            long id = newSensorBrowserHistory.getHistoryId();
//            SensorBrowserHistory existingReminder = map.get(id);
//
//            if (m_checkDifferenceMethodForHistoryChange == null || m_getKeyMethodForSensorContact == null) {
//                m_getKeyMethodForSensorContact = SensorBrowserHistory.class.getDeclaredMethod("getHistoryId");
//                m_getKeyMethodForSensorContact.setAccessible(true);
//                m_checkDifferenceMethodForHistoryChange = getClass().getDeclaredMethod("hasHistoryDifference", new Class[]{SensorBrowserHistory.class, SensorBrowserHistory.class});
//                m_checkDifferenceMethodForHistoryChange.setAccessible(true);
//            }
//            boolean result = checkForChange(map, newSensorBrowserHistory, m_getKeyMethodForSensorContact, m_checkDifferenceMethodForHistoryChange);
//            if (!result) {
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

//        Thread thread = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                syncData();
//                context.getContentResolver().registerContentObserver(URI_BROWSER_HISTORY, true, mObserver);
//            }
//        });
//
//        thread.setName("BrowserHistorySensorThread");
//        thread.start();
//
//        setRunning(true);
    }

    @Override
    public void stopSensor() {
        // TODO: implement this
        setRunning(false);
    }

    @Override
    public int getType() {
        return DtoType.BROWSER_HISTORY;
    }

    @Override
    public void reset() {

    }
}
