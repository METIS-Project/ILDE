/* Copyright (c) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.google.gdata.model.gd;

import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.util.Namespaces;

/**
 * Describes the status of an event attendee.
 *
 * 
 */
public class AttendeeStatus extends Element {

  /** Value. */
  public static final class Value {

    /** Attendee has accepted. */
    public static final String ACCEPTED = Namespaces.gPrefix + "event.accepted";

    /** Attendee has declined. */
    public static final String DECLINED = Namespaces.gPrefix + "event.declined";

    /** Invitation has been sent, but the person has not accepted. */
    public static final String INVITED = Namespaces.gPrefix + "event.invited";

    /** Attendee has accepted tentatively. */
    public static final String TENTATIVE = Namespaces.gPrefix +
        "event.tentative";

    /** Array containing all available values. */
    private static final String[] ALL_VALUES = {
      ACCEPTED,
      DECLINED,
      INVITED,
      TENTATIVE};

    /** Returns an array of all values defined in this class. */
    public static String[] values() {
      return ALL_VALUES;
    }

    private Value() {}
  }

  /**
   * The key for this element.
   */
  public static final ElementKey<Void,
      AttendeeStatus> KEY = ElementKey.of(new QName(Namespaces.gNs,
      "attendeeStatus"), Void.class, AttendeeStatus.class);

  /**
   * Value.
   */
  public static final AttributeKey<String> VALUE = AttributeKey.of(new
      QName(null, "value"), String.class);

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    // The builder for this element
    ElementCreator builder = registry.build(KEY);

    // Local properties
    builder.addAttribute(VALUE).setRequired(true);
  }

  /**
   * Constructs an instance using the default key.
   */
  public AttendeeStatus() {
    super(KEY);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected AttendeeStatus(ElementKey<?, ? extends AttendeeStatus> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementKey} as the key
   * for the element. This constructor is used when adapting from one element
   * key to another. You cannot call this constructor directly, instead use
   * {@link Element#createElement(ElementKey, Element)}.
   *
   * @param key The key to use for this element.
   * @param source source element
   */
  protected AttendeeStatus(ElementKey<?, ? extends AttendeeStatus> key,
      Element source) {
    super(key, source);
  }

  @Override
  public AttendeeStatus lock() {
    return (AttendeeStatus) super.lock();
  }

  /**
   * Returns the value.
   *
   * @return value
   */
  public String getValue() {
    return super.getAttributeValue(VALUE);
  }

  /**
   * Sets the value.
   *
   * @param value value or {@code null} to reset
   * @return this to enable chaining setters
   */
  public AttendeeStatus setValue(String value) {
    super.setAttributeValue(VALUE, value);
    return this;
  }

  /**
   * Returns whether it has the value.
   *
   * @return whether it has the value
   */
  public boolean hasValue() {
    return super.hasAttribute(VALUE);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    AttendeeStatus other = (AttendeeStatus) obj;
    return eq(getValue(), other.getValue());
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (getValue() != null) {
      result = 37 * result + getValue().hashCode();
    }
    return result;
  }
}


