package org.camunda.bpm.getstarted.pizza.uimediator;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;

@RequestScoped
@Named("uiMediator")
public class UIMediator implements Serializable {

	@Inject
	private TaskService taskService;

	@Inject
	private FormService formService;

	private String nextTaskId;

	private String nextTaskForm;

	/**
	 * check for new task in same process for same user
	 */
	public void checkProcessInstanceStatus(String assignee, String processInstanceId) {
		nextTaskId = null;
		nextTaskForm = null;
		if (processInstanceId != null && assignee != null) {
			List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).processInstanceId(processInstanceId)
					.list();
			if (tasks.size() == 1) {
				nextTaskId = tasks.get(0).getId();
				nextTaskForm = formService.getTaskFormData(nextTaskId).getFormKey();
			}
		}
		// possible extension here: wait a couple of (milli)seconds to check if that
		// task is reached asynchronously if you have this requirement
		// Possible addition: wait for a defined time if something pops up.
	    
	    if (nextTaskForm!=null) {
	      System.out.println("Mediator decided to route to task form " + nextTaskForm);      
	    } else {
	      System.out.println("Mediator decided to route back to task list, no user task coming in navigation flow.");      
	    }
	}

	public String getNextTaskId() {
		return nextTaskId;
	}

	public String getNextTaskForm() {
		return nextTaskForm;
	}
}