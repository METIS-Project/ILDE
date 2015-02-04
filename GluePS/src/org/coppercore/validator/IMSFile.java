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

package org.coppercore.validator;

import org.coppercore.common.URL;
import org.coppercore.exceptions.SemanticException;
import org.coppercore.exceptions.URLSyntaxException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Represents a file in the resource section of the ims ld manifest.
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.12 $, $Date: 2009/07/02 09:11:29 $
 */
public class IMSFile
    extends IMSObject {
  private URL url = null;
  private boolean isGlobalContent = false;

  /**
   * Creates a new IMSFile instance with its parameters set to the specified values.
   * @param aNode Node the xml node in the manifest describing this file
   * @param aManifest IMSLDManifest the manifest of this content package
   * @param aBaseURL URL the base url to add to the url of the current file
   */
  public IMSFile(Node aNode, IMSLDManifest aManifest, URL aBaseURL) {
    super.initialize(aNode, aManifest);

    //constuct an URI from the base and the uriString
    try {

      // get href attribute for this node, if absent href will be empty
      String href = ( (Element) node).getAttribute("href");
      url = new URL(aBaseURL, href);
    }
    catch (URLSyntaxException e) {
      isValid = false;
      getLogger().logError("Invalid href attribute for file " + toString() +
                           ".  >> " + e.getMessage());
    }

  }

  protected void isValid() throws SemanticException {
    if (!url.isAbsolute()) {
      // check if resource is in package
      if (!manifest.getPackage().hasResource(url)) {
        throw new SemanticException("Resource " + url.toString() +
                                    " not found in package.");
      }
    }
  }

  /**
   * Returns the url of the file resource.<p>This url has been constructed by combining the
   * base url and the href url of this resource.
   * @return URL the url of the file resource
   */
  public URL getURL() {
    return url;
  }

  /**
   * Marks this file resource as having global content elements.
   *
   * <p>File resource containing global content elements need special treatment
   * when publishing and during the runtime delivery of the content.
   */
  protected void setGlobalContent() {
    isGlobalContent = true;
   
    manifest.getPackage().setFileAsGlobalContent(url);
  }

  /**
   * Returns true if this file resource contains global content elements.
   * @return boolean true if this file resource contains global content elements
   */
  protected boolean isGlobalContent() {
    return isGlobalContent;
  }
}
