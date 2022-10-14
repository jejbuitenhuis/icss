package nl.han.ica.icss.checker.checkers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.types.ExpressionType;

public class DeclarationChecker implements CheckerFunction
{
	private static HashMap< ExpressionType, ArrayList<String> > TYPED_PROPERTIES = new HashMap<>()
	{ // {{{
		private static final long serialVersionUID = 1L;
	{
		put( ExpressionType.COLOR, new ArrayList<>( Arrays.asList(
			"background-color",
			"color"
		) ) );

		put( ExpressionType.PERCENTAGE, new ArrayList<>( Arrays.asList(
			"width",
			"height"
		) ) );

		put( ExpressionType.PIXEL, new ArrayList<>( Arrays.asList(
			"width",
			"height"
		) ) );
	}}; // }}}

	@Override
	public <T extends ASTNode> void check(
		T nodeToCheck,
		IHANLinkedList< HashMap<String, ExpressionType> > variableTypes
	)
	{ // {{{
		if ( !(nodeToCheck instanceof Declaration) )
			throw new RuntimeException( "Expected declaration, got " + nodeToCheck.getClass().getName() );

		Declaration node = (Declaration) nodeToCheck;

		String property = node.property.name;
		Expression expression = node.expression;
		ExpressionType type = ExpressionType.fromExpression(expression, variableTypes);

		ArrayList<String> allowedProperties = TYPED_PROPERTIES.get(type);

		if ( allowedProperties == null || !allowedProperties.contains(property) )
		{
			String error = String.format(
				"Incorrect type '%s' for property '%s'",
				type,
				property
			);

			node.setError(error);
		}
	} // }}}
}
