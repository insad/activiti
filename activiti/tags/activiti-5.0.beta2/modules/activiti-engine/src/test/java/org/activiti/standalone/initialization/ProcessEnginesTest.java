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
package org.activiti.standalone.initialization;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineInfo;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.test.ActivitiInternalTestCase;
import org.activiti.pvm.test.PvmTestCase;

/**
 * @author Tom Baeyens
 */
public class ProcessEnginesTest extends PvmTestCase {
  
  protected void setUp() throws Exception {
    super.setUp();
    ActivitiInternalTestCase.closeProcessEngine();
    ProcessEngines.init();
  }
  
  protected void tearDown() throws Exception {
    ProcessEngines.destroy();
    super.tearDown();
  }

  public void testProcessEngineInfo() {

    List<ProcessEngineInfo> processEngineInfos = ProcessEngines.getProcessEngineInfos();
    assertEquals(1, processEngineInfos.size());

    ProcessEngineInfo processEngineInfo = processEngineInfos.get(0);
    assertNull(processEngineInfo.getException());
    assertNotNull(processEngineInfo.getName());
    assertNotNull(processEngineInfo.getResourceUrl());

    ProcessEngine processEngine = ProcessEngines.getProcessEngine(ProcessEngines.NAME_DEFAULT);
    assertNotNull(processEngine);
  }

}
