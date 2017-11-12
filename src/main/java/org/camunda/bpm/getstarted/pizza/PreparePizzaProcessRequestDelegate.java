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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

//has to be used with Delegate Expression in Camunda; using Java Class will not allow CDI!!!

@Stateless
@Named
public class PreparePizzaProcessRequestDelegate implements JavaDelegate {

	private final static Logger LOGGER = Logger.getLogger("PIZZA-ORDERS");

	@Inject
	private OrderBusinessLogic orderBusinessLogic;

	public void execute(DelegateExecution execution) throws Exception {
		OrderEntity orderEntity = orderBusinessLogic.getOrder((Long) execution.getVariable("orderId"));
		LOGGER.log(Level.INFO, "\n\n\nPreparing pizza {0}, for order {1}, customer {2}, address {3} \n\n\n",
				new String[] { orderEntity.getPizza(), String.valueOf(orderEntity.getId()), orderEntity.getCustomer(),
						orderEntity.getAddress() });
	}

}
