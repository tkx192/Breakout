package programming.breakout;

import acm.graphics.GPoint;

public class Thread1 extends Thread
{
	GPoint ballPoint;
	int ballSpeed = 5;

	public Thread1(GPoint ballPoint_p)
	{
		ballPoint = ballPoint_p;
	}

	@Override
	public void run()
	{
		synchronized (this)
		{
			ballPoint = new GPoint(ballPoint.getX() - ballSpeed, ballPoint.getY() - ballSpeed);
			notify();
		}
	}
}
