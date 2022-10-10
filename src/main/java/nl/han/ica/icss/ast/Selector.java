package nl.han.ica.icss.ast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.util.HashMap;

import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

public abstract class Selector extends ASTNode
{
	private static final HashMap< Character, Constructor<? extends Selector> > SELECTORS = new HashMap<>();
	private static final Constructor<? extends Selector> SELECTOR_DEFAULT;

	static
	{ // {{{
		try
		{
			SELECTORS.put( '#', IdSelector.class.getConstructor(String.class) );
			SELECTORS.put( '.', ClassSelector.class.getConstructor(String.class) );

			SELECTOR_DEFAULT = TagSelector.class.getConstructor(String.class);
		}
		catch (NoSuchMethodException e)
		{
			throw new RuntimeException(e);
		}
	} // }}}

	public static Selector fromString(String selector)
	{ // {{{
		try
		{
			char first = selector.charAt(0);

			if ( SELECTORS.containsKey(first) )
			{
				Constructor<? extends Selector> clazz = SELECTORS.get(first);

				return clazz.newInstance(selector);
			}

			return SELECTOR_DEFAULT.newInstance(selector);
		}
		catch (InstantiationException | InvocationTargetException | IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	} // }}}
}
