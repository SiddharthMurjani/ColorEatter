package recube.cobalt;

import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class ResourceManager 
{
	private class Resource
	{
		String name;
		Object res;
		int restype;
		
		public String GetName() { return name; }
		public Object GetResource() { return res; }
		public int GetResourceType() { return restype; }
		
		public Resource(String _name, Object _res, int _restype)
		{
			name = _name;
			res = _res; restype = _restype;
		}
	}
	
	Vector<Resource> res;
	SoundPool soundPool;
	GameView gameView;
	
	public ResourceManager(GameView _gameView)
	{
		res = new Vector<Resource>();
		gameView = _gameView;
		soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	}
	
	public void AddBitmap(String name, int id)
	{
		res.add(new Resource(name, BitmapFactory.decodeResource(gameView.getResources(), id), 0));
	}
	
	public void AddSound(String name, int id)
	{
		res.add(new Resource(name, soundPool.load(gameView.getContext(), id, 1), 1));
	}
	
	public void AddAnimation(String name, int[] id, int delay)
	{
		res.add(new Resource(name, new Animation(gameView, id, delay), 2));
	}
	
	public void AddImageFont(String name, int[] numid, int[] soid, int[] daeid, int[] els)
	{
		res.add(new Resource(name, new ImageFont(gameView, numid, soid, daeid, els), 3));
	}
	
	public void AddMediaPlayer(String name, int id)
	{
		res.add(new Resource(name, MediaPlayer.create(gameView.getContext(), id), 4));
	}
	
	public void ClearResources()
	{
		while(res.size() != 0)
		{
			if(res.get(0).GetResourceType() == 1)
			{
				soundPool.unload((Integer)res.get(0).GetResource());
			}
			res.remove(0);
		}
	}
	
	private Object GetResource(String name)
	{
		for(int i = 0; i < res.size(); i++)
		{
			if(res.get(i).GetName() == name)
			{
				return res.get(i).GetResource();
			}
		}
		
		return null;
	}
	
	public Bitmap GetBitmap(String name)
	{
		return (Bitmap)GetResource(name);
	}
	
	public MediaPlayer GetMediaPlayer(String name)
	{
		return (MediaPlayer)GetResource(name);	
	}
	
	public void DrawImageFont(String name, Graphics g, String text, int x, int y)
	{
		((ImageFont)GetResource(name)).DrawText(text, x, y, g);
	}
	
	public void PlaySound(String name)
	{
		soundPool.play((Integer)GetResource(name), 1, 1, 1, 0, 1);
	}
	
	public void StopSound(String name)
	{
		soundPool.stop((Integer)GetResource(name));
	}
	
	public void PauseSound(String name)
	{
		soundPool.pause((Integer)GetResource(name));
	}
}