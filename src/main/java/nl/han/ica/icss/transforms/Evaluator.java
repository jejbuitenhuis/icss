package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;

import java.util.HashMap;

public class Evaluator implements Transform
{
	private IHANLinkedList< HashMap<String, Literal> > variableValues;

	public Evaluator()
	{
		// TODO:
		// variableValues = new HANLinkedList<>();
	}

	@Override
	public void apply(AST ast)
	{
		// TODO:
		// variableValues = new HANLinkedList<>();
	}
}
