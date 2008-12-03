/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package converter;

import java.net.URI;
import java.util.Date;
import persistence.Timestamp;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.ws.rs.core.UriBuilder;
import javax.persistence.EntityManager;
import persistence.Event;
import java.util.Collection;

/**
 * 
 * @author dave
 */

@XmlRootElement(name = "timestamp")
public class TimestampConverter {
	private Timestamp	entity;
	private URI			uri;
	private int			expandLevel;

	/** Creates a new instance of TimestampConverter */
	public TimestampConverter() {
		entity = new Timestamp();
	}

	/**
	 * Creates a new instance of TimestampConverter.
	 * 
	 * @param entity
	 *            associated entity
	 * @param uri
	 *            associated uri
	 * @param expandLevel
	 *            indicates the number of levels the entity graph should be
	 *            expanded@param isUriExtendable indicates whether the uri can
	 *            be extended
	 */
	public TimestampConverter(Timestamp entity, URI uri, int expandLevel, boolean isUriExtendable) {
		this.entity = entity;
		this.uri = (isUriExtendable) ? UriBuilder.fromUri(uri).path(entity.getId() + "/").build()
				: uri;
		this.expandLevel = expandLevel;
		getEventCollection();
	}

	/**
	 * Creates a new instance of TimestampConverter.
	 * 
	 * @param entity
	 *            associated entity
	 * @param uri
	 *            associated uri
	 * @param expandLevel
	 *            indicates the number of levels the entity graph should be
	 *            expanded
	 */
	public TimestampConverter(Timestamp entity, URI uri, int expandLevel) {
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
	 * @param value
	 *            the value to set
	 */
	public void setId(String value) {
		entity.setId(value);
	}

	/**
	 * Getter for timestamp.
	 * 
	 * @return value for timestamp
	 */
	@XmlElement
	public Date getTimestamp() {
		return (expandLevel > 0) ? entity.getTimestamp() : null;
	}

	/**
	 * Setter for timestamp.
	 * 
	 * @param value
	 *            the value to set
	 */
	public void setTimestamp(Date value) {
		entity.setTimestamp(value);
	}

	/**
	 * Getter for eventCollection.
	 * 
	 * @return value for eventCollection
	 */
	@XmlElement
	public EventsConverter getEventCollection() {
		if (expandLevel > 0) {
			if (entity.getEventCollection() != null) {
				return new EventsConverter(entity.getEventCollection(), uri
						.resolve("eventCollection/"), expandLevel - 1);
			}
		}
		return null;
	}

	/**
	 * Setter for eventCollection.
	 * 
	 * @param value
	 *            the value to set
	 */
	public void setEventCollection(EventsConverter value) {
		entity.setEventCollection((value != null) ? value.getEntities() : null);
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
	 * Returns the Timestamp entity.
	 * 
	 * @return an entity
	 */
	@XmlTransient
	public Timestamp getEntity() {
		if (entity.getId() == null) {
			TimestampConverter converter = UriResolver.getInstance().resolve(
					TimestampConverter.class, uri);
			if (converter != null) {
				entity = converter.getEntity();
			}
		}
		return entity;
	}

	/**
	 * Returns the resolved Timestamp entity.
	 * 
	 * @return an resolved entity
	 */
	public Timestamp resolveEntity(EntityManager em) {
		Collection<Event> eventCollection = entity.getEventCollection();
		Collection<Event> neweventCollection = new java.util.ArrayList<Event>();
		for (Event item : eventCollection) {
			neweventCollection.add(em.getReference(Event.class, item.getId()));
		}
		entity.setEventCollection(neweventCollection);
		return entity;
	}
}
