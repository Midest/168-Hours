package me.midest.hours168.hibernatepersistence.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SharedSessionContract;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Generic class base with CRUD operations.
 * @param <E> type of objects to process
 */
abstract class BaseCRUD<E> {

    private static final Logger logger = LoggerFactory.getLogger( BaseCRUD.class );

    @Autowired
    private SessionFactory sessionFactory;

    private final Class<E> type;

    BaseCRUD( Class<E> type ){
        this.type = type;
    }

    /**
     * Get object by its id.
     * @param id object id
     * @return object
     */
    E getByID( long id ){
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        E obj = session.get( type, id );
        session.getTransaction().commit();
        return obj;
    }

    /**
     * Save new object.
     * @param obj object
     * @return assigned id
     */
    long saveNew( E obj ){
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        long id = (long) session.save( obj );
        session.getTransaction().commit();
        return id;
    }

    /**
     * Update object.
     * @param obj object with updated fields
     */
    void update( E obj ){
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.update( obj );
        session.getTransaction().commit();
    }

    /**
     * Delete object by id.
     * @param id object id
     * @return deleted object
     */
    E delete( long id ){
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        E obj = session.get( type, id );
        if( obj != null )
            session.delete( obj );
        session.getTransaction().commit();
        return obj;
    }

    /**
     * Delete given object from database.
     * @param obj object to delete
     * @return deleted object
     */
    E delete( E obj ){
        if( obj == null )
            return null;
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.delete( obj );
        session.getTransaction().commit();
        return obj;
    }

    /**
     * Get list of object with query.
     * @param query query string
     * @return list of objects
     */
    List<E> getWithQuery( String query, QueryLang lang ){
        return getWithParamQuery( query, lang, null, -1 );
    }

    /**
     * Get list of object with query.
     * @param query query string
     * @param limit maximum number of results
     * @return list of objects
     */
    List<E> getWithQuery( String query, QueryLang lang, int limit ){
        return getWithParamQuery( query, lang, null, limit );
    }

    /**
     * Get list of object with query.
     * @param query query string
     * @param params query params
     * @return list of objects
     */
    List<E> getWithParamQuery( String query, QueryLang lang, Map<String, Object> params ) {
        return getWithParamQuery( query, lang, params, -1 );
    }

    /**
     * Get list of object with query.
     * @param query query string
     * @param params query params
     * @param limit maximum number of results
     * @return list of objects
     */
    List<E> getWithParamQuery( String query, QueryLang lang, Map<String, Object> params, int limit ) {
        QuerySupplier sup;
        switch( lang ) {
            default:
            case HQL: sup = SharedSessionContract::createQuery; break;
            case SQL: sup = SharedSessionContract::createNativeQuery; break;
        }
        return getWithParamQuery( sup, query, params, limit );
    }

    List getAnyTypeWithParamQuery( String query, QueryLang lang, Map<String, Object> params, int limit ) {
        QuerySupplier sup;
        switch( lang ) {
            default:
            case HQL: sup = SharedSessionContract::createQuery; break;
            case SQL: sup = SharedSessionContract::createNativeQuery; break;
        }
        return getAnyTypeWithParamQuery( sup, query, params, limit );
    }

    @SuppressWarnings( "unchecked" )
    private List<E> getWithParamQuery( QuerySupplier querySupplier, String query, Map<String, Object> params, int limit ){
        return getAnyTypeWithParamQuery( querySupplier, query, params, limit );
    }

    private List getAnyTypeWithParamQuery( QuerySupplier querySupplier, String query, Map<String, Object> params, int limit ){
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        final Query q = querySupplier.get( session, query );
        if( params != null ) params.forEach( q::setParameter );
        if( limit > 0 ) q.setMaxResults( limit );
        List result = q.list();
        session.getTransaction().commit();
        return result;
    }

}
