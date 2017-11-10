package org.camunda.bpm.getstarted.pizza;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

public class AssigneeTaskListener implements TaskListener {

	@Override
	public void notify(DelegateTask delegateTask) {
		
		// TODO: Execute custom identity lookups here if needed
		
		String assignee = (String) delegateTask.getExecution().getProcessInstance().getVariable(OrderService.VARNAME_ASSIGNEE);
	    delegateTask.setAssignee(assignee);
		
	}

}
