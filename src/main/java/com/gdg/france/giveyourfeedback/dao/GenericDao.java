package com.gdg.france.giveyourfeedback.dao;

import com.google.appengine.api.datastore.ReadPolicy;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;

import java.util.List;
import java.util.Map;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Generic DAO for CRUD Operations
 *
 * @param <T> must be an Entity Objectify
 */
public class GenericDao<T> {

    private Class<T> typeParameterClass;

    protected GenericDao() {
    }

    public GenericDao(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
        ObjectifyService.register(typeParameterClass);
    }

    /**
     * return the entire list of Entity
     */
    public List<T> getEntities() {
        return ofy().load().type(this.typeParameterClass).list();
    }

    /**
     * return the entire list of Sorted
     */
    public List<T> getEntitiesSortedByLimited(String sortParam, int limit) {
        return ofy().load().type(this.typeParameterClass).order(sortParam).limit(limit).list();
    }

    /**
     * return the entire list of Entity
     */
    public List<T> getEntities(Map<String, Object> keyValues) {
        final LoadType<T> type = ofy().load().type(this.typeParameterClass);

        Query<T> currentQuery = type;
        for (Map.Entry<String, Object> entry : keyValues.entrySet()) {
            currentQuery = currentQuery.filter(entry.getKey(), entry.getValue());
        }

        return currentQuery.list();
    }

    /**
     * return the entire list of Entity
     */
    public List<T> getEntitiesSortedByLimited(Map<String, Object> keyValues, String sortParam, int limit) {
        final LoadType<T> type = ofy().load().type(this.typeParameterClass);

        Query<T> currentQuery = type;
        for (Map.Entry<String, Object> entry : keyValues.entrySet()) {
            currentQuery = currentQuery.filter(entry.getKey(), entry.getValue());
        }
        //Sort
        currentQuery = currentQuery.order(sortParam);
        //Limit
        currentQuery = currentQuery.limit(limit);

        return currentQuery.list();
    }

    /**
     * return the entity filtered by one criteria
     */
    public T getEntity(String column, String value) {
        return ofy().load().type(this.typeParameterClass).filter(column, value).first().now();
    }

    /**
     * return the entity filtered by a list of criteria
     */
    public T getEntity(Map<String, Object> keyValues) {
        final LoadType<T> type = ofy().consistency(ReadPolicy.Consistency.STRONG).load().type(this.typeParameterClass);

        Query<T> currentQuery = type;
        for (Map.Entry<String, Object> entry : keyValues.entrySet()) {
            currentQuery = currentQuery.filter(entry.getKey(), entry.getValue());
        }

        return currentQuery.first().now();
    }

    /**
     * return the entire list of Entity
     */
    public T getEntityById(long id) {
        return ofy().load().type(this.typeParameterClass).id(id).now();
    }

    /**
     * Insert one Entity
     * Synchronous insert to get the ID
     *
     * @param object entity to save
     */
    public void insertEntity(T object) {
        ofy().save().entity(object).now();
    }

    /**
     * Insert Entity
     * Synchronous insert to get the ID
     *
     * @param objects entities to save
     */
    public void insertEntities(List<T> objects) {
        ofy().save().entities(objects).now();
    }

    /**
     * Delete one entity
     *
     * @param id the id Entity to delete
     */
    public void deleteEntity(Long id) {
        ofy().delete().type(this.typeParameterClass).id(id);
    }

    /**
     * Delete one entity
     *
     * @param id the id Entity to delete
     */
    public void deleteEntity(String id) {
        ofy().delete().type(this.typeParameterClass).id(id);
    }

    /**
     * Delete one entity
     *
     * @param entities the entities to delete
     */
    public void deleteEntities(List<T> entities) {
        ofy().delete().entities(entities);
    }

}
