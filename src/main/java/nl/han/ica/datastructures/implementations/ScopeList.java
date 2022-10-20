package nl.han.ica.datastructures.implementations;

import java.util.HashMap;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.datastructures.IScopeList;

public class ScopeList<T> implements IScopeList<T>
{
	private IHANLinkedList< HashMap<String, T> > scopes = new HANLinkedList<>();
	private HashMap<String, T> currentScope = null;

	@Override
	public void push()
	{ // {{{
		this.scopes.addFirst( new HashMap<>() );

		this.currentScope = this.scopes.getFirst();
	} // }}}
	@Override
	public void pop()
	{ // {{{
		this.scopes.removeFirst();

		this.currentScope = this.scopes.getFirst();
	} // }}}

	@Override
	public T get(String name)
	{ // {{{
		if (this.currentScope == null)
			throw new RuntimeException("ScopeList doesn't have any scopes");

		for (int i = 0; i < this.scopes.getSize(); i++)
		{
			T value = this.scopes.get(i)
				.get(name);

			if (value != null) return value;
		}

		throw new RuntimeException( String.format(
			"Undefined variable '%s' in ScopeList",
			name
		) );
	} // }}}

	@Override
	public void set(String name, T value)
	{ // {{{
		this.currentScope.put(name, value);
	} // }}}
}
