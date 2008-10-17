/*
 *  InterestAreaRefConverter
 *
 * Created on October 11, 2008, 9:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package converter;

import java.net.URI;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.ws.rs.core.UriBuilder;
import persistence.InterestAreas;


/**
 *
 * @author dave
 */

@XmlRootElement(name = "interestAreaRef")
public class InterestAreaRefConverter {
    private InterestAreas entity;
    private boolean isUriExtendable;
    private URI uri;
  
    /** Creates a new instance of InterestAreaRefConverter */
    public InterestAreaRefConverter() {
    }

    /**
     * Creates a new instance of InterestAreaRefConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param isUriExtendable indicates whether the uri can be extended
     */
    public InterestAreaRefConverter(InterestAreas entity, URI uri, boolean isUriExtendable) {
        this.entity = entity;
        this.uri = uri;
        this.isUriExtendable = isUriExtendable;
    }

    /**
     * Getter for id.
     *
     * @return value for id
     */
    @XmlElement
    public String getId() {
        return entity.getId();
    }

    /**
     * Returns the URI associated with this reference converter.
     *
     * @return the converted uri
     */
    @XmlAttribute(name = "uri")
    public URI getResourceUri() {
        if (isUriExtendable) {
            return UriBuilder.fromUri(uri).path(entity.getId() + "/").build();
        }
        return uri;
    }

    /**
     * Sets the URI for this reference converter.
     *
     */
    public void setResourceUri(URI uri) {
        this.uri = uri;
    }

    /**
     * Returns the InterestAreas entity.
     *
     * @return InterestAreas entity
     */
    @XmlTransient
    public InterestAreas getEntity() {
        InterestAreaConverter result = UriResolver.getInstance().resolve(InterestAreaConverter.class, uri);
        if (result != null) {
            return result.getEntity();
        }
        return null;
    }
}
