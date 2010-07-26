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

package org.activiti.test.pvm.activities;

import org.activiti.pvm.activity.ActivityContext;
import org.activiti.pvm.activity.SignallableActivityBehaviour;
import org.activiti.pvm.activity.Transition;


/**
 * @author Tom Baeyens
 */
public class WaitState implements SignallableActivityBehaviour {

  @Override
  public void start(ActivityContext activityExecutionContext) {
  }
  
  @Override
  public void signal(ActivityContext activityExecutionContext, String signal, Object data) {
    Transition transition = activityExecutionContext.getOutgoingTransitions().get(0);
    activityExecutionContext.take(transition);
  }
}
