/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Glue!PS.
 * 
 * Glue!PS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Glue!PS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/*
 * CopperCore, an IMS-LD level C engine
 * Copyright (C) 2003 Harrie Martens and Hubert Vogten
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program (/license.txt); if not,
 * write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 *     Contact information:
 *     Open University of the Netherlands
 *     Valkenburgerweg 177 Heerlen
 *     PO Box 2960 6401 DL Heerlen
 *     e-mail: hubert.vogten@ou.nl or
 *             harrie.martens@ou.nl
 *
 *
 * Open Universiteit Nederland, hereby disclaims all copyright interest
 * in the program CopperCore written by
 * Harrie Martens and Hubert Vogten
 *
 * prof.dr. Rob Koper,
 * director of learning technologies research and development
 *
 */
package org.coppercore.dossier;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Harrie Martens
 * @author Hubert Vogten
 * 
 * @version $Revision: 1.1 $, $Date: 2006/08/18 15:02:42 $
 */
public class PkCache {
	final static int MAX_ENTRIES = 4096;

	static Map cache = Collections.synchronizedMap(new LinkedHashMap(MAX_ENTRIES + 1, .75F, true) {
		// This method is called just after a new entry has been added
		public boolean removeEldestEntry(Map.Entry eldest) {
			return size() > MAX_ENTRIES;
		}
	});

	private static String getKey(int uolId, String propId, String userId, int runId) {
		return propId + "-" + userId + "-" + Integer.toString(uolId) + "-" + Integer.toString(runId);
	}

	/**
	 * Return the PropertyEntityPK corresponding to the passed parameters. Null
	 * will be returned if no corresponding key was found on the cache.
	 * 
	 * @param uolId
	 *          int the id of the unit of learning this pk belongs to
	 * @param propId
	 *          String the learning design of the pk to find
	 * @param userId
	 *          String the id of the owner to find the pk for
	 * @param runId
	 *          int the id of the run the property pk to
	 * @return the PropertyEntityPK corresponding to the passed parameters. Null
	 *         will be returned if no corresponding key was found on the cache.
	 */
	public static synchronized PropertyEntityPK getPK(int uolId, String propId, String userId, int runId) {
		String key = getKey(uolId, propId, userId, runId);
		Integer pk = (Integer) cache.get(key);
		if (pk == null)
			return null;

		// put key on top of linked list
		cache.remove(key);
		cache.put(key, pk);
		return new PropertyEntityPK(pk.intValue());
	}

	/**
	 * Adds a pk key combination to the cache of pks.
	 * 
	 * @param pk
	 *          the pk to be cached.
	 * @param uolId
	 *          int the id of the unit of learning this pk belongs to
	 * @param propId
	 *          String the learning design of the pk to find
	 * @param userId
	 *          String the id of the owner to find the pk for
	 * @param runId
	 *          int the id of the run the property pk to
	 */
	public static void putPK(int pk, int uolId, String propId, String userId, int runId) {
		String key = getKey(uolId, propId, userId, runId);
		cache.put(key, new Integer(pk));
	}

	/**
	 * Remove a specified pk from the cache.
	 * 
	 * @param pk
	 *          the pk of the cache entry to be removed
	 */
	public synchronized static void removeFromCache(int pk) {
		Set entrySet = cache.entrySet();

		Integer pki = new Integer(pk);
		Iterator it = entrySet.iterator();

		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (entry.getValue().equals(pki)) {
				it.remove();
				break;
			}
		}
	}
}
