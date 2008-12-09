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

package session;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import persistence.SourceOrgTypeMap;

/**
 * 
 * @author Dave Angulo
 */
@Stateless
public class SourceOrgTypeMapFacade implements SourceOrgTypeMapFacadeLocal {
	@PersistenceContext
	private EntityManager	em;

	public void create(SourceOrgTypeMap sourceOrgTypeMap) {
		em.persist(sourceOrgTypeMap);
	}

	public void edit(SourceOrgTypeMap sourceOrgTypeMap) {
		em.merge(sourceOrgTypeMap);
	}

	public void remove(SourceOrgTypeMap sourceOrgTypeMap) {
		em.remove(em.merge(sourceOrgTypeMap));
	}

	public SourceOrgTypeMap find(Object id) {
		return em.find(SourceOrgTypeMap.class, id);
	}

	public List<SourceOrgTypeMap> findAll() {
		return em.createQuery("select object(o) from SourceOrgTypeMap as o").getResultList();
	}

}
