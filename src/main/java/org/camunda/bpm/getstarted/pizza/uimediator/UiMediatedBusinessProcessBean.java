package org.camunda.bpm.getstarted.pizza.uimediator;

import java.util.Map;

import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.camunda.bpm.engine.cdi.BusinessProcess;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

@SuppressWarnings("serial")
@Specializes
public class UiMediatedBusinessProcessBean extends BusinessProcess {
	
	//we do not have a logged in user in the demo app
	  private String currentUser = "demo";
	  
	  @Inject
	  private UIMediator uiMediator;
	  
	  @Override
	  @Transactional
	  public void completeTask() {
	    Task task = getTask();
	    super.completeTask();

	    if (task != null) {
	      uiMediator.checkProcessInstanceStatus(task.getAssignee(), task.getProcessInstanceId());
	    }
	  }

	  @Override
	  public ProcessInstance startProcessByKey(String key, Map<String, Object> variables) {
	    ProcessInstance processInstance = super.startProcessByKey(key, variables);
	    uiMediator.checkProcessInstanceStatus(currentUser, processInstance.getProcessInstanceId());
	    
	    return processInstance;
	  }  

}
