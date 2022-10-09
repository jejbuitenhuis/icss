package nl.han.ica.icss.parser;

import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.datastructures.implementations.HANStack;
import nl.han.ica.icss.ast.*;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener
{
	//Accumulator attributes:
	private AST ast;

	// Use this to keep track of the parent nodes when recursively traversing the ast
	private IHANStack<ASTNode> currentContainer;

	public ASTListener()
	{
		this.ast = new AST();
		this.currentContainer = new HANStack<>();
	}

	public AST getAST()
	{
		return this.ast;
	}

	@Override
	public void enterStylesheet(ICSSParser.StylesheetContext ctx)
	{ // {{{
		this.currentContainer.push( new Stylesheet() );
	} // }}}

	@Override
	public void exitStylesheet(ICSSParser.StylesheetContext ctx)
	{ // {{{
		ASTNode stylesheet = this.currentContainer.pop();

		if ( !(stylesheet instanceof Stylesheet) )
			throw new RuntimeException("Unexpected non-stylesheet");

		this.ast = new AST( (Stylesheet) stylesheet );
	} // }}}

	@Override
	public void enterStyleRule(ICSSParser.StyleRuleContext ctx)
	{ // {{{
		this.currentContainer.push(
			new Stylerule()
		);
	} // }}}

	@Override
	public void exitStyleRule(ICSSParser.StyleRuleContext ctx)
	{ // {{{
		ASTNode styleRule = this.currentContainer.pop();

		if ( !(styleRule instanceof Stylerule) )
			throw new RuntimeException("Unexpected non-stylerule");

		this.currentContainer.peek() // the Stylesheet
			.addChild(styleRule);
	} // }}}

	@Override
	public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx)
	{ // {{{
		this.currentContainer.push(
			new VariableAssignment()
		);
	} // }}}

	@Override
	public void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx)
	{ // {{{
		// value got assigned to the VariableAssignment in `exitExpression`
		ASTNode variableAssignment = this.currentContainer.pop();

		if ( !(variableAssignment instanceof VariableAssignment) )
			throw new RuntimeException("Unexpected non-variableAssignment");

		this.currentContainer.peek() // the Stylesheet
			.addChild(variableAssignment);
	} // }}}

	@Override
	public void enterVariableReference(ICSSParser.VariableReferenceContext ctx)
	{ // {{{
		String variableName = ctx.getText();

		ASTNode variableContainer = this.currentContainer.peek();

		if ( !(
			variableContainer instanceof VariableAssignment
			|| variableContainer instanceof Expression
		) )
			throw new RuntimeException("Unexpected non-variableContainer");

		variableContainer.addChild( new VariableReference(variableName) );
	} // }}}

	@Override
	public void enterSelector(ICSSParser.SelectorContext ctx)
	{ // {{{
		this.currentContainer.push(
			Selector.fromString( ctx.getText() )
		);
	} // }}}

	@Override
	public void exitSelector(ICSSParser.SelectorContext ctx)
	{ // {{{
		ASTNode selector = this.currentContainer.pop();

		if ( !(selector instanceof Selector) )
			throw new RuntimeException("Unexpected non-selector");

		this.currentContainer.peek() // the Stylerule
			.addChild(selector);
	} // }}}

	@Override
	public void enterDeclaration(ICSSParser.DeclarationContext ctx)
	{ // {{{
		this.currentContainer.push(
			new Declaration( ctx.getText() )
		);
	} // }}}

	// use exitStyling instead of exitDeclaration
	@Override
	public void exitStyling(ICSSParser.StylingContext ctx)
	{ // {{{
		// value got assigned to the Declaration in `exitExpression`
		ASTNode declaration = this.currentContainer.pop();

		if ( !(declaration instanceof Declaration) )
			throw new RuntimeException("Unexpected non-declaration");

		this.currentContainer.peek() // the Selector
			.addChild(declaration);
	} // }}}

	@Override
	public void enterExpression(ICSSParser.ExpressionContext ctx)
	{ // {{{
		this.currentContainer.push(
			Expression.fromString( ctx.getText() )
		);
	} // }}}

	@Override
	public void exitExpression(ICSSParser.ExpressionContext ctx)
	{ // {{{
		ASTNode expression = this.currentContainer.pop();

		if ( !(expression instanceof Expression) )
			throw new RuntimeException("Unexpected non-expression");

		this.currentContainer.peek() // the Declaration or VariableReference
			.addChild(expression);
	} // }}}
}
