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

import java.io.PrintWriter;
import java.util.ArrayList;

import org.coppercore.common.XMLTag;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design title element.  
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.6 $, $Date: 2005/01/21 14:01:11 $
 */
public class IMSLDTitleNode extends ItemModelNode {

  /**
   * Constructs a IMSLDTitleNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDTitleNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void writeXMLContent(PrintWriter output) {

    XMLTag tag = new XMLTag("title");
    tag.writeOpenTag(output);

    output.print(getContent());

    tag.writeCloseTag(output);
  }

  protected void writeXMLEnvironmentTree(PrintWriter output) {
    writeXMLContent(output);
  }

  protected void writeXMLPlay(PrintWriter output, IMSLDNode aRole) {
    writeXMLContent(output);
  }

  protected void writeXMLPlay(PrintWriter output, IMSLDNode aRole, ArrayList envIds){
    writeXMLPlay(output,aRole);
  }


  protected void writeXMLRolesTree(PrintWriter output) {
    writeXMLContent(output);
  }




}
