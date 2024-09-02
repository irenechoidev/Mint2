package org.minttwo.dataclients;

import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class DataClient<T> {
    private final Db db;

    public DataClient(Db db) {
        this.db = db;
    }

    protected void insert(T data) {
        Transaction transaction = null;
        Session session = db.getCurrentSession();

        try {
            transaction = session.beginTransaction();
            session.persist(data);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
        } finally {
            session.close();
        }
    }
}
