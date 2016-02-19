import java.awt.Color;

import acm.graphics.GRect;

public class Brick extends GRect
{
	Color color;
	static double width = 70;
	static double heigth = 20;

	public Brick()
	{
		super(width, heigth);
		super.setFillColor(color);
	}

	public void SetColor(Color c)
	{
		color = c;
	}

	// public Brick(int layer_i)
	// {
	// switch (layer_i)
	// {
	// case 0:
	// SetColor(Color.CYAN);
	// break;
	// case 1:
	// SetColor(Color.GREEN);
	// break;
	// case 2:
	// SetColor(Color.YELLOW);
	// break;
	// case 3:
	// SetColor(Color.ORANGE);
	// break;
	// case 4:
	// SetColor(Color.RED);
	// break;
	// }
	// }

}
