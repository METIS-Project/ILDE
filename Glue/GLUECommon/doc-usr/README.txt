###################################################################
# GLUE                                                            #
#                                                                 #
# GSIC's  middleware architecture for the integration of existing #
# external tools in existing Virtual Learning Environments (VLEs) #
#                                                                 #
# Copyright 2011 GSIC (UVA)                                       #
###################################################################


###########
# LICENSE #
###########

GLUEletManager, tool adapters and VLE adapters included in this package 
are properties of the Intelligent & Cooperative Systems 
Research Group (GSIC) from the University of Valladolid (UVA). 

GLUEletManager, tool adapters and VLE adapters in this package are licensed 
under the GNU General Public License (GPL) EXCLUSIVELY FOR NON-COMMERCIAL USES.
Please, note this is an additional restriction to the terms of GPL that must 
be kept in any redistribution of the original code or any derivative work by 
third parties. To see the details of the GPL, please check the GPL.txt file.

If you intend to use GLUEletManager, any tool adapter or any VLE adapter in
this package for any commercial purpose you can contact to GSIC to obtain a 
commercial license at <glue@gsic.tel.uva.es>.

If you have licensed this package under a commercial license from GSIC,
please see the file LICENSE.txt included in this package for the
terms of the license.


########################
# THIRD PARTY LICENSES #
########################

This package contains some third party software provided to make easier the
installation and configuration process. All the third party software included
is redistributed in binary form under the terms and conditions of their 
original licenses. These licenses are compatible with both the commercial and 
the GPL licenses that govern this package, for the purposes here intended.

The third party software included is:

 * Eclipselink, version 2.0.0
   by The Eclipse Foundation
   under Eclipse Public License 1.0 and Eclipse Distribution License 1.0
   placed at GLUE_HOME/lib/dep/eclipselink
   see http://www.eclipse.org/eclipselink
	
 * Google Data API Client Libraries for Java, version 1.41.1
   by Google Inc.
   under Apache 2 license
   placed at GLUE_HOME/tool_adapters/gdata/lib/dep/gdata-1.41.1
   see http://code.google.com/apis/gdata/docs/client-libraries.html
 
 * JavaMail API, version 1.4.3
   by Sun Microsystems Inc.
   under Sun Microsystems, Inc. ("Sun") entitlement for JavaMail
   placed at GLUE_HOME/tool_adapters/gdata/lib/dep/javamail-1.4.3
   see http://www.oracle.com/technetwork/java/javamail/javamail143-243221.html

 * RESTlet, version 2.0.11
   by Noelios Technologies
   under Apache 2 license
   placed at GLUE_HOME/lib/dep/restlet-jse-2.0.11
   see http://www.restlet.org

 * Simple Logging Facade for Java (SLF4J), version 1.6.0
   by QOS.ch
   under MIT license
   placed at GLUE_HOME/tool_adapters/wookiewidgets/lib/dep/slf4j
   see http://www.slf4j.org
 
 * Wookie Connector Framework for Java
   by The Apache Software Foundation
   jar file generated by GSIC
   under Apache 2 license
   placed at 
     GLUE_HOME/tool_adapters/wookiewidgets/lib/dep/wookie-connector-framework
   see http://incubator.apache.org/wookie/docs/embedding.html
   
 * Prototype Window
   by Sébastien Gruhier
   under a MIT-style license 
   placed at GLUE_HOME/vle_adapters/mediawiki_vle_adapter/
               MediaWikiVLEGlueExtension/javascripts/windows and 
			 GLUE_HOME/vle_adapters/mediawiki_vle_adapter/
               MediaWikiVLEGlueExtension/themes
   see http://prototype-window.xilinus.com
  
 * Procrun, version 1.0.8
   by The Apache Software Foundation
   under Apache 2 license
   file bin/ServiceInstaller.exe
   see http://commons.apache.org/daemon/procrun.html
 
The original license documents, copyright notices and other information 
documents are included in the subdirectory containing each third party library.

Only the binary jar files needed to run some component in this package have 
been included. None of them was modified by GSIC. If you need a full copy 
of any third party library, visit the web site of its authors.
