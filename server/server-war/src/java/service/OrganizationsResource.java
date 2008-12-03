/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service;

import java.util.Collection;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import com.sun.jersey.api.core.ResourceContext;
import persistence.Organization;
import converter.OrganizationsConverter;
import converter.OrganizationConverter;
import converter.OrganizationListConverter;

/**
 * 
 * @author dave
 */

@Path("/organizations/")
public class OrganizationsResource extends Base {
	@Context
	protected UriInfo			uriInfo;
	@Context
	protected ResourceContext	resourceContext;

	/** Creates a new instance of OrganizationsResource */
	public OrganizationsResource() {
	}

	/**
	 * Get method for retrieving a collection of Organization instance in XML
	 * format.
	 * 
	 * @return an instance of OrganizationsConverter
	 */
	@GET
	@Produces( { "application/xml", "application/json" })
	public OrganizationsConverter get(@QueryParam("start") @DefaultValue("0") int start,
			@QueryParam("max") @DefaultValue("10") int max,
			@QueryParam("expandLevel") @DefaultValue("1") int expandLevel,
			@QueryParam("query") @DefaultValue("SELECT e FROM Organization e") String query) {
		return new OrganizationsConverter(getEntities(start, max, query),
				uriInfo.getAbsolutePath(), expandLevel);
	}

	/**
	 * Post method for creating an instance of Organization using XML as the
	 * input format.
	 * 
	 * @param data
	 *            an OrganizationConverter entity that is deserialized from an
	 *            XML stream
	 * @return an instance of OrganizationConverter
	 */
	@POST
	@Consumes( { "application/xml", "application/json" })
	public Response post(OrganizationConverter data) {
		Organization entity = data.getEntity();
		createEntity(entity);
		return Response.created(uriInfo.getAbsolutePath().resolve(entity.getId() + "/")).build();
	}

	@Path("list/")
	@GET
	@Produces( { "application/json" })
	public OrganizationListConverter list(@QueryParam("start") @DefaultValue("0") int start,
			@QueryParam("max") @DefaultValue("10") int max,
			@QueryParam("query") @DefaultValue("SELECT e FROM Organization e") String query) {
		return new OrganizationListConverter(getEntities(start, max, query), uriInfo
				.getAbsolutePath(), uriInfo.getBaseUri());
	}

	/**
	 * Returns a dynamic instance of OrganizationResource used for entity
	 * navigation.
	 * 
	 * @return an instance of OrganizationResource
	 */
	@Path("{id}/")
	public service.OrganizationResource getOrganizationResource(@PathParam("id") String id) {
		OrganizationResource resource = resourceContext.getResource(OrganizationResource.class);
		resource.setId(id);
		return resource;
	}

	/**
	 * Returns all the entities associated with this resource.
	 * 
	 * @return a collection of Organization instances
	 */
	@Override
	protected Collection<Organization> getEntities(int start, int max, String query) {
		return (Collection<Organization>) super.getEntities(start, max, query);
	}
}
