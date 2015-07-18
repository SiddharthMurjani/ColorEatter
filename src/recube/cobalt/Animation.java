package recube.cobalt;

import java.util.Vector;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Animation
{
	Vector<Bitmap> images;
	Timer timer;
	int index = 0;
	boolean isplay;
	boolean isloop;
	
	public Animation(GameView gameView, int[] res, int delay)
	{
		for(int i = 0; i < res.length; i++)
		{
			images.add(BitmapFactory.decodeResource(
					gameView.getContext().getResources(), res[i]));
		}
		
		timer = new Timer(delay);
		isplay = false;
	}
	
	public void Update(float d)
	{
		if(isplay)
		{
			timer.Update(d);
			if(timer.CheckTimer())
			{
				index++;
				if(images.size() <= index)
				{
					if(!isloop)
						isplay = false;
					index = 0;
				}
			
				timer.Reset();
			}
		}
	}
	
	public Bitmap getBitmap()
	{
		return images.get(index);
	}
	
	public void Play(boolean _isloop)
	{
		isplay = true;
		isloop = _isloop;
	}
	
	public void Pause()
	{
		isplay = false;
	}
	
	public void Stop()
	{
		Pause();
		index = 0;
	}
}