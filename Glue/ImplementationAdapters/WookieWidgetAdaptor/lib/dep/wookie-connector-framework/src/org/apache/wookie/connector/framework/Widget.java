package org.apache.wookie.connector.framework;

/*
 * Copyright 2008 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");   *
 * you may not use this file except in compliance with the License.  *
 * You may obtain a copy of the License at                           *
 *                                                                   *
 *   http://www.apache.org/licenses/LICENSE-2.0                      *
 *                                                                   *
 * Unless required by applicable law or agreed to in writing,        *
 * software distributed under the License is distributed on an       *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY            *
 * KIND, either express or implied.  See the License for the         *
 * specific language governing permissions and limitations           *
 * under the License.                                                *
 */
import java.net.URL;

/**
 * A client side representation of a widget. 
 * 
 * @refactor this duplicates data stored in the Widget bean on the server side.
 */
public class Widget { 
  String identifier;
  String title;
  String description;
  URL icon;

  public Widget(String identifier, String title, String description, URL icon) {
    this.identifier = identifier;
    this.title = title;
    this.description = description;
    this.icon = icon;
  }
  
  /**
   * Get a unique identifier for this widget type.
   * 
   * @return
   */
  public String getIdentifier() {
    return identifier;
  }

  /**
   * Get the human readable title of this widget.
   * @return
   */
  public String getTitle() {
    return title;
  }

  /**
   * Get the location of a logo for this widget.
   * @return
   */
  public URL getIcon() {
    return icon;
  }
  
  /**
   * Get the description of the widget.
   * 
   * @return
   */
  public String getDescription() {
    return description;
  }
}
