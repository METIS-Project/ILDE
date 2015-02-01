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

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.IllegalOperandException;
import org.coppercore.exceptions.NullValueException;
import org.coppercore.exceptions.RestrictionViolationException;
import org.coppercore.exceptions.TypeCastException;
import org.coppercore.expression.Operand;
import org.coppercore.expression.PropertyConstant;

/**
 * Implements the IMS Learning Design duration property type.
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.11 $, $Date: 2005/01/11 13:15:16 $
 */
public class LDDuration extends LDDataType {

  public final static Pattern PATTERN = Pattern.compile(
      "P((\\d+)Y)?+((\\d+)M)?+((\\d+)D)?+T?((\\d+)H)?+((\\d+)M)?((\\d+)S)?");
  private String value = null;

  public LDDuration(String aValue) throws TypeCastException {
    value = convert(aValue);
  }

  private LDDuration(int[] aValue) {
    value = aValue[0] + "," + aValue[1] + "," + aValue[2] + "," + aValue[3] + "," +
            aValue[4] + "," + aValue[5];
  }

  public static String convert(String aValue) throws TypeCastException {
    Matcher matcher = PATTERN.matcher(aValue);
    if (matcher.matches()) {
      int match[] = {0, 0, 0, 0, 0, 0};

      // Get all groups for this match
      for (int i = 2, j = 0; i <= matcher.groupCount(); i += 2, j++) {
        String groupStr = matcher.group(i);
        if (groupStr != null) {
          match[j] = Integer.parseInt(groupStr);
        }
      }

      return (match[0] + "," + match[1] + "," + match[2] + "," + match[3] + "," +
              match[4] + "," + match[5]);
    }
    throw new TypeCastException(aValue + " is not a valid duration format (PnYnMnDTnHnMnS");

  }

  public int getType() {
    return LDDURATION;
  }

  public String getTypeName() {
    return LDDURATION_NAME;
  }

  public LDDuration toLDuration() {
    return this;
  }

  /**
   * Returns the value of this datatype in the native format
   *
   * @return String
   */
  public String getValue() {
    return value;
  }

  public String toString() {
    if (value != null) {
      String result[] = value.split(",");
      return ("P" + result[0] + "Y" +
              result[1] + "M" +
              result[2] + "DT" +
              result[3] + "H" +
              result[4] + "M" +
              result[5] + "S");
    }
    return null;
  }

  private LDDuration add(LDDuration valueToAdd) throws NullValueException {
    if (value != null && valueToAdd.getValue() != null) {

      int result[] = {
                     0, 0, 0, 0, 0, 0};
      String op1[] = value.split(",");
      String op2[] = valueToAdd.getValue().split(",");

      for (int i = 0; i < 6; i++) {
        result[i] = Integer.parseInt(op1[i]) + Integer.parseInt(op2[i]);
      }

      return new LDDuration(result);
    }
    throw new NullValueException("Null value encountered for Duration property");
  }

  private LDDuration min(LDDuration valueToAdd) throws NullValueException {
    if (value != null && valueToAdd.getValue() != null) {

      int result[] = {
                     0, 0, 0, 0, 0, 0};
      String op1[] = value.split(",");
      String op2[] = valueToAdd.getValue().split(",");

      for (int i = 0; i < 6; i++) {
        result[i] = Integer.parseInt(op1[i]) - Integer.parseInt(op2[i]);
      }

      return new LDDuration(result);
    }
    throw new NullValueException("Null value encountered for Duration property");
  }

  private LDDateTime add(LDDateTime dateTime) throws NullValueException,
      TypeCastException {
    Calendar date = dateTime.getValue();
    if (value != null && date != null) {

      String duration[] = value.split(",");
      //modified 2004-11-20 removed incorrect P prefix for datetime string
      return new LDDateTime((date.get(Calendar.YEAR)  + Integer.parseInt(duration[0])) + "-" +
                            (date.get(Calendar.MONTH)  + 1 + Integer.parseInt(duration[1])) + "-" +
                            (date.get(Calendar.DAY_OF_MONTH)  + Integer.parseInt(duration[2])) + "T" +
                            (date.get(Calendar.HOUR_OF_DAY)  + Integer.parseInt(duration[3])) + ":" +
                            (date.get(Calendar.MINUTE)  + Integer.parseInt(duration[4])) + ":" +
                            (date.get(Calendar.SECOND)  + Integer.parseInt(duration[5])));
    }
    throw new NullValueException("Null value encountered for Duration property");
  }

  public boolean equals(LDDataType obj) {
    if (this == obj) {
      return true;
    }
    if (! (obj instanceof LDDateTime)) {
      return false;
    }
    LDDuration that = (LDDuration) obj;
    if (! (that.value == null ? this.value == null :
           that.value.equals(this.value))) {
      return false;
    }
    return true;
  }

  public static int is(int operandType, Operand operand) throws
      ExpressionException {

    switch (operandType) {
      case LDDURATION: {
        return LDBOOLEAN;
      }
      case LDCONSTANT: {
        ( (PropertyConstant) operand).getValue().toLDDuration();
        return LDBOOLEAN;
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType is(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDDURATION: {
        return new LDBoolean(equals(operand));
      }
      case LDCONSTANT: {
        return new LDBoolean(equals(operand.toLDDuration()));
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
        return LDDURATION;
      }
      case LDDATETIME: {
        return LDDATETIME;
      }
      case LDCONSTANT: {
        try {
          ( (PropertyConstant) operand).getValue().toLDDuration();
          return LDDURATION;
        }
        catch (TypeCastException ex) {
          ( (PropertyConstant) operand).getValue().toLDDateTime();
          return LDDATETIME;
        }
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType sum(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDDURATION: {
        return add( (LDDuration) operand);
      }
      case LDDATETIME: {
        return add( (LDDateTime) operand);
      }
      case LDCONSTANT: {
        try {
          LDDuration op = operand.toLDDuration();
          return add(op);
        }
        catch (TypeCastException ex) {
          LDDateTime op = operand.toLDDateTime();
          return add(op);
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
      case LDDURATION: {
        return LDDURATION;
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
        return min( (LDDuration) operand);
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

  public LDDuration toLDDuration() throws TypeCastException {
    return this;
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
      return equals(new LDDuration(restrictionValue));
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
      Calendar now = Calendar.getInstance();
      LDDuration duration = new LDDuration(restrictionValue);

      Calendar cal1 = toCalendar(now);
      Calendar cal2 = duration.toCalendar(now);

      if (!cal1.before(cal2)) {
        return true;
      }
      throw new RestrictionViolationException("Duration " + toString() + " should be greater or equal to " +
                                                restrictionValue);
    }
    catch (TypeCastException ex) {
      //do nothing will not occur because typecasting to string is always allowed
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
      Calendar now = Calendar.getInstance();
      LDDuration value = new LDDuration(restrictionValue);

      Calendar cal1 = toCalendar(now);
      Calendar cal2 = value.toCalendar(now);

      if (!cal2.before(cal1)) {
        return true;
      }
      throw new RestrictionViolationException("Duration " + toString() + " should be less or equal to " +
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
   * @param restrictionValue String the lower bound that should not be included
   * @throws RestrictionViolationException when this restriction does not apply
   *   to this data type
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean minExclusive(String restrictionValue) throws
      RestrictionViolationException {
    try {
      Calendar now = Calendar.getInstance();
      LDDuration value = new LDDuration(restrictionValue);

      Calendar cal1 = toCalendar(now);
      Calendar cal2 = value.toCalendar(now);
      if (cal1.after(cal2)) {
        return true;
      }
      throw new RestrictionViolationException("Duration " + toString() + " should be greater than " +
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
   * @param restrictionValue String the upper bound that should not be included
   * @throws RestrictionViolationException when this restriction does not apply
   *   to this data type
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean maxExclusive(String restrictionValue) throws
      RestrictionViolationException {
    try {
      Calendar now = Calendar.getInstance();
      LDDuration value = new LDDuration(restrictionValue);

      Calendar cal1 = toCalendar(now);
      Calendar cal2 = value.toCalendar(now);
      if (cal1.before(cal2)) {
        return true;
      }
      throw new RestrictionViolationException("Duration " + toString() + " should be less than  " + restrictionValue);
    }
    catch (TypeCastException ex) {
      //do nothing will not occur because typecasting to string is alway allowed
      throw new RestrictionViolationException(ex);
    }
  }

  private Calendar toCalendar(Calendar date) {
    String duration[] = value.split(",");

    date.add(Calendar.YEAR, Integer.parseInt(duration[0]));
    date.add(Calendar.MONTH, Integer.parseInt(duration[1]));
    date.add(Calendar.DAY_OF_MONTH, Integer.parseInt(duration[2]));
    date.add(Calendar.HOUR_OF_DAY, Integer.parseInt(duration[3]));
    date.add(Calendar.MINUTE, Integer.parseInt(duration[4]));
    date.add(Calendar.SECOND, Integer.parseInt(duration[5]));

    return date;
  }

}
