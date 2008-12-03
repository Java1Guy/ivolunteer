/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package converter;

import java.net.URI;
import persistence.Integration;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.ws.rs.core.UriBuilder;
import javax.persistence.EntityManager;
import persistence.Network;
import persistence.IvUser;

/**
 * 
 * @author dave
 */

@XmlRootElement(name = "integration")
public class IntegrationConverter {
	private Integration	entity;
	private URI			uri;
	private int			expandLevel;

	/** Creates a new instance of IntegrationConverter */
	public IntegrationConverter() {
		entity = new Integration();
	}

	/**
	 * Creates a new instance of IntegrationConverter.
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
	public IntegrationConverter(Integration entity, URI uri, int expandLevel,
			boolean isUriExtendable) {
		this.entity = entity;
		this.uri = (isUriExtendable) ? UriBuilder.fromUri(uri).path(entity.getId() + "/").build()
				: uri;
		this.expandLevel = expandLevel;
		getUserId();
		getNetworkId();
	}

	/**
	 * Creates a new instance of IntegrationConverter.
	 * 
	 * @param entity
	 *            associated entity
	 * @param uri
	 *            associated uri
	 * @param expandLevel
	 *            indicates the number of levels the entity graph should be
	 *            expanded
	 */
	public IntegrationConverter(Integration entity, URI uri, int expandLevel) {
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
	 * Getter for userName.
	 * 
	 * @return value for userName
	 */
	@XmlElement
	public String getUserName() {
		return (expandLevel > 0) ? entity.getUserName() : null;
	}

	/**
	 * Setter for userName.
	 * 
	 * @param value
	 *            the value to set
	 */
	public void setUserName(String value) {
		entity.setUserName(value);
	}

	/**
	 * Getter for password.
	 * 
	 * @return value for password
	 */
	@XmlElement
	public String getPassword() {
		return (expandLevel > 0) ? entity.getPassword() : null;
	}

	/**
	 * Setter for password.
	 * 
	 * @param value
	 *            the value to set
	 */
	public void setPassword(String value) {
		entity.setPassword(value);
	}

	/**
	 * Getter for userId.
	 * 
	 * @return value for userId
	 */
	@XmlElement
	public IvUserConverter getUserId() {
		if (expandLevel > 0) {
			if (entity.getUserId() != null) {
				return new IvUserConverter(entity.getUserId(), uri.resolve("userId/"),
						expandLevel - 1, false);
			}
		}
		return null;
	}

	/**
	 * Setter for userId.
	 * 
	 * @param value
	 *            the value to set
	 */
	public void setUserId(IvUserConverter value) {
		entity.setUserId((value != null) ? value.getEntity() : null);
	}

	/**
	 * Getter for networkId.
	 * 
	 * @return value for networkId
	 */
	@XmlElement
	public NetworkConverter getNetworkId() {
		if (expandLevel > 0) {
			if (entity.getNetworkId() != null) {
				return new NetworkConverter(entity.getNetworkId(), uri.resolve("networkId/"),
						expandLevel - 1, false);
			}
		}
		return null;
	}

	/**
	 * Setter for networkId.
	 * 
	 * @param value
	 *            the value to set
	 */
	public void setNetworkId(NetworkConverter value) {
		entity.setNetworkId((value != null) ? value.getEntity() : null);
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
	 * Returns the Integration entity.
	 * 
	 * @return an entity
	 */
	@XmlTransient
	public Integration getEntity() {
		if (entity.getId() == null) {
			IntegrationConverter converter = UriResolver.getInstance().resolve(
					IntegrationConverter.class, uri);
			if (converter != null) {
				entity = converter.getEntity();
			}
		}
		return entity;
	}

	/**
	 * Returns the resolved Integration entity.
	 * 
	 * @return an resolved entity
	 */
	public Integration resolveEntity(EntityManager em) {
		IvUser userId = entity.getUserId();
		if (userId != null) {
			entity.setUserId(em.getReference(IvUser.class, userId.getId()));
		}
		Network networkId = entity.getNetworkId();
		if (networkId != null) {
			entity.setNetworkId(em.getReference(Network.class, networkId.getId()));
		}
		return entity;
	}
}
