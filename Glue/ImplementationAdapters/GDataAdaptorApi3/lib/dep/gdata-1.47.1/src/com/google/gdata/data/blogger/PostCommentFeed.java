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


package com.google.gdata.data.blogger;

import com.google.gdata.data.BaseFeed;

/**
 * Describes a feed of a blog post's comments.
 *
 * 
 */
public class PostCommentFeed extends BaseFeed<PostCommentFeed, CommentEntry> {

  /**
   * Default mutable constructor.
   */
  public PostCommentFeed() {
    super(CommentEntry.class);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseFeed} instance.
   *
   * @param sourceFeed source feed
   */
  public PostCommentFeed(BaseFeed<?, ?> sourceFeed) {
    super(CommentEntry.class, sourceFeed);
  }

  @Override
  public String toString() {
    return "{PostCommentFeed " + super.toString() + "}";
  }

}
