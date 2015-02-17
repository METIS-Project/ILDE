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


/**
 * @todo validate class tag to contain at least one class name (implied attribute, should be required)
 *
 */
package org.coppercore.validator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.coppercore.common.MessageList;
import org.coppercore.exceptions.SemanticException;
import org.coppercore.exceptions.TechnicalException;
import org.coppercore.exceptions.ValidationException;
import org.coppercore.resources.MessageKeys;
import org.coppercore.resources.MessageUtil;

/**
 * IMSExplodedCP represents an IMS Learning Design Content Package.
 *
 * <p>An exploded Content Packages is a collection of files stored on a
 * filesystem. The manifest is stored in a file called imsmanifest.xml in the
 * root of the package.
 *
 * <p>This class is based on the work of Paul Sharples.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.10 $, $Date: 2009/07/02 09:11:30 $
 */
public class IMSExplodedCP extends IMSPackage implements MessageKeys {

  public boolean validate() {
		// TODO Auto-generated method stub
		return validate(contentPackage);
	}

/**
   * Constructs a new IMSPackage object with the given parameters.
   *
   * @param contentPackage is the directory where the unpacked IMS Learning
   *   Design content package is located
   * @param schemaLocation is the path where the ims-ld schemas can be found
   * @param messageList is the CCLogger object where all messages generated
   *   during the validation are stored
   */
  public IMSExplodedCP(String contentPackage, String schemaLocation,
                       MessageList messageList) {
    super(contentPackage, schemaLocation, messageList);
  }

  protected void analysePackage() throws ValidationException {
    try {
      File manifestFileRef = new File(contentPackage, IMSPackage.IMSMANIFEST_FILE);
      if (manifestFileRef.exists()) {
        byte[] xmlBinding = getBytesFromFile(manifestFileRef);
        manifest = new IMSLDManifest(xmlBinding, this, schemaLocation); // create the manifest
      }
      else {
        throw new SemanticException(MessageUtil.formatMessage(MSG_MANIFEST_NOT_FOUND, contentPackage));
      }
      // next do the resources....
      int basePathLength = contentPackage.getPath().length() + 1;
      parseForFiles(contentPackage, basePathLength);
    }
    catch (Exception ex) {
      throw new TechnicalException(ex);
    }
  }

  private void parseForFiles(File dir, int basePathLength) {
    if (dir.isDirectory()) {
      File[] children = dir.listFiles();
      for (int i = 0; i < children.length; i++) {
        if (children[i].isDirectory()) {
          parseForFiles(children[i], basePathLength);
        }
        else {
          File child = children[i];
          if (!child.getName().equals(IMSPackage.IMSMANIFEST_FILE)) {
            // Strip filepath offset to exploded package
            String relativeLink = child.toString().substring(
                basePathLength, child.toString().length());
            File relativeFile = new File(relativeLink);
            // use File as key to resolve path separators problems (/ and \)
            files.put(getURIKey(relativeLink), new PhysicalFile(relativeFile)); //create a resource
          }
        }
      }
    }
  }

//Returns the contents of the file in a byte array.
  private static byte[] getBytesFromFile(File file) throws IOException {
    FileInputStream fin1 = new FileInputStream(file);
    DataInputStream din1 = new DataInputStream(fin1);
    //byte Array output stream is a temporary byte array storage place.
    ByteArrayOutputStream byteArrOut = new ByteArrayOutputStream();
    int ln;
    byte[] buf = new byte[1024 * 12];

    while ( (ln = din1.read(buf)) != -1) {
      byteArrOut.write(buf, 0, ln); // writes the byte as read from the input
      // stream onto the byte array buf.
    }
    fin1.close();
    din1.close();
    return byteArrOut.toByteArray(); //gets the byte array back
  }

  /**
   * Extracts the ims LD package into the specified destination path.
   *
   * <p>Files containing globalcontent are patched before they are stored.
   *
   * @param destinationPath String the location where the resources are stored
   * @throws IOException when there is an error accessing the filesytem during reading or writing
   * @throws TechnicalException when there is an error patching the global content
   */
  public void storeResources(String destinationPath) throws IOException, TechnicalException {
    File destinationFolder = new File(destinationPath);
    destinationFolder.mkdir();
    copyFolder(contentPackage, destinationFolder, contentPackage.getPath().length() + 1, destinationPath);
  }

  private  boolean copyFolder(File srcFolder, File destFolder, int basePathLength, String destinationPath) throws
      IOException, TechnicalException {
    if (srcFolder.equals(destFolder)) {
      throw new IOException("Source and Target Folders cannot be the same");
    }
    destFolder.mkdirs();
    File[] srcFiles = srcFolder.listFiles();
    for (int i = 0; i < srcFiles.length; i++) {
      File srcFile = srcFiles[i];
      if (srcFile.isDirectory()) {
        copyFolder(srcFile, new File(destFolder, srcFile.getName()), basePathLength, destinationPath);
      }
      else {
        String relativeLink = srcFile.toString().substring(basePathLength, srcFile.toString().length());
        PhysicalFile file = (PhysicalFile) files.get(getURIKey(relativeLink));
        if (file != null) {
          if (file.containsGlobalElements) {
            file.patch(getBytesFromFile(srcFile), manifest, destinationPath);
          }
          else {
            copyFile(srcFile, new File(destFolder, srcFile.getName()));
          }
        }
      }
    }
    return true;
  }

  private static void copyFile(File srcFile, File destFile) throws IOException {
    if (srcFile.equals(destFile)) {
      throw new IOException("Source and Target Files cannot be the same");
    }

    int bufSize = 1024;
    byte[] buf = new byte[bufSize];
    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile), bufSize);
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile), bufSize);
    int size;
    while ( (size = bis.read(buf)) != -1) bos.write(buf, 0, size);
    bos.flush();
    bos.close();
    bis.close();
  }

  protected boolean validateGlobalContent() throws ValidationException {
    boolean result = true;
    try {
      Iterator it = files.values().iterator();
     while (it.hasNext()) {
       PhysicalFile file = (PhysicalFile) it.next();
          if ( (file != null) && (file.hasGlobalContent())) {
            byte[] globalContent = getBytesFromFile(new File(contentPackage, file.getFilePath()));
            result &= file.isValid(globalContent, messageList, manifest);
          }
      }
      return result;
    }
    catch (Exception ex) {
      throw new TechnicalException(ex);
    }
  }

}
