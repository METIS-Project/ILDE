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

package org.coppercore.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * This class contains utility functions for accessing the database.
 *
 * <p> The class implements a method for retrieving a connection to the database and another for returning the last auto
 * generated key for a record in the database.
 *
 * <p> The class is implemented as a singleton, to call a method the callee must first get the singleton instance using
 * the static getUtil() method.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.5 $, $Date: 2006/08/17 15:14:55 $
 */
public class Util {
  // private static String JNDI_RESOURCE_BES = "java:comp/env/jdbc/Publication";
  private static String JNDI_RESOURCE_JBoss = "java:/Publication";

  private static Util instance = null;

  private DataSource ds = null;
  private String identityQuery = null;

  private Util() {
    try {
      ds = (DataSource) (new InitialContext()).lookup(JNDI_RESOURCE_JBoss);
    }
    catch (NamingException ex) {
      throw new EJBException(ex);
    }
  }

  /**
   * Returns a connection to the database.
   *
   * @throws SQLException when there is an error connecting to the database
   * @return Connection the connection to the database
   */
  public Connection getConnection() throws SQLException {
  	
    return ds.getConnection();
  }
  
 
  /**
   * Returns the database specific sql statement for retrieving the last auto generated key for the
   * specified table.
   *
   * <p>If the database driver is not supported, the method throws an exception. The method uses the
   * database.properties file to lookup the correct sql statement. See the database.properties file
   * for more information about adding support for another database to CopperCore
   *
   * @param connection Connection to the database to retrieve the sql statement for
   * @param tablename String the name of the table which is merged into the sql statement
   * @param columnname String the name of the column which is merged into the sql statement
   * @throws SQLException when there is an error retrieving or executing the sql statement
   * @return String the database specific sql statement for retrieving the auto generated key
   */
  private String getIdentityQuery(Connection connection, String tablename, String columnname) throws SQLException {
  if (identityQuery == null) {
      Properties props = new Properties();

      try {
        String driverName = connection.getMetaData().getDriverName();
        props.load(getClass().getResourceAsStream("database.properties"));
        Enumeration keys = props.keys();
        while ((identityQuery == null) && (keys.hasMoreElements())) {
          String key = (String) keys.nextElement();
          if (key.startsWith("driver.name.")) {
            String value = props.getProperty(key);
            if ((value != null) && (value.equals(driverName))) {
              key = "query." + key.substring(key.lastIndexOf('.') + 1);
              identityQuery = props.getProperty(key);
            }
          }
        }
        if (identityQuery == null) {
          throw new SQLException("unsupported database driver: " + driverName);
        }
      }
      catch (Exception ex) {
        throw new SQLException("Failed to parse database.properties, errormessage: " + ex.getMessage());
      }
    }

    String query = identityQuery;
    query = query.replaceAll("<tablename>", tablename);
    query = query.replaceAll("<columnname>", columnname);
    return query;
  }

  /**
   * Returns the most recent identity value.
   *
   * <p> This utility method is necessary because most jdbc drivers do not implement the level 3
   * method for retrieving the identity of a newly created record. This method hides the intricacies
   * of using the driver specific sql extensions to retrieve this value.
   *
   * <p>If the database driver is not supported, the method throws an exception. The method uses the
   * database.properties file to lookup the correct sql statement. See the database.properties file
   * for more information about adding support for another database to CopperCore
   *
   * @param connection Connection is the actual connection to the database
   * @param tablename is the name of the table to retrieve the identity value for
   * @param columnname String the name of the column to retrieve the identity value for
   * @throws SQLException whenever the drivername of the jdbc driver or the identity value couldn't
   *   be retrieved
   * @return int containing the identity value
   */
  public int getIdentity(Connection connection, String tablename, String columnname) throws
      SQLException {
    String sql = getIdentityQuery(connection, tablename, columnname);

    int result = -1;

    PreparedStatement statement = connection.prepareStatement(sql);
    try {
      ResultSet rs = statement.executeQuery();

      if (rs.next()) {
        result = rs.getInt(1);
      }
      else {
        throw new SQLException("Could not retrieve identity value for " +
                               tablename);
      }
    }
    finally {
      statement.close();
    }
    return result;
  }

  /**
   * Returns the singleton Util instance of this class.<p>
   * If this local instance of the Util class was not already created, the method creates a new
   * instance.
   *
   * @return Util the singleton instance of this class
   */
  public static Util getInstance() {
    if (instance == null) {
      instance = new Util();
    }
    return instance;
  }

}
