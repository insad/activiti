package org.activiti.cycle.impl.components;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.activiti.cycle.annotations.CycleComponent;
import org.activiti.cycle.context.CycleContextType;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

/**
 * {@link CycleComponent} for asynchronous email-dispatching (extremely basic
 * implementation)
 * 
 * @author daniel.meyer@camunda.com
 */
@CycleComponent(context = CycleContextType.APPLICATION)
public class CycleEmailDispatcher {

  private static int QUEUE_SIZE = 100;

  Logger logger = Logger.getLogger(CycleEmailDispatcher.class.getName());

  private final BlockingQueue<Email> emailQueue = new ArrayBlockingQueue<Email>(QUEUE_SIZE);

  public void sendEmail(Email email) {
    // if the queue is full, the email is simply not added.
    emailQueue.offer(email);
    if (!emailDispatcherThread.isAlive()) {
      emailDispatcherThread.start();
    }
  }

  public void startDispatchment() {
    emailDispatcherThread.start();
  }

  public void stopDispatchment() {
    emailDispatcherThread.interrupt();
  }

  private final Thread emailDispatcherThread = new Thread("Cycle email dispatcher thread") {

    public void run() {
      while (true && !isInterrupted()) {
        try {
          Email mail = emailQueue.take();
          mail.sendMimeMessage();
        } catch (EmailException e) {
          logger.log(Level.SEVERE, "Could not send email", e);
          // TODO: retry?
        } catch (InterruptedException e) {
          // just terminate
          return;
        }
      }
    }
  };

}