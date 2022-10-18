package nl.han.ica.icss.ast;

public abstract class Literal extends Expression
{
	/**
	 * Used in the {@link OperationEvaluator} to get the value of the Literal.
	 */
	public abstract int getValue();
}
