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
import org.coppercore.datatypes.LDDataType;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.IllegalOperandException;
import org.coppercore.exceptions.PropertyException;

public class NotExpression extends Expression {
  private static final long serialVersionUID = 42L;
  
  public NotExpression() {
    //default constructor
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
        throw new IllegalOperandException("If expression expects boolean value");
      }
      }
    }
    throw new IllegalOperandException(
        "Incorrect number of operands encountered");
  }

  public LDDataType evaluate(Uol uol, Run run, User user)
      throws PropertyException, ExpressionException {
    //check if we have the correct number of operands
    if (operands.size() == 1) {
      Operand opOne = (Operand) operands.get(0);
      return opOne.evaluate(uol, run, user).not();
    }
    throw new IllegalOperandException(
        "Incorrect number of operands encountered");
  }

  public void toXml(PrintWriter out) {
    XMLTag tag = new XMLTag(Operand.NOT);
    tag.writeOpenTag(out);

    super.toXml(out);

    tag.writeCloseTag(out);
  }

}
