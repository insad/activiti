/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.engine;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;

/** information about ongoing and past process instances.  This is different
 * from the runtime information in the sense that this runtime information only contains 
 * the actual runtime state at any given moment and it is optimized for runtime 
 * process execution performance.  The history information is optimized for easy 
 * querying and remains permanent in the persistent storage.
 * 
 * @author Christian Stettler
 * @author Tom Baeyens
 */
public interface HistoryService {

  /** the {@link HistoricProcessInstance} or null if the given process instance 
   * does not exists. */
  HistoricProcessInstance findHistoricProcessInstanceById(String processInstanceId);

  /** creates a new programmatic query to search for {@link HistoricProcessInstance}s. */
  HistoricProcessInstanceQuery createHistoricProcessInstanceQuery();
}
