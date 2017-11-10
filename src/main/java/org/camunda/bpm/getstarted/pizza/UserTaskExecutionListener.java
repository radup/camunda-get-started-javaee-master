package org.camunda.bpm.getstarted.pizza;

import javax.inject.Inject;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.runtime.ProcessInstance;


public class UserTaskExecutionListener implements TaskListener{


	@Override
	public void notify(DelegateTask delegateTask) {
		
		delegateTask.setAssignee((String)delegateTask.getVariable(OrderStartListener.VARNAME_ASSIGNEE));
		
		
	}
	


}
