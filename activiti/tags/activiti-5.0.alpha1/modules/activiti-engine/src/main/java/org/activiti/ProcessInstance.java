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
package org.activiti;

import java.util.List;


/** represents one execution of a  {@link ProcessDefinition}.
 * 
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public interface ProcessInstance {
  
  /**
   * the unique identifier of the process instance.
   */
  String getId();
  
  /**
   * the id of the process definition of the process instance.
   */
  String getProcessDefinitionId();
  
  /**
   * the names of the activities that currently are active.
   */
  List<String> getActivityNames();
  
  /**
   * indicates if the process instance is ended.
   * @return
   */
  boolean isEnded();
  
}
