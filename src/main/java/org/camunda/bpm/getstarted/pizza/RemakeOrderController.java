package org.camunda.bpm.getstarted.pizza;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.engine.cdi.BusinessProcess;

@Named
@RequestScoped
public class RemakeOrderController implements Serializable {
	private static final long serialVersionUID = -1982810046204165898L;

	private final static Logger LOGGER = Logger.getLogger("PIZZA-ORDERS");

	// the BusinessProcess to access the process variables
	@Inject
	private BusinessProcess businessProcess;

	// Inject the EntityManager to access the persisted order
	@PersistenceContext
	private EntityManager entityManager;

	// Inject the OrderBusinessLogic to update the persisted order
	@Inject
	private OrderBusinessLogic orderBusinessLogic;

	// Caches the OrderEntity during the request -jsf
	private OrderEntity orderEntity;

	private OrderEntity initialOrderEntity;

	public OrderEntity getOrderEntity() {
		if (orderEntity == null) {
			// Load the order entity from the database if not already cached
			orderEntity = orderBusinessLogic.getOrder((Long) businessProcess.getVariable("orderId"));
		}
		initialOrderEntity = orderEntity;
		return orderEntity;
	}

	public void submitForm() throws IOException {
		// Persist updated order entity and complete task form
		LOGGER.log(Level.INFO, "\n\n\nOrder {0} for {1} pizza was changed to {2} pizza.\n\n\n",
				new String[] { String.valueOf(orderEntity.getId()), initialOrderEntity.pizza, orderEntity.pizza });
		orderBusinessLogic.mergeOrderAndCompleteTask(orderEntity);
	}

}
