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
package org.activiti.engine.impl;

import java.util.Map;
import java.util.logging.Logger;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.cfg.TransactionContextFactory;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.db.DbSqlSessionFactory;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.jobexecutor.JobExecutor;

/**
 * @author Tom Baeyens
 */
public class ProcessEngineImpl implements ProcessEngine {

  private static Logger log = Logger.getLogger(ProcessEngineImpl.class.getName());

  protected String name;
  protected RepositoryService repositoryService;
  protected RuntimeService runtimeService;
  protected HistoryService historicDataService;
  protected IdentityService identityService;
  protected TaskService taskService;
  protected FormService formService;
  protected ManagementService managementService;
  protected String dbSchemaStrategy;
  protected JobExecutor jobExecutor;
  protected CommandExecutor commandExecutor;
  protected Map<Class<?>, SessionFactory> sessionFactories;
  protected ExpressionManager expressionManager;
  protected int historyLevel;
  protected TransactionContextFactory transactionContextFactory;
  
  // TODO remove or refactor this
  protected ProcessEngineConfigurationImpl processEngineConfiguration;

  public ProcessEngineImpl(ProcessEngineConfigurationImpl processEngineConfiguration) {
    this.processEngineConfiguration = processEngineConfiguration;
    
    this.name = processEngineConfiguration.getProcessEngineName();
    this.repositoryService = processEngineConfiguration.getRepositoryService();
    this.runtimeService = processEngineConfiguration.getRuntimeService();
    this.historicDataService = processEngineConfiguration.getHistoryService();
    this.identityService = processEngineConfiguration.getIdentityService();
    this.taskService = processEngineConfiguration.getTaskService();
    this.formService = processEngineConfiguration.getFormService();
    this.managementService = processEngineConfiguration.getManagementService();
    this.dbSchemaStrategy = processEngineConfiguration.getDbSchemaStrategy();
    this.jobExecutor = processEngineConfiguration.getJobExecutor();
    this.commandExecutor = processEngineConfiguration.getCommandExecutorTxRequired();
    this.sessionFactories = processEngineConfiguration.getSessionFactories();
    this.historyLevel = processEngineConfiguration.getHistoryLevel();
    this.transactionContextFactory = processEngineConfiguration.getTransactionContextFactory();
    
    commandExecutor.execute(new Command<Object>() {
      public Object execute(CommandContext commandContext) {
        performSchemaOperationsCreate();
        return null;
      }
    });

    if (name == null) {
      log.info("default activiti ProcessEngine created");
    } else {
      log.info("ProcessEngine " + name + " created");
    }
    
    ProcessEngines.registerProcessEngine(this);

    if ((jobExecutor != null) && (jobExecutor.isAutoActivate())) {
      jobExecutor.start();
    }
  }

  protected void performSchemaOperationsCreate() {
    if (ProcessEngineConfigurationImpl.DB_SCHEMA_STRATEGY_DROP_CREATE.equals(dbSchemaStrategy)) {
      try {
        getDbSqlSessionFactory().dbSchemaDrop();
      } catch (RuntimeException e) {
        // ignore
      }
    }
    if ( org.activiti.engine.ProcessEngineConfiguration.DB_SCHEMA_STRATEGY_CREATE_DROP.equals(dbSchemaStrategy) 
         || ProcessEngineConfigurationImpl.DB_SCHEMA_STRATEGY_DROP_CREATE.equals(dbSchemaStrategy)
         || ProcessEngineConfigurationImpl.DB_SCHEMA_STRATEGY_CREATE.equals(dbSchemaStrategy)
       ) {
      getDbSqlSessionFactory().dbSchemaCreate();
      
    } else if (org.activiti.engine.ProcessEngineConfiguration.DB_SCHEMA_STRATEGY_CHECK_VERSION.equals(dbSchemaStrategy)) {
      getDbSqlSessionFactory().dbSchemaCheckVersion();
      
    } else if (ProcessEngineConfigurationImpl.DB_SCHEMA_STRATEGY_CREATE_IF_NECESSARY.equals(dbSchemaStrategy)) {
      try {
        getDbSqlSessionFactory().dbSchemaCheckVersion();
      } catch (Exception e) {
        if (e.getMessage().indexOf("no activiti tables in db")!=-1) {
          getDbSqlSessionFactory().dbSchemaCreate();
        }
      }
    }
  }

  public void close() {
    ProcessEngines.unregister(this);
    if ((jobExecutor != null) && (jobExecutor.isActive())) {
      jobExecutor.shutdown();
    }

    commandExecutor.execute(new Command<Object>() {
      public Object execute(CommandContext commandContext) {
        performSchemaOperationsClose();
        return null;
      }
    });
  }

  private void performSchemaOperationsClose() {
    if (org.activiti.engine.ProcessEngineConfiguration.DB_SCHEMA_STRATEGY_CREATE_DROP.equals(dbSchemaStrategy)) {
      getDbSqlSessionFactory().dbSchemaDrop();
    }
  }
  
  public DbSqlSessionFactory getDbSqlSessionFactory() {
    return (DbSqlSessionFactory) sessionFactories.get(DbSqlSession.class);
  }
  
  // getters and setters //////////////////////////////////////////////////////

  public String getName() {
    return name;
  }

  public JobExecutor getJobExecutor() {
    return jobExecutor;
  }

  public IdentityService getIdentityService() {
    return identityService;
  }

  public ManagementService getManagementService() {
    return managementService;
  }

  public TaskService getTaskService() {
    return taskService;
  }

  public HistoryService getHistoryService() {
    return historicDataService;
  }

  public RuntimeService getRuntimeService() {
    return runtimeService;
  }
  
  public String getDbSchemaStrategy() {
    return dbSchemaStrategy;
  }
  
  public RepositoryService getRepositoryService() {
    return repositoryService;
  }
  
  public FormService getFormService() {
    return formService;
  }
  
  public Map<Class< ? >, SessionFactory> getSessionFactories() {
    return sessionFactories;
  }

  public ExpressionManager getExpressionManager() {
    return expressionManager;
  }

  public int getHistoryLevel() {
    return historyLevel;
  }
  
  public TransactionContextFactory getTransactionContextFactory() {
    return transactionContextFactory;
  }

  public ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
    return processEngineConfiguration;
  }
}
