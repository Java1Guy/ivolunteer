/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package converter;

import java.net.URI;
import persistence.SourceOrgTypeMap;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.ws.rs.core.UriBuilder;
import javax.persistence.EntityManager;
import persistence.Source;
import persistence.OrganizationType;

/**
 *
 * @author dave
 */

@XmlRootElement(name = "sourceOrgTypeMap")
public class SourceOrgTypeMapConverter {
    private SourceOrgTypeMap entity;
    private URI uri;
    private int expandLevel;
  
    /** Creates a new instance of SourceOrgTypeMapConverter */
    public SourceOrgTypeMapConverter() {
        entity = new SourceOrgTypeMap();
    }

    /**
     * Creates a new instance of SourceOrgTypeMapConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded@param isUriExtendable indicates whether the uri can be extended
     */
    public SourceOrgTypeMapConverter(SourceOrgTypeMap entity, URI uri, int expandLevel, boolean isUriExtendable) {
        this.entity = entity;
        this.uri = (isUriExtendable) ? UriBuilder.fromUri(uri).path(entity.getId() + "/").build() : uri;
        this.expandLevel = expandLevel;
        getOrganizationTypeId();
        getSourceId();
    }

    /**
     * Creates a new instance of SourceOrgTypeMapConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public SourceOrgTypeMapConverter(SourceOrgTypeMap entity, URI uri, int expandLevel) {
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
     * Getter for sourceKey.
     *
     * @return value for sourceKey
     */
    @XmlElement
    public String getSourceKey() {
        return (expandLevel > 0) ? entity.getSourceKey() : null;
    }

    /**
     * Setter for sourceKey.
     *
     * @param value the value to set
     */
    public void setSourceKey(String value) {
        entity.setSourceKey(value);
    }

    /**
     * Getter for organizationTypeId.
     *
     * @return value for organizationTypeId
     */
    @XmlElement
    public OrganizationTypeConverter getOrganizationTypeId() {
        if (expandLevel > 0) {
            if (entity.getOrganizationTypeId() != null) {
                return new OrganizationTypeConverter(entity.getOrganizationTypeId(), uri.resolve("organizationTypeId/"), expandLevel - 1, false);
            }
        }
        return null;
    }

    /**
     * Setter for organizationTypeId.
     *
     * @param value the value to set
     */
    public void setOrganizationTypeId(OrganizationTypeConverter value) {
        entity.setOrganizationTypeId((value != null) ? value.getEntity() : null);
    }

    /**
     * Getter for sourceId.
     *
     * @return value for sourceId
     */
    @XmlElement
    public SourceConverter getSourceId() {
        if (expandLevel > 0) {
            if (entity.getSourceId() != null) {
                return new SourceConverter(entity.getSourceId(), uri.resolve("sourceId/"), expandLevel - 1, false);
            }
        }
        return null;
    }

    /**
     * Setter for sourceId.
     *
     * @param value the value to set
     */
    public void setSourceId(SourceConverter value) {
        entity.setSourceId((value != null) ? value.getEntity() : null);
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
     * Returns the SourceOrgTypeMap entity.
     *
     * @return an entity
     */
    @XmlTransient
    public SourceOrgTypeMap getEntity() {
        if (entity.getId() == null) {
            SourceOrgTypeMapConverter converter = UriResolver.getInstance().resolve(SourceOrgTypeMapConverter.class, uri);
            if (converter != null) {
                entity = converter.getEntity();
            }
        }
        return entity;
    }

    /**
     * Returns the resolved SourceOrgTypeMap entity.
     *
     * @return an resolved entity
     */
    public SourceOrgTypeMap resolveEntity(EntityManager em) {
        OrganizationType organizationTypeId = entity.getOrganizationTypeId();
        if (organizationTypeId != null) {
            entity.setOrganizationTypeId(em.getReference(OrganizationType.class, organizationTypeId.getId()));
        }
        Source sourceId = entity.getSourceId();
        if (sourceId != null) {
            entity.setSourceId(em.getReference(Source.class, sourceId.getId()));
        }
        return entity;
    }
}
