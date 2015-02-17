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

package org.coppercore.common;

import java.util.Properties;

import org.coppercore.exceptions.ConfigurationException;

/**
 * This class provides configuration information to the application.
 * 
 * <p>
 * The information is read from a configuration file called
 * coppercore.properties. This file has to be located in the classpath to enable
 * this class to locate it.
 * 
 * <p>
 * The following information is retrieved from the coppercore.properties file
 * <ul>
 * <li>webroot - the file location of the root of the webserver content
 * directory (htdocs or inetpub)</li>
 * <li>schemalocation - the path to the location where the xml schemas needed
 * for xml schema validation are stored</li>
 * <li>uploadlocation - the path to the location where the uploaded content
 * packges will be stored from where they will be published</li>
 * <li>filepropertyuploadlocation - the path to the location where the files
 * uploaded by the users via the fileproperty are stored</li>
 * <li>contenturioffset - the uri to the root of the webserver where the
 * webcontent from the ld package is stored</li>
 * <li>smtphost - the uri to the smtp host for email</li>
 * <li>smtpport - the portnumber of the smtphost for email</li>
 * <li>smtpusername - the username used to authenticate on the smtp host for email</li>
 * <li>smtppassword - the password used to authenticate on the smtp host for email</li>
 * <li>systememail - the system email address used for notifications</li>
 * <li>removedoctype - boolean parameter indicating whether DOCTYPE declaration
 * should be removed from IMS LD content</li>
 * </ul>
 * All paths can be prefixed with an offset for parameterising the configuration
 * data. This prefix is set via the system property "coppercore.prefix", This
 * prefix will be prepended to all path parameters. To set the system property
 * use the -Dcoppercore.prefix=[path] commandline option when starting
 * coppercore.
 * <p>
 * When specifiying paths on a Microsoft Windows platform remember to escape the
 * \ by using \\. For example to point to c:\inetpub\wwwroot\ld use
 * webroot=c:\inetpub\\wwwroot\\ld
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.9 $, $Date: 2009/07/02 09:11:30 $
 */
public class Configuration {

	private String schemaLocation = null;
	private String webRoot = null;
	private String contentUriOffset = null;
	private String uploadLocation = null;
	private String filePropertyUploadLocation = null;
	private String smtpHost = null;
	private String smtpPort = null;
	private String smtpUser = null;
	private String smtpPassword = null;
	private String systemEmail = null;
	private String removeDoctype = null;

	private static Configuration conf = null;

	private Configuration() throws ConfigurationException {
		Properties props = new Properties();

		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("coppercore.properties"));
		} catch (Exception ex) {
			throw new ConfigurationException("Failed to load coppercore.properties, errormessage: " + ex.getMessage());
		}

		String prefix = System.getProperty("coppercore.prefix");
		if (prefix == null) {
			prefix = "";
		}

		webRoot = props.getProperty("webroot");
		webRoot = (webRoot == null) ? null : prefix + webRoot;

		schemaLocation = props.getProperty("schemalocation");
		schemaLocation = (schemaLocation == null) ? null : prefix + schemaLocation;

		uploadLocation = props.getProperty("uploadlocation");
		uploadLocation = (uploadLocation == null) ? null : prefix + uploadLocation;

		filePropertyUploadLocation = props.getProperty("filepropertyuploadlocation");
		filePropertyUploadLocation = (filePropertyUploadLocation == null) ? null : prefix + filePropertyUploadLocation;

		contentUriOffset = props.getProperty("contenturioffset");

		smtpHost = props.getProperty("smtphost");
		smtpPort = props.getProperty("smtpport");
		systemEmail = props.getProperty("systememail");
		smtpUser = props.getProperty("smtpuser");
		smtpPassword = props.getProperty("smtppassword");
		removeDoctype = props.getProperty("removedoctype");
	}

	private static Configuration getConfiguration() throws ConfigurationException {
		if (conf == null) {
			conf = new Configuration();
		}
		return conf;
	}

	private static String checkProperty(String value, String propertyName) throws ConfigurationException {
		if (value != null) {
			return value;
		}
		throw new ConfigurationException("Missing property [" + propertyName + "] in coppercore.properties");
	}

	/**
	 * Returns the schemalocation parameter from the coppercore.properties file.
	 * 
	 * <p>
	 * This parameter represents the path to the xml schema's used for
	 * validating the learning design package
	 * <p>
	 * Example: schemalocation=c:\\data\\schemas
	 * 
	 * @throws ConfigurationException
	 *             when there is an error accessing the property
	 * @return String the schemalocation
	 */
	public static String getSchemaLocation() throws ConfigurationException {
		String result = Configuration.getConfiguration().schemaLocation;
		return checkProperty(result, "schemalocation");
	}

	/**
	 * Returns the webroot parameter from the coppercore.properties file.
	 * 
	 * <p>
	 * This parameter represents the path to the physical documentroot of the
	 * webserver used for delivering the content.<br>
	 * Example
	 * webroot=jboss-3.2.6\\server\\default\\deploy\\jbossweb-tomcat50.sar
	 * \\ROOT.war\\ld
	 * 
	 * @throws ConfigurationException
	 *             when there is an error accessing the property
	 * @return String containing the webroot
	 */
	public static String getWebRoot() throws ConfigurationException {
		String result = Configuration.getConfiguration().webRoot;
		return checkProperty(result, "schemalocation");
	}

	/**
	 * Returns the contenturioffset parameter from the coppercore.properties
	 * file.
	 * <p>
	 * This parameter represents the url of the webserver where the content of
	 * the learning design package is stored.
	 * <p>
	 * Example: contenturioffset=http://localhost:8080/ld
	 * 
	 * @throws ConfigurationException
	 *             when there is an error accessing the property
	 * @return String containing the webroot
	 */
	public static String getContentUriOffset() throws ConfigurationException {
		String result = Configuration.getConfiguration().contentUriOffset;
		return checkProperty(result, "contenturioffset");
	}

	/**
	 * Returns the uploadlocation from the coppercore.properties file.
	 * <p>
	 * This parameter represents the path where the publish process stores the
	 * uploaded learning design content packages.
	 * <p>
	 * Example: uploadlocation=usr/home/upload
	 * 
	 * @throws ConfigurationException
	 *             when there is an error accessing the property
	 * @return String containing the uploadlocation
	 */
	public static String getUploadLocation() throws ConfigurationException {
		String result = Configuration.getConfiguration().uploadLocation;
		return checkProperty(result, "uploadlocation");
	}

	/**
	 * Returns the filepropertyuploadlocation parameter from the
	 * coppercore.properties file.
	 * <p>
	 * This parameter represents the path where the webplayer stores the files
	 * that are uploaded by the users via a file property.
	 * <p>
	 * Example: filepropertyuploadlocation=c:\\uploads
	 * 
	 * @throws ConfigurationException
	 *             when there is an error accessing the property
	 * @return String containing the filepropertyuploadlocation
	 */
	public static String getFilePropertyUploadLocation() throws ConfigurationException {
		String result = Configuration.getConfiguration().filePropertyUploadLocation;
		return checkProperty(result, "filepropertyuploadlocation");
	}

	/**
	 * Returns the smtphost parameter from the coppercore.properties file.
	 * <p>
	 * This parameter represents the internet address of the smtp email server
	 * for CopperCore.
	 * <p>
	 * Exampe: smtphost=smtp.whitehouse.gov
	 * 
	 * @throws ConfigurationException
	 *             when there is an error accessing the property
	 * @return String containing the stmphost parameter
	 */
	public static String getSmtpHost() throws ConfigurationException {
		String result = Configuration.getConfiguration().smtpHost;
		return checkProperty(result, "smtphost");
	}

	/**
	 * Returns the smtpport parameter from the coppercore.properties file.
	 * <p>
	 * This parameter represents the ip port of the smtp email server for
	 * CopperCore.
	 * <p>
	 * Exampe: smtpport=35
	 * 
	 * @throws ConfigurationException
	 *             when there is an error accessing the property
	 * @return String containing the stmpport parameter
	 */
	public static String getSmtpPort() throws ConfigurationException {
		String result = Configuration.getConfiguration().smtpPort;
		return checkProperty(result, "smtpport");
	}

	/**
	 * Returns the smtpuser parameter from the coppercore.properties file.
	 * <p>
	 * This parameter represents the user name used for authenticating the smtp
	 * email server for CopperCore.
	 * <p>
	 * Exampe: smtpuser=fred
	 * 
	 * @throws ConfigurationException
	 *             when there is an error accessing the property
	 * @return String containing the smtpuser parameter or null if this
	 *         parameter was omitted from the property file
	 */
	public static String getSmtpUser() throws ConfigurationException {
		return Configuration.getConfiguration().smtpUser;
	}

	

	/**
	 * Returns the smtppassword parameter from the coppercore.properties file.
	 * <p>
	 * This parameter represents the password used for authenticating the smtp
	 * email server for CopperCore.
	 * <p>
	 * Exampe: smtppassword=secret
	 * 
	 * @throws ConfigurationException
	 *             when there is an error accessing the property
	 * @return String containing the smtppassword parameter or null if this
	 *         parameter was omitted from the property file
	 */
	public static String getSmtpPassword() throws ConfigurationException {
		return Configuration.getConfiguration().smtpPassword;
	}

	/**
	 * Returns the sender email address parameter from the coppercore.properties
	 * file.
	 * <p>
	 * This parameter represents the sender email address used for sending
	 * notifcations.
	 * <p>
	 * Example: systememail=coppercore@coppercore.org
	 * 
	 * @throws ConfigurationException
	 *             when there is an error accessing the property
	 * @return String containing the system email parameter, or
	 *         notification@coppercore.nl if the parameter is missing
	 */
	public static String getSystemEmail() throws ConfigurationException {
		String result = Configuration.getConfiguration().systemEmail;
		// return checkProperty(result, "smtpport");
		return (result == null) ? "notification@coppercore.org" : result;
	}

	/**
	 * Returns the removedoctype parameter from the coppercore.properties file.
	 * <p>
	 * This parameter determines whether a DOCTYPE declaration in a IMS LD
	 * content resource will be removed or not. The option has been added to
	 * solve a problem with some XHTML editors that require a DOCTYPE
	 * declaration. This DOCTYPE declaration prevents some parsers to work
	 * correctly when LD content is added to the XHTML. Allowed values are true
	 * and false. Default is false..
	 * <p>
	 * Example: removedoctype=true
	 * 
	 * @throws ConfigurationException
	 *             when there is an error accessing the property
	 * @return true if removedoctype parameter was set to TRUE or true, false
	 *         otherwise
	 */
	public static boolean removeDoctype() throws ConfigurationException {
		String result = Configuration.getConfiguration().removeDoctype;
		return (result != null) ? result.equalsIgnoreCase("true") ? true : false : false;
	}

}
