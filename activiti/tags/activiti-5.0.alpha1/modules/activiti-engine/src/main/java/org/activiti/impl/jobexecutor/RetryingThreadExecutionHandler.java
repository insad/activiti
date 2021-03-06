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
package org.activiti.impl.jobexecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * An implementation of a {@link RejectedExecutionHandler}, which
 *  attempts to retry adding the item to the queue.
 */
public class RetryingThreadExecutionHandler implements RejectedExecutionHandler {
  private JobExecutor jobExecutor;
  
  public RetryingThreadExecutionHandler(JobExecutor jobExecutor) {
    this.jobExecutor = jobExecutor;
  }
  
  public void rejectedExecution(Runnable job, ThreadPoolExecutor threadPool) {
    if(jobExecutor.isActive()) {
      threadPool.execute(job);
    } else {
      jobExecutor.giveBack(job);
    }
  }
}
