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

import org.coppercore.common.XMLTag;

public abstract class ShowHide  extends ThenAction {
  protected String identifier = null;
  protected boolean show = true;

  public ShowHide(String identifier, boolean show) {
    this.identifier = identifier;
    this.show = show;
  }

  /**
   * HasLocalScope
   *
   * @return boolean
   */
  protected boolean hasLocalScope() {
    // all the showhide actions have a local-personal scope, so we can return false here
    return false;
  }

  protected void toXml(PrintWriter out,String tagName) {
    XMLTag tag = new XMLTag(tagName);
    tag.addAttribute("ref",identifier);
    tag.addAttribute("show",Boolean.valueOf(show).toString());
    tag.writeEmptyTag(out);
  }

}
