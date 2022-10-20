package nl.han.ica.datastructures;

public interface IScopeList<T>
{
	public void push();
	public void pop();

	public T get(String name);
	public void set(String name, T value);
}
