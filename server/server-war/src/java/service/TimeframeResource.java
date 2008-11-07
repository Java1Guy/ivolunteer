/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.QueryParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import com.sun.jersey.api.core.ResourceContext;
import javax.ws.rs.WebApplicationException;
import javax.persistence.NoResultException;
import javax.persistence.EntityManager;
import java.util.Collection;
import persistence.Filter;
import converter.TimeframeConverter;
import persistence.Timeframe;

/**
 *
 * @author dave
 */

public class TimeframeResource {
    @Context
    protected UriInfo uriInfo;
    @Context
    protected ResourceContext resourceContext;
    protected String id;
  
    /** Creates a new instance of TimeframeResource */
    public TimeframeResource() {
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get method for retrieving an instance of Timeframe identified by id in XML format.
     *
     * @param id identifier for the entity
     * @return an instance of TimeframeConverter
     */
    @GET
    @Produces({"application/xml", "application/json"})
    public TimeframeConverter get(@QueryParam("expandLevel")
    @DefaultValue("1")
    int expandLevel) {
        PersistenceService persistenceSvc = PersistenceService.getInstance();
        try {
            persistenceSvc.beginTx();
            return new TimeframeConverter(getEntity(), uriInfo.getAbsolutePath(), expandLevel);
        } finally {
            PersistenceService.getInstance().close();
        }
    }

    /**
     * Put method for updating an instance of Timeframe identified by id using XML as the input format.
     *
     * @param id identifier for the entity
     * @param data an TimeframeConverter entity that is deserialized from a XML stream
     */
    @PUT
    @Consumes({"application/xml", "application/json"})
    public void put(TimeframeConverter data) {
        PersistenceService persistenceSvc = PersistenceService.getInstance();
        try {
            persistenceSvc.beginTx();
            EntityManager em = persistenceSvc.getEntityManager();
            updateEntity(getEntity(), data.resolveEntity(em));
            persistenceSvc.commitTx();
        } finally {
            persistenceSvc.close();
        }
    }

    /**
     * Delete method for deleting an instance of Timeframe identified by id.
     *
     * @param id identifier for the entity
     */
    @DELETE
    public void delete() {
        PersistenceService persistenceSvc = PersistenceService.getInstance();
        try {
            persistenceSvc.beginTx();
            deleteEntity(getEntity());
            persistenceSvc.commitTx();
        } finally {
            persistenceSvc.close();
        }
    }

    /**
     * Returns an instance of Timeframe identified by id.
     *
     * @param id identifier for the entity
     * @return an instance of Timeframe
     */
    protected Timeframe getEntity() {
        EntityManager em = PersistenceService.getInstance().getEntityManager();
        try {
            return (Timeframe) em.createQuery("SELECT e FROM Timeframe e where e.id = :id").setParameter("id", id).getSingleResult();
        } catch (NoResultException ex) {
            throw new WebApplicationException(new Throwable("Resource for " + uriInfo.getAbsolutePath() + " does not exist."), 404);
        }
    }

    /**
     * Updates entity using data from newEntity.
     *
     * @param entity the entity to update
     * @param newEntity the entity containing the new data
     * @return the updated entity
     */
    protected Timeframe updateEntity(Timeframe entity, Timeframe newEntity) {
        EntityManager em = PersistenceService.getInstance().getEntityManager();
        Collection<Filter> filterCollection = entity.getFilterCollection();
        Collection<Filter> filterCollectionNew = newEntity.getFilterCollection();
        entity = em.merge(newEntity);
        for (Filter value : filterCollection) {
            if (!filterCollectionNew.contains(value)) {
                throw new WebApplicationException(new Throwable("Cannot remove items from filterCollection"));
            }
        }
        for (Filter value : filterCollectionNew) {
            if (!filterCollection.contains(value)) {
                Timeframe oldEntity = value.getTimeframeId();
                value.setTimeframeId(entity);
                if (oldEntity != null && !oldEntity.equals(entity)) {
                    oldEntity.getFilterCollection().remove(value);
                }
            }
        }
        return entity;
    }

    /**
     * Deletes the entity.
     *
     * @param entity the entity to deletle
     */
    protected void deleteEntity(Timeframe entity) {
        EntityManager em = PersistenceService.getInstance().getEntityManager();
        if (!entity.getFilterCollection().isEmpty()) {
            throw new WebApplicationException(new Throwable("Cannot delete entity because filterCollection is not empty."));
        }
        em.remove(entity);
    }

    /**
     * Returns a dynamic instance of FiltersResource used for entity navigation.
     *
     * @param id identifier for the parent entity
     * @return an instance of FiltersResource
     */
    @Path("filterCollection/")
    public FiltersResource getFilterCollectionResource() {
        FilterCollectionResourceSub resource = resourceContext.getResource(FilterCollectionResourceSub.class);
        resource.setParent(getEntity());
        return resource;
    }

    public static class FilterCollectionResourceSub extends FiltersResource {

        private Timeframe parent;

        public void setParent(Timeframe parent) {
            this.parent = parent;
        }

        @Override
        protected Collection<Filter> getEntities(int start, int max, String query) {
            Collection<Filter> result = new java.util.ArrayList<Filter>();
            int index = 0;
            for (Filter e : parent.getFilterCollection()) {
                if (index >= start && (index - start) < max) {
                    result.add(e);
                }
                index++;
            }
            return result;
        }
    }
}
