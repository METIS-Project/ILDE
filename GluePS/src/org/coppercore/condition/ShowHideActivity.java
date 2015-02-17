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

import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.component.ComponentFactory;
import org.coppercore.component.LearningActivityProperty;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.TypeCastException;
import org.w3c.dom.Element;

public class ShowHideActivity extends ShowHide {
  private static final long serialVersionUID = 42L;

  public ShowHideActivity(String identifier, boolean show) {
    super(identifier,show);
  }

  public static ShowHideActivity create(Element node) {
    boolean show = "true".equalsIgnoreCase(node.getAttribute("show"));
    String id = node.getAttribute("ref");
    return new ShowHideActivity(id,show);
  }

  protected void performAction(Uol uol, Run run, User user, Collection firedActions) throws TypeCastException, PropertyException, ExpressionException {
    /**@todo check if this construct causes problems with a SupportActivity */
    LearningActivityProperty activity = ComponentFactory.getPropertyFactory().getLearningActivity(uol,run,user,identifier);
    activity.setVisibility(show);
  }


  protected void toXml(PrintWriter out) {
    toXml(out,"show-hide-activity");
  }
}
