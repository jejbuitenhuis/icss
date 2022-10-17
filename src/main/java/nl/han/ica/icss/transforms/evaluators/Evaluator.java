package nl.han.ica.icss.transforms.evaluators;

import java.util.HashMap;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.Literal;

abstract class Evaluator implements EvaluatorFunction
{
	private IHANLinkedList< HashMap<String, Literal> > variableValues;

	protected void setVariableValues(IHANLinkedList< HashMap<String, Literal> > variableValues)
	{
		this.variableValues = variableValues;
	}

	protected Literal getVariableValue(String variableName)
	{ // {{{
		if (this.variableValues == null)
			throw new RuntimeException("Evaluator doesn't have any variables");

		for (int i = 0; i < this.variableValues.getSize(); i++)
		{
			Literal value = this.variableValues.get(i)
				.get(variableName);

			if (value != null) return value;
		}

		throw new RuntimeException( String.format(
			"Undefined variable '%s'",
			variableName
		) );
	} // }}}
}
