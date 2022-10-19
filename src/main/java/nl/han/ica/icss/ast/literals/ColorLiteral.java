package nl.han.ica.icss.ast.literals;

import nl.han.ica.icss.ast.Literal;

import java.util.Objects;

public class ColorLiteral extends Literal
{
	public String value;

	public ColorLiteral(String value)
	{
		this.value = value;
	}

	public ColorLiteral(int value)
	{
		int r = (value >> 16) & 0xff;
		int g = (value >> 8) & 0xff;
		int b = value & 0xff;

		this.value = String.format("#%02x%02x%02x", r, g, b);
	}

	@Override
	public int getValue()
	{ // {{{
		String value = this.value.substring(1);
		int r = Integer.parseInt( value.substring(0, 2), 16 );
		int g = Integer.parseInt( value.substring(2, 4), 16 );
		int b = Integer.parseInt( value.substring(4, 6), 16 );

		return (r << 16) + (g << 8) + b;
	} // }}}

	@Override
	public String toCSSString()
	{ // {{{
		return this.value;
	} // }}}

	@Override
	public String getNodeLabel()
	{
		return "Color literal (" + value + ")";
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ColorLiteral that = (ColorLiteral) o;

		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(value);
	}
}
