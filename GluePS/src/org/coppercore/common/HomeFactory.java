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

package org.coppercore.common;

/**
* Defines the jndi name for all ejb data beans in CopperCore.
*
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.8 $, $Date: 2005/01/11 13:15:09 $
 */
public class HomeFactory {
  /** The jndi name of the UnitOfLearning bean. */
  final public static String UNITOFLEARNING = "java:comp/env/ejb/unitoflearning";

  /** The jndi name of the RoleInstanceEntity bean. */
  final public static String ROLEINSTANCEENTITY = "java:comp/env/ejb/roleinstanceentity";

  /** The jndi name of the UserEntity bean. */
  final public static String USERENTITY = "java:comp/env/ejb/userentity";

  /** The jndi name of the RunEntity bean. */
  final public static String RUNENTITY = "java:comp/env/ejb/runentity";

  /** The jndi name of the EventEntity bean. */
  final public static String EVENTENTITY = "java:comp/env/ejb/evententity";

  /** The jndi name of the PropertyDefinition bean. */
  final public static String PROPERTYDEFENTITY = "java:comp/env/ejb/propertydefentity";

  /** The jndi name of the RunParticipation bean. */
  final public static String RUNPARTICIPATION = "java:comp/env/ejb/runparticipation";

  /** The jndi name of the RoleParticipationEntity bean. */
  final public static String ROLEPARTICIPATIONENTITY = "java:comp/env/ejb/roleparticipationentity";

  /** The jndi name of the PropertyEntity bean. */
  final public static String PROPERTYENTITY = "java:comp/env/ejb/propertyentity";

  /** The jndi name of the PropertyLookupEntity bean. */
  final public static String PROPERTYLOOKUPENTITY =
      "java:comp/env/ejb/propertylookupentity";


}
