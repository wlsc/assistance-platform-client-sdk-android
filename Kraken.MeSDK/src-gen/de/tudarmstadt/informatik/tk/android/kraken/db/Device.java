package de.tudarmstadt.informatik.tk.android.kraken.db;

import de.tudarmstadt.informatik.tk.android.kraken.db.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "device".
 */
public class Device {

    private Long id;
    private String device_identifier;
    private String os;
    private String os_version;
    private String brand;
    private String model;
    /** Not-null value. */
    private String created;
    private Long login_id;
    private Long user_id;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient DeviceDao myDao;

    private Login login;
    private Long login__resolvedKey;

    private User user;
    private Long user__resolvedKey;


    public Device() {
    }

    public Device(Long id) {
        this.id = id;
    }

    public Device(Long id, String device_identifier, String os, String os_version, String brand, String model, String created, Long login_id, Long user_id) {
        this.id = id;
        this.device_identifier = device_identifier;
        this.os = os;
        this.os_version = os_version;
        this.brand = brand;
        this.model = model;
        this.created = created;
        this.login_id = login_id;
        this.user_id = user_id;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDeviceDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDevice_identifier() {
        return device_identifier;
    }

    public void setDevice_identifier(String device_identifier) {
        this.device_identifier = device_identifier;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOs_version() {
        return os_version;
    }

    public void setOs_version(String os_version) {
        this.os_version = os_version;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    /** Not-null value. */
    public String getCreated() {
        return created;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCreated(String created) {
        this.created = created;
    }

    public Long getLogin_id() {
        return login_id;
    }

    public void setLogin_id(Long login_id) {
        this.login_id = login_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    /** To-one relationship, resolved on first access. */
    public Login getLogin() {
        Long __key = this.login_id;
        if (login__resolvedKey == null || !login__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LoginDao targetDao = daoSession.getLoginDao();
            Login loginNew = targetDao.load(__key);
            synchronized (this) {
                login = loginNew;
            	login__resolvedKey = __key;
            }
        }
        return login;
    }

    public void setLogin(Login login) {
        synchronized (this) {
            this.login = login;
            login_id = login == null ? null : login.getId();
            login__resolvedKey = login_id;
        }
    }

    /** To-one relationship, resolved on first access. */
    public User getUser() {
        Long __key = this.user_id;
        if (user__resolvedKey == null || !user__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
            	user__resolvedKey = __key;
            }
        }
        return user;
    }

    public void setUser(User user) {
        synchronized (this) {
            this.user = user;
            user_id = user == null ? null : user.getId();
            user__resolvedKey = user_id;
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