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

package org.activiti.engine.test.bpmn.sequenceflow;

import org.activiti.engine.ProcessInstance;
import org.activiti.engine.Task;
import org.activiti.engine.impl.util.CollectionUtil;
import org.activiti.engine.test.Deployment;
import org.activiti.engine.test.ProcessEngineTestCase;


/**
 * @author Joram Barrez
 */
public class ConditionalSequenceFlowTest extends ProcessEngineTestCase {
  
  @Deployment
  public void testUelValueExpression() {
    ProcessInstance pi = runtimeService.startProcessInstanceByKey(
            "condSeqFlowUelValueExpr", CollectionUtil.singletonMap("input", "right"));
    Task task = taskService.createTaskQuery().processInstance(pi.getId()).singleResult();
    assertEquals("task right", task.getName());
  }
  

}
