/*
 * Copyright 2016 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package px.internal.op

import px.Pollable

class Filter<T>(val upstream: Pollable<T>, val func: (T) -> Boolean) : Pollable<T>() {
  override fun start() = Operator(upstream.start(), func)

  class Operator<T>(val upstream: Machine<T>, val func: (T) -> Boolean) : Machine<T> by upstream {
    override fun poll(): T? = upstream.poll()?.let { return if (func.invoke(it)) it else null }
  }
}
