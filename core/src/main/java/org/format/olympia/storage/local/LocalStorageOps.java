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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.format.olympia.storage.AtomicOutputStream;
import org.format.olympia.storage.CommonStorageOpsProperties;
import org.format.olympia.storage.LiteralURI;
import org.format.olympia.storage.SeekableInputStream;
import org.format.olympia.storage.StorageOps;
import org.format.olympia.storage.StorageOpsProperties;
import org.format.olympia.util.ValidationUtil;

public class LocalStorageOps implements StorageOps {

  private CommonStorageOpsProperties commonProperties;
  private LocalStorageOpsProperties localProperties;

  public LocalStorageOps() {
    this(CommonStorageOpsProperties.instance(), LocalStorageOpsProperties.instance());
  }

  public LocalStorageOps(
      CommonStorageOpsProperties commonProperties, LocalStorageOpsProperties localProperties) {
    this.commonProperties = commonProperties;
    this.localProperties = localProperties;
  }

  @Override
  public StorageOpsProperties commonProperties() {
    return commonProperties;
  }

  @Override
  public StorageOpsProperties systemSpecificProperties() {
    return localProperties;
  }

  @Override
  public void prepareToReadLocal(LiteralURI uri) {}

  @Override
  public SeekableInputStream startRead(LiteralURI uri) {
    return startReadLocal(uri);
  }

  @Override
  public LocalInputStream startReadLocal(LiteralURI uri) {
    return new LocalInputStream(new File(fileSystemPath(uri)));
  }

  @Override
  public AtomicOutputStream startCommit(LiteralURI uri) {
    return new LocalAtomicStagingOutputStream(
        Paths.get(fileSystemPath(uri)), commonProperties, localProperties);
  }

  @Override
  public OutputStream startOverwrite(LiteralURI uri) {
    return new LocalOverwriteOutputStream(
        Paths.get(fileSystemPath(uri)), commonProperties, localProperties);
  }

  @Override
  public boolean exists(LiteralURI uri) {
    return new File(fileSystemPath(uri)).exists();
  }

  @Override
  public void delete(List<LiteralURI> uris) {
    for (LiteralURI uri : uris) {
      File file = new File(fileSystemPath(uri));
      file.delete();
    }
  }

  @Override
  public List<LiteralURI> list(LiteralURI prefix) {
    Path startingPath = Paths.get(fileSystemPath(prefix));
    String[] result = startingPath.toFile().list();
    ValidationUtil.checkNotNull(result, "Invalid prefix for listing: %s", prefix);
    return Arrays.stream(result)
        .map(name -> "file://" + startingPath.resolve(name))
        .map(LiteralURI::new)
        .collect(Collectors.toList());
  }

  @Override
  public void close() throws IOException {}

  @Override
  public void initialize(Map<String, String> properties) {
    this.commonProperties = new CommonStorageOpsProperties(properties);
    this.localProperties = new LocalStorageOpsProperties(properties);
  }

  private static String fileSystemPath(LiteralURI uri) {
    return "/" + uri.path();
  }
}
