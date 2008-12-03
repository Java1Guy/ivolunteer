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
import persistence.Source;
import persistence.InterestArea;
import converter.SourceInterestMapConverter;
import persistence.SourceInterestMap;

/**
 * 
 * @author dave
 */

public class SourceInterestMapResource {
	@Context
	protected UriInfo			uriInfo;
	@Context
	protected ResourceContext	resourceContext;
	protected String			id;

	/** Creates a new instance of SourceInterestMapResource */
	public SourceInterestMapResource() {
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get method for retrieving an instance of SourceInterestMap identified by
	 * id in XML format.
	 * 
	 * @param id
	 *            identifier for the entity
	 * @return an instance of SourceInterestMapConverter
	 */
	@GET
	@Produces( { "application/xml", "application/json" })
	public SourceInterestMapConverter get(
			@QueryParam("expandLevel") @DefaultValue("1") int expandLevel) {
		PersistenceService persistenceSvc = PersistenceService.getInstance();
		try {
			persistenceSvc.beginTx();
			return new SourceInterestMapConverter(getEntity(), uriInfo.getAbsolutePath(),
					expandLevel);
		} finally {
			PersistenceService.getInstance().close();
		}
	}

	/**
	 * Put method for updating an instance of SourceInterestMap identified by id
	 * using XML as the input format.
	 * 
	 * @param id
	 *            identifier for the entity
	 * @param data
	 *            an SourceInterestMapConverter entity that is deserialized from
	 *            a XML stream
	 */
	@PUT
	@Consumes( { "application/xml", "application/json" })
	public void put(SourceInterestMapConverter data) {
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
	 * Delete method for deleting an instance of SourceInterestMap identified by
	 * id.
	 * 
	 * @param id
	 *            identifier for the entity
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
	 * Returns an instance of SourceInterestMap identified by id.
	 * 
	 * @param id
	 *            identifier for the entity
	 * @return an instance of SourceInterestMap
	 */
	protected SourceInterestMap getEntity() {
		EntityManager em = PersistenceService.getInstance().getEntityManager();
		try {
			return (SourceInterestMap) em.createQuery(
					"SELECT e FROM SourceInterestMap e where e.id = :id").setParameter("id", id)
					.getSingleResult();
		} catch (NoResultException ex) {
			throw new WebApplicationException(new Throwable("Resource for "
					+ uriInfo.getAbsolutePath() + " does not exist."), 404);
		}
	}

	/**
	 * Updates entity using data from newEntity.
	 * 
	 * @param entity
	 *            the entity to update
	 * @param newEntity
	 *            the entity containing the new data
	 * @return the updated entity
	 */
	protected SourceInterestMap updateEntity(SourceInterestMap entity, SourceInterestMap newEntity) {
		EntityManager em = PersistenceService.getInstance().getEntityManager();
		InterestArea interestAreaId = entity.getInterestAreaId();
		InterestArea interestAreaIdNew = newEntity.getInterestAreaId();
		Source sourceId = entity.getSourceId();
		Source sourceIdNew = newEntity.getSourceId();
		entity = em.merge(newEntity);
		if (interestAreaId != null && !interestAreaId.equals(interestAreaIdNew)) {
			interestAreaId.getSourceInterestMapCollection().remove(entity);
		}
		if (interestAreaIdNew != null && !interestAreaIdNew.equals(interestAreaId)) {
			interestAreaIdNew.getSourceInterestMapCollection().add(entity);
		}
		if (sourceId != null && !sourceId.equals(sourceIdNew)) {
			sourceId.getSourceInterestMapCollection().remove(entity);
		}
		if (sourceIdNew != null && !sourceIdNew.equals(sourceId)) {
			sourceIdNew.getSourceInterestMapCollection().add(entity);
		}
		return entity;
	}

	/**
	 * Deletes the entity.
	 * 
	 * @param entity
	 *            the entity to deletle
	 */
	protected void deleteEntity(SourceInterestMap entity) {
		EntityManager em = PersistenceService.getInstance().getEntityManager();
		InterestArea interestAreaId = entity.getInterestAreaId();
		if (interestAreaId != null) {
			interestAreaId.getSourceInterestMapCollection().remove(entity);
		}
		Source sourceId = entity.getSourceId();
		if (sourceId != null) {
			sourceId.getSourceInterestMapCollection().remove(entity);
		}
		em.remove(entity);
	}

	/**
	 * Returns a dynamic instance of InterestAreaResource used for entity
	 * navigation.
	 * 
	 * @param id
	 *            identifier for the parent entity
	 * @return an instance of InterestAreaResource
	 */
	@Path("interestAreaId/")
	public service.InterestAreaResource getInterestAreaIdResource() {
		InterestAreaIdResourceSub resource = resourceContext
				.getResource(InterestAreaIdResourceSub.class);
		resource.setParent(getEntity());
		return resource;
	}

	/**
	 * Returns a dynamic instance of SourceResource used for entity navigation.
	 * 
	 * @param id
	 *            identifier for the parent entity
	 * @return an instance of SourceResource
	 */
	@Path("sourceId/")
	public SourceResource getSourceIdResource() {
		SourceIdResourceSub resource = resourceContext.getResource(SourceIdResourceSub.class);
		resource.setParent(getEntity());
		return resource;
	}

	public static class InterestAreaIdResourceSub extends InterestAreaResource {

		private SourceInterestMap	parent;

		public void setParent(SourceInterestMap parent) {
			this.parent = parent;
		}

		@Override
		protected InterestArea getEntity() {
			InterestArea entity = parent.getInterestAreaId();
			if (entity == null) {
				throw new WebApplicationException(new Throwable("Resource for "
						+ uriInfo.getAbsolutePath() + " does not exist."), 404);
			}
			return entity;
		}
	}

	public static class SourceIdResourceSub extends SourceResource {

		private SourceInterestMap	parent;

		public void setParent(SourceInterestMap parent) {
			this.parent = parent;
		}

		@Override
		protected Source getEntity() {
			Source entity = parent.getSourceId();
			if (entity == null) {
				throw new WebApplicationException(new Throwable("Resource for "
						+ uriInfo.getAbsolutePath() + " does not exist."), 404);
			}
			return entity;
		}
	}
}
