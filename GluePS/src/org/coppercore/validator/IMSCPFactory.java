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
 * CopperCore , an IMS-LD level C engine
 * Copyright (C) 2003, 2004 Harrie Martens and Hubert Vogten
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

package org.coppercore.validator;

import java.io.File;

import org.coppercore.common.MessageList;
import org.coppercore.exceptions.ValidationException;

/**
 * A factory class for creating an IMSPackage for the specified ims
 * contentpackage.
 *
 * <p> This factory deals with the differences between zipped and exploded
 * content packages.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.7 $, $Date: 2009/01/07 21:10:14 $
 */
public class IMSCPFactory {
  public IMSCPFactory() {
    // default contructor
  }

  /**
   * Returns an IMSPackage for the specified content package.
   *
   * @param filename String the location or name of the content package
   * @param schemaLocation String the location where the xml schemas needed for
   *   validation can be found
   * @param messageList MessageList the list where all validation mesages are
   *   stored
   * @throws ValidationException when the file does not point to an IMS content
   *   package
   * @return IMSPackage the IMSPackage for the specified content package.
   */
  public static IMSPackage getIMSPackage(String filename, String schemaLocation,
  MessageList messageList) throws ValidationException {
    File cp = new File(filename);
    if (cp.isDirectory()) {
      return new IMSExplodedCP(filename, schemaLocation, messageList);
    }

    if (cp.exists()) {
      String name = cp.getName().toLowerCase();
      if (name.endsWith(".zip")) {
        return new IMSZipPackage(filename, schemaLocation, messageList);
      }
      else if (name.endsWith(".xml")) {
        return new IMSExplodedCP(cp.getParent(), schemaLocation, messageList);
      }
    }

    throw new ValidationException("[" + filename + "] does not refer to an IMS Content Package");
  }
}
