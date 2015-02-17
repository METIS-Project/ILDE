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

package org.coppercore.parser;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class represents an IMS Content Package organizations element.
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.6 $, $Date: 2005/01/11 13:15:08 $
 */
public class IMSCPOrganizationsNode
    extends IMSLDNode {

  /**
   * Constructs a new IMSCPOrganizationsNode by parsing passed the xml dom
   * element.
   *
   * @param aNode Node the xml dom node to parse
   * @param aParent IMSLDNode the parsed IMS learing design element that is the
   *   parent element of this object
   */
  public IMSCPOrganizationsNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void parseLDElement(Element childNode, String nodeName) throws
      Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("learning-design")) {

      addElement(new IMSLDLearningDesignNode(childNode, this));
    }
    else if (nodeName.equals("method")) {
      addElement(new IMSLDMethodNode(childNode, this));
    }
  }

}
