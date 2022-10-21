package nl.han.ica.icss.checker;

import java.util.ArrayList;

import javafx.util.Pair;
import nl.han.ica.datastructures.IScopeList;
import nl.han.ica.datastructures.implementations.ScopeList;
import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.IfClause;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.VariableAssignment;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.checkers.CheckerFunction;
import nl.han.ica.icss.checker.checkers.DeclarationChecker;
import nl.han.ica.icss.checker.checkers.IfClauseChecker;
import nl.han.ica.icss.checker.checkers.OperationChecker;

public class Checker
{
	private IScopeList<ExpressionType> variableTypes;

	private static final ArrayList< Pair< Class<? extends ASTNode>, CheckerFunction > > CHECKERS = new ArrayList<>()
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

	private void extractVariableTypes(ASTNode node)
	{ // {{{
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

				this.variableTypes.set(name, type);
			}
		}
	} // }}}

	private void check(ASTNode node)
	{ // {{{
		this.variableTypes.push();
		this.extractVariableTypes(node);

		for ( ASTNode childNode : node.getChildren() )
		{
			CheckerFunction checker = Checker.getCheckerForNode(childNode);

			if ( checker != null && !childNode.hasError() )
				checker.check(childNode, this.variableTypes);

			if ( childNode.getChildren().size() > 0 )
				this.check(childNode);
		}

		this.variableTypes.pop();
	} // }}}

	public void check(AST ast)
	{ // {{{
		this.variableTypes = new ScopeList<>();

		this.check(ast.root);
	} // }}}
}
