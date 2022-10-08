package nl.han.ica.icss.ast;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

public abstract class Selector extends ASTNode
{
	private static final HashMap< Character, Class<? extends Selector> > SELECTORS = new HashMap<>()
	{
		private static final long serialVersionUID = -2103460650832121914L;
	{
		put('#', IdSelector.class);
		put('.', ClassSelector.class);
	}};
	private static final Class<? extends Selector> SELECTOR_DEFAULT = TagSelector.class;

	public static Selector fromString(String selector)
	{ // {{{
		try
		{
			char first = selector.charAt(0);

			if ( SELECTORS.containsKey(first) )
			{
				Class<? extends Selector> clazz = SELECTORS.get(first);

				return clazz.getConstructor(String.class)
					.newInstance(selector);
			}

			return SELECTOR_DEFAULT.getConstructor(String.class)
				.newInstance(selector);
		}
		catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	} // }}}
}
