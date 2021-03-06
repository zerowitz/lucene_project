package org.apache.lucene.search;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * A cache for queries.
 *
 * @see LRUQueryCache
 * @lucene.experimental
 */
public interface QueryCache {

  /**
   * Return a key for the given query that only takes matching documents into
   * account. Boosts will be ignored.
   * @lucene.internal
   */
  public static Query cacheKey(Query query) {
    if (query.getBoost() == 1f) {
      return query;
    } else {
      Query key = query.clone();
      key.setBoost(1f);
      assert key == cacheKey(key);
      return key;
    }
  }

  /**
   * Return a wrapper around the provided <code>weight</code> that will cache
   * matching docs per-segment accordingly to the given <code>policy</code>.
   * NOTE: The returned weight will only be equivalent if scores are not needed.
   * @see Collector#needsScores()
   */
  Weight doCache(Weight weight, QueryCachingPolicy policy);

}
