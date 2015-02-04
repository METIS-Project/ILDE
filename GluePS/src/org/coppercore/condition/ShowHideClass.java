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

import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.XMLTag;
import org.coppercore.component.ClassProperty;
import org.coppercore.component.ClassPropertyDef;
import org.coppercore.component.ComponentFactory;
import org.coppercore.component.LocalPersonalContent;
import org.coppercore.exceptions.PropertyException;
import org.w3c.dom.Element;

public class ShowHideClass extends ShowHide{
  private static final long serialVersionUID = 42L;
  private String title = null;
  private boolean withControl = false;

  public ShowHideClass(String classNames, boolean show, String title, Boolean withControl ) {
    super(classNames, show);

    if (title != null) {
      this.title = title;
    }

    if (withControl != null) {
      this.withControl = withControl.booleanValue();
    }
  }

  public static ShowHideClass create(Element node) {
    boolean show = "true".equalsIgnoreCase(node.getAttribute("show"));
    Boolean withControl = Boolean.valueOf("true".equalsIgnoreCase(node.getAttribute("with-control")));
    String classNames = node.getAttribute("class");
    String title = node.getAttribute("title");
    return new ShowHideClass(classNames,show,title,withControl);
  }

  protected void performAction(Uol uol, Run run, User user, Collection firedActions) throws PropertyException  {
     String[] classIds = identifier.split(" ");

     int length = classIds.length;
     for (int i = 0; i < length; i++) {
       ClassProperty classes = ComponentFactory.getPropertyFactory().
           getClasses(uol, run, user, ClassPropertyDef.ID);
       classes.setVisibility(classIds[i],title,withControl,show);
       //Now set the visibility of the components effected by this class.
       //This code should be removed whenever a client wants to deal with the
       //effects of the class manipulation for the service and learnin-objects/
       //Default the manipulations cause effects the visibility of the components.
       Collection components = classes.getComponents(classIds[i]);

       Iterator iter = components.iterator();
       while (iter.hasNext()) {
         LocalPersonalContent component = (LocalPersonalContent)iter.next();
         component.setVisibility(show);
       }
     }
  }

  protected void toXml(PrintWriter out) {
    XMLTag tag = new XMLTag("show-hide-class");
    tag.addAttribute("class",identifier);
    tag.addAttribute("show",Boolean.valueOf(show).toString());
    if (title != null) {
      tag.addAttribute("title", title);
    }
    tag.addAttribute("with-control",Boolean.valueOf(withControl).toString());
    tag.writeEmptyTag(out);
  }
}
