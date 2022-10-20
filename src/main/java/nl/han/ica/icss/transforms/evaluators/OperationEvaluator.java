package nl.han.ica.icss.transforms.evaluators;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BinaryOperator;

import nl.han.ica.datastructures.IScopeList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.*;

public class OperationEvaluator implements EvaluatorFunction
{
	private static final HashMap< Class<? extends Operation>, BinaryOperator<Integer> > OPERATORS = new HashMap<>()
	{
		private static final long serialVersionUID = 8293429628768571917L;
	{
		put( AddOperation.class, (Integer l, Integer r) -> l + r );
		put( SubtractOperation.class, (Integer l, Integer r) -> l - r );
		put( MultiplyOperation.class, (Integer l, Integer r) -> l * r );
	}};

	private Literal expressionToLiteral(Expression expr, IScopeList<Literal> variableValues)
	{ // {{{
		if (expr instanceof Literal)
			return (Literal) expr;
		else if (expr instanceof Operation)
			return this.evaluate( (Operation) expr, variableValues );
		else if (expr instanceof VariableReference)
			return variableValues.get( ( (VariableReference) expr ).name );
		else
			throw new RuntimeException( "Unexpected left operation type " + expr.getClass().getName() );
	} // }}}

	private Literal evaluate(Operation node, IScopeList<Literal> variableValues)
	{ // {{{
		BinaryOperator<Integer> function = OPERATORS.get( node.getClass() );

		if (function == null)
			throw new RuntimeException( String.format(
				"No operation named '%s' found",
				node.getClass().getSimpleName()
			) );

		Literal literalLeft = this.expressionToLiteral(node.lhs, variableValues);
		Literal literalRight = this.expressionToLiteral(node.rhs, variableValues);

		int valueLeft = literalLeft.getValue();
		int valueRight = literalRight.getValue();

		int result = function.apply(valueLeft, valueRight);
		Class<? extends Literal> literalClass = literalLeft.getClass();

		if (literalLeft instanceof ScalarLiteral)
			literalClass = literalRight.getClass();

		try
		{
			return literalClass.getConstructor(int.class)
				.newInstance(result);
		}
		catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e)
		{
			// sadly java doesn't have abstract constructors, so we can't
			// enforce this constructor
			throw new RuntimeException( String.format(
				"Literal '%s' doesn't have an int constructor, which is required",
				literalClass.getName()
			) );
		}
	} // }}}

	@Override
	public <T extends ASTNode> ArrayList<ASTNode> evaluate(
		T nodeToEvaluate,
		IScopeList<Literal> variableValues
	)
	{ // {{{
		if ( !(nodeToEvaluate instanceof Operation) )
			throw new RuntimeException( "Expected Operation, got " + nodeToEvaluate.getClass().getName() );

		Operation node = (Operation) nodeToEvaluate;

		return new ArrayList<>( List.of( this.evaluate(node, variableValues) ) );
	} // }}}
}
