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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.IllegalOperandException;
import org.coppercore.exceptions.RestrictionViolationException;
import org.coppercore.exceptions.TypeCastException;
import org.coppercore.expression.Operand;
import org.coppercore.expression.PropertyConstant;

public class LDUri extends LDDataType {
  URI value = null;

  public LDUri(String value) throws TypeCastException {
    try {
      this.value = new URI(value);
    } catch (URISyntaxException ex) {
      throw new TypeCastException(ex);
    }
  }

  public int getType() {
    return LDURI;
  }

  public String getTypeName() {
    return LDURI_NAME;
  }

  public LDUri toLDUri() throws TypeCastException {
    return this;
  }

  /**
   * Returns the value of this datatype in the native format
   * 
   * @return String
   */
  public URI getValue() {
    return value;
  }

  public String toString() {
    return value.toString();
  }

  public boolean equals(LDDataType obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof LDUri)) {
      return false;
    }
    LDUri that = (LDUri) obj;
    if (!(that.value == null ? this.value == null : that.value.equals(this.value))) {
      return false;
    }
    return true;
  }

  public static int sum(int operandType, Operand operand) throws ExpressionException {

    switch (operandType) {
    case LDSTRING:
      LDTEXT: {
        return LDSTRING;
      }
    case LDCONSTANT: {
      ((PropertyConstant) operand).getValue().toLDString();
      return LDSTRING;
    }
    default: {
      throw new IllegalOperandException("Illegal type cast");
    }
    }
  }

  public LDDataType sum(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
    case LDSTRING:
      LDTEXT: {
        return new LDString(toString() + ((LDString) operand).getValue());
      }
    case LDCONSTANT: {
      return new LDString(toString() + operand.toLDString().getValue());
    }
    default: {
      throw new IllegalOperandException("Illegal type cast");
    }
    }
  }

  public static int is(int operandType, Operand operand) throws ExpressionException {

    switch (operandType) {
    case LDURI: {
      return LDBOOLEAN;
    }
    case LDSTRING:
      LDTEXT: {
        return LDBOOLEAN;
      }
    case LDCONSTANT: {
      ((PropertyConstant) operand).getValue().toLDString();
      return LDBOOLEAN;
    }

    default: {
      throw new IllegalOperandException("Illegal type cast");
    }
    }
  }

  public LDDataType is(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
    case LDSTRING:
      LDTEXT: {
        return toLDString().is(operand);
      }
    case LDURI: {
      return new LDBoolean(equals(operand));
    }
    case LDCONSTANT: {
      return new LDBoolean(equals(operand.toLDString()));
    }
    default: {
      throw new IllegalOperandException("Illegal type cast");
    }
    }
  }

  /**
   * Returns true when value matches the passed pattern. If the value equals
   * null the method returns true.
   * 
   * @param restrictionValue
   *          String the pattern to be matched
   * @throws RestrictionViolationException
   *           when this restriction does not apply to this data type
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean pattern(String restrictionValue) throws RestrictionViolationException {
    Pattern pattern = Pattern.compile(restrictionValue);
    Matcher matcher = pattern.matcher(toString());
    if (matcher.matches()) {
      return true;
    }
    throw new RestrictionViolationException("URI \"" + toString() + "\" does not satisfy pattern \"" + restrictionValue
        + "\"");
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
      return equals(new LDUri(restrictionValue));
    } catch (TypeCastException ex) {
      throw new RestrictionViolationException(ex);
    }
  }

  /**
   * Returns true when the length of the passed value is at equal or more than
   * the specified length. If the value equals null the method returns true.
   * 
   * @param length
   *          int the minimum length required
   * @throws RestrictionViolationException
   *           when this restriction does not apply to this data type
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean minLength(int length) throws RestrictionViolationException {
    if (toString().length() >= length) {
      return true;
    }
    throw new RestrictionViolationException("Length of \"" + toString() + "\" should be at least " + length);
  }

  /**
   * Returns true when the length of the passed value is at less or equal than
   * the specified length. If the value equals null the method returns true.
   * 
   * @param length
   *          int the minimum length required
   * @throws RestrictionViolationException
   *           when this restriction does not apply to this data type
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean maxLength(int length) throws RestrictionViolationException {
    if (toString().length() <= length) {
      return true;
    }
    throw new RestrictionViolationException("Length of \"" + toString() + "\" should be no greater than" + length);
  }

  /**
   * Returns true when the length of the passed value is equals the specified
   * length. If the value equals null the method returns true.
   * 
   * @param length
   *          int the minimum length required
   * @param value
   *          String the value to checked against this restriction
   * @throws RestrictionViolationException
   *           when this restriction does not apply to this data type
   * @return boolean true when the restriction is satisfied, false otherwise
   */
  public boolean length(int length) throws RestrictionViolationException {
    if (toString().length() == length) {
      return true;
    }
    throw new RestrictionViolationException("Length of \"" + toString() + "\" should be " + length);
  }
}
