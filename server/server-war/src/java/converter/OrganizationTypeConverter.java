/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package converter;

import java.net.URI;
import persistence.OrganizationType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.ws.rs.core.UriBuilder;
import javax.persistence.EntityManager;
import java.util.Collection;
import persistence.Organization;
import persistence.Filter;
import persistence.SourceOrgTypeMap;

/**
 *
 * @author dave
 */

@XmlRootElement(name = "organizationType")
public class OrganizationTypeConverter {
    private OrganizationType entity;
    private URI uri;
    private int expandLevel;
  
    /** Creates a new instance of OrganizationTypeConverter */
    public OrganizationTypeConverter() {
        entity = new OrganizationType();
    }

    /**
     * Creates a new instance of OrganizationTypeConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded@param isUriExtendable indicates whether the uri can be extended
     */
    public OrganizationTypeConverter(OrganizationType entity, URI uri, int expandLevel, boolean isUriExtendable) {
        this.entity = entity;
        this.uri = (isUriExtendable) ? UriBuilder.fromUri(uri).path(entity.getId() + "/").build() : uri;
        this.expandLevel = expandLevel;
        getFilterCollection();
        getOrganizationCollection();
        getSourceOrgTypeMapCollection();
    }

    /**
     * Creates a new instance of OrganizationTypeConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public OrganizationTypeConverter(OrganizationType entity, URI uri, int expandLevel) {
        this(entity, uri, expandLevel, false);
    }

    /**
     * Getter for id.
     *
     * @return value for id
     */
    @XmlElement
    public String getId() {
        return (expandLevel > 0) ? entity.getId() : null;
    }

    /**
     * Setter for id.
     *
     * @param value the value to set
     */
    public void setId(String value) {
        entity.setId(value);
    }

    /**
     * Getter for name.
     *
     * @return value for name
     */
    @XmlElement
    public String getName() {
        return (expandLevel > 0) ? entity.getName() : null;
    }

    /**
     * Setter for name.
     *
     * @param value the value to set
     */
    public void setName(String value) {
        entity.setName(value);
    }

    /**
     * Getter for filterCollection.
     *
     * @return value for filterCollection
     */
    @XmlElement
    public FiltersConverter getFilterCollection() {
        if (expandLevel > 0) {
            if (entity.getFilterCollection() != null) {
                return new FiltersConverter(entity.getFilterCollection(), uri.resolve("filterCollection/"), expandLevel - 1);
            }
        }
        return null;
    }

    /**
     * Setter for filterCollection.
     *
     * @param value the value to set
     */
    public void setFilterCollection(FiltersConverter value) {
        entity.setFilterCollection((value != null) ? value.getEntities() : null);
    }

    /**
     * Getter for organizationCollection.
     *
     * @return value for organizationCollection
     */
    @XmlElement
    public OrganizationsConverter getOrganizationCollection() {
        if (expandLevel > 0) {
            if (entity.getOrganizationCollection() != null) {
                return new OrganizationsConverter(entity.getOrganizationCollection(), uri.resolve("organizationCollection/"), expandLevel - 1);
            }
        }
        return null;
    }

    /**
     * Setter for organizationCollection.
     *
     * @param value the value to set
     */
    public void setOrganizationCollection(OrganizationsConverter value) {
        entity.setOrganizationCollection((value != null) ? value.getEntities() : null);
    }

    /**
     * Getter for sourceOrgTypeMapCollection.
     *
     * @return value for sourceOrgTypeMapCollection
     */
    @XmlElement
    public SourceOrgTypeMapsConverter getSourceOrgTypeMapCollection() {
        if (expandLevel > 0) {
            if (entity.getSourceOrgTypeMapCollection() != null) {
                return new SourceOrgTypeMapsConverter(entity.getSourceOrgTypeMapCollection(), uri.resolve("sourceOrgTypeMapCollection/"), expandLevel - 1);
            }
        }
        return null;
    }

    /**
     * Setter for sourceOrgTypeMapCollection.
     *
     * @param value the value to set
     */
    public void setSourceOrgTypeMapCollection(SourceOrgTypeMapsConverter value) {
        entity.setSourceOrgTypeMapCollection((value != null) ? value.getEntities() : null);
    }

    /**
     * Returns the URI associated with this converter.
     *
     * @return the uri
     */
    @XmlAttribute
    public URI getUri() {
        return uri;
    }

    /**
     * Sets the URI for this reference converter.
     *
     */
    public void setUri(URI uri) {
        this.uri = uri;
    }

    /**
     * Returns the OrganizationType entity.
     *
     * @return an entity
     */
    @XmlTransient
    public OrganizationType getEntity() {
        if (entity.getId() == null) {
            OrganizationTypeConverter converter = UriResolver.getInstance().resolve(OrganizationTypeConverter.class, uri);
            if (converter != null) {
                entity = converter.getEntity();
            }
        }
        return entity;
    }

    /**
     * Returns the resolved OrganizationType entity.
     *
     * @return an resolved entity
     */
    public OrganizationType resolveEntity(EntityManager em) {
        Collection<Filter> filterCollection = entity.getFilterCollection();
        Collection<Filter> newfilterCollection = new java.util.ArrayList<Filter>();
        for (Filter item : filterCollection) {
            newfilterCollection.add(em.getReference(Filter.class, item.getId()));
        }
        entity.setFilterCollection(newfilterCollection);
        Collection<Organization> organizationCollection = entity.getOrganizationCollection();
        Collection<Organization> neworganizationCollection = new java.util.ArrayList<Organization>();
        for (Organization item : organizationCollection) {
            neworganizationCollection.add(em.getReference(Organization.class, item.getId()));
        }
        entity.setOrganizationCollection(neworganizationCollection);
        Collection<SourceOrgTypeMap> sourceOrgTypeMapCollection = entity.getSourceOrgTypeMapCollection();
        Collection<SourceOrgTypeMap> newsourceOrgTypeMapCollection = new java.util.ArrayList<SourceOrgTypeMap>();
        for (SourceOrgTypeMap item : sourceOrgTypeMapCollection) {
            newsourceOrgTypeMapCollection.add(em.getReference(SourceOrgTypeMap.class, item.getId()));
        }
        entity.setSourceOrgTypeMapCollection(newsourceOrgTypeMapCollection);
        return entity;
    }
}
