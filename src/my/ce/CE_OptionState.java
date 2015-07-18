package my.ce;

import android.graphics.Color;
import android.view.MotionEvent;
import recube.cobalt.Crash;
import recube.cobalt.FileProc;
import recube.cobalt.GameView;
import recube.cobalt.Graphics;
import recube.cobalt.State;

public class CE_OptionState extends State
{
	boolean isfadeMode;
	float alpha;
	
	boolean[] optionValue;
	static int[][] cursorCoordinate = {{116, 163}, {116, 218}};
	int cursor;
	
	FileProc optionFile;
	
	public CE_OptionState(GameView _gameView) 
	{
		super("option", _gameView);
		
		optionValue = new boolean[2];
		optionFile = new FileProc(GetGameView(), "option.txt");
	}

	@Override
	public void Initial() 
	{
		isfadeMode = true;
		alpha = 0;
		
		GetResourceManager().AddBitmap("bg", R.drawable.option_bg);
		GetResourceManager().AddBitmap("cursor", R.drawable.option_cursor);
		GetResourceManager().AddSound("select", R.raw.option_select);

		cursor = 0;

		try
		{
			optionFile.InitInput();
			optionValue[0] = optionFile.ReadBoolean();
			optionValue[1] = optionFile.ReadBoolean();
			optionFile.DestInput();
		}
		catch(Exception e)
		{
			
		}
	}

	@Override
	public void Update(float d) 
	{
		if(isfadeMode)
		{
			alpha += (20 * d);
			if(alpha >= 255)
				alpha = 255;
		}
		else
		{
			alpha -= (20 * d);
			if(alpha <= 0)
			{
				alpha = 0;
				GetGameView().GetStateManager().OutroState();
			}
		}
	}

	@Override
	public void Draw(Graphics g, float d) 
	{
		g.Clear(Color.WHITE);
		
		g.SetColor(Color.BLACK);
		g.SetAlpha(255);
		g.DrawImage(GetResourceManager().GetBitmap("bg"), 0, 0);
		
		for(int i = 0; i < 2; i++)
		{
			g.DrawImage(GetResourceManager().GetBitmap("cursor"),
					cursorCoordinate[i][(optionValue[i]) ? 1 : 0], 154 + i * 86);
		}
		
		if(alpha != 255)
		{
			g.SetColor(Color.WHITE);
			g.SetAlpha(255 - (int)alpha);
			g.SetFillMode(true);
			g.DrawRectangle(0, 0, 320, 480);
		}
	}

	@Override
	public void Destroy() 
	{
		GetResourceManager().ClearResources();
		
		optionFile.InitOutput();
		optionFile.WriteBoolean(optionValue[0]);
		optionFile.WriteBoolean(optionValue[1]);
		optionFile.DestOutput();
	}
	
	public void Touch(float x, float y, int pId, int event)
	{
		if(event == MotionEvent.ACTION_UP)
		{
			if(Crash.IsRectCrash(x, y, 0, 0, 130, 155, 164 - 130, 178 - 155))
			{
				optionValue[0] = false;
				PlaySelectSound();
			}
			if(Crash.IsRectCrash(x, y, 0, 0, 172, 155, 214 - 172, 178 - 155))
			{
				optionValue[0] = true;
				PlaySelectSound();
			}
			
			if(Crash.IsRectCrash(x, y, 0, 0, 130, 238, 222 - 130, 264 - 238))
			{
				optionValue[1] = false;
				PlaySelectSound();
			}
			if(Crash.IsRectCrash(x, y, 0, 0, 224, 238, 280 - 224, 264 - 238))
			{
				optionValue[1] = true;
				PlaySelectSound();
			}
			
			if(Crash.IsRectCrash(x, y, 0, 0, 0, 448, 320, 32))
			{
				isfadeMode = false;
				PlaySelectSound();
			}
		}
	}
	
	private void PlaySelectSound()
	{
		if(!optionValue[0])
		{
			GetResourceManager().PlaySound("select");
		}
	}
}