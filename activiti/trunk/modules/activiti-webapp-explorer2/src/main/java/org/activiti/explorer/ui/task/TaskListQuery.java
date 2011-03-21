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
package org.activiti.explorer.ui.task;

import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.explorer.data.AbstractLazyLoadingQuery;
import org.activiti.explorer.data.LazyLoadingContainer;

import com.vaadin.data.Item;
import com.vaadin.ui.Table;


/**
 * @author Joram Barrez
 */
public class TaskListQuery extends AbstractLazyLoadingQuery<Task> {
  
  protected TaskService taskService;
  protected LazyLoadingContainer lazyLoadingContainer;
  
  public TaskListQuery(TaskService taskService, Table taskTable) {
    this.taskService = taskService;
  }

  public int size() {
    return (int) taskService.createTaskQuery().count();
  }
  
  public List<Task> loadBeans(int start, int count) {
   return createBaseQuery().listPage(start, count);
  }
  
  protected Task loadBean(String id) {
    return createBaseQuery().taskId(id).singleResult();
  }
  
  protected TaskQuery createBaseQuery() {
    return taskService.createTaskQuery().orderByTaskId().asc();
  }
  
  public int compareTo(Item searched, Item other) {
    String searchedTaskId = (String) searched.getItemProperty("id").getValue();
    String otherTaskId = (String) other.getItemProperty("id").getValue();
    return searchedTaskId.compareTo(otherTaskId);
  }
  
  public void setSorting(Object[] propertyId, boolean[] ascending) {
    throw new UnsupportedOperationException();
  }
  
}
