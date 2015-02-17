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

package org.coppercore.datatypes;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.coppercore.exceptions.DivideByZeroException;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.IllegalOperandException;
import org.coppercore.exceptions.RestrictionViolationException;
import org.coppercore.exceptions.TypeCastException;
import org.coppercore.expression.Operand;
import org.coppercore.expression.PropertyConstant;

public class LDReal extends LDDataType {
  Double value = null;

  public LDReal(String aValue) throws TypeCastException {
    try {
      value = Double.valueOf(aValue);
    } catch (NumberFormatException ex) {
      throw new TypeCastException(ex);
    }
  }

  protected LDReal(double aValue) throws TypeCastException {
    value = new Double(aValue);
  }

  public int getType() {
    return LDREAL;
  }

  public String getTypeName() {
    return LDREAL_NAME;
  }

  public LDReal toLDReal() throws TypeCastException {
    return this;
  }

  /**
   * Returns the value of this datatype in the native format
   * 
   * @return String
   */
  public Double getValue() {
    return value;
  }

  public String toString() {
    return value.toString();
  }

  public boolean equals(LDDataType obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof LDReal)) {
      return false;
    }
    LDReal that = (LDReal) obj;
    if (!(that.value == null ? this.value == null : that.value.equals(this.value))) {
      return false;
    }
    return true;
  }

  public static int sum(int operandType, Operand operand) throws ExpressionException {

    switch (operandType) {
    case LDREAL: {
      return LDREAL;
    }

    case LDINTEGER: {
      return LDREAL;
    }
    case LDCONSTANT: {
      ((PropertyConstant) operand).getValue().toLDReal();
      return LDREAL;
    }
    default: {
      throw new IllegalOperandException("Illegal type cast");
    }
    }
  }

  public LDDataType sum(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
    case LDREAL: {
      return new LDReal(getValue().doubleValue() + ((LDReal) operand).getValue().doubleValue());
    }
    case LDINTEGER: {
      return new LDReal(getValue().doubleValue() + ((LDInteger) operand).getValue().doubleValue());
    }
    case LDCONSTANT: {
      return new LDReal(getValue().doubleValue() + operand.toLDReal().getValue().doubleValue());
    }
    default: {
      throw new IllegalOperandException("Illegal type cast");
    }
    }
  }

  public static int subtract(int operandType, Operand operand) throws ExpressionException {

    switch (operandType) {
    case LDREAL: {
      return LDREAL;
    }

    case LDINTEGER: {
      return LDREAL;
    }
    case LDCONSTANT: {
      ((PropertyConstant) operand).getValue().toLDReal();
      return LDREAL;
    }
    default: {
      throw new IllegalOperandException("Illegal type cast");
    }
    }
  }

  public LDDataType subtract(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
    case LDREAL: {
      return new LDReal(getValue().doubleValue() - ((LDReal) operand).getValue().doubleValue());
    }
    case LDINTEGER: {
      return new LDReal(getValue().doubleValue() - ((LDInteger) operand).getValue().doubleValue());
    }
    case LDCONSTANT: {
      return new LDReal(getValue().doubleValue() - operand.toLDReal().getValue().doubleValue());
    }
    default: {
      throw new IllegalOperandException("Illegal type cast");
    }
    }
  }

  public static int multiply(int operandType, Operand operand) throws ExpressionException {

    switch (operandType) {
    case LDREAL: {
      return LDREAL;
    }

    case LDINTEGER: {
      return LDREAL;
    }
    case LDCONSTANT: {
      ((PropertyConstant) operand).getValue().toLDReal();
      return LDREAL;
    }
    default: {
      throw new IllegalOperandException("Illegal type cast");
    }
    }
  }

  public LDDataType multiply(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
    case LDREAL: {
      return new LDReal(getValue().doubleValue() * ((LDReal) operand).getValue().doubleValue());
    }
    case LDINTEGER: {
      return new LDReal(getValue().doubleValue() * ((LDInteger) operand).getValue().doubleValue());
    }
    case LDCONSTANT: {
      return new LDReal(getValue().doubleValue() * operand.toLDReal().getValue().doubleValue());
    }
    default: {
      throw new IllegalOperandException("Illegal type cast");
    }
    }
  }

  public static int divide(int operandType, Operand operand) throws ExpressionException {

    switch (operandType) {
    case LDINTEGER: {
      return LDREAL;
    }
    case LDREAL: {
      return LDREAL;
    }
    case LDCONSTANT: {
      ((PropertyConstant) operand).getValue().toLDReal();
      return LDREAL;
    }
    default: {
      throw new IllegalOperandException("Illegal type cast");
    }
    }
  }

  public LDDataType divide(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
    case LDINTEGER: {
      double op1 = getValue().doubleValue();
      double op2 = ((LDInteger) operand).getValue().doubleValue();
      if (op2 != 0) {
        return new LDReal(op1 / op2);
      }
      throw new DivideByZeroException("Division by zero not allowed");
    }
    case LDREAL: {
      double op1 = getValue().doubleValue();
      double op2 = ((LDReal) operand).getValue().doubleValue();
      if (op2 != 0) {
        return new LDReal(op1 / op2);
      }
      throw new DivideByZeroException("Division by zero not allowed");
    }
    case LDCONSTANT: {
      double op1 = toLDReal().getValue().doubleValue();
      double op2 = ((LDConstant) operand).toLDReal().getValue().doubleValue();
      if (op2 != 0) {
        return new LDReal(op1 / op2);
      }
      throw new DivideByZeroException("Division by zero not allowed");
    }
    default: {
      throw new IllegalOperandException("Illegal type cast");
    }
    }
  }

  public static int lessThan(int operandType, Operand operand) throws ExpressionException {

    switch (operandType) {
    case LDINTEGER: {
      return LDBOOLEAN;
    }
    case LDREAL: {
      return LDBOOLEAN;
    }
    case LDCONSTANT: {
      ((PropertyConstant) operand).getValue().toLDReal();
      return LDBOOLEAN;
    }
    default: {
      throw new IllegalOperandException("Illegal type cast");
    }
    }
  }

  public LDDataType lessThan(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
    case LDINTEGER: {
      return new LDBoolean(getValue().doubleValue() < ((LDInteger) operand).getValue().doubleValue());
    }
    case LDREAL: {
      return new LDBoolean(getValue().doubleValue() < ((LDReal) operand).getValue().doubleValue());

    }
    case LDCONSTANT: {
      LDReal op = ((LDConstant) operand).toLDReal();
      return new LDBoolean(getValue().doubleValue() < op.getValue().doubleValue());
    }
    default: {
      throw new IllegalOperandException("Illegal type cast");
    }
    }
  }

  public static int greaterThan(int operandType, Operand operand) throws ExpressionException {

    switch (operandType) {
    case LDINTEGER: {
      return LDBOOLEAN;
    }
    case LDREAL: {
      return LDBOOLEAN;
    }
    case LDCONSTANT: {
      ((PropertyConstant) operand).getValue().toLDReal();
      return LDBOOLEAN;
    }
    default: {
      throw new IllegalOperandException("Illegal type cast");
    }
    }
  }

  public LDDataType greaterThan(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
    case LDINTEGER: {
      return new LDBoolean(getValue().doubleValue() > ((LDInteger) operand).getValue().doubleValue());
    }
    case LDREAL: {
      return new LDBoolean(getValue().doubleValue() > ((LDReal) operand).getValue().doubleValue());

    }
    case LDCONSTANT: {
      LDReal op = ((LDConstant) operand).toLDReal();
      return new LDBoolean(getValue().doubleValue() > op.getValue().doubleValue());
    }
    default: {
      throw new IllegalOperandException("Illegal type cast");
    }
    }
  }

  public static int is(int operandType, Operand operand) throws ExpressionException {

    switch (operandType) {
    case LDREAL: {
      return LDBOOLEAN;
    }
    case LDINTEGER: {
      return LDBOOLEAN;
    }
    case LDCONSTANT: {
      // 06-02-2009: fixed to use proper type cast
      ((PropertyConstant) operand).getValue().toLDReal();
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
      return new LDBoolean(equals(operand));
    }
    case LDINTEGER: {
      return new LDBoolean(equals(operand.toLDReal()));
    }
    case LDCONSTANT: {
      return new LDBoolean(equals(operand.toLDReal()));
    }
    default: {
      throw new IllegalOperandException("Illegal type cast");
    }
    }
  }

  /**
   * Returns true when the value equals the restriction value. If the value
   * equals null the method returns true.
   * 
   * @param restrictionValue
   *          String the value to be matched
   * @throws RestrictionViolationException
   *           when this restriction does not apply to this data type
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean enumeration(String restrictionValue) throws RestrictionViolationException {
    try {
      return (getValue().equals(Double.valueOf(restrictionValue)));
    } catch (NumberFormatException ex) {
      throw new RestrictionViolationException(restrictionValue + " cannot not be applied to an Real enumeration");
    }
  }

  /**
   * Returns true when the number of digits of the passed Real value equals the
   * required number which was passed as parameter or if the passed value is
   * NULL. False is returned otherwise.
   * 
   * @param digits
   *          int the number of digits required
   * @throws RestrictionViolationException
   *           when this restriction does not apply to this data type or if the
   *           value passed can be converted to Long
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean totalDigits(int digits) throws RestrictionViolationException {
    BigDecimal big = new BigDecimal(toString());
    BigInteger bigInt = big.toBigInteger();
    int hasDigits = big.scale() + bigInt.toString().length();

    if (digits == hasDigits) {
      return true;
    }
    throw new RestrictionViolationException("The number of digits for \"" + toString() + "\" must be " + digits);
  }

  /**
   * Returns true when the number of digits of the passed Real value equals the
   * required number which was passed as parameter or if the passed value is
   * NULL. False is returned otherwise.
   * 
   * @param digits
   *          int the number of digits required
   * @throws RestrictionViolationException
   *           when this restriction does not apply to this data type or if the
   *           value passed can be converted to Long
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean fractionDigits(int digits) throws RestrictionViolationException {

    if (digits >= new BigDecimal(toString()).scale()) {
      return true;
    }
    throw new RestrictionViolationException("The number of fraction digits for \"" + toString() + "\"must be " + digits);
  }

  /**
   * Returns true if the passed Integer is greater or equal than the passed
   * lowerbound or if the passed value is NULL. Return false otherwise.
   * 
   * @param restrictionValue
   *          String the lower bound that should be included
   * @throws RestrictionViolationException
   *           when this restriction does not apply to this data type or if the
   *           passed value can not be converted to Long
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean minInclusive(String restrictionValue) throws RestrictionViolationException {
    try {
      double lowerBound = Double.parseDouble(restrictionValue);

      if (getValue().doubleValue() >= lowerBound) {
        return true;
      }
      throw new RestrictionViolationException("Real value " + toString() + " should be greater or equal than "
          + restrictionValue);
    } catch (NumberFormatException ex) {
      throw new RestrictionViolationException(restrictionValue + " cannot not be used as lower bound for an Real");
    }
  }

  /**
   * Returns true if the passed Integer is less or equal to the passed
   * upperbound or if the passed value is NULL. Return false otherwise.
   * 
   * @param restrictionValue
   *          String the upper bound that should be included
   * @throws RestrictionViolationException
   *           when this restriction does not apply to this data type or if the
   *           passed value can not be converted to Long
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean maxInclusive(String restrictionValue) throws RestrictionViolationException {
    try {
      double upperBound = Double.parseDouble(restrictionValue);

      if (getValue().doubleValue() <= upperBound) {
        return true;
      }
      throw new RestrictionViolationException("Real value " + toString() + " should be less or equal than "
          + restrictionValue);
    } catch (NumberFormatException ex) {
      throw new RestrictionViolationException(restrictionValue + " cannot not be used as lower bound for an Integer");
    }

  }

  /**
   * Returns true if the passed Integer is greater than the passed lowerbound or
   * if the passed value is NULL. Return false otherwise.
   * 
   * @param restrictionValue
   *          String the lower bound that should not be included
   * @throws RestrictionViolationException
   *           when this restriction does not apply to this data type or if the
   *           passed value can not be converted to Long
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean minExclusive(String restrictionValue) throws RestrictionViolationException {
    try {
      double lowerBound = Double.parseDouble(restrictionValue);

      if (getValue().doubleValue() > lowerBound) {
        return true;
      }
      throw new RestrictionViolationException("Real value " + toString() + " should be greater than "
          + restrictionValue);
    } catch (NumberFormatException ex) {
      throw new RestrictionViolationException(restrictionValue + " cannot not be used as lower bound for an Integer");
    }

  }

  /**
   * Returns true if the passed Integer is less or equal to the passed
   * upperbound or if the passed value is NULL. Return false otherwise.
   * 
   * @param restrictionValue
   *          String the upper bound that should not be included
   * @throws RestrictionViolationException
   *           when this restriction does not apply to this data type or if the
   *           passed value can not be converted to Long
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean maxExclusive(String restrictionValue) throws RestrictionViolationException {
    try {
      double upperBound = Double.parseDouble(restrictionValue);

      if (getValue().doubleValue() < upperBound) {
        return true;
      }
      throw new RestrictionViolationException("Real value " + toString() + " should be less than " + restrictionValue);
    } catch (NumberFormatException ex) {
      throw new RestrictionViolationException(restrictionValue + " cannot not be used as lower bound for an Integer");
    }
  }

}
