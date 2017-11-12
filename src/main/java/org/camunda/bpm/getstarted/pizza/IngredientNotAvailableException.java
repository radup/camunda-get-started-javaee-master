package org.camunda.bpm.getstarted.pizza;

import java.util.logging.Logger;

import org.camunda.bpm.engine.ProcessEngineException;

public class IngredientNotAvailableException extends ProcessEngineException {

	private static final long serialVersionUID = 6820615345122650081L;

	private String ingredient;

	public IngredientNotAvailableException(String ingredient) {
		this.ingredient = ingredient;
	}

	@Override
	public String getMessage() {
		return "An ingredient necessary for the pizza is not available - missing ingredient: " + ingredient;
	}

}
