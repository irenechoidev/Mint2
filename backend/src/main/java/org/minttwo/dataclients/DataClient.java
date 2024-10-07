package org.minttwo.dataclients;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

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
            throw e;
        }
    }

    protected T getById(Class<T> entityClass, String id) {
        Transaction transaction = null;
        Session session = db.getCurrentSession();
        T data = null;

        try {
            transaction = session.beginTransaction();
            data = session.get(entityClass, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
        }

        return data;
    }

    protected List<T> getByField(Class<T> entityClass, String fieldName, String fieldValue) {
        Transaction transaction = null;
        Session session = db.getCurrentSession();
        List<T> data = Collections.emptyList();

        try {
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteria =builder.createQuery(entityClass);
            Root<T> root = criteria.from(entityClass);
            criteria.select(root).where(builder.equal(root.get(fieldName), fieldValue));

            data = session.createQuery(criteria).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
        }

        return data;
    }

    protected T getByUniqueField(Class<T> entityClass, String fieldName, String fieldValue) {
        Transaction transaction = null;
        Session session = db.getCurrentSession();
        T result = null;

        try {
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteria = builder.createQuery(entityClass);
            Root<T> root = criteria.from(entityClass);
            criteria.select(root).where(builder.equal(root.get(fieldName), fieldValue));

            result = session.createQuery(criteria).uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
        }

        return result;
    }
}
