package de.tudarmstadt.informatik.tk.assistance.sdk.provider.dao;

import java.util.Collections;
import java.util.List;

import de.greenrobot.dao.AbstractDao;

/**
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 * @date 22.11.2015
 */
public abstract class CommonDaoImpl<T> implements CommonDao<T> {

    protected AbstractDao<T, Long> dao;

    public CommonDaoImpl(AbstractDao<T, Long> dao) {

        if (this.dao == null) {
            this.dao = dao;
        }
    }

    @Override
    public List<T> getAll() {
        return dao
                .queryBuilder()
                .build()
                .list();
    }

    @Override
    public List<T> getFirstN(int amount) {

        if (amount <= 0) {
            return Collections.emptyList();
        }

        return dao
                .queryBuilder()
                .limit(amount)
                .build()
                .list();
    }

    @Override
    public long insert(T dbItem) {

        if (dbItem == null) {
            return -1l;
        }

        return dao.insertOrReplace(dbItem);
    }

    @Override
    public void insert(List<T> dbItems) {

        if (dbItems == null || dbItems.isEmpty()) {
            return;
        }

        dao.insertOrReplaceInTx(dbItems);
    }

    @Override
    public void update(T dbItem) {

        if (dbItem == null) {
            return;
        }

        dao.update(dbItem);
    }

    @Override
    public void update(List<T> dbItems) {

        if (dbItems == null || dbItems.isEmpty()) {
            return;
        }

        dao.updateInTx(dbItems);
    }

    @Override
    public void delete(T dbItem) {

        if (dbItem == null) {
            return;
        }

        dao.delete(dbItem);
    }

    @Override
    public void delete(List<T> dbItems) {

        if (dbItems == null || dbItems.isEmpty()) {
            return;
        }

        dao.deleteInTx(dbItems);
    }
}
