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

		this.currentContainer.peek()
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
		ASTNode variableAssignment = this.currentContainer.pop();

		if ( !(variableAssignment instanceof VariableAssignment) )
			throw new RuntimeException("Unexpected non-variableAssignment:" + variableAssignment);

		this.currentContainer.peek()
			.addChild(variableAssignment);
	} // }}}

	@Override
	public void enterVariableReference(ICSSParser.VariableReferenceContext ctx)
	{ // {{{
		if ( ctx.parent instanceof ICSSParser.ExpressionContext )
			// the VariableReference is already handled by `enterExpression`.
			// This can be handled by antlr in the .g4 file, but that creates
			// a less readable .g4 file, because almost all `expression`s then
			// become `(expression | variableReference)`, because
			// `variableReference` gets removed from `expression`
			return;

		String variableName = ctx.getText();

		this.currentContainer.push(
			new VariableReference(variableName)
		);
	} // }}}

	@Override
	public void exitVariableReference(ICSSParser.VariableReferenceContext ctx)
	{ // {{{
		if ( ctx.parent instanceof ICSSParser.ExpressionContext )
			// the VariableReference is already handled by `exitExpression`.
			// This can be handled by antlr in the .g4 file, but that creates
			// a less readable .g4 file, because almost all `expression`s then
			// become `(expression | variableReference)`, because
			// `variableReference` gets removed from `expression`
			return;

		ASTNode variableReference = this.currentContainer.pop();

		if ( !(variableReference instanceof VariableReference) )
			throw new RuntimeException("Unexpected non-variableReference:" + variableReference);

		this.currentContainer.peek()
			.addChild(variableReference);
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

		this.currentContainer.peek()
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
		ASTNode declaration = this.currentContainer.pop();

		if ( !(
			declaration instanceof Declaration
			|| declaration instanceof IfClause)
		)
			throw new RuntimeException("Unexpected non-declaration or non-ifClause:" + declaration);

		this.currentContainer.peek()
			.addChild(declaration);
	} // }}}

	@Override
	public void enterOperation(ICSSParser.OperationContext ctx)
	{ // {{{
		// if this is an math operation, it consists of 3 children, one for the
		// left side of the operator, the operator itself and the right side of
		// the operator. If this isn't the case, it is a single expression, so
		// the parsing is handled by `enterExpression` and nothing needs to be
		// done here
		if ( ctx.getChildCount() == 3 )
		{
			Operation operation = (Operation) Expression.fromString(
				ctx.getChild(1).getText()
			);

			this.currentContainer.push(operation);
		}
	} // }}}

	@Override
	public void exitOperation(ICSSParser.OperationContext ctx)
	{ // {{{
		if ( ctx.getChildCount() == 3 )
		{
			ASTNode operation = this.currentContainer.pop();

			if ( !(operation instanceof Operation) )
				throw new RuntimeException("Unexpected non-operation:" + operation);

			this.currentContainer.peek()
				.addChild(operation);
		}
	} // }}}

	@Override
	public void enterIfStatement(ICSSParser.IfStatementContext ctx)
	{ // {{{
		this.currentContainer.push(
			new IfClause()
		);
	} // }}}

	@Override
	public void enterElseStatement(ICSSParser.ElseStatementContext ctx)
	{ // {{{
		this.currentContainer.push(
			new ElseClause()
		);
	} // }}}

	@Override
	public void exitElseStatement(ICSSParser.ElseStatementContext ctx)
	{ // {{{
		ASTNode elseStatement = this.currentContainer.pop();

		if ( !(elseStatement instanceof ElseClause) )
			throw new RuntimeException("Unexpected non-elseClause:" + elseStatement);

		this.currentContainer.peek()
			.addChild(elseStatement);
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
			throw new RuntimeException("Unexpected non-expression:" + expression);

		this.currentContainer.peek()
			.addChild(expression);
	} // }}}
}
