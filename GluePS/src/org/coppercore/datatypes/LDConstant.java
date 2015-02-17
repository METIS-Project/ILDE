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
package org.coppercore.datatypes;

import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.IllegalOperandException;
import org.coppercore.exceptions.TypeCastException;
import org.coppercore.expression.Operand;
import org.coppercore.expression.PropertyConstant;

public class LDConstant
    extends LDDataType {
  private String value = null;

  public LDConstant(String value) {
    this.value = value;
  }

  public LDConstant(int aValue) {
    this.value = Integer.toString(aValue);
  }

  public int getType() {
    return LDCONSTANT;
  }

  public String getTypeName() {
    return LDCONSTANT_NAME;
  }

  public LDBoolean toLDBoolean() throws TypeCastException {
    return new LDBoolean(value);
  }

  public LDInteger toLDInteger() throws TypeCastException {
    return new LDInteger(value);
  }

  public LDReal toLDReal() throws TypeCastException {
    return new LDReal(value);
  }

  public LDDateTime toLDDateTime() throws TypeCastException {
    return new LDDateTime(value);
  }

  public LDDuration toLDDuration() throws TypeCastException {
    return new LDDuration(value);
  }

  public LDUri toLDUri() throws TypeCastException {
    return new LDUri(value);
  }

  public int sum(Operand operand) throws ExpressionException {
    int operandType = operand.checkType();

    switch (operandType) {
      case LDCONSTANT: {
        //fallback to default type
        ( (PropertyConstant) operand).getValue().toString();
        toLDString();
        return LDString.sum(operandType, operand);
      }
      case LDINTEGER: {
        toLDInteger();
        return LDInteger.sum(operandType, operand);
      }
      case LDBOOLEAN: {
        toLDBoolean();
        return LDBoolean.sum(operandType, operand);
      }
      case LDSTRING:
        LDTEXT:
            {
          toLDString();
          return LDString.sum(operandType, operand);
        }
      case LDREAL: {
        toLDReal();
        return LDReal.sum(operandType, operand);
      }
      case LDDATETIME: {
        toLDDuration();
        return LDDuration.sum(operandType, operand);
      }
      case LDURI: {
        toLDString();
        return LDString.sum(operandType, operand);
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType sum(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDCONSTANT: {
        //fallback to default type
        return toLDString().sum(operand.toLDString());
      }
      case LDINTEGER: {
        return toLDInteger().sum(operand);
      }
      case LDBOOLEAN: {
        return toLDBoolean().sum(operand);
      }
      case LDSTRING:
        LDTEXT:
            {
          return toLDString().sum(operand);
        }
      case LDREAL: {
        return toLDReal().sum(operand);
      }
      case LDURI: {
        return toLDString().sum(operand);
      }
      case LDDATETIME: {
        return toLDDuration().sum(operand);
      }
      case LDDURATION: {
        return toLDDuration().sum(operand);
      }

      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public int subtract(Operand operand) throws ExpressionException {
    int operandType = operand.checkType();
    switch (operandType) {
      case LDCONSTANT: {
        //fallback to default type
        ( (PropertyConstant) operand).getValue().toString();
        toLDString();
        return LDString.sum(operandType, operand);
      }
      case LDINTEGER: {
        toLDInteger();
        return LDInteger.subtract(operandType, operand);
      }
      case LDBOOLEAN: {
        toLDBoolean();
        return LDBoolean.subtract(operandType, operand);
      }
      case LDSTRING: { /* fall through */ }
      case LDTEXT: {
        toLDString();
        return LDString.subtract(operandType, operand);
      }
      case LDREAL: {
        toLDReal();
        return LDReal.subtract(operandType, operand);
      }
      case LDDATETIME: {
        toLDDuration();
        return LDDuration.subtract(operandType, operand);
      }
      case LDURI: {
        toLDString();
        return LDString.subtract(operandType, operand);
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType subtract(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDCONSTANT: {
        //fallback to default type
        return toLDString().subtract(operand.toLDString());
      }
      case LDINTEGER: {
        return toLDInteger().subtract(operand);
      }
      case LDBOOLEAN: {
        return toLDBoolean().subtract(operand);
      }
      case LDSTRING:
        LDTEXT:
            {
          return toLDString().subtract(operand);
        }
      case LDREAL: {
        return toLDReal().subtract(operand);
      }
      case LDURI: {
        return toLDString().subtract(operand);
      }
      case LDDATETIME: {
        return toLDDuration().subtract(operand);
      }
      case LDDURATION: {
        return toLDDuration().subtract(operand);
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public int multiply(Operand operand) throws ExpressionException {
    int operandType = operand.checkType();
    switch (operandType) {
      case LDCONSTANT: {
        //fallback to default type
        ( (PropertyConstant) operand).getValue().toString();

        toLDString(); 
        return LDString.multiply(operandType, operand);
      }
      case LDINTEGER: {
        toLDInteger();
        return LDInteger.multiply(operandType, operand);
      }
      case LDBOOLEAN: {
        toLDBoolean();
        return LDBoolean.multiply(operandType, operand);
      }
      case LDSTRING: { /* fall trough */ }
      case LDTEXT: {
        toLDString();
        return LDString.multiply(operandType, operand);
      }
      case LDREAL: {
        toLDReal();
        return LDReal.multiply(operandType, operand);
      }
      case LDDATETIME: {
        toLDDuration();
        return LDDuration.multiply(operandType, operand);
      }
      case LDURI: {
        toLDString();
        return LDString.multiply(operandType, operand);
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType multiply(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDCONSTANT: {
        //fallback to default type
        return toLDString().multiply(operand.toLDString());
      }
      case LDINTEGER: {
        return toLDInteger().multiply(operand);
      }
      case LDBOOLEAN: {
        return toLDBoolean().multiply(operand);
      }
      case LDSTRING:
        LDTEXT:
            {
          return toLDString().multiply(operand);
        }
      case LDREAL: {
        return toLDReal().multiply(operand);
      }
      case LDURI: {
        return toLDString().multiply(operand);
      }
      case LDDATETIME: {
        return toLDDuration().multiply(operand);
      }
      case LDDURATION: {
        return toLDDuration().multiply(operand);
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public int divide(Operand operand) throws ExpressionException {
    int operandType = operand.checkType();
    switch (operandType) {
      case LDCONSTANT: {
        //fallback to default type
        ( (PropertyConstant) operand).getValue().toString();
        
        toLDString();
        return LDString.divide(operandType, operand);
      }
      case LDINTEGER: {
        toLDInteger();
        return LDInteger.divide(operandType, operand);
      }
      case LDBOOLEAN: {
        toLDBoolean();
        return LDBoolean.divide(operandType, operand);
      }
      case LDSTRING: { /* fall trough */ }
      case LDTEXT: {
        toLDString();
        return LDString.divide(operandType, operand);
      }
      case LDREAL: {
        toLDReal();
        return LDReal.divide(operandType, operand);
      }
      case LDDATETIME: {
        toLDDuration();
        return LDDuration.divide(operandType, operand);
      }
      case LDURI: {
        toLDString();
        return LDString.divide(operandType, operand);
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType divide(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDCONSTANT: {
        //fallback to default type 
        return toLDString().divide(operand.toLDString());
      }
      case LDINTEGER: {
        return toLDInteger().divide(operand);
      }
      case LDBOOLEAN: {
        return toLDBoolean().divide(operand);
      }
      case LDSTRING:
        LDTEXT:
            {
          return toLDString().divide(operand);
        }
      case LDREAL: {
        return toLDReal().divide(operand);
      }
      case LDURI: {
        return toLDString().divide(operand);
      }
      case LDDATETIME: {
        return toLDDuration().divide(operand);
      }
      case LDDURATION: {
        return toLDDuration().divide(operand);
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public int greaterThan(Operand operand) throws ExpressionException {

    int operandType = operand.checkType();

    switch (operandType) {
      case LDCONSTANT: {
        //fallback to default type
        ( (PropertyConstant) operand).getValue().toString();
        toLDString();
        return LDString.greaterThan(operandType, operand);
      }
      case LDINTEGER: {
        toLDInteger();        
        return LDInteger.greaterThan(operandType, operand);
      }
      case LDBOOLEAN: {
        toLDBoolean();
        return LDBoolean.greaterThan(operandType, operand);
      }
      case LDSTRING: { /*fall through*/ }
      case LDTEXT: {
          toLDString();
          return LDString.greaterThan(operandType, operand);
      }
      case LDREAL: {
        toLDReal();        
        return LDReal.greaterThan(operandType, operand);
      }
      case LDDATETIME: {
        toLDDuration();
        return LDDuration.greaterThan(operandType, operand);
      }
      case LDURI: {
        toLDString();
        return LDString.greaterThan(operandType, operand);
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType greaterThan(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDCONSTANT: {
        //fallback to default type
        return toLDString().greaterThan(operand.toLDString());
      }
      case LDINTEGER: {
        return toLDInteger().greaterThan(operand);
      }
      case LDBOOLEAN: {
        return toLDBoolean().greaterThan(operand);
      }
      case LDSTRING:
        LDTEXT:
            {
          return toLDString().greaterThan(operand);
        }
      case LDREAL: {
        return toLDReal().greaterThan(operand);
      }
      case LDURI: {
        return toLDString().greaterThan(operand);
      }
      case LDDATETIME: {
        return toLDDuration().greaterThan(operand);
      }
      case LDDURATION: {
        return toLDDuration().greaterThan(operand);
      }

      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public int lessThan(Operand operand) throws ExpressionException {

    int operandType = operand.checkType();

    switch (operandType) {
      case LDCONSTANT: {
        //fallback to default type
        ( (PropertyConstant) operand).getValue().toString();
 
        toLDString(); 
        return LDString.lessThan(operandType, operand);
      }
      case LDINTEGER: {
        toLDInteger();
        return LDInteger.lessThan(operandType, operand);
      }
      case LDBOOLEAN: {
        toLDBoolean();
        return LDBoolean.lessThan(operandType, operand);
      }
      case LDSTRING: { /* fall through */ }
      case LDTEXT: {
        toLDString();
        return LDString.lessThan(operandType, operand);
      }
      case LDREAL: {
        toLDReal();
        return LDReal.lessThan(operandType, operand);
      }
      case LDDATETIME: {
        toLDDuration();          
        return LDDuration.lessThan(operandType, operand);
      }
      case LDURI: {
        toLDString();
        return LDString.lessThan(operandType, operand);
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType lessThan(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDCONSTANT: {
        //fallback to default type
        return toLDString().lessThan(operand.toLDString());
      }
      case LDINTEGER: {
        return toLDInteger().lessThan(operand);
      }
      case LDBOOLEAN: {
        return toLDBoolean().lessThan(operand);
      }
      case LDSTRING:
        LDTEXT:
            {
          return toLDString().lessThan(operand);
        }
      case LDREAL: {
        return toLDReal().lessThan(operand);
      }
      case LDURI: {
        return toLDString().lessThan(operand);
      }
      case LDDATETIME: {
        return toLDDuration().lessThan(operand);
      }
      case LDDURATION: {
        return toLDDuration().lessThan(operand);
      }

      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public int is(Operand operand) throws ExpressionException {

    int operandType = operand.checkType();

    switch (operandType) {
      case LDCONSTANT: {
        //fallback to default type
        toLDString();
        return LDString.is(operandType, operand);
      }
      case LDINTEGER: {
        toLDInteger();
        return LDInteger.is(operandType, operand);
      }
      case LDBOOLEAN: {
        toLDBoolean();
        return LDBoolean.is(operandType, operand);
      }
      case LDSTRING: { /* fall trough */ }
      case LDTEXT: {
        toLDString();
        return LDString.is(operandType, operand);
      }
      case LDREAL: {
        toLDReal();        
        return LDReal.is(operandType, operand);
      }
      case LDDATETIME: {
        toLDDateTime();
        return LDDateTime.is(operandType, operand);
      }
      case LDDURATION: {
        toLDDuration();        
        return LDDuration.is(operandType, operand);
      }
      case LDURI: {
        toLDUri();
        return LDUri.is(operandType, operand);
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public LDDataType is(LDDataType operand) throws ExpressionException {
    switch (operand.getType()) {
      case LDCONSTANT: {
        //fallback to default type
        return toLDString().is(operand);
      }
      case LDINTEGER: {
        return toLDInteger().is(operand);
      }
      case LDBOOLEAN: {
        return toLDBoolean().is(operand);
      }
      case LDSTRING: { /* fall through */ }
      case LDTEXT: {
          return toLDString().is(operand);
      }
      case LDREAL: {
        return toLDReal().is(operand);
      }
      case LDDURATION: {
        return toLDDuration().is(operand);
      }
      case LDDATETIME: {
        return toLDDateTime().is(operand);
      }
      case LDURI: {
        return toLDUri().is(operand);
      }
      default: {
        throw new IllegalOperandException("Illegal type cast");
      }
    }
  }

  public String toString() {
    return value;
  }

  public boolean equals(LDDataType obj) {
    if (this == obj) {
      return true;
    }
    if (! (obj instanceof LDConstant)) {
      return false;
    }
    LDConstant that = (LDConstant) obj;
    if (! (that.value == null ? this.value == null :
           that.value.equals(this.value))) {
      return false;
    }
    return true;
  }
}
