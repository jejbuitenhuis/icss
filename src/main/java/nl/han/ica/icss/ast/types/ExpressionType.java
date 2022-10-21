package nl.han.ica.icss.ast.types;

import java.util.HashMap;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.datastructures.IScopeList;
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

	private static ExpressionType fromOperation(Operation operation, IScopeList<ExpressionType> variableTypes)
	{ // {{{
		ExpressionType typeLeft = ExpressionType.fromExpression(operation.lhs, variableTypes);
		ExpressionType typeRight = ExpressionType.fromExpression(operation.rhs, variableTypes);

		// if typeLeft is a scalar, the operation is probably something like
		// `2 * 3px`, so the typeRight is the type of the evaluated expression
		if (typeLeft == SCALAR) return typeRight;

		// typeLeft isn't a scalar, so typeRight must be a scalar or the same
		// type as typeLeft, so typeLeft can be returned
		return typeLeft;
	} // }}}

	public static ExpressionType fromExpression(Expression expression, IScopeList<ExpressionType> variableTypes)
	{ // {{{
		ExpressionType type = EXPRESSION_TYPES.get( expression.getClass() );

		if (type != null) return type;

		if (expression instanceof VariableReference)
		{
			type = variableTypes.get( ( (VariableReference) expression ).name );

			return type == null ? UNDEFINED : type;
		}

		if (expression instanceof Operation)
			return ExpressionType.fromOperation(
				(Operation) expression,
				variableTypes
			);

		return UNDEFINED;
	} // }}}
}
