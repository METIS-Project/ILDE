/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wookie.connector.framework;

import java.io.IOException;
import java.util.HashMap;


/**
 * This service needs to be implemented on each platform. It provides methods
 * for interfacing with the host environment. In order to use the connection
 * service the Wookie Connection must have been initialised by calling
 * connect(...), usually from within the constructor.
 */
public interface IWookieConnectorService {
  
  /**
   * Setup the connection to the Wookie service. This must be called before any
   * other method.
   * 
   * @param conn
   *          - a connection to the Wookie server
   * @return true if the connection has been correctly configured.
   * @throws WookieConnectorException
   *           if there is a problem setting up the connection
   */
  public void setConnection(WookieServerConnection conn)
      throws WookieConnectorException;
  
  /**
   * Get the currently active connection to the Wookie server.
   * 
   * @return
   */
  public WookieServerConnection getConnection();

  /**
   * Retrieve the details of the current user.
   */
  public User getCurrentUser();

  /**
   * Set the current user.
   * 
   * @param user - the current user
   */
  public void setCurrentUser(User user);

  /**
   * Set the current user.
   * 
   * @param userId - id of the current user. Usually this would be the login
   * name of the user, but it need not be, it simply needs to be a unique identifier
   * for theis user.
   */
  public void setCurrentUser(String userId);
  
  /**
   * Retrieve the details of a specific user, identified by their userId.
   */
  public User getUser(String userId);

  /**
   * Get or create an instance of a widget. The current user will be added as a participant.
   * 
   * @param widget
   * @return the widget instance
   * @throws IOException
   * @throws SimalRepositoryException
   */
  public WidgetInstance getOrCreateInstance(Widget widget) throws IOException,
      WookieConnectorException;

  /**
   * Get or create an instance of a widget. The current user will be added as a participant.
   * 
   * @param guid global identifier for widget to be instantiated
   * @return the widget instance
   * @throws IOException
   * @throws SimalRepositoryException
   */
  public WidgetInstance getOrCreateInstance(String guid) throws IOException,
      WookieConnectorException;
  
  /**
   * Add a participant to a widget.
   * 
   * @param instance the Widget Instance to add the participant to
   * @param user the user to add as a participant
   * 
   * @throws WookieConnectorexception
   */
  public void addParticipant(WidgetInstance widget, User user) throws WookieConnectorException;
  
  /**
   * Get a set of all the available widgets in the server. If there is an error
   * communicating with the server return an empty set, or the set received so
   * far in order to allow the application to proceed. The application should
   * display an appropriate message in this case.
   * 
   * @return
   * @throws SimalException
   */
  public HashMap<String, Widget> getAvailableWidgets()
      throws WookieConnectorException;
  /**
   * Get all the instances of widgets that are currently managed by this service.
   *
   * @return
   */
  public WidgetInstances getInstances();

}