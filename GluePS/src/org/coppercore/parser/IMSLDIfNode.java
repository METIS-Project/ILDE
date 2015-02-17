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

package org.coppercore.parser;

import javax.xml.transform.TransformerException;

import org.coppercore.common.MessageList;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.ValidationException;
import org.coppercore.expression.Expression;
import org.coppercore.expression.IfExpression;
import org.coppercore.expression.Operand;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design if element.  
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.13 $, $Date: 2005/01/21 14:01:30 $
 */
public class IMSLDIfNode
    extends ExpressionEntity {
  private Expression expression = null;

  /**
   * Constructs a IMSLDIfNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDIfNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }


  protected boolean isValid(MessageList messages) {

    boolean result = super.isValid(messages);

    try {
      //check if the expression can be parsed according to this type
      expression.checkType();
    }
    catch (ExpressionException ex) {
      //there was a type cast error so validation is false
      messages.logError(ex.getMessage());

      try {
        messages.appendToLastLog(" in \"" + toXML() + "\"");
      }
      catch (TransformerException ex1) {
      //do nothing
      }
      result = false;
    }
    return result;
  }

  protected Operand getExpression() throws ValidationException {
    if (expression == null) {
      IfExpression ifExpression = new IfExpression();

      return getExpression(ifExpression);
    }
    return expression;
  }


  protected boolean buildComponentModel(int uolId, MessageList messages) {
    boolean result = super.buildComponentModel(uolId, messages);

    if (result) {
      try {
        //add the expression to the model
        expression = (Expression) getExpression();
      }
      catch (ValidationException ex) {
        //something went wrong will the build of the expression, probably due to
        //errors earlier on
        messages.logError(ex.getMessage());
        result = false;
      }
    }
    return result;
  }
}
