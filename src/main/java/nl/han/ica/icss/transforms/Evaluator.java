package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.IScopeList;
import nl.han.ica.datastructures.implementations.ScopeList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.transforms.evaluators.*;

import java.util.ArrayList;

import javafx.util.Pair;

public class Evaluator implements Transform
{
	private static final ArrayList< Pair< Class<? extends ASTNode>, EvaluatorFunction > > EVALUATORS = new ArrayList<>()
	{
		private static final long serialVersionUID = 1511648775391348036L;
	{
		add( new Pair<>( Stylerule.class, new StyleruleEvaluator() ) );
		add( new Pair<>( Declaration.class, new DeclarationEvaluator() ) );
		add( new Pair<>( VariableAssignment.class, new VariableAssignmentEvaluator() ) );
		add( new Pair<>( VariableReference.class, new VariableReferenceEvaluator() ) );
		add( new Pair<>( Operation.class, new OperationEvaluator() ) );
		add( new Pair<>( IfClause.class, new IfClauseEvaluator() ) );
		add( new Pair<>( ElseClause.class, new ElseClauseEvaluator() ) );
	}};

	private IScopeList<Literal> variableValues;

	public Evaluator()
	{
		this.variableValues = new ScopeList<>();
	}

	private static EvaluatorFunction getEvaluatorForNode(ASTNode node)
	{ // {{{
		for ( Pair< Class<? extends ASTNode>, EvaluatorFunction > pair : EVALUATORS )
			if ( pair.getKey().isInstance(node) )
				return pair.getValue();

		return null;
	} // }}}

	public void apply(ASTNode node)
	{ // {{{
		this.variableValues.push();

		// clone the children to prevent a ConcurrentModificationException from
		// being thrown in the loop below
		ArrayList<ASTNode> children = (ArrayList<ASTNode>) node.getChildren()
			.clone();

		for (ASTNode childNode : children)
		{
			EvaluatorFunction function = Evaluator.getEvaluatorForNode(childNode);

			if (function != null)
			{
				ArrayList<ASTNode> newNodes = function.evaluate(
					childNode,
					this.variableValues,
					this
				);

				if (newNodes != null)
				{
					node.removeChild(childNode);

					for (ASTNode newChild : newNodes)
						node.addChild(newChild);
				}
			}
		}

		this.variableValues.pop();
	} // }}}

	@Override
	public void apply(AST ast)
	{
		this.variableValues = new ScopeList<>();

		this.apply(ast.root);
	}
}
