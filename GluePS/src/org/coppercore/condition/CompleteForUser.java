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

package org.coppercore.condition;

import java.io.PrintWriter;
import java.util.Collection;

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

public class CompleteForUser extends ThenAction {
  private static final long serialVersionUID = 1L;
  private String propertyId = null;
  private String type;

  /**
   * Constructor create the CompleteForRole action.
   *
   * @param propertyDef CompletionComponentDef the components to be completed
   * @param roleId String a space separated string containing all role ids for
   *   which the completion has to be set
   */
  public CompleteForUser(CompletionComponentDef propertyDef) {

    this.propertyId = propertyDef.getPropertyId();
    this.type = propertyDef.getDataType();
  }

  /**
   * Constructor create the CompleteForRole action.
   *
   * @param propId String
   * @param type String
   * @param roleId String a space separated string containing all role ids for
   *   which the completion has to be set
   */
  public CompleteForUser(String propId, String type) {
    this.propertyId = propId;
    this.type = type;
  }

  public static CompleteForUser create(Element node) {
    String propId = node.getAttribute("ref");
    String type = node.getAttribute("type");
    return new CompleteForUser(propId, type);
  }

  protected void performAction(Uol uol, Run run, User user, Collection firedActions) throws
      TypeCastException, PropertyException, ExpressionException {

    if (LearningActivityPropertyDef.DATATYPE.equals(type)) {
      LearningActivityProperty property = ComponentFactory.getPropertyFactory().
          getLearningActivity(uol, run, user, propertyId);
      property.complete(firedActions);
    }
    else if (SupportActivityPropertyDef.DATATYPE.equals(type)) {
      SupportActivityProperty property = ComponentFactory.getPropertyFactory().
          getSupportActivity(uol, run, user, propertyId);
      property.complete(firedActions);
    }
    else if (ActivityStructurePropertyDef.DATATYPE.equals(type)) {
      ActivityStructureProperty property = ComponentFactory.getPropertyFactory().
        getActivityStructure(uol, run, user, propertyId);
      property.complete(firedActions);
    }
    else if (RolePartPropertyDef.DATATYPE.equals(type)) {
      RolePartProperty property = ComponentFactory.getPropertyFactory().
          getRolePart(uol, run, user, propertyId);
      property.complete(firedActions);
    }
    else if (ActPropertyDef.DATATYPE.equals(type)) {
      ActProperty property = ComponentFactory.getPropertyFactory().
          getAct(uol, run, user, propertyId);
      property.complete(firedActions);
    }
    else if (PlayPropertyDef.DATATYPE.equals(type)) {
      PlayProperty property = ComponentFactory.getPropertyFactory().
          getPlay(uol, run, user, propertyId);
      property.complete(firedActions);
    }
    else if (UnitOfLearningPropertyDef.DATATYPE.equals(type)) {
      UnitOfLearningProperty property = ComponentFactory.getPropertyFactory().
          getUnitOfLearning(uol, run, user, propertyId);
      property.complete(firedActions);
    }
    else {
      Logger logger = Logger.getLogger(this.getClass());
      logger.error("Unknown datatype encounterd \"" + type + "\"");
      throw new EJBException("Unknown datatype encounterd \"" + type + "\"");
    }
  }

  protected void toXml(PrintWriter out) {
    XMLTag tag = new XMLTag("complete-for-user");
    tag.addAttribute("ref", propertyId);
    tag.addAttribute("type", type);
    tag.writeEmptyTag(out);
  }


  protected boolean hasLocalScope() {
    // all these actions reflect changes for a local personal property
    return false;
  }

  protected boolean isValid(MessageList messages) {
    //check if the type of the property corresponds with the type of the expression
    return true;
  }
}
