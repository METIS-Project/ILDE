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

import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.XMLTag;
import org.coppercore.datatypes.LDBoolean;
import org.coppercore.datatypes.LDDataType;
import org.coppercore.datatypes.LDDateTime;
import org.coppercore.datatypes.LDDuration;
import org.coppercore.datatypes.LDInteger;
import org.coppercore.datatypes.LDReal;
import org.coppercore.datatypes.LDString;
import org.coppercore.datatypes.LDUri;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.IllegalOperandException;
import org.coppercore.exceptions.PropertyException;

public class SubtractExpression extends Expression {
  private static final long serialVersionUID = 42L;
  public SubtractExpression() {
    //default constructor
  }

  public LDDataType evaluate(Uol uol, Run run, User user)
      throws PropertyException, ExpressionException {
    //check if we have the correct number of operands
    if (operands.size() == 2) {
      //get the operands
      Operand opOne = (Operand) operands.get(0);
      Operand opTwo = (Operand) operands.get(1);
      return opOne.evaluate(uol, run, user).subtract(
          opTwo.evaluate(uol, run, user));
    }
    throw new IllegalOperandException(
        "Incorrect number of operands encountered");

  }

  public int checkType() throws ExpressionException {

    //check if we have the correct number of operands
    if (operands.size() == 2) {
      //get the operands
      Operand opOne = (Operand) operands.get(0);
      Operand opTwo = (Operand) operands.get(1);

      //check if errors are encountered
      switch (opOne.checkType()) {
      case LDDataType.LDINTEGER: {
        return LDInteger.subtract(opTwo.checkType(), opTwo);
      }
      case LDDataType.LDSTRING: {
        return LDString.subtract(opTwo.checkType(), opTwo);
      }
      case LDDataType.LDBOOLEAN: {
        return LDBoolean.subtract(opTwo.checkType(), opTwo);
      }
      case LDDataType.LDREAL: {
        return LDReal.subtract(opTwo.checkType(), opTwo);
      }
      case LDDataType.LDDATETIME: {
        return LDDateTime.subtract(opTwo.checkType(), opTwo);
      }
      case LDDataType.LDDURATION: {
        return LDDuration.subtract(opTwo.checkType(), opTwo);
      }
      case LDDataType.LDURI: {
        return LDUri.subtract(opTwo.checkType(), opTwo);
      }
      case LDDataType.LDCONSTANT: {
        //apply sum method directly on constant as we have the object during
        // parse time
        return ((PropertyConstant) opOne).getValue().subtract(opTwo);
      }
      default: {
        throw new IllegalOperandException("Unknown operand type encountered");
      }
      }
    }
    throw new IllegalOperandException(
        "Incorrect number of operands encountered");
  }

  public void toXml(PrintWriter out) {
    XMLTag tag = new XMLTag(Operand.SUBTRACT);
    tag.writeOpenTag(out);

    super.toXml(out);

    tag.writeCloseTag(out);
  }

}