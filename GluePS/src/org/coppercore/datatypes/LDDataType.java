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

import java.io.PrintWriter;
import java.util.Collection;

import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.IllegalOperationException;
import org.coppercore.exceptions.RestrictionViolationException;
import org.coppercore.exceptions.TypeCastException;
import org.coppercore.expression.Operand;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public abstract class LDDataType {
  public final static int LDCONSTANT = 0;
  public final static int LDINTEGER = 1;
  public final static int LDSTRING = 2;
  public final static int LDBOOLEAN = 3;
  public final static int LDDATETIME = 4;
  public final static int LDDURATION = 5;
  public final static int LDREAL = 6;
  public final static int LDURI = 7;
  public final static int LDTEXT = 8;
  public final static int LDFILE = 9;

  public final static String LDCONSTANT_NAME = "constant";
  public final static String LDINTEGER_NAME = "integer";
  public final static String LDSTRING_NAME = "string";
  public final static String LDBOOLEAN_NAME = "boolean";
  public final static String LDDATETIME_NAME = "datetime";
  public final static String LDDURATION_NAME = "duration";
  public final static String LDREAL_NAME = "real";
  public final static String LDURI_NAME = "uri";
  public final static String LDTEXT_NAME = "text";
  public final static String LDFILE_NAME = "file";


  public LDDataType() {
    //default constructor     
  }

  public abstract int getType();
  public abstract String getTypeName();


  /**
   * Returns this datatype as a DOM Node.
   * @param owner Document context for which this node is created
   * @return Node the created Node
   */
  public Node asDomNode(Document owner) {
    Node result = owner.createTextNode(toString());
    return result;
   }


  public LDString toLDString() throws TypeCastException {
    return new LDString(toString());
  }

  public LDString toLDText() throws TypeCastException {
    return new LDText(toString());
  }

  public LDInteger toLDInteger() throws TypeCastException {
    throw new TypeCastException("Illegal type cast");
  }

  public LDReal toLDReal() throws TypeCastException {
    throw new TypeCastException("Illegal type cast");
  }

  public LDBoolean toLDBoolean() throws TypeCastException {
    throw new TypeCastException("Illegal type cast");
  }

  public LDUri toLDUri() throws TypeCastException {
    throw new TypeCastException("Illegal type cast");
  }

  public LDFile toLDFile() throws TypeCastException {
    throw new TypeCastException("Illegal type cast");
  }

  public LDDuration toLDDuration() throws TypeCastException {
    throw new TypeCastException("Illegal type cast");
  }

  public LDDateTime toLDDateTime() throws TypeCastException {
    throw new TypeCastException("Illegal type cast");
  }


  public static int sum(int operandType, Operand operand) throws ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }

  public LDDataType sum(LDDataType operand) throws ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }



  public static int subtract(int operandType, Operand operand) throws ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }

  public LDDataType subtract(LDDataType operand) throws
      ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }

  public static int multiply(int operandType, Operand operand) throws ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }

  public LDDataType multiply(LDDataType operand) throws ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }


  public static int divide(int operandType, Operand operand) throws ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }

  public LDDataType divide(LDDataType operand) throws ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }


  public static int greaterThan(int operandType, Operand operand) throws ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }

  public LDDataType greaterThan(LDDataType operand) throws
      ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }



  public static int lessThan(int operandType, Operand operand) throws ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }

  public LDDataType lessThan(LDDataType operand) throws
      ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }




  public static int not(int operandType, Operand operand) throws ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }

  public LDDataType not() throws ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }



  public static int is(int operandType, Operand operand) throws ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }

  public LDDataType is(LDDataType operand) throws ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }



  public static int and(int operandType, Operand operand) throws ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }

  public LDDataType and(Collection operands) throws ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }


  public static int or(int operandType, Operand operand) throws ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }

  public LDDataType or(Collection operands) throws ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }


  public LDDataType count(Collection operands) throws ExpressionException {
    throw new IllegalOperationException("Illegal operation");
  }


  public void toXml(PrintWriter result) {
    result.write("<value>" + toString() + "</value>");
  }

  public abstract String toString();

  public abstract boolean equals(LDDataType value);






  /**
   * By default throws RestrictionViolationException.
   *
   * @param length int the minimum length required
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean minLength(int length) throws
      RestrictionViolationException {
    throw new RestrictionViolationException("minLength restriction not supported for  \"" + this.getTypeName() + "\"");
  }

  /**
   * By default throws RestrictionViolationException.
   *
   * @param length int the minimum length required
   * @throws RestrictionViolationException when this restriction does not apply
   *   to this data type
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean maxLength(int length) throws
      RestrictionViolationException {
    throw new RestrictionViolationException("maxLength restriction not supported for  \"" + this.getTypeName() + "\"");
  }

  /**
   * By default throws RestrictionViolationException.
   *
   * @param length int the minimum length required
   * @throws RestrictionViolationException when this restriction does not apply
   *   to this data type
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean length(int length) throws
      RestrictionViolationException {
    throw new RestrictionViolationException("length restriction not supported for  \"" + this.getTypeName() + "\"");
  }

  /**
   * By default throws RestrictionViolationException.
   *
   * @param digits int the number of digits required
   * @throws RestrictionViolationException when this restriction does not apply
   *   to this data type
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean totalDigits(int digits) throws
      RestrictionViolationException {
    throw new RestrictionViolationException("totalDigits restriction not supported for  \"" + this.getTypeName() + "\"");
  }

  /**
   * By default throws RestrictionViolationException.
   *
   * @param digits int the number of fraction digits required
   * @throws RestrictionViolationException when this restriction does not apply
   *   to this data type
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean fractionDigits(int digits) throws
      RestrictionViolationException {
    throw new RestrictionViolationException("fractionDigits restriction not supported for  \"" + this.getTypeName() + "\"");
  }

  /**
   * By default throws RestrictionViolationException.
   *
   * @param restrictionValue String the pattern to be matched
   * @throws RestrictionViolationException when this restriction does not apply
   *   to this data type
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean pattern(String restrictionValue) throws
      RestrictionViolationException {
    throw new RestrictionViolationException("pattern restriction not supported for  \"" + this.getTypeName() + "\"");
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
    throw new RestrictionViolationException("enumeration restriction not supported for  \"" + this.getTypeName() + "\"");
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
    throw new RestrictionViolationException("minInclusive restriction not supported for  \"" + this.getTypeName() + "\"");
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
    throw new RestrictionViolationException("maxInclusive restriction not supported for  \"" + this.getTypeName() + "\"");
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
    throw new RestrictionViolationException("minExclusive restriction not supported for  \"" + this.getTypeName() + "\"");
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
    throw new RestrictionViolationException("maxExclusive restriction not supported for  \"" + this.getTypeName() + "\"");
  }
}
