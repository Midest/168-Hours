package me.midest.hours168.hibernatepersistence.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;

@FunctionalInterface
public interface QuerySupplier {

    Query get( Session session, String query );

}
