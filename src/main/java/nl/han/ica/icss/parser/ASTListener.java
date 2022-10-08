package nl.han.ica.icss.parser;

import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.datastructures.implementations.HANStack;
import nl.han.ica.icss.ast.*;

import javax.management.RuntimeErrorException;

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
		ASTNode declaration = this.currentContainer.pop();

		if ( !(declaration instanceof Declaration) )
			throw new RuntimeException("Unexpected non-declaration");

		this.currentContainer.peek() // the Selector
			.addChild(declaration);
	} // }}}

	@Override
	public void enterLiteral(ICSSParser.LiteralContext ctx)
	{ // {{{
		this.currentContainer.push(
			Literal.fromString( ctx.getText() )
		);
	} // }}}

	@Override
	public void exitLiteral(ICSSParser.LiteralContext ctx)
	{ // {{{
		ASTNode literal = this.currentContainer.pop();

		if ( !(literal instanceof Literal) )
			throw new RuntimeException("Unexpected non-literal");

		this.currentContainer.peek() // the Declaration
			.addChild(literal);
	} // }}}
}
