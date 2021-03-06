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

package org.activiti.engine.impl.test;

import java.util.logging.Logger;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineBuilder;


/**
 * @author Tom Baeyens
 */
public class DefaultProcessEngineInitializer implements ProcessEngineInitializer {
  
  private static Logger log = Logger.getLogger(DefaultProcessEngineInitializer.class.getName());

  public ProcessEngine getProcessEngine() {
    log.fine("==== BUILDING PROCESS ENGINE ========================================================================");
    ProcessEngine processEngine = new ProcessEngineBuilder()
      .configureFromPropertiesResource("activiti.properties")
      .buildProcessEngine();
    log.fine("==== PROCESS ENGINE CREATED =========================================================================");
    return processEngine;
  }

}
