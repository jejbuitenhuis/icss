package nl.han.ica.icss.ast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javafx.util.Pair;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;

public abstract class Expression extends ASTNode
{
	private static final List< Pair< Pattern, Constructor<? extends Expression> > > EXPRESSIONS = new ArrayList<>();

	static
	{ // {{{
		try
		{
			EXPRESSIONS.add( new Pair<>( Pattern.compile("^\\d+%$"), PercentageLiteral.class.getConstructor(String.class) ) );
			EXPRESSIONS.add( new Pair<>( Pattern.compile("^\\d+px$"), PixelLiteral.class.getConstructor(String.class) ) );
			EXPRESSIONS.add( new Pair<>( Pattern.compile("^#[0-9a-f]{6}$"), ColorLiteral.class.getConstructor(String.class) ) );
			EXPRESSIONS.add( new Pair<>( Pattern.compile("^TRUE|FALSE$"), BoolLiteral.class.getConstructor(String.class) ) );
			EXPRESSIONS.add( new Pair<>( Pattern.compile("^\\d+$"), ScalarLiteral.class.getConstructor(String.class) ) );
			// variable names should be last, because they also match TRUE and FALSE
			EXPRESSIONS.add( new Pair<>( Pattern.compile("^[A-Z][A-Za-z0-9_]+$"), VariableReference.class.getConstructor(String.class) ) );
		}
		catch (NoSuchMethodException e)
		{
			throw new RuntimeException(e);
		}
	} // }}}

	public static final Expression fromString(String literal)
	{ // {{{
		try
		{
			for ( Pair< Pattern, Constructor<? extends Expression> > entry : EXPRESSIONS )
			{
				boolean matched = entry.getKey()
					.matcher(literal)
					.find();

				if (matched)
				{
					return entry.getValue()
						.newInstance(literal);
				}
			}
		}
		catch (InstantiationException | InvocationTargetException | IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}

		throw new RuntimeException(
			String.format("No Literal matched with \"%s\"", literal)
		);
	} // }}}
}
