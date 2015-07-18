package recube.cobalt;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

class RunThread extends Thread
{
	SurfaceHolder surfaceHolder;
	GameView gameView;
	Graphics graphics;
	boolean run;
	
	public StateManager GetStateManager()
	{
		return gameView.GetStateManager();
	}
	
	public RunThread(SurfaceHolder _surfaceHolder, GameView _gameView)
	{
		surfaceHolder = _surfaceHolder;
		gameView = _gameView;
		graphics = new Graphics();
		run = true;
	}
	
	public void setRun(boolean _run)
	{
		run = _run;
	}
	
	@Override
	public void run()
	{
		Canvas c = null;
		long last = System.currentTimeMillis();
		long now = last;
		float elapsed;
		int count = 0;
		long nujuk = 0;
		
		while(true)
		{		
			try
			{
				c = surfaceHolder.lockCanvas();
				graphics.setCanvas(c);
			
				synchronized(surfaceHolder)
				{
					now = System.currentTimeMillis();
					count++;
					nujuk += elapsed = (now - last) / 50.0f;
					if(nujuk * 50 >= 1000)
					{
						gameView.fps = count;
						count = 0;
						nujuk = 0;
					}
					GetStateManager().Update(elapsed);
					GetStateManager().Draw(graphics, elapsed);
					last = now;
				}
			}
			finally
			{
				if(c != null)
					surfaceHolder.unlockCanvasAndPost(c);
			}
		}
	}
}