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

import java.math.BigInteger;

import org.coppercore.exceptions.DivideByZeroException;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.IllegalOperandException;
import org.coppercore.exceptions.RestrictionViolationException;
import org.coppercore.exceptions.TypeCastException;
import org.coppercore.expression.Operand;
import org.coppercore.expression.PropertyConstant;

public class LDInteger
    extends LDDataType {
  Long value = null;

  public LDInteger(String aValue) throws TypeCastException {
    try {
      value = Long.valueOf(aValue);
    }
    catch (NumberFormatException ex) {
      throw new TypeCastException(ex);
    }
  }

  protected LDInteger(long aValue) throws TypeCastException {
    value = new Long(aValue);
  }

  public int getType() {
    return LDINTEGER;
  }

  public String getTypeName() {
    return LDINTEGER_NAME;
  }

  public LDInteger toLDInteger() throws TypeCastException {
    return this;
  }

  /**
   * Returns the value of this datatype in the native format
   *
   * @return String
   */
  public Long getValue() {
    return value;
  }

  public Long getAbsoluteValue() {
    return new Long(Math.abs(getValue().longValue()));
  }

  public String toString() {
    return value.toString();
  }

  public boolean equals(LDDataType obj) {
    if (this == obj) {
      return true;
    }
    if (! (obj instanceof LDInteger)) {
      return false;
    }
    LDInteger that = (LDInteger) obj;
    if (! (that.value == null ? this.value == null :
           that.value.equals(this.value))) {
      return false;
    }
    return true;
  }

  public static int sum(int operandType, Operand operand) throws
      ExpressionException {

    switch (operandType) {
      case LDINTEGER: {
        return LDINTEGER;
      }
      case LDREAL: {
        return LDREAL;
      }
      case LDCONSTANT: {
        //first try to typecast to LDInteger, if this fails, try to typecast
        //to LDReal.
        try {
          ( (PropertyConstant) operand).getValue().toLDInteger();
          return LDINTEGER;
        }
        catch (TypeCastException ex) {
          ( (PropertyConstant) operand).getValue().toLDReal();
          return LDREAL;
        }
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType sum(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDINTEGER: {
        return new LDInteger(getValue().longValue() +
                             ( (LDInteger) operand).getValue().longValue());
      }
      case LDREAL: {
        return toLDReal().sum(operand);
      }
      case LDCONSTANT: {
        //first try to typecast to LDInteger, if this fails, try to typecast
        //to LDReal.
        try {
          LDInteger op = ( (LDConstant) operand).toLDInteger();
          return new LDInteger(getValue().longValue() + op.getValue().longValue());
        }
        catch (TypeCastException ex) {
          LDReal op = ( (LDConstant) operand).toLDReal();
          return new LDReal(toLDReal().getValue().doubleValue() +
                            op.getValue().doubleValue());
        }
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public static int subtract(int operandType, Operand operand) throws
      ExpressionException {

    switch (operandType) {
      case LDINTEGER: {
        return LDINTEGER;
      }
      case LDREAL: {
        return LDREAL;
      }
      case LDCONSTANT: {
        //first try to typecast to LDInteger, if this fails, try to typecast
        //to LDReal
        try {
          ( (PropertyConstant) operand).getValue().toLDInteger();
          return LDINTEGER;
        }
        catch (TypeCastException ex) {
          ( (PropertyConstant) operand).getValue().toLDReal();
          return LDREAL;
        }
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType subtract(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDINTEGER: {
        return new LDInteger(getValue().longValue() -
                             ( (LDInteger) operand).getValue().longValue());
      }
      case LDREAL: {
        return new LDReal(toLDReal().getValue().doubleValue() -
                          ( (LDReal) operand).getValue().doubleValue());
      }
      case LDCONSTANT: {
        //first try to typecast to LDInteger, if this fails, try to typecast
        //to LDReal.
        try {
          LDInteger op = ( (LDConstant) operand).toLDInteger();
          return new LDInteger(getValue().longValue() - op.getValue().longValue());
        }
        catch (TypeCastException ex) {
          LDReal op = ( (LDConstant) operand).toLDReal();
          return new LDReal(toLDReal().getValue().doubleValue() -
                            op.getValue().doubleValue());
        }
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public static int multiply(int operandType, Operand operand) throws
      ExpressionException {

    switch (operandType) {
      case LDINTEGER: {
        return LDINTEGER;
      }
      case LDREAL: {
        return LDREAL;
      }
      case LDCONSTANT: {
        //first try to typecast to LDInteger, if this fails, try to typecast
        //to LDReal.
        try {
          ( (PropertyConstant) operand).getValue().toLDInteger();
          return LDINTEGER;
        }
        catch (TypeCastException ex) {
          ( (PropertyConstant) operand).getValue().toLDReal();
          return LDREAL;
        }

      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType multiply(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDINTEGER: {
        return new LDInteger(getValue().longValue() *
                             ( (LDInteger) operand).getValue().longValue());
      }
      case LDREAL: {
        return toLDReal().multiply(operand);
      }
      case LDCONSTANT: {
        //first try to typecast to LDInteger, if this fails, try to typecast
        //to LDReal.
        try {
          LDInteger op = ( (LDConstant) operand).toLDInteger();
          return new LDInteger(getValue().longValue() * op.getValue().longValue());
        }
        catch (TypeCastException ex) {
          LDReal op = ( (LDConstant) operand).toLDReal();
          return new LDReal(toLDReal().getValue().doubleValue() *
                            op.getValue().doubleValue());
        }
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public static int divide(int operandType, Operand operand) throws
      ExpressionException {

    switch (operandType) {
      case LDINTEGER: {
        return LDINTEGER;
      }
      case LDREAL: {
        return LDREAL;
      }
      case LDCONSTANT: {
        //first try to typecast to LDInteger, if this fails, try to typecast
        //to LDReal.
        try {
          ( (PropertyConstant) operand).getValue().toLDInteger();
          return LDINTEGER;
        }
        catch (TypeCastException ex) {
          ( (PropertyConstant) operand).getValue().toLDReal();
          return LDREAL;
        }

      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType divide(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDINTEGER: {
        long op1 = getValue().longValue();
        long op2 = ( (LDInteger) operand).getValue().longValue();
        if (op2 != 0) {
          return new LDInteger(op1 / op2);
        }
        throw new DivideByZeroException("Division by zero not allowed");
      }
      case LDREAL: {
        double op1 = getValue().doubleValue();
        double op2 = ( (LDReal) operand).getValue().doubleValue();
        if (op2 != 0) {
          return new LDReal(op1 / op2);
        }
        throw new DivideByZeroException("Division by zero not allowed");
      }
      case LDCONSTANT: {
        //first try to typecast to LDInteger, if this fails, try to typecast
        //to LDReal.
        try {
          long op1 = getValue().longValue();
          long op2 = ( (LDConstant) operand).toLDInteger().getValue().longValue();
          if (op2 != 0) {
            return new LDInteger(op1 / op2);
          }
          throw new DivideByZeroException("Division by zero not allowed");
        }
        catch (TypeCastException ex) {
          double op1 = getValue().doubleValue();
          double op2 = ( (LDConstant) operand).toLDReal().getValue().
              doubleValue();
          if (op2 != 0) {
            return new LDReal(op1 / op2);
          }
          throw new DivideByZeroException("Division by zero not allowed");
        }
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public static int lessThan(int operandType, Operand operand) throws
      ExpressionException {

    switch (operandType) {
      case LDINTEGER: {
        return LDBOOLEAN;
      }
      case LDREAL: {
        return LDBOOLEAN;
      }
      case LDCONSTANT: {
        //first try to typecast to LDInteger, if this fails, try to typecast
        //to LDReal.
        try {
          ( (PropertyConstant) operand).getValue().toLDInteger();
          return LDBOOLEAN;
        }
        catch (TypeCastException ex) {
          ( (PropertyConstant) operand).getValue().toLDReal();
          return LDBOOLEAN;
        }
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType lessThan(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDINTEGER: {
        return new LDBoolean(getValue().longValue() <
                             ( (LDInteger) operand).getValue().longValue());
      }
      case LDREAL: {
        return new LDBoolean(getValue().doubleValue() <
                             ( (LDReal) operand).getValue().doubleValue());

      }
      case LDCONSTANT: {
        //first try to typecast to LDInteger, if this fails, try to typecast
        //to LDReal.
        try {
          LDInteger op = ( (LDConstant) operand).toLDInteger();
          return new LDBoolean(getValue().longValue() < op.getValue().longValue());
        }
        catch (TypeCastException ex) {
          LDReal op = ( (LDConstant) operand).toLDReal();
          return new LDBoolean(getValue().doubleValue() <
                               op.getValue().doubleValue());
        }
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public static int greaterThan(int operandType, Operand operand) throws
      ExpressionException {

    switch (operandType) {
      case LDINTEGER: {
        return LDBOOLEAN;
      }
      case LDREAL: {
        return LDBOOLEAN;
      }
      case LDCONSTANT: {
        //first try to typecast to LDInteger, if this fails, try to typecast
        //to LDReal.
        try {
          ( (PropertyConstant) operand).getValue().toLDInteger();
          return LDBOOLEAN;
        }
        catch (TypeCastException ex) {
          ( (PropertyConstant) operand).getValue().toLDReal();
          return LDBOOLEAN;
        }
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType greaterThan(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDINTEGER: {
        return new LDBoolean(getValue().longValue() >
                             ( (LDInteger) operand).getValue().longValue());
      }
      case LDREAL: {
        return new LDBoolean(getValue().doubleValue() >
                             ( (LDReal) operand).getValue().doubleValue());

      }
      case LDCONSTANT: {
        //first try to typecast to LDInteger, if this fails, try to typecast
        //to LDReal.
        try {
          LDInteger op = ( (LDConstant) operand).toLDInteger();
          return new LDBoolean(getValue().longValue() > op.getValue().longValue());
        }
        catch (TypeCastException ex) {
          LDReal op = ( (LDConstant) operand).toLDReal();
          return new LDBoolean(getValue().doubleValue() >
                               op.getValue().doubleValue());
        }
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public static int is(int operandType, Operand operand) throws
      ExpressionException {

    switch (operandType) {
      case LDINTEGER: {
        return LDBOOLEAN;
      }
      case LDREAL: {
        return LDBOOLEAN;
      }
      case LDCONSTANT: {
        ( (PropertyConstant) operand).getValue().toLDInteger();
        return LDBOOLEAN;
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType is(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDREAL: {
        return operand.toLDReal().is(this);
      }
      case LDINTEGER: {
        return new LDBoolean(equals(operand));
      }
      case LDCONSTANT: {
        return new LDBoolean(equals(operand.toLDInteger()));
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  /**
   * Returns true when the value equals the restriction value. If the value equals
   * null the method returns true.
   *
   * @param restrictionValue String the value to be matched
   * @throws RestrictionViolationException when this restriction does not apply
   *   to this data type
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean enumeration(String restrictionValue) throws
      RestrictionViolationException {

    try {
      return (getValue().equals(Long.valueOf(restrictionValue)));
    }
    catch (NumberFormatException ex) {
      throw new RestrictionViolationException(restrictionValue +
                                              " cannot not be applied to an Integer enumeration");
    }
  }


  /**
   * Returns true when the number of digits of the passed Integer value equals
   * the required number which was passed as parameter or if the passed value is
   * NULL.. False is returned otherwise.
   *
   * @param digits int the number of digits required
   * @throws RestrictionViolationException when this restriction does not apply
   *   to this data type or if the value passed can be converted to Long
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean totalDigits(int digits) throws
      RestrictionViolationException {

    if (digits ==  new BigInteger(toString()).toString().length()) {
      return true;
    }
    throw new RestrictionViolationException("The number of digits for \""+ toString() + "\"must be " + digits);
  }


  /**
   * Returns true if the passed Integer is greater or equal than the passed
   * lowerbound or if the passed value is NULL. Return false otherwise.
   *
   * @param restrictionValue String the lower bound that should be included
   * @throws RestrictionViolationException when this restriction does not apply
   *   to this data type or if the passed value can not be converted to Long
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean minInclusive(String restrictionValue) throws
      RestrictionViolationException {
    try {
      long lowerBound = Long.parseLong(restrictionValue);

      if (getValue().longValue() >= lowerBound) {
        return true;
      }
      throw new RestrictionViolationException("Integer value " + toString() +
          " should be greater or equal than " + restrictionValue);
    }
    catch (NumberFormatException ex) {
      throw new RestrictionViolationException(restrictionValue +
                                              " cannot not be used as lower bound for an Integer");
    }
  }

  /**
   * Returns true if the passed Integer is less or equal to the passed
   * upperbound or if the passed value is NULL. Return false otherwise.
   *
   * @param restrictionValue String the upper bound that should be included
   * @throws RestrictionViolationException when this restriction does not apply
   *   to this data type or if the passed value can not be converted to Long
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean maxInclusive(String restrictionValue) throws
      RestrictionViolationException {
    try {
      long upperBound = Long.parseLong(restrictionValue);

      if (getValue().longValue() <= upperBound) {
        return true;
      }
      throw new RestrictionViolationException("Integer value " + toString() +
          " should be less or equal than " + restrictionValue);
    }
    catch (NumberFormatException ex) {
      throw new RestrictionViolationException(restrictionValue +
                                              " cannot not be used as lower bound for an Integer");
    }

  }

  /**
   * Returns true if the passed Integer is greater than the passed lowerbound or
   * if the passed value is NULL. Return false otherwise.
   *
   * @param restrictionValue String the lower bound that should not be included
   * @throws RestrictionViolationException when this restriction does not apply
   *   to this data type or if the passed value can not be converted to Long
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean minExclusive(String restrictionValue) throws
      RestrictionViolationException {
    try {
      long lowerBound = Long.parseLong(restrictionValue);

      if (getValue().longValue() > lowerBound) {
        return true;
      }
      throw new RestrictionViolationException("Integer value " + toString() +
                                              " should be greater than " +
                                              restrictionValue);
    }
    catch (NumberFormatException ex) {
      throw new RestrictionViolationException(restrictionValue +
                                              " cannot not be used as lower bound for an Integer");
    }

  }

  /**
   * Returns true if the passed Integer is less or equal to the passed
   * upperbound or if the passed value is NULL. Return false otherwise.
   *
   * @param restrictionValue String the upper bound that should not be included
   * @throws RestrictionViolationException when this restriction does not apply
   *   to this data type or if the passed value can not be converted to Long
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean maxExclusive(String restrictionValue) throws
      RestrictionViolationException {
    try {
      long upperBound = Long.parseLong(restrictionValue);

      if (getValue().longValue() < upperBound) {
        return true;
      }
      throw new RestrictionViolationException("Integer value " + toString() +
                                              " should be less than " +
                                              restrictionValue);
    }
    catch (NumberFormatException ex) {
      throw new RestrictionViolationException(restrictionValue +
                                              " cannot not be used as lower bound for an Integer");
    }
  }
}
