package nl.han.ica.icss.transforms.evaluators;

import nl.han.ica.datastructures.IScopeList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.ElseClause;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.transforms.Evaluator;

import java.util.ArrayList;

public class ElseClauseEvaluator implements EvaluatorFunction
{
	@Override
	public <T extends ASTNode> ArrayList<ASTNode> evaluate(
		T nodeToEvaluate,
		IScopeList<Literal> variableValues,
		Evaluator evaluator
	)
	{ // {{{
		if ( !(nodeToEvaluate instanceof ElseClause) )
			throw new RuntimeException( "Expected ElseClause, got " + nodeToEvaluate.getClass().getName() );

		ElseClause node = (ElseClause) nodeToEvaluate;

		evaluator.apply(node);

		return null;
	} // }}}
}
