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

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A connection to a Wookie server. This maintains the necessary data for
 * connecting to the server and provides utility methods for making common calls
 * via the Wookie REST API.
 * 
 */
public class WookieServerConnection implements Serializable {
  private static final long serialVersionUID = 1L;
  private static final Logger logger = LoggerFactory
      .getLogger(WookieServerConnection.class);
  private String url;
  private String apiKey = "TEST";
  private String sharedDataKey = "mysharedkey";

  /**
   * Create a connection to a Wookie server at a given URL.
   * @param url the URL of the wookie server
   * @param apiKey the API key for the server
   * @param sharedDataKey the sharedDataKey for the server connection 
   * 
   * @throws WookieConnectorException if there is a problem setting up this connection.
   */
  public WookieServerConnection(String url, String apiKey, String sharedDataKey) throws WookieConnectorException {
    setURL(url);
    setApiKey(apiKey);
    setSharedDataKey(sharedDataKey);
  }

  /**
   * Get the URL of the wookie server.
   * 
   * @return
   * @throws WookieConnectionException
   */
  public String getURL() {
    return url;
  }
  
  /**
   * Set the URL of the wookie server.
   * 
   * @throws WookieConnectionException
   */
  public void setURL(String newUrl) throws WookieConnectorException {
    this.url = newUrl;
  }
  
  /**
   * Get the API key for this server.
   * 
   * @return
   */
  public String getApiKey() {
    return apiKey;
  }

  /**
   * Set the API key for this server.
   * 
   */
  public void setApiKey(String newApiKey) {
    apiKey = newApiKey;
  }

  /**
   * Get the shared data key for this server.
   * 
   * @return
   */
  public String getSharedDataKey() {
    return sharedDataKey;
  }

  /**
   * Set the shared data key for this server.
   *
   */
  public void setSharedDataKey(String newKey) {
    sharedDataKey = newKey;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder("Wookie Server Connection - ");
    sb.append("URL: ");
    sb.append(getURL());
    sb.append("API Key: ");
    sb.append(getApiKey());
    sb.append("Shared Data Key: ");
    sb.append(getSharedDataKey());
    return sb.toString();
  }
}
