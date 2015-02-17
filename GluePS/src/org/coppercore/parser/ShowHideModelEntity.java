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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;
import org.coppercore.common.MessageList;
import org.coppercore.condition.ShowHideActivity;
import org.coppercore.condition.ShowHideActivityStructure;
import org.coppercore.condition.ShowHideEnvironment;
import org.coppercore.condition.ShowHideItem;
import org.coppercore.condition.ShowHidePlay;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class is the base class for all IMS learning Design elements that 
 * show or hide other elements.
 * <p>
 * These elements are:
 * <ul>
 * <li>show</li>
 * <li>hide</li>
 * </ul>
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.8 $, $Date: 2005/09/27 21:19:54 $
 */
public class ShowHideModelEntity
    extends IMSLDNode {

  private ArrayList referedObjects = new ArrayList();
  private ArrayList classObjects = new ArrayList();
  private ArrayList thenActions = new ArrayList();

  /**
   * Constructs a ShowHideModelEntity instance from the passed xml dom element.
   * 
   * @param aNode
   *          the xml dom node to parse
   * @param aParent
   *          the parsed IMS learning design element that is the parent element of this object
   */
  public ShowHideModelEntity(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("class")) {
      classObjects.add(addElement(new IMSLDClassNode(childNode, this)));
    }
    else if (nodeName.equals("item-ref")) {
      referedObjects.add(childNode.getAttribute("ref"));
    }
    else if (nodeName.equals("environment-ref")) {
      referedObjects.add(childNode.getAttribute("ref"));
    }
    else if (nodeName.equals("learning-activity-ref")) {
      referedObjects.add(childNode.getAttribute("ref"));
    }
    else if (nodeName.equals("support-activity-ref")) {
      referedObjects.add(childNode.getAttribute("ref"));
    }
    else if (nodeName.equals("activity-structure-ref")) {
      referedObjects.add(childNode.getAttribute("ref"));
    }
    else if (nodeName.equals("play-ref")) {
      referedObjects.add(childNode.getAttribute("ref"));
    }
    else if (nodeName.equals("unit-of-learning-href")) {
      //not implemented yet
    }
  }

  protected void resolveReferences() throws Exception {
    lookupAll(referedObjects);

    super.resolveReferences();
  }

  void buildClassList(HashSet classes) {
    Iterator myIterator = referedObjects.iterator();

     while (myIterator.hasNext()) {
       IMSLDNode referedObject = (IMSLDNode) myIterator.next();
       if (referedObject instanceof IMSLDClassNode) {
         String[] classNames =((IMSLDClassNode) referedObject).getClassNames().split(" ");

         int length = classNames.length;
         for (int i = 0; i < length; i++) {
           classes.add(classNames[i]);
         }
       }
     }
  }


  protected void buildItemReferenceList(HashSet referencedItems) {

   Iterator myIterator = referedObjects.iterator();

    while (myIterator.hasNext()) {
      IMSLDNode referedObject = (IMSLDNode) myIterator.next();
      if (referedObject instanceof IMSLDItemNode) {
        referencedItems.add(referedObject.getIdentifier());
      }
    }
  }


  protected boolean buildComponentModel(int uolId, MessageList messages) {
    boolean result = super.buildComponentModel(uolId, messages);

    Iterator iter = classObjects.iterator();
    while (iter.hasNext()) {
      IMSLDClassNode classNode = (IMSLDClassNode) iter.next();

      thenActions.add(classNode.getShowHideClass());
    }

    boolean show = this instanceof IMSLDShowNode;

    iter = referedObjects.iterator();
    while (iter.hasNext()) {
      IMSLDNode referedObject = (IMSLDNode) iter.next();

      if (referedObject instanceof IMSLDItemNode) {
        try {
          String identifier = referedObject.getIdentifier();
          String tuple[] = getManifest().getLDLearningDesignNode().getComponentIdForItem(
              identifier);
          //TODO FIX BUG DEALING WITH HIDING ITEMS
          thenActions.add(new ShowHideItem(identifier, tuple[0], tuple[1], show));         
        }
        catch (Exception ex) {
          Logger logger = Logger.getLogger(this.getClass());
          logger.error(ex);
          throw new EJBException(ex);

        }
      }
      else if (referedObject instanceof IMSLDEnvironmentNode) {
        thenActions.add(new ShowHideEnvironment(referedObject.getIdentifier(), show));
      }
      else if (referedObject instanceof IMSLDLearningActivityNode) {
        thenActions.add(new ShowHideActivity(referedObject.getIdentifier(), show));
      }
      else if (referedObject instanceof IMSLDActivityStructureNode) {
        thenActions.add(new ShowHideActivityStructure(referedObject.getIdentifier(),
            show));
      }
      else if (referedObject instanceof IMSLDPlayNode) {
        thenActions.add(new ShowHidePlay(referedObject.getIdentifier(), show));
      }
      /*
             else if (node instanceof IMSLDUnitOfLearningNode) {
             } */

    }
    return result;
  }

  Collection getThenActions() {
    return thenActions;
  }

}
