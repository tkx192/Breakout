package programming.breakout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

import acm.graphics.*;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

public class Start extends GraphicsProgram implements KeyListener
{
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	double rectWidth = 70;
	double rectHeigth = 20;
	int boardMoveSpeed = 15;
	int ballSpeed = 5;
	int collision_i = 2;

	boolean ballStarted = false;

	GRect[][] bricks = new GRect[10][10];
	GRect board = new GRect(120, 20);
	GOval ball = new GOval(20, 20);

	private Timer timer = null;

	public void run()
	{
		this.setSize(((int) (rectWidth + 5) * 10) - 4, screenSize.height - 150);
		generateBricks();

		this.addKeyListeners();

		// Methode: generateBall() ?
		ball.setLocation((board.getX() + board.getWidth() / 2) - (ball.getWidth() / 2), board.getY() - board.getHeight());
		ball.setFillColor(Color.BLACK);
		ball.setFilled(true);
		ball.setColor(this.getBackground());
		add(ball);

		setLocations();
	}

	private void setLocations()
	{
		timer = new Timer(ballSpeed * 4, new ActionListener()

		{
			public void actionPerformed(ActionEvent e)
			{
				synchronized (this)
				{
					collision_i = collisionBall();

					switch (collision_i)
					{
					case 1:
						// Ball fliegt nach oben rechts
						ball.setLocation(ball.getX() + ballSpeed, ball.getY() - ballSpeed);
						break;
					case 2:
						// Ball fliegt nach oben links
						ball.setLocation(ball.getX() - ballSpeed, ball.getY() - ballSpeed);
						break;
					case 3:
						// Ball fliegt nach unten rechts
						ball.setLocation(ball.getX() + ballSpeed, ball.getY() + ballSpeed);
						break;
					case 4:
						// Ball fliegt nach unten links
						ball.setLocation(ball.getX() - ballSpeed, ball.getY() + ballSpeed);
						break;
					}

					// Rows
					for (int i = 0; i < bricks.length; i++)
					{
						// Columns
						for (int j = 0; j < bricks[i].length; j++)
						{
							if (!bricks[i][j].isVisible())
								bricks[i][j].setLocation(-99, -99);
						}
					}

				}
			}
		});
	}

	private int collisionBall()
	{
		if (ball.getY() > board.getY() + board.getHeight())
			timer.stop();

		// Wand links getroffen
		if ((ball.getX() - ballSpeed) <= 0)
		{
			// falls Y-
			if (collision_i == 2)
				return 1;
			// falls Y+
			else if (collision_i == 4)
				return 3;
		}

		// Wand rechts getroffen
		else if ((ball.getX() + ballSpeed) >= this.getWidth() - ball.getWidth())
		{
			// falls Y-
			if (collision_i == 1)
				return 2;
			// falls Y+
			else if (collision_i == 3)
				return 4;
		}

		// Board getroffen
		for (double i = board.getX(); i <= board.getX() + board.getWidth(); i++)
		{
			for (double j = board.getY(); j <= board.getY() + board.getHeight(); j++)
			{
				if ((ball.getY() - ballSpeed) >= j - board.getHeight() - ball.getHeight() / 2) // ca.
				{
					if (collision_i == 3)
					{
						if ((ball.getX() + ballSpeed) == i)
							return 1;
					}
					else if (collision_i == 4)
					{
						if ((ball.getX() - ballSpeed) == i)
							return 2;
					}
				}
			}
		}

		// Spielstein getroffen
		// Liste mit allen Spielsteinen durchlaufen
		// Rows
		for (int i = 0; i < bricks.length; i++)
		{
			// Columns
			for (int j = 0; j < bricks[i].length; j++)
			{
				// Breite des aktuellen Spielsteins Punkt für Punkt vergleichen
				for (double k = bricks[i][j].getX(); k <= bricks[i][j].getX() + bricks[i][j].getWidth(); k++)
				{
					// Befindet sich Ball auf X-Höhe eines Spielsteins?
					if (ball.getX() == k)
					{
						// Höhe des aktuellen Spielsteins Punkt für Punkt
						// vergleichen
						for (double l = bricks[i][j].getY(); l <= bricks[i][j].getY() + bricks[i][j].getHeight(); l++)
						{
							// Würde die nächste Ballbewegung eine Kollision
							// auslösen?
							if ((ball.getY() - ballSpeed) <= l) // PRÜFEN!
							{
								// bricks[i][j]=null;
								bricks[i][j].setVisible(false);
								// this.repaint();

								if (collision_i == 1)
									return 3;
								else if (collision_i == 2)
									return 4;
							}
						}
					}
				}
			}
		}

		// // Untere Grenze -> Spielaus
		// if ((ball.getY() + ballSpeed) >= (this.getHeight() + 1000))
		// {
		// if (collision_i == 3)
		// return 1;
		// else if (collision_i == 4)
		// return 2;
		// }

		return collision_i;
	}

	public void keyPressed(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
		case KeyEvent.VK_RIGHT:
			if (!collisionBoard(false))
			{
				board.setLocation(board.getX() + boardMoveSpeed, board.getY());
				if (!ballStarted)
					ball.setLocation(ball.getX() + boardMoveSpeed, ball.getY());
			}
			break;
		case KeyEvent.VK_LEFT:
			if (!collisionBoard(true))
			{
				board.setLocation(board.getX() - boardMoveSpeed, board.getY());
				if (!ballStarted)
					ball.setLocation(ball.getX() - boardMoveSpeed, ball.getY());
			}
			break;
		case KeyEvent.VK_SPACE:
			ballStarted = true;
			timer.start();
			break;
		}
	}

	private boolean collisionBoard(boolean directionLeft)
	{
		if (directionLeft)
		{
			if ((board.getX() - boardMoveSpeed) <= 0)
				return true;
		}
		else
		{
			if ((board.getX() + board.getWidth() + boardMoveSpeed) >= this.getWidth())
				return true;
		}

		return false;
	}

	private void generateBricks()
	{
		for (int i = 0; i < 10; i++)
		{
			int tmp = 25 * (i + 1);

			Color color;
			switch (i)
			{
			case 0:
			case 1:
				color = Color.RED;
				break;
			case 2:
			case 3:
				color = Color.ORANGE;
				break;
			case 4:
			case 5:
				color = Color.YELLOW;
				break;
			case 6:
			case 7:
				color = Color.GREEN;
				break;
			case 8:
			case 9:
				color = Color.CYAN;
				break;
			default:
				color = Color.BLACK;
				break;
			}

			for (int j = 0; j < 10; j++)
			{
				GRect rect = new GRect(70, 20);
				rect.setLocation((75 * j), tmp); // (x , y)
				rect.setFillColor(color);
				rect.setFilled(true);
				rect.setColor(this.getBackground());
				add(rect);
				bricks[i][j] = rect;

				if (i == 0 && j == 9)
				{
					// x0, y0, x1, y1
					GLine line = new GLine((75 * (j + 1)), tmp, (75 * (j + 1)), (11 * tmp));
					add(line);
				}
			}
		}

		// generateBall()?
		board.setLocation(((((int) (rectWidth + 5) * 10) - 4) / 2) - 60, screenSize.height - 250);
		board.setFillColor(Color.BLACK);
		board.setFilled(true);
		board.setColor(this.getBackground());
		add(board);

	}

}
