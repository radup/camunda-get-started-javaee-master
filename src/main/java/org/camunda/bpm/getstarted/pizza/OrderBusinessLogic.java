/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camunda.bpm.getstarted.pizza;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.cdi.BusinessProcess;
import org.camunda.bpm.engine.cdi.jsf.TaskForm;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.impl.context.Context;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@Named
public class OrderBusinessLogic {

	// Inject the entity manager
	@PersistenceContext
	private EntityManager entityManager;

	@Inject
	private BusinessProcess businessProcess;

	// Inject task form available through the camunda cdi artifact
	@Inject
	private TaskForm taskForm;

	private static Logger LOGGER = Logger.getLogger(OrderBusinessLogic.class.getName());

	public void persistOrder(DelegateExecution delegateExecution) {
		// Create new order instance
		// OrderEntity orderEntity = new OrderEntity();

		// Get all process variables
		Map<String, Object> variables = delegateExecution.getVariables();
		OrderEntity orderEntity = getOrder((Long) variables.get("orderId"));

		// Set order attributes
		// orderEntity.setCustomer((String) variables.get("customer"));
		// orderEntity.setAddress((String) variables.get("address"));
		// orderEntity.setPizza((String) variables.get("pizza"));

		// Persist order instance and flush. After the flush the
		// id of the order instance is set.
		// entityManager.persist(orderEntity);

		entityManager.flush();

		String assignee = (String) delegateExecution.getVariable(OrderService.VARNAME_ASSIGNEE);

		// Remove no longer needed process variables
		delegateExecution.removeVariables(variables.keySet());

		// Add newly created order id as process variable
		delegateExecution.setVariable("orderId", orderEntity.getId());
		delegateExecution.setVariable(OrderService.VARNAME_ASSIGNEE, assignee);

		LOGGER.log(Level.INFO, "\n\n\nOrder {0} for {1} pizza was sent for approval to user: {2}.\n\n\n",
				new String[] { String.valueOf(orderEntity.getId()), orderEntity.pizza, assignee });

	}

	public OrderEntity getOrder(Long orderId) {
		// Load order entity from database
		return entityManager.find(OrderEntity.class, orderId);
	}

	/*
	 * Merge updated order entity and complete task form in one transaction. This
	 * ensures that both changes will rollback if an error occurs during
	 * transaction.
	 */
	public void mergeOrderAndCompleteTask(OrderEntity orderEntity) {
		// Merge detached order entity with current persisted state
		entityManager.merge(orderEntity);
		entityManager.flush(); //TODO: needed?
		businessProcess.completeTask();
	}

	public void rejectOrder(DelegateExecution delegateExecution) {
		OrderEntity order = getOrder((Long) delegateExecution.getVariable("orderId"));
		LOGGER.log(Level.INFO, "\n\n\nSending Email:\nDear {0}, your order {1} of a {2} pizza has been rejected.\n\n\n",
				new String[] { order.getCustomer(), String.valueOf(order.getId()), order.getPizza() });
	}


	
	public boolean ingredientsAvailable(DelegateExecution delegateExecution) throws ProcessEngineException {
		int randomNum = ThreadLocalRandom.current().nextInt(1, 20);
		if (randomNum % 2 == 0 ) {
			return true;
		}
		return false;
	}
	
	//TODO: how to work with Error Events?!!
	
	public void checkMainIngredient(DelegateExecution delegateExecution) throws ProcessEngineException {
		int randomNum = ThreadLocalRandom.current().nextInt(1, 20);
		if (randomNum % 5 == 0 ) {
			//throw new BpmnError("INGREDIENT-UNAVAILABLE");
			throw new IngredientNotAvailableException("main");
		}
	}

	public void checkSpecialityIngredient(DelegateExecution delegateExecution) throws ProcessEngineException{
		int randomNum = ThreadLocalRandom.current().nextInt(1, 20);
		if (randomNum % 2 == 0 ) {
			//throw new BpmnError("INGREDIENT-UNAVAILABLE");
			throw new IngredientNotAvailableException("speciality");
		}
	}

}
