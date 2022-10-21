package nl.han.ica.icss.transforms.evaluators;

import java.util.ArrayList;

import nl.han.ica.datastructures.IScopeList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.transforms.Evaluator;

public class DeclarationEvaluator implements EvaluatorFunction
{
	@Override
	public <T extends ASTNode> ArrayList<ASTNode> evaluate(
		T nodeToEvaluate,
		IScopeList<Literal> variableValues,
		Evaluator evaluator
	)
	{ // {{{
		if ( !(nodeToEvaluate instanceof Declaration) )
			throw new RuntimeException( "Expected Declaration, got " + nodeToEvaluate.getClass().getName() );

		Declaration node = (Declaration) nodeToEvaluate;

		evaluator.apply(node);

		return null;
	} // }}}
}
