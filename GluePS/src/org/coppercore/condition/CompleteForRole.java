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

package org.coppercore.condition;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;
import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.MessageList;
import org.coppercore.common.XMLTag;
import org.coppercore.component.ActProperty;
import org.coppercore.component.ActPropertyDef;
import org.coppercore.component.ActivityStructureProperty;
import org.coppercore.component.ActivityStructurePropertyDef;
import org.coppercore.component.CompletionComponentDef;
import org.coppercore.component.ComponentFactory;
import org.coppercore.component.LearningActivityProperty;
import org.coppercore.component.LearningActivityPropertyDef;
import org.coppercore.component.PlayProperty;
import org.coppercore.component.PlayPropertyDef;
import org.coppercore.component.RolePartProperty;
import org.coppercore.component.RolePartPropertyDef;
import org.coppercore.component.SupportActivityProperty;
import org.coppercore.component.SupportActivityPropertyDef;
import org.coppercore.component.UnitOfLearningProperty;
import org.coppercore.component.UnitOfLearningPropertyDef;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.TypeCastException;
import org.w3c.dom.Element;

public class CompleteForRole extends ThenAction {
  private static final long serialVersionUID = 1L;
  private String propertyId = null;
  private String type;
  private String roleId = null;

  /**
   * Constructor create the CompleteForRole action.
   *
   * @param propertyDef CompletionComponentDef the components to be completed
   * @param roleId String a space separated string containing all role ids for
   *   which the completion has to be set
   */
  public CompleteForRole(CompletionComponentDef propertyDef,
                         String roleId) {

    this.propertyId = propertyDef.getPropertyId();
    this.type = propertyDef.getDataType();
    this.roleId = roleId;

  }

  /**
   * Constructor create the CompleteForRole action.
   *
   * @param propId String
   * @param type String
   * @param roleId String a space separated string containing all role ids for
   *   which the completion has to be set
   */
  public CompleteForRole(String propId, String type, String roleId) {
    this.propertyId = propId;
    this.type = type;
    this.roleId = roleId;
  }

  public static CompleteForRole create(Element node) {
    String propId = node.getAttribute("ref");
    String type = node.getAttribute("type");
    String roleId = node.getAttribute("role-ref");
    return new CompleteForRole(propId, type, roleId);
  }

  protected void performAction(Uol uol, Run run, User user,  Collection firedActions) throws
      TypeCastException, PropertyException, ExpressionException {

    //assign the value to all users in the role with id roleId
    Collection peers = User.findByRoleId(run.getId(), roleId);

    Iterator iter = peers.iterator();
    while (iter.hasNext()) {
      User peer = (User) iter.next();

      if (LearningActivityPropertyDef.DATATYPE.equals(type)) {
        LearningActivityProperty property = ComponentFactory.getPropertyFactory().
            getLearningActivity(uol, run, peer, propertyId);
        property.complete(firedActions);
      }
      else if (SupportActivityPropertyDef.DATATYPE.equals(type)) {
        SupportActivityProperty property = ComponentFactory.getPropertyFactory().
            getSupportActivity(uol, run, peer, propertyId);
        property.complete(firedActions);
      }
      else if (ActivityStructurePropertyDef.DATATYPE.equals(type)) {
        ActivityStructureProperty property = ComponentFactory.
            getPropertyFactory().
            getActivityStructure(uol, run, peer, propertyId);
        property.complete(firedActions);
      }
      else if (RolePartPropertyDef.DATATYPE.equals(type)) {
        RolePartProperty property = ComponentFactory.getPropertyFactory().
            getRolePart(uol, run, peer, propertyId);
        property.complete(firedActions);
      }
      else if (ActPropertyDef.DATATYPE.equals(type)) {
        ActProperty property = ComponentFactory.getPropertyFactory().
            getAct(uol, run, peer, propertyId);
        property.complete(firedActions);
      }
      else if (PlayPropertyDef.DATATYPE.equals(type)) {
        PlayProperty property = ComponentFactory.getPropertyFactory().
            getPlay(uol, run, peer, propertyId);
        property.complete(firedActions);
      }
      else if (UnitOfLearningPropertyDef.DATATYPE.equals(type)) {
        UnitOfLearningProperty property = ComponentFactory.getPropertyFactory().
            getUnitOfLearning(uol, run, peer, propertyId);
        property.complete(firedActions);
      }

      /**@todo additional constructs like play, act still have to be added */
      else {
        Logger logger = Logger.getLogger(this.getClass());
        logger.error("Unknown datatype encountered \"" + type + "\"");
        throw new EJBException("Unknown datatype encountered \"" + type + "\"");

      }
    }
  }

  protected void toXml(PrintWriter out) {

    XMLTag tag = new XMLTag("complete-for-role");
    tag.addAttribute("ref", propertyId);
    tag.addAttribute("type", type);
    tag.addAttribute("role-ref", roleId);
    tag.writeEmptyTag(out);
  }


  protected boolean hasLocalScope() {
    // all these actions reflect changes for a complete group and should therefore
    // considered local
    return true;
  }


  protected boolean isValid(MessageList messages) {
    //check if the type of the property corresponds with the type of the expression
    return true;
  }

}
