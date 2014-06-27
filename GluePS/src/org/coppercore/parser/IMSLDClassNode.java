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

import org.coppercore.common.MessageList;
import org.coppercore.common.Util;
import org.coppercore.condition.ShowHideClass;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design class element. 
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.7 $, $Date: 2005/01/20 16:12:45 $
 */
public class IMSLDClassNode
    extends IMSLDNode {

  private ShowHideClass showHideClass = null;
  private String classTitle = null;
  private String withControl = null;
  private String classNames = null;

  /**
   * Constructs a IMSLDClassNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDClassNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);

    classTitle = this.getNamedAttribute("title");
    withControl = this.getNamedAttribute("with-control");
    classNames = getNamedAttribute("class");
  }

  protected boolean buildComponentModel(int uolId, MessageList messages) {
    boolean result = super.buildComponentModel(uolId, messages);

    boolean show = parent instanceof IMSLDShowNode;

    showHideClass = new ShowHideClass(classNames, show, classTitle,
                                      Util.stringToBoolean(withControl));

    return result;
  }

  /**
   * Retuns the ShowHideClass component of the component model created from this
   * element.
   * @return the ShowHideClass component of this element
   */
  protected ShowHideClass getShowHideClass() {
    return showHideClass;
  }

  /**
   * Returns the title of this class.<p>This title has to be shown in the userinterface
   * when the class is collapsed and has with-control set to true.
   * @return the title of this class.
   */
  protected String getClassTitle() {
    return classTitle;
  }

  /**
   * Returns the value of the with-control attribute of the class element.<p>
   * If this attribute is not set the method returns null.
   * @return "true" if this class needs a ui control, otherwise "false".
   */
  protected String getWithControl() {
    return withControl;
  }

  /**
   * Returns the value of the class attribute of the class element.<p>
   * The attribute is optional, if it is not set the method returns null.
   * @return the value of the class attribute of the class element
   */
  protected String getClassNames() {
    return classNames;
  }
}
