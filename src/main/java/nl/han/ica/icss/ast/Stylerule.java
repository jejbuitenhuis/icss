package nl.han.ica.icss.ast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Stylerule extends ASTNode
{
	private static final int INDENT_AMOUNT = 2;
	private static final String WHITE_SPACE = IntStream.range(0, INDENT_AMOUNT)
		.mapToObj(i -> " ")
		.collect( Collectors.joining() );

	public ArrayList<Selector> selectors = new ArrayList<>();
	public ArrayList<ASTNode> body = new ArrayList<>();

	public Stylerule()
	{}

	public Stylerule(Selector selector, ArrayList<ASTNode> body)
	{
		this.selectors = new ArrayList<>();
		this.selectors.add(selector);
		this.body = body;
	}

	@Override
	public String getNodeLabel()
	{
		return "Stylerule";
	}

	@Override
	public String toCSSString()
	{ // {{{
		StringBuilder sb = new StringBuilder();

		String selectors = this.selectors.stream()
			.map(ASTNode::toCSSString)
			.collect( Collectors.joining(", ") );

		sb.append(selectors);

		sb.append(" {\n");

		for (ASTNode child : this.body)
			sb.append( WHITE_SPACE + child.toCSSString() + "\n" );

		sb.append("}\n");

		return sb.toString();
	} // }}}

	@Override
	public ArrayList<ASTNode> getChildren()
	{
		ArrayList<ASTNode> children = new ArrayList<>();

		children.addAll(selectors);
		children.addAll(body);

		return children;
	}

	@Override
	public ASTNode addChild(ASTNode child)
	{
		if(child instanceof Selector) selectors.add( (Selector) child );
		else body.add(child);

		return this;
	}

	@Override
	public ASTNode removeChild(ASTNode child)
	{
		if (child instanceof Selector) this.selectors.remove(child);
		else this.body.remove(child);

		return this;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		Stylerule stylerule = (Stylerule) o;

		return Objects.equals(selectors, stylerule.selectors) &&
				Objects.equals(body, stylerule.body);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(selectors, body);
	}
}
