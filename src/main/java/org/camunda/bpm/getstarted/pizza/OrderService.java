package org.camunda.bpm.getstarted.pizza;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.engine.cdi.BusinessProcess;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.getstarted.pizza.uimediator.TaskCompletionEvent;

@Stateful
@Named
@ConversationScoped
public class OrderService {

	
	public static final String VARNAME_ASSIGNEE  = "assignee";
	
	public static final String VARNAME_ORDER_ID = "orderId";

	public static final String PROCESS_KEY = "orderPizza";

	@PersistenceContext
	private EntityManager entityManager;

	@Inject
	private BusinessProcess businessProcess;

	private OrderEntity orderEntity;

	@Produces
	@Named("orderEntity")
	public OrderEntity orderEntity() {
		if (orderEntity == null) {
			Long orderId = businessProcess.getVariable(VARNAME_ORDER_ID);
			orderEntity = entityManager.find(OrderEntity.class, orderId);
		}
		return orderEntity;
	}

	public void mergeChanges(@Observes TaskCompletionEvent evt) {
		entityManager.merge(orderEntity);
		orderEntity = null;
	}

	@Produces
	@Named("newOrderEntity")
	public OrderEntity getNewOrder() {
		if (orderEntity == null) {
			orderEntity = new OrderEntity();
		}
		return orderEntity;
	}

	public ProcessInstance startOrder() {
		entityManager.persist(orderEntity);
		//entityManager.flush(); - done in task Persist Order prolly not best idea

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(VARNAME_ORDER_ID, orderEntity.getId());
		variables.put(VARNAME_ASSIGNEE, "kermit");
		this.orderEntity = null;

		return businessProcess.startProcessByKey(PROCESS_KEY, variables);
	}

}
