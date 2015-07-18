package recube.cobalt;

import android.content.Context;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
	RunThread runThread;
	StateManager stateManager;
	int width, height;
	float screenw, screenh;
	float fps = 0;
	
	public float GetFPS() { return fps; }
	
	public StateManager GetStateManager()
	{
		return stateManager;
	}
	
	public int GetFixedWidth()
	{
		return width;
	}
	
	public int GetFixedHeight()
	{
		return height;
	}
	
	public GameView(Context context, int _width, int _height) 
	{
		super(context);
		
		getHolder().addCallback(this);
		
		width = _width;
		height = _height;
		
		getHolder().setFixedSize(_width, _height);
		
		stateManager = new StateManager();
		runThread = new RunThread(getHolder(), this);
		
		((android.app.Activity)context).setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		setFocusable(true);
	}

	public void surfaceCreated(SurfaceHolder holder) 
	{
		screenw = (float)width / (float)getWidth();
		screenh = (float)height / (float)getHeight();
		
		runThread.setRun(true);
		runThread.start();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) 
	{

	}

	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		runThread.setRun(false);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		float x = 0, y = 0;
		
		switch(action)
		{
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			{
				x = event.getX() * screenw;
				y = event.getY() * screenh;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			{
				x = event.getX() * screenw;
				y = event.getY() * screenh;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
		case MotionEvent.ACTION_CANCEL:
			{
				x = event.getX() * screenw;
				y = event.getY() * screenh;
			}
			break;
		}
		
		GetStateManager().Touch(x, y, 0, action);
		
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keycode, KeyEvent event)
	{
		stateManager.Key(keycode, 0);
		return true;
	}
	
	@Override
	public boolean onKeyUp(int keycode, KeyEvent event)
	{
		stateManager.Key(keycode, 1);
		return true;
	}
}