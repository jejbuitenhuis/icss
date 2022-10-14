package nl.han.ica.icss.ast.types;

import java.util.HashMap;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;

public enum ExpressionType
{
	PIXEL,
	PERCENTAGE,
	COLOR,
	SCALAR,
	UNDEFINED,
	BOOL;

	private static final HashMap< Class<? extends Expression>, ExpressionType > EXPRESSION_TYPES = new HashMap<>()
	{
		private static final long serialVersionUID = -3894815695838192913L;
	{
		put(PercentageLiteral.class, PERCENTAGE);
		put(ScalarLiteral.class, SCALAR);
		put(PixelLiteral.class, PIXEL);
		put(ColorLiteral.class, COLOR);
		put(BoolLiteral.class, BOOL);
	}};

	private static ExpressionType fromVariableReference(VariableReference reference, IHANLinkedList< HashMap<String, ExpressionType> > variableTypes)
	{ // {{{
		for (int i = 0; i < variableTypes.getSize(); i++)
		{
			ExpressionType type = variableTypes.get(i).get(reference.name);

			if (type != null) return type;
		}

		return UNDEFINED;
	} // }}}

	private static ExpressionType fromOperation(Operation operation, IHANLinkedList< HashMap<String, ExpressionType> > variableTypes)
	{ // {{{
		ExpressionType typeLeft = ExpressionType.fromExpression(operation.lhs, variableTypes);
		ExpressionType typeRight = ExpressionType.fromExpression(operation.rhs, variableTypes);

		// if typeLeft is a scalar, the operation is probably something like
		// `2 * 3px`, so the typeRight is the type of the evaluated expression
		if (typeLeft == SCALAR) return typeRight;

		return typeLeft;
	} // }}}

	public static ExpressionType fromExpression(Expression expression, IHANLinkedList< HashMap<String, ExpressionType> > variableTypes)
	{ // {{{
		ExpressionType type = EXPRESSION_TYPES.get( expression.getClass() );

		if (type != null) return type;

		if (expression instanceof VariableReference)
			return ExpressionType.fromVariableReference(
				(VariableReference) expression,
				variableTypes
			);

		if (expression instanceof Operation)
			return ExpressionType.fromOperation(
				(Operation) expression,
				variableTypes
			);

		return UNDEFINED;
	} // }}}
}
