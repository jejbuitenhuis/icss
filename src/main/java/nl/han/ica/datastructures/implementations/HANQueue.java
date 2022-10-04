package nl.han.ica.datastructures.implementations;

import nl.han.ica.datastructures.IHANQueue;
import nl.han.ica.datastructures.IHANLinkedList;

public class HANQueue<T> implements IHANQueue<T>
{
	private IHANLinkedList<T> queue = new HANLinkedList<>();

	@Override
	public void clear()
	{ // {{{
		this.queue.clear();
	} // }}}

	@Override
	public boolean isEmpty()
	{ // {{{
		// `getFirst() == null` ( O(1) ) is faster then `size() == 0` ( O(n) )
		return this.queue.getFirst() == null;
	} // }}}

	@Override
	public void enqueue(T value)
	{ // {{{
		this.queue.addFirst(value);
	} // }}}

	@Override
	public T dequeue()
	{ // {{{
		try {
			return this.queue.remove(0);
		} catch (Exception e) {
			return null;
		}
	} // }}}

	@Override
	public T peek()
	{ // {{{
		try {
			return this.queue.get(0);
		} catch (Exception e) {
			return null;
		}
	} // }}}

	@Override
	public int getSize()
	{ // {{{
		return this.queue.getSize();
	} // }}}
}
