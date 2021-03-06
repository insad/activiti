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
package org.activiti.engine.impl.jobexecutor;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.entity.TimerEntity;
import org.activiti.engine.impl.util.ClockUtil;

/**
 * 
 * @author Daniel Meyer
 */
public class AcquireJobsRunnable implements Runnable {

  private static Logger log = Logger.getLogger(AcquireJobsRunnable.class.getName());

  protected final JobExecutor jobExecutor;

  protected volatile boolean isInterrupted = false;
  protected volatile boolean isJobAdded = false;
  protected final Object MONITOR = new Object();
  protected final AtomicBoolean isWaiting = new AtomicBoolean(false);

  public AcquireJobsRunnable(JobExecutor jobExecutor) {
    this.jobExecutor = jobExecutor;
  }

  public synchronized void run() {
    log.info(jobExecutor.getName() + " starting to acquire jobs");

    final CommandExecutor commandExecutor = jobExecutor.getCommandExecutor();
    long millisToWait = 0;
    float waitIncreaseFactor = 2;
    long maxWait = 60 * 1000;

    while (!isInterrupted) {
      int maxJobsPerAcquisition = jobExecutor.getMaxJobsPerAcquisition();

      try {
        AcquiredJobs acquiredJobs = commandExecutor.execute(jobExecutor.getAcquireJobsCmd());

        for (List<String> jobIds : acquiredJobs.getJobIdBatches()) {
          jobExecutor.executeJobs(jobIds);
        }

        // if all jobs were executed
        millisToWait = jobExecutor.getWaitTimeInMillis();
        int jobsAcquired = acquiredJobs.getJobIdBatches().size();
        if (jobsAcquired < maxJobsPerAcquisition) {
          
          isJobAdded = false;
          
          // check if the next timer should fire before the normal sleep time is over
          Date duedate = new Date(ClockUtil.getCurrentTime().getTime() + millisToWait);
          List<TimerEntity> nextTimers = commandExecutor.execute(new GetUnlockedTimersByDuedateCmd(duedate, new Page(0, 1)));
          
          if (!nextTimers.isEmpty()) {
          long millisTillNextTimer = nextTimers.get(0).getDuedate().getTime() - ClockUtil.getCurrentTime().getTime();
            if (millisTillNextTimer < millisToWait) {
              millisToWait = millisTillNextTimer;
            }
          }
          
        } else {
          millisToWait = 0;
        }

      } catch (Exception e) {
        log.log(Level.SEVERE, "exception during job acquisition: " + e.getMessage(), e);
        millisToWait *= waitIncreaseFactor;
        if (millisToWait > maxWait) {
          millisToWait = maxWait;
        }
      }

      if ((millisToWait > 0) && (!isJobAdded) && (!isInterrupted)) {
        try {
          log.fine("job acquisition thread sleeping for " + millisToWait + " millis");
          synchronized (MONITOR) {
            isWaiting.set(true);
            MONITOR.wait(millisToWait);            
          }
          log.fine("job acquisition thread woke up");
        } catch (InterruptedException e) {
          log.fine("job acquisition wait interrupted");
        } finally {
          isWaiting.set(false);
        }
      }
    }
    log.info(jobExecutor.getName() + " stopped job acquisition");
  }

  public void stop() {
    isInterrupted = true; 
    synchronized (MONITOR) {
      MONITOR.notifyAll();
    }
  }

  public void jobWasAdded() {    
    isJobAdded = true;
    if(isWaiting.compareAndSet(true, false)) {
      synchronized (MONITOR) {
        MONITOR.notifyAll();
      }
    }    
  }

}
