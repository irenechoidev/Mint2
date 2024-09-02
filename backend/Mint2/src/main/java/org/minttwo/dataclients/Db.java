package org.minttwo.dataclients;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Db {
    private final SessionFactory sessionFactory;
    private Session currentSession;

    public Db(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getCurrentSession() {
        if (currentSession == null) {
            this.currentSession = sessionFactory.openSession();
        }

        return currentSession;
    }
}
