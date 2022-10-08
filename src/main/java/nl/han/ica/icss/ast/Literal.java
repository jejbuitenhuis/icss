package nl.han.ica.icss.ast;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;

public abstract class Literal extends Expression
{
	private static final HashMap< Pattern, Class<? extends Literal> > LITERALS = new HashMap<>()
	{
		private static final long serialVersionUID = -455280851437246030L;
	{
		put( Pattern.compile("^\\d+%$"), PercentageLiteral.class );
		put( Pattern.compile("^\\d+px$"), PixelLiteral.class );
		put( Pattern.compile("^#[0-9a-f]{6}$"), ColorLiteral.class );
		put( Pattern.compile("^TRUE|FALSE$"), BoolLiteral.class );
		put( Pattern.compile("^\\d+$"), ScalarLiteral.class );
	}};

	public static final Literal fromString(String literal)
	{ // {{{
		try
		{
			for ( Map.Entry< Pattern, Class<? extends Literal> > entry : LITERALS.entrySet() )
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
