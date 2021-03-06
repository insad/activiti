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

package org.activiti.impl.history.event;

import java.util.logging.Logger;

import org.activiti.impl.execution.ExecutionImpl;
import org.activiti.impl.history.HistoryEvent;


/**
 * @author Tom Baeyens
 */
public class EndProcessInstanceHistoryEvent implements HistoryEvent {

  private static final long serialVersionUID = 1L;
  
  private static Logger log = Logger.getLogger(EndProcessInstanceHistoryEvent.class.getName());
  
  public static final EndProcessInstanceHistoryEvent INSTANCE = new EndProcessInstanceHistoryEvent();
  
  public void process(ExecutionImpl execution) {
    log.info("history event end "+execution);
  }

}
