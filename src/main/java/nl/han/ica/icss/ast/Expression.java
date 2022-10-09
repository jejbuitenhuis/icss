package nl.han.ica.icss.ast;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javafx.util.Pair;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;

public abstract class Expression extends ASTNode
{
	private static final List< Pair< Pattern, Class<? extends Expression> > > EXPRESSIONS = new ArrayList<>()
	{
		private static final long serialVersionUID = -455280851437246030L;
	{
		add( new Pair<>( Pattern.compile("^\\d+%$"), PercentageLiteral.class ) );
		add( new Pair<>( Pattern.compile("^\\d+px$"), PixelLiteral.class ) );
		add( new Pair<>( Pattern.compile("^#[0-9a-f]{6}$"), ColorLiteral.class ) );
		add( new Pair<>( Pattern.compile("^TRUE|FALSE$"), BoolLiteral.class ) );
		add( new Pair<>( Pattern.compile("^\\d+$"), ScalarLiteral.class ) );
		// variable names should be last, because they also match TRUE and FALSE
		add( new Pair<>( Pattern.compile("^[A-Z][A-Za-z0-9_]+$"), VariableReference.class ) );
	}};

	public static final Expression fromString(String literal)
	{ // {{{
		try
		{
			for ( Pair< Pattern, Class<? extends Expression> > entry : EXPRESSIONS )
			{
				boolean matched = entry.getKey()
					.matcher(literal)
					.find();

				if (matched)
				{
					return entry.getValue()
						.getConstructor(String.class)
						.newInstance(literal);
				}
			}
		}
		catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}

		throw new RuntimeException(
			String.format("No Literal matched with \"%s\"", literal)
		);
	} // }}}
}
