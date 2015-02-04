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

package org.coppercore.expression;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.XMLTag;
import org.coppercore.datatypes.LDDataType;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.IllegalOperandException;
import org.coppercore.exceptions.PropertyException;

public class CountExpression extends Expression {
  private static final long serialVersionUID = 1L;

  public CountExpression() {
    //default constructor
  }

  public int checkType() throws ExpressionException {
    //check if we have the correct number of operands
      //check all operands

      Iterator iter = operands.iterator();
      while (iter.hasNext()) {
        Operand operand = (Operand)iter.next();
        //check if errors are encountered
        switch (operand.checkType()) {
          case LDDataType.LDBOOLEAN: {
            //that is ok, no further action needed
            break;
          }
          case LDDataType.LDCONSTANT: {
            ((PropertyConstant) operand).getValue().toLDBoolean();
            //no error thrown, so this one is ok
            break;
          }
          default: {
            throw new IllegalOperandException("Boolean operand type required");
          }
        }
      }
      //all operands where LDBoolean, so everything is ok
      return LDDataType.LDINTEGER;
    }



  /** @todo complete list with complete actions checks */
  public LDDataType evaluate(Uol uol, Run run, User user) throws PropertyException {
      LDDataType operandValue = null;
      ArrayList operandValues = new ArrayList();

      Iterator iter = operands.iterator();
      while (iter.hasNext()) {
        Operand operand = (Operand)iter.next();
        operandValue = operand.evaluate(uol,run,user);
        operandValues.add(operandValue);
      }
      return operandValue.count(operandValues);
  }

  public void toXml(PrintWriter out) {
    XMLTag tag = new XMLTag(Operand.COUNT);
    tag.writeOpenTag(out);

    super.toXml(out);

    tag.writeCloseTag(out);
  }
}
