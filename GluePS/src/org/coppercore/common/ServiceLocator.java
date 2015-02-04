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

package org.coppercore.common;

import java.util.Hashtable;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.coppercore.exceptions.ServiceLocatorException;

/**
 * Utility class for locating the CopperCore EJB's.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.9 $, $Date: 2005/01/05 15:12:57 $
 */
public class ServiceLocator {
  private static ServiceLocator serviceLocator;

  private static Context context;

  private ServiceLocator() throws ServiceLocatorException {
    context = getInitialContext();
  }

  /**
   * Returns an new default initial context.
   * 
   * @throws ServiceLocatorException
   *           when the initial context could not be retrieved
   * @return Context the created initial context
   */
  private Context getInitialContext() throws ServiceLocatorException {
    try {
      Hashtable environment = new Hashtable();

      //      environment.put(Context.INITIAL_CONTEXT_FACTORY,
      // "org.jnp.interfaces.NamingContextFactory");
      //     environment.put(Context.URL_PKG_PREFIXES,
      // "org.jboss.naming:org.jnp.interfaces");
      //   environment.put(Context.PROVIDER_URL, "jnp://localhost:1099");

      return new InitialContext(environment);
    } catch (NamingException e) {
      throw new ServiceLocatorException(e);
    }
  }

  /**
   * Retrieves the remote EJB home interface for the given name and class.
   * 
   * @param ejbName
   *          String the name of the ejb bean to create the home interface for
   * @param ejbClass
   *          Class the class of the ejb to create the home interface for
   * @throws ServiceLocatorException
   *           when there is an error retrieving the interface
   * @return EJBHome the home interface for the specified bean
   */
  public EJBHome getEjbHome(String ejbName, Class ejbClass) throws ServiceLocatorException {
    try {
      Object object = context.lookup(ejbName);
      EJBHome ejbHome = null;
      ejbHome = (EJBHome) PortableRemoteObject.narrow(object, ejbClass);
      if (ejbHome == null) {
        throw new ServiceLocatorException("Could not get home for " + ejbName);
      }
      return ejbHome;
    } catch (NamingException ne) {
      throw new ServiceLocatorException(ne);
    }
  }

  /**
   * Returns the local home interface for the specified bean.
   * 
   * @param ejbName
   *          String the name of the ejb to return the interface of
   * @throws ServiceLocatorException
   *           when the interface could not be created
   * @return EJBLocalHome the local home interface for the specified bean
   */
  public EJBLocalHome getEjbLocalHome(String ejbName) throws ServiceLocatorException {
    try {
      Object object = context.lookup(ejbName);
      EJBLocalHome ejbLocalHome = null;
      ejbLocalHome = (EJBLocalHome) object;
      if (ejbLocalHome == null) {
        throw new ServiceLocatorException("Could not get local home for " + ejbName);
      }
      return ejbLocalHome;
    } catch (NamingException ne) {
      throw new ServiceLocatorException(ne);
    }
  }

  /**
   * Returns a single ServiceLocator instance.
   * 
   * <p>
   * Requesting a ServiceLocator is an expensive operation, therefor this class
   * implements a singleton pattern to retrieve only one instance of the
   * ServiceLoactor and to re-use this instance later on.
   * 
   * @throws ServiceLocatorException
   *           when the ServiceLocator could not be created
   * @return ServiceLocator the singleton ServiceLocator
   */
  public static synchronized ServiceLocator getInstance() throws ServiceLocatorException {
    if (serviceLocator == null) {
      serviceLocator = new ServiceLocator();
    }
    return serviceLocator;
  }

}