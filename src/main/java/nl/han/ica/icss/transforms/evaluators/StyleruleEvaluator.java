package nl.han.ica.icss.transforms.evaluators;

import java.util.ArrayList;

import nl.han.ica.datastructures.IScopeList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Stylerule;
import nl.han.ica.icss.transforms.Evaluator;

public class StyleruleEvaluator implements EvaluatorFunction
{
	@Override
	public <T extends ASTNode> ArrayList<ASTNode> evaluate(
		T nodeToEvaluate,
		IScopeList<Literal> variableValues,
		Evaluator evaluator
	)
	{ // {{{
		if ( !(nodeToEvaluate instanceof Stylerule) )
			throw new RuntimeException( "Expected Stylerule, got " + nodeToEvaluate.getClass().getName() );

		Stylerule node = (Stylerule) nodeToEvaluate;

		evaluator.apply(node);

		return null;
	} // }}}
}
