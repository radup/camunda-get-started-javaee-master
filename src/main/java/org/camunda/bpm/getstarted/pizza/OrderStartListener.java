package org.camunda.bpm.getstarted.pizza;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

public class OrderStartListener implements ExecutionListener{

	public static final String VARNAME_ASSIGNEE  = "customer";
	
	
	@Override
	public void notify(DelegateExecution delegateExecution) throws Exception {
		
		//TODO: identify user somehow based on HTTPSession 
		
		delegateExecution.getProcessInstance().setVariable(VARNAME_ASSIGNEE, "kermit");
	}

}
