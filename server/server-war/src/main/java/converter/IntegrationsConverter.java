/*
 *  Copyright (c) 2008 Boulder Community Foundation - iVolunteer
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package converter;

import java.net.URI;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.ArrayList;
import persistence.Integration;

/**
 * 
 * @author Dave Angulo
 */

@XmlRootElement(name = "integrations")
public class IntegrationsConverter {
	private Collection<Integration>						entities;
	private Collection<converter.IntegrationConverter>	items;
	private URI											uri;
	private int											expandLevel;

	/** Creates a new instance of IntegrationsConverter */
	public IntegrationsConverter() {
	}

	/**
	 * Creates a new instance of IntegrationsConverter.
	 * 
	 * @param entities
	 *            associated entities
	 * @param uri
	 *            associated uri
	 * @param expandLevel
	 *            indicates the number of levels the entity graph should be
	 *            expanded
	 */
	public IntegrationsConverter(Collection<Integration> entities, URI uri, int expandLevel) {
		this.entities = entities;
		this.uri = uri;
		this.expandLevel = expandLevel;
		getIntegration();
	}

	/**
	 * Returns a collection of IntegrationConverter.
	 * 
	 * @return a collection of IntegrationConverter
	 */
	@XmlElement
	public Collection<converter.IntegrationConverter> getIntegration() {
		if (items == null) {
			items = new ArrayList<IntegrationConverter>();
		}
		if (entities != null) {
			for (Integration entity : entities) {
				items.add(new IntegrationConverter(entity, uri, expandLevel, true));
			}
		}
		return items;
	}

	/**
	 * Sets a collection of IntegrationConverter.
	 * 
	 * @param a
	 *            collection of IntegrationConverter to set
	 */
	public void setIntegration(Collection<converter.IntegrationConverter> items) {
		this.items = items;
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
	 * Returns a collection Integration entities.
	 * 
	 * @return a collection of Integration entities
	 */
	@XmlTransient
	public Collection<Integration> getEntities() {
		entities = new ArrayList<Integration>();
		if (items != null) {
			for (IntegrationConverter item : items) {
				entities.add(item.getEntity());
			}
		}
		return entities;
	}
}
