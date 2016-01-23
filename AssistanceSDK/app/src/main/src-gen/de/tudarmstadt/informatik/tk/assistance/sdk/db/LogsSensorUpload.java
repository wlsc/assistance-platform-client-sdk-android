package de.tudarmstadt.informatik.tk.assistance.sdk.db;

import de.tudarmstadt.informatik.tk.assistance.sdk.db.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "logs_sensor_upload".
 */
public class LogsSensorUpload {

    private Long id;
    private Long startTime;
    private Long processingTime;
    private Long responseTime;
    private String networkType;
    private Integer eventsNumber;
    private Long bodySize;
    private Long userId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient LogsSensorUploadDao myDao;

    private DbUser dbUser;
    private Long dbUser__resolvedKey;


    public LogsSensorUpload() {
    }

    public LogsSensorUpload(Long id) {
        this.id = id;
    }

    public LogsSensorUpload(Long id, Long startTime, Long processingTime, Long responseTime, String networkType, Integer eventsNumber, Long bodySize, Long userId) {
        this.id = id;
        this.startTime = startTime;
        this.processingTime = processingTime;
        this.responseTime = responseTime;
        this.networkType = networkType;
        this.eventsNumber = eventsNumber;
        this.bodySize = bodySize;
        this.userId = userId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLogsSensorUploadDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Long processingTime) {
        this.processingTime = processingTime;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public Integer getEventsNumber() {
        return eventsNumber;
    }

    public void setEventsNumber(Integer eventsNumber) {
        this.eventsNumber = eventsNumber;
    }

    public Long getBodySize() {
        return bodySize;
    }

    public void setBodySize(Long bodySize) {
        this.bodySize = bodySize;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /** To-one relationship, resolved on first access. */
    public DbUser getDbUser() {
        Long __key = this.userId;
        if (dbUser__resolvedKey == null || !dbUser__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DbUserDao targetDao = daoSession.getDbUserDao();
            DbUser dbUserNew = targetDao.load(__key);
            synchronized (this) {
                dbUser = dbUserNew;
            	dbUser__resolvedKey = __key;
            }
        }
        return dbUser;
    }

    public void setDbUser(DbUser dbUser) {
        synchronized (this) {
            this.dbUser = dbUser;
            userId = dbUser == null ? null : dbUser.getId();
            dbUser__resolvedKey = userId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}