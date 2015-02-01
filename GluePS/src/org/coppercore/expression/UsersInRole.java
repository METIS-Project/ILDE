/*
 * CopperCore, an IMS-LD level C engine Copyright (C) 2003 Harrie Martens and
 * Hubert Vogten
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program (/license.txt); if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * 
 * Contact information: Open University of the Netherlands Valkenburgerweg 177
 * Heerlen PO Box 2960 6401 DL Heerlen e-mail: hubert.vogten@ou.nl or
 * harrie.martens@ou.nl
 * 
 * 
 * Open Universiteit Nederland, hereby disclaims all copyright interest in the
 * program CopperCore written by Harrie Martens and Hubert Vogten
 * 
 * prof.dr. Rob Koper, director of learning technologies research and
 * development
 *  
 */

package org.coppercore.expression;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.XMLTag;
import org.coppercore.datatypes.LDBoolean;
import org.coppercore.datatypes.LDDataType;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.IllegalOperandException;
import org.coppercore.exceptions.PropertyException;

public class UsersInRole extends Expression {
  private static final long serialVersionUID = 42L;
  String roleId = null;

  public UsersInRole(String roleId) {
    this.roleId = roleId;
  }

  public int checkType() throws ExpressionException {
    //check if we have the correct number of operands
    if (operands.size() == 1) {
      //get the operand
      Operand operand = (Operand) operands.get(0);

      switch (operand.checkType()) {
      case LDDataType.LDBOOLEAN: {
        return LDDataType.LDBOOLEAN;
      }
      case LDDataType.LDCONSTANT: {
        ((PropertyConstant) operand).getValue().toLDBoolean();
        return LDDataType.LDBOOLEAN;
      }
      default: {
        throw new IllegalOperandException("UsersInRole expression expects boolean value");
      }
      }
    }
    throw new IllegalOperandException("Incorrect number of operands encountered");
  }

  public LDDataType evaluate(Uol uol, Run run, User user) throws PropertyException, ExpressionException {
    boolean result = true;
    //check if we have the correct number of operands
    if (operands.size() == 1) {
      Operand opOne = (Operand) operands.get(0);

      Collection peers = User.findByRoleId(run.getId(), roleId);
      Iterator iter = peers.iterator();
      while (iter.hasNext()) {
        User peer = (User) iter.next();
        result &= opOne.evaluate(uol, run, peer).toLDBoolean().getValue().booleanValue();
        if (!result) {
          break;
        }
      }
      return new LDBoolean(result);
    }
    throw new IllegalOperandException("Incorrect number of operands encountered");
  }

  public void toXml(PrintWriter out) {
    XMLTag tag = new XMLTag(Operand.USERSINROLE);
    tag.addAttribute("role-ref", roleId);
    tag.writeOpenTag(out);

    super.toXml(out);

    tag.writeCloseTag(out);
  }
}