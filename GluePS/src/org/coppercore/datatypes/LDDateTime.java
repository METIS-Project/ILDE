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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.IllegalOperandException;
import org.coppercore.exceptions.NullValueException;
import org.coppercore.exceptions.RestrictionViolationException;
import org.coppercore.exceptions.TypeCastException;
import org.coppercore.expression.Operand;
import org.coppercore.expression.PropertyConstant;

public class LDDateTime extends LDDataType {
  private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
  private Calendar value = null;
  private static final String[] patterns = {
                                           "yyyy-MM-dd'T'HH:mm:ssZ",
                                           "yyyy-MM-dd'T'HH:mm:ss",
                                           "yyyy-MM-dd'T'HH:mmZ",
                                           "yyyy-MM-dd'T'HH:mm",
                                           "yyyy-MM-dd",
                                           "yyyy-MM",
                                           "yyyy", };

  public LDDateTime(String aValue) throws TypeCastException {
    value = convert(aValue);
  }

  public LDDateTime(Calendar aValue) throws TypeCastException {
    value = aValue;
  }

  public static Calendar convert(String aValue) throws TypeCastException {
    Date date = null;
    for (int i = 0; i < patterns.length; i++) {
      try {
        DATEFORMAT.applyPattern(patterns[i]);
        date = DATEFORMAT.parse(aValue);
        break;
      }
      catch (Exception e) {
        // ignore, try next pattern
      }
    }
    if (date != null) {
      Calendar result = Calendar.getInstance();
      result.setTime(date);
      return result;
    }
    throw new TypeCastException(aValue + " is not a valid date format (yyyy-MM-dd'T'HH:mm:ssZ)");
  }

  public int getType() {
    return LDDATETIME;
  }

  public String getTypeName() {
    return LDDATETIME_NAME;
  }

  public LDDateTime toLDDateTime() {
    return this;
  }

  private LDDateTime min(LDDuration aDuration) throws NullValueException,
      TypeCastException {
    if (value != null && aDuration != null) {
      String duration[] = aDuration.getValue().split(",");

      return new LDDateTime((value.get(Calendar.YEAR) - Integer.parseInt(duration[0])) + "-" +
                            (value.get(Calendar.MONTH) + 1 - Integer.parseInt(duration[1])) + "-" +
                            value.get(Calendar.DAY_OF_MONTH - Integer.parseInt(duration[2])) + "T" +
                            value.get(Calendar.HOUR_OF_DAY - Integer.parseInt(duration[3])) + ":" +
                            value.get(Calendar.MINUTE - Integer.parseInt(duration[4])) + ":" +
                            value.get(Calendar.SECOND - Integer.parseInt(duration[5])));
    }
    throw new NullValueException("Null value encountered for Duration property");
  }

  /**
   * Returns the value of this datatype in the native format
   *
   * @return Calendar
   */
  public Calendar getValue() {
    return value;
  }

  public String toString() {
    return DATEFORMAT.format(value.getTime());
  }

  public boolean equals(LDDataType obj) {
    if (this == obj) {
      return true;
    }
    if (! (obj instanceof LDDateTime)) {
      return false;
    }
    LDDateTime that = (LDDateTime) obj;
    if (! (that.value == null ? this.value == null :
           that.value.equals(this.value))) {
      return false;
    }
    return true;
  }

  public static int is(int operandType, Operand operand) throws
      ExpressionException {

    switch (operandType) {
      case LDDATETIME: {
        return LDBOOLEAN;
      }
      case LDCONSTANT: {
        ( (PropertyConstant) operand).getValue().toLDDateTime();
        return LDBOOLEAN;
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType is(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDDATETIME: {
        return new LDBoolean(equals(operand));
      }
      case LDCONSTANT: {
        return new LDBoolean(equals(operand.toLDDateTime()));
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public static int sum(int operandType, Operand operand) throws
      ExpressionException {

    switch (operandType) {
      case LDDURATION: {
        return LDDATETIME;
      }
      case LDCONSTANT: {
        ( (PropertyConstant) operand).getValue().toLDDuration();
        return LDDATETIME;
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType sum(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDDURATION: {
        return ( (LDDuration) operand).sum(this);
      }
      case LDCONSTANT: {
        LDDuration op = operand.toLDDuration();

        return op.sum(this);
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public static int subtract(int operandType, Operand operand) throws
      ExpressionException {

    switch (operandType) {
      case LDDURATION: {
        return LDDATETIME;
      }
      case LDCONSTANT: {
        ( (PropertyConstant) operand).getValue().toLDDuration();
        return LDDATETIME;
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType subtract(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDDURATION: {
        return (min( (LDDuration) operand));
      }
      case LDCONSTANT: {
        LDDuration op = operand.toLDDuration();
        return min(op);
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public static int lessThan(int operandType, Operand operand) throws
      ExpressionException {

    switch (operandType) {
      case LDDATETIME: {
        return LDBOOLEAN;
      }
      case LDCONSTANT: {
        ( (PropertyConstant) operand).getValue().toLDDateTime();
        return LDBOOLEAN;
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType lessThan(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDDATETIME: {
        return new LDBoolean(getValue().getTimeInMillis() < ( (LDDateTime) operand).getValue().getTimeInMillis());
      }
      case LDCONSTANT: {
        LDDateTime op = operand.toLDDateTime();
        return new LDBoolean(getValue().getTimeInMillis() < ( (op.getValue().getTimeInMillis())));
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public static int greaterThan(int operandType, Operand operand) throws
      ExpressionException {

    switch (operandType) {
      case LDDATETIME: {
        return LDBOOLEAN;
      }
      case LDCONSTANT: {
        ( (PropertyConstant) operand).getValue().toLDDateTime();
        return LDBOOLEAN;
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType greaterThan(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDDATETIME: {
        return new LDBoolean(getValue().getTimeInMillis() > (( (LDDateTime) operand).getValue().getTimeInMillis()));
      }
      case LDCONSTANT: {
        LDDateTime op = operand.toLDDateTime();
        return new LDBoolean(getValue().getTimeInMillis() > ( (op.getValue().getTimeInMillis())));
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  /**
   * By default throws RestrictionViolationException.
   *
   * @param restrictionValue String the value to be matched
   * @throws RestrictionViolationException when this restriction does not apply
   *   to this data type
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean enumeration(String restrictionValue) throws
      RestrictionViolationException {
    try {
      return equals(new LDDateTime(restrictionValue));
    }
    catch (TypeCastException ex) {
      //do nothing will not occur because typecasting to string is alway allowed
      throw new RestrictionViolationException(ex);
    }
  }

  /**
   * By default throws RestrictionViolationException.
   *
   * @param restrictionValue String the lower bound that should be included
   * @throws RestrictionViolationException when this restriction does not apply
   *   to this data type
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean minInclusive(String restrictionValue) throws
      RestrictionViolationException {
    try {
      LDDateTime enumerationValue = new LDDateTime(restrictionValue);
      if (value.getTimeInMillis() >= enumerationValue.getValue().getTimeInMillis()) {
        return true;
      }
      throw new RestrictionViolationException("Date " + toString() + " should be after or equal to " +
                                              restrictionValue);
    }
    catch (TypeCastException ex) {
      //do nothing will not occur because typecasting to string is alway allowed
      throw new RestrictionViolationException(ex);
    }
  }

  /**
   * By default throws RestrictionViolationException.
   *
   * @param restrictionValue String the upper bound that should be included
   * @throws RestrictionViolationException when this restriction does not apply
   *   to this data type
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean maxInclusive(String restrictionValue) throws
      RestrictionViolationException {
    try {
      LDDateTime enumerationValue = new LDDateTime(restrictionValue);
      if (value.getTimeInMillis() <= enumerationValue.getValue().getTimeInMillis()) {
        return true;
      }
      throw new RestrictionViolationException("Date " + toString() + " should be before or equal to " +
                                              restrictionValue);
    }
    catch (TypeCastException ex) {
      throw new RestrictionViolationException(ex);
    }
  }

  /**
   * By default throws RestrictionViolationException.
   *
   * @param restrictionValue String the lower bound that should not be included
   * @throws RestrictionViolationException when this restriction does not apply
   *   to this data type
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean minExclusive(String restrictionValue) throws
      RestrictionViolationException {
    try {
      LDDateTime enumerationValue = new LDDateTime(restrictionValue);
      if (value.getTimeInMillis() > enumerationValue.getValue().getTimeInMillis()) {
        return true;
      }
      throw new RestrictionViolationException("Date " + toString() + " should be before " + restrictionValue);
    }
    catch (TypeCastException ex) {
      //do nothing will not occur because typecasting to string is alway allowed
      throw new RestrictionViolationException(ex);
    }
  }

  /**
   * By default throws RestrictionViolationException.
   *
   * @param restrictionValue String the upper bound that should not be included
   * @throws RestrictionViolationException when this restriction does not apply
   *   to this data type
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean maxExclusive(String restrictionValue) throws
      RestrictionViolationException {
    try {
      LDDateTime enumerationValue = new LDDateTime(restrictionValue);
      if (value.getTimeInMillis() < enumerationValue.getValue().getTimeInMillis()) {
        return true;
      }
      throw new RestrictionViolationException("Date " + toString() + " should be after " + restrictionValue);
    }
    catch (TypeCastException ex) {
      //do nothing will not occur because typecasting to string is alway allowed
      throw new RestrictionViolationException(ex);
    }
  }
}
