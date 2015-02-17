########################################################################################
# GLUE!PS - README                                                                     #
# GLUE!PS (standing for Group Learning Unified Environment - Pedagogical Scripting)    #
# is a software architecture and data model designed to deploy (and manage in run-time)#
# learning designs specified in different languages (e.g. the IMS-LD specification),   #
# into different existing Virtual Learning Environments (VLEs, e.g. Moodle).           #
# Copyright 2012 GSIC (UVA)                                                            #
########################################################################################


###########
# LICENSE #
###########

Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
Valladolid, Spain. https://www.gsic.uva.es/

This file is part of Glue!PS.

Glue!PS is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or 
(at your option) any later version.

Glue!PS is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

The interactive user interfaces in modified source and object code versions
of this program must display Appropriate Legal Notices, as required under
Section 5 of the GNU Affero General Public License version 3.

In accordance with Section 7(b) of the GNU Affero General Public License version 3,
these Appropriate Legal Notices must display the "Glue!PS" logo with a link to the 
website http://www.gsic.uva.es/glueps/.


If the display of the logo is not reasonably feasible for
technical reasons, the Appropriate Legal Notices must display the words
"Powered by Glue!PS" with the link to the website http://www.gsic.uva.es/glueps/.


########################
# THIRD PARTY LICENSES #
########################

This product contains some third party software provided to make easier the
installation and configuration process. All the third party software included
is redistributed in binary form under the terms and conditions of their 
original licenses. These licenses are compatible with both the commercial and 
the GPL licenses that govern GLUEPSManager, for the purposes here 
intended.

The third party software included is:

 * JavaBeans Activation Framework, version 1.0.2
   by Oracle
   under Sun Mycrosystems Binary Code License Agreement
   place at GLUEPS_HOME/lib/activation.jar
   see http://www.oracle.org
   
 * Google App Engine
   by Google
   under Google License Agreement
   place at GLUEPS_HOME/lib/appengine-api.jar
   see https://developers.google.com/
   
 * Apache Commons Codec, version 1.4
   by The Apache Software Foundation
   under Apache 2 license
   place at GLUEPS_HOME/lib/commons-codec-1.4.jar
   see http://commons.apache.org/codec/
   
 * Apache Commons Collections, version 3.2.1
   by The Apache Software Foundation
   under Apache 2 license
   place at GLUEPS_HOME/lib/commons-collections-3.2.1.jar
   see http://commons.apache.org/collections/
   
 * Apache Commons FileUpload, version 1.2.2
   by The Apache Software Foundation
   under Apache 2 license
   place at GLUEPS_HOME/lib/commons-fileupload-1.2.2.jar
   see http://commons.apache.org/fileupload/
   
 * Apache Commons IO, version 2.0.1
   by The Apache Software Foundation
   under Apache 2 license
   place at GLUEPS_HOME/lib/commons-io-2.0.1.jar
   see http://commons.apache.org/io/
   
 * Apache Commons Lang, version 3.3.1
   by The Apache Software Foundation
   under Apache 2 license
   place at GLUEPS_HOME/lib/commons-lang3-3.1.jar
   see http://commons.apache.org/lang/
      
 * Apache Commons Logging, version 1.1.1
   by The Apache Software Foundation
   under Apache 2 license
   place at GLUEPS_HOME/lib/commons-logging-1.1.1.jar
   see http://commons.apache.org/logging/
   
  * Dom4j, flexible XML framework for Java, version 1.6.1
   place at GLUEPS_HOME/lib/dom4j-1.6.1.jar
   see http://dom4j.sourceforge.net/dom4j-1.6.1/
   
 * Eclipselink, version 2.0.0
   by The Eclipse Foundation
   under Eclipse Public License 1.0 and Eclipse Distribution License 1.0
   placed at GLUE_HOME/lib/dep/eclipselink.jar
   see http://www.eclipse.org/eclipselink
   
 * Google-gson, version 2.1
   by Google
   under Google License Agreement
   placed at GLUEPS_HOME/lib/gson-2.1.jar
   see http://code.google.com/p/google-gson/  
     
 * Apache HttpClient, version 4.1.3
   by The Apache Software Foundation
   under Apache 2 license
   placed at GLUEPS_HOME/lib/httpclient-4.1.3.jar
   see http://hc.apache.org/httpcomponents-client-ga/httpclient/index.html
          
 * Apache HttpClient Cache, version 4.1.3
   by The Apache Software Foundation
   under Apache 2 license
   placed at GLUEPS_HOME/lib/httpclient-cache-4.1.3.jar
   see http://hc.apache.org/httpcomponents-client-ga/httpclient-cache/index.html
          
 * Apache HttpClient Core, version 4.1.4
   by The Apache Software Foundation
   under Apache 2 license
   placed at GLUEPS_HOME/lib/httpcore4.1.4.jar
   see http://hc.apache.org/httpcomponents-core-ga/index.html
               
 * Apache HttpMime, version 4.1.3
   by The Apache Software Foundation
   under Apache 2 license
   placed at GLUEPS_HOME/lib/httpcore4.1.4.jar
   see http://hc.apache.org/httpcomponents-client-ga/httpmime/index.html
                    
 * Apache XMLBeans, version 2.4.0
   by The Apache Software Foundation
   under Apache 2 license
   placed at GLUEPS_HOME/lib/jsr173_1.0_api.jar
   see http://xmlbeans.apache.org/index.html
   
 * Apache log4j, version 1.2.16
   by The Apache Software Foundation
   under Apache 2 license
   placed at GLUEPS_HOME/lib/log4j-1.2.16.jar
   see http://xmlbeans.apache.org/index.html
        
 * Apache WS Common Utilities, version 1.0.2
   by The Apache Software Foundation
   under Apache 2 license
   placed at GLUEPS_HOME/lib/ws-commons-util-1.0.2.jar
   see http://ws.apache.org/commons/util/   
           
 * Apache XML-RPC, version 3.1.2
   by The Apache Software Foundation
   under Apache 2 license
   placed at GLUEPS_HOME/lib/xmlrpc*.jar
   see http://ws.apache.org/xmlrpc/xmlrpc-common/
        
 * Apache James Mime4j
   by The Apache Software Foundation
   under Apache 2 license
   placed at GLUEPS_HOME/lib/org.apache.james.mime4j.jar
   see http://james.apache.org/index.html
   
 * JAXB, Java Architecture for XML Binding
   by The Java Community Process
   under CDDL Open source license
   placed at GLUEPS_HOME/lib/jaxb*.jar
   see http://jaxb.java.net/
         
 * JDO, Java Data Objects API, version 3.0.1
   by The Java Community Process
   under Apache 2 license
   placed at GLUEPS_HOME/lib/jdo-api-3.0.1.jar
   see http://db.apache.org/jdo/

 * JDOM, Java Representation of an XML document, version 1.1.3
   by JDOM Project
   under Apache-style open source license, with the acknowledgment clause removed
   placed at GLUEPS_HOME/lib/jdom.jar
   see http://www.restlet.org
      
 * JavaMail, JavaMail API, version 1.4.3
   by Oracle
   under Sun Mycrosystems Binary Code License Agreement
   placed at GLUEPS_HOME/lib/mailapi.jar
   see http://www.oracle.com/technetwork/java/javamail/index.html   
         
 * Jetty, Eclipse Jetty, version 1.4.3
   by Eclipse Foundation
   under Apache 2 license, Eclipse Public license 1
   placed at GLUEPS_HOME/lib/org.eclipse.jetty.*
   see http://www.eclipse.org/jetty/
   
 * RESTlet, version 2.0.11
   by Noelios Technologies
   under Apache 2 license
   placed at GLUEPS_HOME/lib/org.restlet.*
   see http://www.restlet.org

 * Procrun, version 1.0.8
   by The Apache Software Foundation
   under Apache 2 license
   file bin/ServiceInstaller.exe
   see http://commons.apache.org/daemon/procrun.html
 
The original license documents, copyright notices and other information 
documents are included in the subdirectory containing each third party library.

Only the binary jar files needed to run GLUEPSManager have been included. 
None of them was modified by GSIC. If you need a full copy of any third party 
library, visit the web site of its authors.
