package org.camunda.bpm.getstarted.pizza.uimediator;

import java.util.Map;

import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.camunda.bpm.engine.cdi.BusinessProcess;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.getstarted.pizza.OrderStartListener;

@SuppressWarnings("serial")
@Specializes
public class UiMediatedBusinessProcessBean extends BusinessProcess {

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
		uiMediator.checkProcessInstanceStatus((String) variables.get(OrderStartListener.VARNAME_ASSIGNEE),
				processInstance.getProcessInstanceId());

		return processInstance;
	}

}
