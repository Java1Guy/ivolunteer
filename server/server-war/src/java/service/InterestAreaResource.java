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
import persistence.Event;
import java.util.Collection;
import persistence.InterestArea;
import persistence.Organization;
import persistence.Filter;
import persistence.SourceInterestMap;
import converter.InterestAreaConverter;

/**
 *
 * @author dave
 */

public class InterestAreaResource {
    @Context
    protected UriInfo uriInfo;
    @Context
    protected ResourceContext resourceContext;
    protected String id;
  
    /** Creates a new instance of InterestAreaResource */
    public InterestAreaResource() {
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get method for retrieving an instance of InterestArea identified by id in XML format.
     *
     * @param id identifier for the entity
     * @return an instance of InterestAreaConverter
     */
    @GET
    @Produces({"application/xml", "application/json"})
    public InterestAreaConverter get(@QueryParam("expandLevel")
    @DefaultValue("1")
    int expandLevel) {
        PersistenceService persistenceSvc = PersistenceService.getInstance();
        try {
            persistenceSvc.beginTx();
            return new InterestAreaConverter(getEntity(), uriInfo.getAbsolutePath(), expandLevel);
        } finally {
            PersistenceService.getInstance().close();
        }
    }

    /**
     * Put method for updating an instance of InterestArea identified by id using XML as the input format.
     *
     * @param id identifier for the entity
     * @param data an InterestAreaConverter entity that is deserialized from a XML stream
     */
    @PUT
    @Consumes({"application/xml", "application/json"})
    public void put(InterestAreaConverter data) {
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
     * Delete method for deleting an instance of InterestArea identified by id.
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
     * Returns an instance of InterestArea identified by id.
     *
     * @param id identifier for the entity
     * @return an instance of InterestArea
     */
    protected InterestArea getEntity() {
        EntityManager em = PersistenceService.getInstance().getEntityManager();
        try {
            return (InterestArea) em.createQuery("SELECT e FROM InterestArea e where e.id = :id").setParameter("id", id).getSingleResult();
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
    protected InterestArea updateEntity(InterestArea entity, InterestArea newEntity) {
        EntityManager em = PersistenceService.getInstance().getEntityManager();
        Collection<Event> eventCollection = entity.getEventCollection();
        Collection<Event> eventCollectionNew = newEntity.getEventCollection();
        Collection<Organization> organizationCollection = entity.getOrganizationCollection();
        Collection<Organization> organizationCollectionNew = newEntity.getOrganizationCollection();
        Collection<Filter> filterCollection = entity.getFilterCollection();
        Collection<Filter> filterCollectionNew = newEntity.getFilterCollection();
        Collection<SourceInterestMap> sourceInterestMapCollection = entity.getSourceInterestMapCollection();
        Collection<SourceInterestMap> sourceInterestMapCollectionNew = newEntity.getSourceInterestMapCollection();
        entity = em.merge(newEntity);
        for (Event value : eventCollection) {
            if (!eventCollectionNew.contains(value)) {
                value.getInterestAreaCollection().remove(entity);
            }
        }
        for (Event value : eventCollectionNew) {
            if (!eventCollection.contains(value)) {
                value.getInterestAreaCollection().add(entity);
            }
        }
        for (Organization value : organizationCollection) {
            if (!organizationCollectionNew.contains(value)) {
                value.getInterestAreaCollection().remove(entity);
            }
        }
        for (Organization value : organizationCollectionNew) {
            if (!organizationCollection.contains(value)) {
                value.getInterestAreaCollection().add(entity);
            }
        }
        for (Filter value : filterCollection) {
            if (!filterCollectionNew.contains(value)) {
                value.getInterestAreaCollection().remove(entity);
            }
        }
        for (Filter value : filterCollectionNew) {
            if (!filterCollection.contains(value)) {
                value.getInterestAreaCollection().add(entity);
            }
        }
        for (SourceInterestMap value : sourceInterestMapCollection) {
            if (!sourceInterestMapCollectionNew.contains(value)) {
                throw new WebApplicationException(new Throwable("Cannot remove items from sourceInterestMapCollection"));
            }
        }
        for (SourceInterestMap value : sourceInterestMapCollectionNew) {
            if (!sourceInterestMapCollection.contains(value)) {
                InterestArea oldEntity = value.getInterestAreaId();
                value.setInterestAreaId(entity);
                if (oldEntity != null && !oldEntity.equals(entity)) {
                    oldEntity.getSourceInterestMapCollection().remove(value);
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
    protected void deleteEntity(InterestArea entity) {
        EntityManager em = PersistenceService.getInstance().getEntityManager();
        for (Event value : entity.getEventCollection()) {
            value.getInterestAreaCollection().remove(entity);
        }
        for (Organization value : entity.getOrganizationCollection()) {
            value.getInterestAreaCollection().remove(entity);
        }
        for (Filter value : entity.getFilterCollection()) {
            value.getInterestAreaCollection().remove(entity);
        }
        if (!entity.getSourceInterestMapCollection().isEmpty()) {
            throw new WebApplicationException(new Throwable("Cannot delete entity because sourceInterestMapCollection is not empty."));
        }
        em.remove(entity);
    }

    /**
     * Returns a dynamic instance of EventsResource used for entity navigation.
     *
     * @param id identifier for the parent entity
     * @return an instance of EventsResource
     */
    @Path("eventCollection/")
    public EventsResource getEventCollectionResource() {
        EventCollectionResourceSub resource = resourceContext.getResource(EventCollectionResourceSub.class);
        resource.setParent(getEntity());
        return resource;
    }

    /**
     * Returns a dynamic instance of OrganizationsResource used for entity navigation.
     *
     * @param id identifier for the parent entity
     * @return an instance of OrganizationsResource
     */
    @Path("organizationCollection/")
    public OrganizationsResource getOrganizationCollectionResource() {
        OrganizationCollectionResourceSub resource = resourceContext.getResource(OrganizationCollectionResourceSub.class);
        resource.setParent(getEntity());
        return resource;
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

    /**
     * Returns a dynamic instance of SourceInterestMapsResource used for entity navigation.
     *
     * @param id identifier for the parent entity
     * @return an instance of SourceInterestMapsResource
     */
    @Path("sourceInterestMapCollection/")
    public SourceInterestMapsResource getSourceInterestMapCollectionResource() {
        SourceInterestMapCollectionResourceSub resource = resourceContext.getResource(SourceInterestMapCollectionResourceSub.class);
        resource.setParent(getEntity());
        return resource;
    }

    public static class EventCollectionResourceSub extends EventsResource {

        private InterestArea parent;

        public void setParent(InterestArea parent) {
            this.parent = parent;
        }

        @Override
        protected Collection<Event> getEntities(int start, int max, String query) {
            Collection<Event> result = new java.util.ArrayList<Event>();
            int index = 0;
            for (Event e : parent.getEventCollection()) {
                if (index >= start && (index - start) < max) {
                    result.add(e);
                }
                index++;
            }
            return result;
        }
    }

    public static class OrganizationCollectionResourceSub extends OrganizationsResource {

        private InterestArea parent;

        public void setParent(InterestArea parent) {
            this.parent = parent;
        }

        @Override
        protected Collection<Organization> getEntities(int start, int max, String query) {
            Collection<Organization> result = new java.util.ArrayList<Organization>();
            int index = 0;
            for (Organization e : parent.getOrganizationCollection()) {
                if (index >= start && (index - start) < max) {
                    result.add(e);
                }
                index++;
            }
            return result;
        }
    }

    public static class FilterCollectionResourceSub extends FiltersResource {

        private InterestArea parent;

        public void setParent(InterestArea parent) {
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

    public static class SourceInterestMapCollectionResourceSub extends SourceInterestMapsResource {

        private InterestArea parent;

        public void setParent(InterestArea parent) {
            this.parent = parent;
        }

        @Override
        protected Collection<SourceInterestMap> getEntities(int start, int max, String query) {
            Collection<SourceInterestMap> result = new java.util.ArrayList<SourceInterestMap>();
            int index = 0;
            for (SourceInterestMap e : parent.getSourceInterestMapCollection()) {
                if (index >= start && (index - start) < max) {
                    result.add(e);
                }
                index++;
            }
            return result;
        }
    }
}
