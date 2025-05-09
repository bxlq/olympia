/*
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
package org.format.olympia.storage.local;

import java.util.Map;
import java.util.Set;
import org.format.olympia.relocated.com.google.common.collect.ImmutableMap;
import org.format.olympia.relocated.com.google.common.collect.ImmutableSet;
import org.format.olympia.storage.StorageOpsProperties;

public class LocalStorageOpsProperties implements StorageOpsProperties {

  private static final LocalStorageOpsProperties INSTANCE = new LocalStorageOpsProperties();

  public static final Set<String> PROPERTIES = ImmutableSet.<String>builder().build();

  private final Map<String, String> propertiesMap;

  public LocalStorageOpsProperties() {
    this(ImmutableMap.of());
  }

  public LocalStorageOpsProperties(Map<String, String> input) {
    this.propertiesMap = ImmutableMap.copyOf(input);
  }

  public static LocalStorageOpsProperties instance() {
    return INSTANCE;
  }

  @Override
  public Map<String, String> asStringMap() {
    return propertiesMap;
  }
}
