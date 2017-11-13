package org.camunda.bpm.getstarted.pizza;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class LoggingValidateDelegate implements JavaDelegate {

	private final static Logger LOGGER = Logger.getLogger("PIZZA-ORDERS");
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		boolean logged = Math.random() < 0.5;
		
		if (logged) {
			LOGGER.log(Level.INFO, "Order logged succesfully");
		}
		else {
			LOGGER.log(Level.WARNING, "Order not logged!");
			throw new org.camunda.bpm.engine.delegate.BpmnError("LoggingError", "There was a problem with the logging of the order!");
		}
		
		
	}
	

}
