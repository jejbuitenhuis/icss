package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.datastructures.implementations.HANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.transforms.evaluators.*;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.util.Pair;

public class Evaluator implements Transform
{
	private static final ArrayList< Pair< Class<? extends ASTNode>, EvaluatorFunction > > EVALUATORS = new ArrayList<>()
	{
		private static final long serialVersionUID = 1511648775391348036L;
	{
		add( new Pair<>( VariableReference.class, new VariableReferenceEvaluator() ) );
	}};

	private IHANLinkedList< HashMap<String, Literal> > variableValues;

	public Evaluator()
	{
		this.variableValues = new HANLinkedList<>();
	}

	private static EvaluatorFunction getEvaluatorForNode(ASTNode node)
	{ // {{{
		for ( Pair< Class<? extends ASTNode>, EvaluatorFunction > pair : EVALUATORS )
			if ( pair.getKey().isInstance(node) )
				return pair.getValue();

		return null;
	} // }}}

	private Literal getVariableValue(String variableName)
	{ // {{{
		for (int i = 0; i < this.variableValues.getSize(); i++)
		{
			Literal value = this.variableValues.get(i)
				.get(variableName);

			if (value != null) return value;
		}

		throw new RuntimeException( String.format(
			"Undefined variable '%s'",
			variableName
		) );
	} // }}}

	private HashMap<String, Literal> extractVariableTypes(ASTNode node)
	{ // {{{
		HashMap<String, Literal> temp = new HashMap<>();

		for ( ASTNode childNode : node.getChildren() )
		{
			if (childNode instanceof VariableAssignment)
			{
				VariableAssignment assignment = (VariableAssignment) childNode;
				String name = assignment.name.name;
				Expression expression = assignment.expression;

				if (expression instanceof Literal)
				{
					temp.put( name, (Literal) expression );
				}
				else if (expression instanceof VariableReference)
				{
					temp.put(
						name,
						this.getVariableValue(
							( (VariableReference) expression ).name
						)
					);
				}
				else
				{
					// TODO: Can an Operation be in a VariableAssignment?
					throw new RuntimeException( String.format(
						"Unexpected expression type '%s'",
						expression.getClass().getSimpleName()
					) );
				}
			}
		}

		return temp;
	} // }}}

	private void apply(ASTNode node)
	{ // {{{
		this.variableValues.addFirst( this.extractVariableTypes(node) );

		for ( ASTNode childNode : node.getChildren() )
		{
			EvaluatorFunction function = Evaluator.getEvaluatorForNode(childNode);

			if (function != null)
			{
				ASTNode newNode = function.evaluate(childNode, this.variableValues);

				node.removeChild(childNode);
				node.addChild(newNode);
			}

			if ( childNode.getChildren().size() > 0 )
				this.apply(childNode);
		}

		this.variableValues.removeFirst();
	} // }}}

	@Override
	public void apply(AST ast)
	{
		this.variableValues = new HANLinkedList<>();

		this.apply(ast.root);
	}
}
