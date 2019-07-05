package hibernate;

import org.hibernate.Session;

@FunctionalInterface
public interface HibernateTransactionFunction <T, R>{
    R apply (Session session, T obj);
}
