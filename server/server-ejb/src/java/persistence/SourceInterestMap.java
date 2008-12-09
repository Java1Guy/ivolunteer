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

package persistence;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * @author Dave Angulo
 */
@Entity
@Table(name = "SOURCE_INTEREST_MAP")
@NamedQueries( {
		@NamedQuery(name = "SourceInterestMap.findAll", query = "SELECT s FROM SourceInterestMap s"),
		@NamedQuery(name = "SourceInterestMap.findById", query = "SELECT s FROM SourceInterestMap s WHERE s.id = :id"),
		@NamedQuery(name = "SourceInterestMap.findBySourceKey", query = "SELECT s FROM SourceInterestMap s WHERE s.sourceId = :source and s.sourceKey = :sourceKey") })
public class SourceInterestMap implements Serializable, IdInterface {
	private static final long	serialVersionUID	= 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "ID")
	private String				id;
	@Basic(optional = false)
	@Column(name = "SOURCE_KEY")
	private String				sourceKey;
	@JoinColumn(name = "INTEREST_AREA_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	private InterestArea		interestAreaId;
	@JoinColumn(name = "SOURCE_ID", referencedColumnName = "ID")
	@ManyToOne(optional = false)
	private Source				sourceId;

	public SourceInterestMap() {
	}

	public SourceInterestMap(String id) {
		this.id = id;
	}

	public SourceInterestMap(String id, String sourceKey) {
		this.id = id;
		this.sourceKey = sourceKey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSourceKey() {
		return sourceKey;
	}

	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}

	public InterestArea getInterestAreaId() {
		return interestAreaId;
	}

	public void setInterestAreaId(InterestArea interestAreaId) {
		this.interestAreaId = interestAreaId;
	}

	public Source getSourceId() {
		return sourceId;
	}

	public void setSourceId(Source sourceId) {
		this.sourceId = sourceId;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof SourceInterestMap)) {
			return false;
		}
		SourceInterestMap other = (SourceInterestMap) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "persistence.SourceInterestMap[id=" + id + "]";
	}

}
