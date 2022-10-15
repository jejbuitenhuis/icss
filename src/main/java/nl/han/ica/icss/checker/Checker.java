package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.datastructures.implementations.HANLinkedList;
import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.IfClause;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.VariableAssignment;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.checkers.*;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.util.Pair;

public class Checker
{
	private IHANLinkedList< HashMap<String, ExpressionType> > variableTypes;

	private static ArrayList< Pair< Class<? extends ASTNode>, CheckerFunction > > CHECKERS = new ArrayList<>()
	{
		private static final long serialVersionUID = -5415055408447394756L;
	{
		add( new Pair<>( Declaration.class, new DeclarationChecker() ) );
		add( new Pair<>( Operation.class, new OperationChecker() ) );
		add( new Pair<>( IfClause.class, new IfClauseChecker() ) );
	}};

	private static CheckerFunction getCheckerForNode(ASTNode node)
	{ // {{{
		for ( Pair< Class<? extends ASTNode>, CheckerFunction > pair : CHECKERS )
			if ( pair.getKey().isInstance(node) )
				return pair.getValue();

		return null;
	} // }}}

	private HashMap<String, ExpressionType> extractVariableTypes(ASTNode node)
	{ // {{{
		HashMap<String, ExpressionType> temp = new HashMap<>();

		for ( ASTNode childNode : node.getChildren() )
		{
			if (childNode instanceof VariableAssignment)
			{
				VariableAssignment assignment = (VariableAssignment) childNode;
				String name = assignment.name.name;
				ExpressionType type = ExpressionType.fromExpression(
					assignment.expression,
					this.variableTypes
				);

				temp.put(name, type);
			}
		}

		return temp;
	} // }}}

	private void check(ASTNode node)
	{ // {{{
		this.variableTypes.addFirst( this.extractVariableTypes(node) );

		for ( ASTNode childNode : node.getChildren() )
		{
			CheckerFunction checker = Checker.getCheckerForNode(childNode);

			if ( checker != null && !childNode.hasError() )
				checker.check(childNode, this.variableTypes);

			if ( childNode.getChildren().size() > 0 )
				this.check(childNode);
		}

		this.variableTypes.removeFirst();
	} // }}}

	public void check(AST ast)
	{ // {{{
		this.variableTypes = new HANLinkedList<>();

		this.check(ast.root);
	} // }}}
}
