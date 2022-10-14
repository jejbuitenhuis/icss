package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.datastructures.implementations.HANLinkedList;
import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.VariableAssignment;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.checkers.*;

import java.util.HashMap;

public class Checker
{
	private IHANLinkedList< HashMap<String, ExpressionType> > variableTypes;

	private static HashMap< Class<? extends ASTNode>, CheckerFunction > CHECKERS = new HashMap<>()
	{
		private static final long serialVersionUID = 1L;
	{
		put( Declaration.class, new DeclarationChecker() );
	}};

	private void extractVariableTypes(ASTNode node)
	{ // {{{
		HashMap<String, ExpressionType> temp = new HashMap<>();

		for ( ASTNode child : node.getChildren() )
		{
			if (child instanceof VariableAssignment)
			{
				VariableAssignment assignment = (VariableAssignment) child;
				String name = assignment.name.name;
				ExpressionType type = ExpressionType.fromExpression(
					assignment.expression,
					this.variableTypes
				);

				temp.put(name, type);
			}
		}

		this.variableTypes.addFirst(temp);
	} // }}}

	private void check(ASTNode node)
	{ // {{{
		this.extractVariableTypes(node);

		for ( ASTNode child : node.getChildren() )
		{
			CheckerFunction checker = CHECKERS.get( child.getClass() );

			if (checker != null)
				checker.check(child, this.variableTypes);

			if ( child.getChildren().size() > 0 )
				this.check(child);
		}
	} // }}}

	public void check(AST ast)
	{ // {{{
		this.variableTypes = new HANLinkedList<>();

		this.check(ast.root);
	} // }}}
}
