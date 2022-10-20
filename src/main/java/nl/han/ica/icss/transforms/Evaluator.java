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
		add( new Pair<>( VariableAssignment.class, new VariableAssignmentEvaluator() ) );
		add( new Pair<>( VariableReference.class, new VariableReferenceEvaluator() ) );
		add( new Pair<>( Operation.class, new OperationEvaluator() ) );
		add( new Pair<>( IfClause.class, new IfClauseEvaluator() ) );
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

	private void apply(ASTNode node)
	{ // {{{
		this.variableValues.push();

		// keep a copy we can update beside the node itself, because if we
		// don't, the for loop won't loop over the newly added children
		// (because the `getChildren` returns a copy of the list instead of the
		// list that the node actually has)
		ArrayList<ASTNode> children = node.getChildren();
		// because we can't loop over a List and update it at the same time, we
		// loop over it with a while loop and keep track of the index ourself
		int index = 0;

		while ( index < children.size() )
		{
			ASTNode childNode = children.get(index);

			EvaluatorFunction function = Evaluator.getEvaluatorForNode(childNode);

			if (function == null)
			{
				++index;
				continue;
			}

			ArrayList<ASTNode> newNodes = function.evaluate(childNode, this.variableValues);

			int indexOfRemovedChild = children.indexOf(childNode);

			children.remove(childNode);
			node.removeChild(childNode);

			// the object being removed is before the index, so everything,
			// including the current index, is shifted to the left once
			if (indexOfRemovedChild <= index && index > 0) --index;

			if (newNodes != null)
			{
				children.addAll(newNodes);

				for (ASTNode newChild : newNodes)
					node.addChild(newChild);
			}
		}

		// some evaluators change the node's structure so much, we can't have
		// this check inside the loop above, because then we would also
		// evaluate the children of the childNode, even if these don't have to
		// be evaluated (e.g. some children of the IfClause and ElseClause)
		for ( ASTNode childNode : node.getChildren() )
			if ( childNode.getChildren().size() > 0 )
				this.apply(childNode);

		this.variableValues.pop();
	} // }}}

	@Override
	public void apply(AST ast)
	{
		this.variableValues = new ScopeList<>();

		this.apply(ast.root);
	}
}
