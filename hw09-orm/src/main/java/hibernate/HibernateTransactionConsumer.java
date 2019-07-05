package hibernate;

import org.hibernate.Session;

@FunctionalInterface
public interface HibernateTransactionConsumer <T>{
    void perform (Session session, T obj);
}