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

package org.coppercore.datatypes;

import java.util.Collection;
import java.util.Iterator;

import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.IllegalOperandException;
import org.coppercore.exceptions.TypeCastException;
import org.coppercore.expression.Operand;
import org.coppercore.expression.PropertyConstant;

public class LDBoolean
    extends LDDataType {
  Boolean value = null;

  public LDBoolean(String aValue) throws TypeCastException {
    if (aValue.equalsIgnoreCase("true") || aValue.equals("1")) {
      value = Boolean.TRUE;
    }
    else if (aValue.equalsIgnoreCase("false") || aValue.equals("0")) {
      value = Boolean.FALSE;
    }
    else {
      throw new TypeCastException("Invalid boolean value \"" + aValue + "\" encounterd. Allowed values are 1,true,0,false");
    }
  }

  public LDBoolean(boolean aValue) throws TypeCastException {
    value = Boolean.valueOf(aValue);
  }

  public int getType() {
    return LDBOOLEAN;
  }

  public String getTypeName() {
    return LDBOOLEAN_NAME;
  }

  public LDBoolean toLDBoolean() throws TypeCastException {
    return this;
  }

  /**
   * Returns the value of this datatype in the native format
   *
   * @return String
   */
  public Boolean getValue() {
    return value;
  }

  public String toString() {
    return value.toString();
  }

  public boolean equals(LDDataType obj) {
    if (this == obj) {
      return true;
    }
    if (! (obj instanceof LDBoolean)) {
      return false;
    }
    LDBoolean that = (LDBoolean) obj;
    if (! (that.value == null ? this.value == null :
           that.value.equals(this.value))) {
      return false;
    }
    return true;
  }


  public static int is(int operandType, Operand operand) throws
      ExpressionException {

    switch (operandType) {
      case LDBOOLEAN: {
        return LDBOOLEAN;
      }
      case LDCONSTANT: {
        ( (PropertyConstant) operand).getValue().toLDBoolean();
        return LDBOOLEAN;
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType is(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDBOOLEAN: {
        return new LDBoolean(equals(operand));
      }
      case LDCONSTANT: {
        return new LDBoolean(equals(operand.toLDBoolean()));
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType not() throws ExpressionException {
    /*
    switch (operand.getType()) {
      case LDBOOLEAN: {
        return new LDBoolean(! ( (LDBoolean) operand).getValue().booleanValue());
      }
      case LDCONSTANT: {
        return new LDBoolean(! (operand.toLDBoolean().getValue().booleanValue()));
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
        */
    return new LDBoolean(!getValue().booleanValue());
  }

  public LDDataType and(Collection operands) throws ExpressionException {
    boolean result = true;
    Iterator iter = operands.iterator();
    while (iter.hasNext()) {
      LDDataType operand = (LDDataType) iter.next();

      switch (operand.getType()) {
        case LDBOOLEAN: {
          result &= ( (LDBoolean) operand).getValue().booleanValue();
          break;
        }
        case LDCONSTANT: {
          result &= operand.toLDBoolean().getValue().booleanValue();
          break;
        }
        default: {
          throw new IllegalOperandException("Illegal type cast");
        }
      }
    }
    return new LDBoolean(result);
  }

  public LDDataType or(Collection operands) throws ExpressionException {
    boolean result = false;
    Iterator iter = operands.iterator();
    while (iter.hasNext()) {
      LDDataType operand = (LDDataType) iter.next();

      switch (operand.getType()) {
        case LDBOOLEAN: {
          result |= ( (LDBoolean) operand).getValue().booleanValue();
          break;
        }
        case LDCONSTANT: {
          result |= operand.toLDBoolean().getValue().booleanValue();
          break;
        }
        default: {
          throw new IllegalOperandException("Illegal type cast");
        }
      }
    }
    return new LDBoolean(result);
  }


  public LDDataType count(Collection operands) throws ExpressionException {
    long result = 0;
    Iterator iter = operands.iterator();
    while (iter.hasNext()) {
      LDDataType operand = (LDDataType) iter.next();

      switch (operand.getType()) {
        case LDBOOLEAN: {
          result += ( (LDBoolean) operand).getValue().booleanValue() ? 1 : 0;
          break;
        }
        case LDCONSTANT: {
          result += ( (LDConstant) operand).toLDBoolean().getValue().booleanValue() ? 1 : 0;
          break;
        }
        default: {
          throw new IllegalOperandException("Illegal type cast");
        }
      }
    }
    return new LDInteger(result);

  }

}
