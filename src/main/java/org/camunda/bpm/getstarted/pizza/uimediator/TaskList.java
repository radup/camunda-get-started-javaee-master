package org.camunda.bpm.getstarted.pizza.uimediator;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.task.Task;

@SessionScoped
@Named
public class TaskList implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TaskService taskService;

	@Inject
	private FormService formService;

	public void update() {
		// do nothing here, since a refreh trigger a reload of the list anyway
	}

	public List<Task> getList() {
		
//		taskService.get
		
		return taskService.createTaskQuery().taskAssignee("kermit").list();
	}

	public String getFormKey(Task task) {
		TaskFormData taskFormData = formService.getTaskFormData(task.getId());
		
		
		if (taskFormData != null) {
			return taskFormData.getFormKey().replaceAll("app:", "").replaceAll(".jsf", "");
		} else {
			// we do not want to fail just because we have tasks in the task list without
			// form data (typically manually created tasks)
			return null;
		}
	}
	
	
}
