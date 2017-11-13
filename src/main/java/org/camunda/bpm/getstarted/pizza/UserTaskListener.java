package org.camunda.bpm.getstarted.pizza;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

public class UserTaskListener implements TaskListener {

	public void notify(DelegateTask delegateTask) {
	    delegateTask.setAssignee((String)delegateTask.getVariable(OrderService.VARNAME_ASSIGNEE));
		
	}

	

}
