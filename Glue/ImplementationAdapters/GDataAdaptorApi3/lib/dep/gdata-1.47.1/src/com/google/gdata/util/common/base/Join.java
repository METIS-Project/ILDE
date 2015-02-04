/*
 * Copyright (C) 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gdata.util.common.base;

import static com.google.gdata.util.common.base.Preconditions.checkNotNull;

import com.google.common.annotations.GwtCompatible;

import java.io.IOException;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Utility for joining pieces of text separated by a delimiter. It can handle
 * iterators, collections, arrays, and varargs, and can append to any
 * {@link Appendable} or just return a {@link String}. For example,
 * {@code join(":", "a", "b", "c")} returns {@code "a:b:c"}.
 *
 * <p>All methods of this class throw {@link NullPointerException} when a value
 * of {@code null} is supplied for any parameter. The elements within the
 * collection, iterator, array, or varargs parameter list <i>may</i> be null --
 * these will be represented in the output by the string {@code "null"}.
 *
 * 
 */
@GwtCompatible
public final class Join {
  private Join() {}

  /**
   * Returns a string containing the {@code tokens}, converted to strings if
   * necessary, separated by {@code delimiter}. If {@code tokens} is empty, it
   * returns an empty string.
   *
   * <p>Each token will be converted to a {@link CharSequence} using
   * {@link String#valueOf(Object)}, if it isn't a {@link CharSequence} already.
   * Note that this implies that null tokens will be appended as the
   * four-character string {@code "null"}.
   *
   * @param delimiter a string to append between every element, but not at the
   *     beginning or end
   * @param tokens objects to append
   * @return a string consisting of the joined elements
   */
  public static String join(String delimiter, Iterable<?> tokens) {
    return Joiner.on(delimiter).useForNull("null").join(tokens);
  }

  /**
   * Returns a string containing the {@code tokens}, converted to strings if
   * necessary, separated by {@code delimiter}. If {@code tokens} is empty, it
   * returns an empty string.
   *
   * <p>Each token will be converted to a {@link CharSequence} using
   * {@link String#valueOf(Object)}, if it isn't a {@link CharSequence} already.
   * Note that this implies that null tokens will be appended as the
   * four-character string {@code "null"}.
   *
   * @param delimiter a string to append between every element, but not at the
   *     beginning or end
   * @param tokens objects to append
   * @return a string consisting of the joined elements
   */
  public static String join(String delimiter, Object[] tokens) {
    return Joiner.on(delimiter).useForNull("null").join(tokens);
  }

  /**
   * Returns a string containing the {@code tokens}, converted to strings if
   * necessary, separated by {@code delimiter}.
   *
   * <p>Each token will be converted to a {@link CharSequence} using
   * {@link String#valueOf(Object)}, if it isn't a {@link CharSequence} already.
   * Note that this implies that null tokens will be appended as the
   * four-character string {@code "null"}.
   *
   * @param delimiter a string to append between every element, but not at the
   *     beginning or end
   * @param firstToken the first object to append
   * @param otherTokens subsequent objects to append
   * @return a string consisting of the joined elements
   */
  public static String join(
      String delimiter, Object firstToken, Object... otherTokens) {
    return Joiner.on(delimiter).useForNull("null").join(
        iterable(firstToken, otherTokens));
  }

  /**
   * Returns a string containing the {@code tokens}, converted to strings if
   * necessary, separated by {@code delimiter}. If {@code tokens} is empty, it
   * returns an empty string.
   *
   * <p>Each token will be converted to a {@link CharSequence} using
   * {@link String#valueOf(Object)}, if it isn't a {@link CharSequence} already.
   * Note that this implies that null tokens will be appended as the
   * four-character string {@code "null"}.
   *
   * @param delimiter a string to append between every element, but not at the
   *     beginning or end
   * @param tokens objects to append
   * @return a string consisting of the joined elements
   */
  public static String join(String delimiter, Iterator<?> tokens) {
    StringBuilder sb = new StringBuilder();
    join(sb, delimiter, tokens);
    return sb.toString();
  }

  /**
   * Returns a string containing the contents of {@code map}, with entries
   * separated by {@code entryDelimiter}, and keys and values separated with
   * {@code keyValueSeparator}.
   *
   * <p>Each key and value will be converted to a {@link CharSequence} using
   * {@link String#valueOf(Object)}, if it isn't a {@link CharSequence} already.
   * Note that this implies that null tokens will be appended as the
   * four-character string {@code "null"}.
   *
   * @param keyValueSeparator a string to append between every key and its
   *     associated value
   * @param entryDelimiter a string to append between every entry, but not at
   *     the beginning or end
   * @param map the map containing the data to join
   * @return a string consisting of the joined entries of the map; empty if the
   *     map is empty
   */
  public static String join(
      String keyValueSeparator, String entryDelimiter, Map<?, ?> map) {
    return Joiner
        .on(entryDelimiter)
        .useForNull("null")
        .withKeyValueSeparator(keyValueSeparator)
        .join(map);
  }

  /**
   * Appends each of the {@code tokens} to {@code appendable}, separated by
   * {@code delimiter}.
   *
   * <p>Each token will be converted to a {@link CharSequence} using
   * {@link String#valueOf(Object)}, if it isn't a {@link CharSequence} already.
   * Note that this implies that null tokens will be appended as the
   * four-character string {@code "null"}.
   *
   * @param appendable the object to append the results to
   * @param delimiter a string to append between every element, but not at the
   *     beginning or end
   * @param tokens objects to append
   * @return the same {@code Appendable} instance that was passed in
   * @throws JoinException if an {@link IOException} occurs
   */
  public static <T extends Appendable> T join(
      T appendable, String delimiter, Iterable<?> tokens) {
    try {
      return Joiner.on(delimiter).useForNull("null")
          .appendTo(appendable, tokens);
    } catch (IOException e) {
      throw new JoinException(e);
    }
  }

  /**
   * Appends each of the {@code tokens} to {@code appendable}, separated by
   * {@code delimiter}.
   *
   * <p>Each token will be converted to a {@link CharSequence} using
   * {@link String#valueOf(Object)}, if it isn't a {@link CharSequence} already.
   * Note that this implies that null tokens will be appended as the
   * four-character string {@code "null"}.
   *
   * @param appendable the object to append the results to
   * @param delimiter a string to append between every element, but not at the
   *     beginning or end
   * @param tokens objects to append
   * @return the same {@code Appendable} instance that was passed in
   * @throws JoinException if an {@link IOException} occurs
   */
  public static <T extends Appendable> T join(
      T appendable, String delimiter, Object[] tokens) {
    return join(appendable, delimiter, Arrays.asList(tokens));
  }

  /**
   * Appends each of the {@code tokens} to {@code appendable}, separated by
   * {@code delimiter}.
   *
   * <p>Each token will be converted to a {@link CharSequence} using
   * {@link String#valueOf(Object)}, if it isn't a {@link CharSequence} already.
   * Note that this implies that null tokens will be appended as the
   * four-character string {@code "null"}.
   *
   * @param appendable the object to append the results to
   * @param delimiter a string to append between every element, but not at the
   *     beginning or end
   * @param firstToken the first object to append
   * @param otherTokens subsequent objects to append
   * @return the same {@code Appendable} instance that was passed in
   * @throws JoinException if an {@link IOException} occurs
   */
  public static <T extends Appendable> T join(T appendable, String delimiter,
      Object firstToken, Object... otherTokens) {
    return join(appendable, delimiter, iterable(firstToken, otherTokens));
  }

  /**
   * Appends each of the {@code tokens} to {@code appendable}, separated by
   * {@code delimiter}.
   *
   * <p>Each token will be converted to a {@link CharSequence} using
   * {@link String#valueOf(Object)}, if it isn't a {@link CharSequence} already.
   * Note that this implies that null tokens will be appended as the
   * four-character string {@code "null"}.
   *
   * @param appendable the object to append the results to
   * @param delimiter a string to append between every element, but not at the
   *     beginning or end
   * @param tokens objects to append
   * @return the same {@code Appendable} instance that was passed in
   * @throws JoinException if an {@link IOException} occurs
   */
  public static <T extends Appendable> T join(
      T appendable, String delimiter, Iterator<?> tokens) {
    checkNotNull(appendable);
    checkNotNull(delimiter);
    if (tokens.hasNext()) {
      try {
        appendOneToken(appendable, tokens.next());
        while (tokens.hasNext()) {
          appendable.append(delimiter);
          appendOneToken(appendable, tokens.next());
        }
      } catch (IOException e) {
        throw new JoinException(e);
      }
    }
    return appendable;
  }

  /**
   * Appends the contents of {@code map} to {@code appendable}, with entries
   * separated by {@code entryDelimiter}, and keys and values separated with
   * {@code keyValueSeparator}.
   *
   * <p>Each key and value will be converted to a {@link CharSequence} using
   * {@link String#valueOf(Object)}, if it isn't a {@link CharSequence} already.
   * Note that this implies that null tokens will be appended as the
   * four-character string {@code "null"}.
   *
   * @param appendable the object to append the results to
   * @param keyValueSeparator a string to append between every key and its
   *     associated value
   * @param entryDelimiter a string to append between every entry, but not at
   *     the beginning or end
   * @param map the map containing the data to join
   * @return the same {@code Appendable} instance that was passed in
   */
  public static <T extends Appendable> T join(T appendable,
      String keyValueSeparator, String entryDelimiter, Map<?, ?> map) {
    try {
      return Joiner.on(entryDelimiter).useForNull("null")
          .withKeyValueSeparator(keyValueSeparator).appendTo(appendable, map);
    } catch (IOException e) {
      throw new JoinException(e);
    }
  }

  private static void appendOneToken(Appendable appendable, Object token)
      throws IOException {
    appendable.append(toCharSequence(token));
  }

  private static CharSequence toCharSequence(Object token) {
    return (token instanceof CharSequence)
        ? (CharSequence) token
        : String.valueOf(token);
  }

  /**
   * Exception thrown in response to an {@link IOException} from the supplied
   * {@link Appendable}. This is used because most callers won't want to
   * worry about catching an IOException.
   */
  public static class JoinException extends RuntimeException {
    private JoinException(IOException cause) {
      super(cause);
    }

    private static final long serialVersionUID = 1L;
  }

  /**
   * Duplicate of
   * {@link com.google.common.collect.Lists#asList(Object, Object[])}, copied
   * here to remove dependencies.
   */
  private static Iterable<Object> iterable(
      final Object first, final Object[] rest) {
    checkNotNull(rest);
    return new AbstractList<Object>() {
      @Override public int size() {
        return rest.length + 1;
      }
      @Override public Object get(int index) {
        return (index == 0) ? first : rest[index - 1];
      }
    };
  }
}
