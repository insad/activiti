/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.cycle.impl.connector.fs;

import java.io.File;
import java.io.IOException;

import org.activiti.cycle.RepositoryException;
import org.activiti.cycle.conf.RepositoryConnectorConfiguration;

/**
 * Object used to configure FS connector. Candidate for Entity to save config
 * later on.
 * 
 * @author bernd.ruecker@camunda.com
 */
public class FileSystemConnectorConfiguration extends RepositoryConnectorConfiguration {

  /**
   * default URL
   */
  private File baseFilePath;

  // TODO?
  private String name;

  public FileSystemConnectorConfiguration() {
    basePath = "c:";
    baseFilePath = new File(basePath);
  }

  public FileSystemConnectorConfiguration(String basePath) {
    setBasePath(basePath);
  }

  public String getBasePath() {
    try {
      return baseFilePath.getCanonicalPath();
    } catch (IOException ioe) {
      throw new RepositoryException("Unable to get canonical representation of basePath!", ioe);
    }
  }

  public void setBasePath(String basePath) {
    if (basePath != null && !basePath.endsWith("/")) {
      basePath = basePath + "/";
    }
    this.baseFilePath = new File(basePath);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
