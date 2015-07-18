package my.ce;

import java.io.IOException;

import recube.cobalt.Crash;
import recube.cobalt.FileProc;
import recube.cobalt.GameView;
import recube.cobalt.Graphics;
import recube.cobalt.State;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.MotionEvent;

public class CE_MenuState extends State 
{
	boolean isfadeMode;
	float alpha;
	String nextState = "";
	boolean isHelpMode;
	
	int helpPage;
	
	String[] page = {"help1", "help2", "help3", "help4"};
	
	FileProc file;
	int lastStage, lastScore;
	
	FileProc optionFile;
	boolean[] optionValue;
	
	public CE_MenuState(GameView _gameView) 
	{
		super("menu", _gameView);
		file = new FileProc(GetGameView(), "score.txt");

		optionValue = new boolean[2];
		optionFile = new FileProc(GetGameView(), "option.txt");
	}

	@Override
	public void Initial() 
	{
		GetResourceManager().AddBitmap("bg", R.drawable.menu_titlebg);
		GetResourceManager().AddBitmap("gs", R.drawable.menu_gamestart);
		GetResourceManager().AddBitmap("op", R.drawable.menu_option);
		GetResourceManager().AddBitmap("hp", R.drawable.menu_help);
		GetResourceManager().AddBitmap("hpb", R.drawable.menu_help_box);
		GetResourceManager().AddBitmap("help1", R.drawable.menu_help_num1);
		GetResourceManager().AddBitmap("help2", R.drawable.menu_help_num2);
		GetResourceManager().AddBitmap("help3", R.drawable.menu_help_num3);
		GetResourceManager().AddBitmap("help4", R.drawable.menu_help_num4);

		GetResourceManager().AddSound("select", R.raw.menu_select);

		int[] numFont = {R.drawable.game_num0, R.drawable.game_num1, R.drawable.game_num2, R.drawable.game_num3,
				R.drawable.game_num4, R.drawable.game_num5, R.drawable.game_num6, R.drawable.game_num7,
				R.drawable.game_num8, R.drawable.game_num9};
		GetResourceManager().AddImageFont("score", numFont, null, null, null);
		
		isfadeMode = true;
		isHelpMode = false;
		alpha = 0;
		
		if(file.InitInput() == false)
		{
			file.DestInput();
			file.InitOutput();
			file.WriteInteger(0);
			file.WriteInteger(0);
			file.DestOutput();
		}
		else
			file.DestInput();

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
		
		helpPage = 0;
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
				GetGameView().GetStateManager().IntroState(nextState);
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
		g.DrawImage(GetResourceManager().GetBitmap("gs"), 60, 240);
		g.DrawImage(GetResourceManager().GetBitmap("op"), 60, 300);
		g.DrawImage(GetResourceManager().GetBitmap("hp"), 60, 360);
		
		if(isHelpMode)
		{
			g.SetColor(Color.BLACK);
			g.SetAlpha(255);
			g.DrawImage((Bitmap)GetResourceManager().GetBitmap("hpb"), 10, 90);
			g.DrawImage((Bitmap)GetResourceManager().GetBitmap(page[helpPage]), 20, 120);
			if(helpPage == 2)
			{
				GetResourceManager().DrawImageFont("score", g, String.valueOf(lastStage), 20 + 132, 120 + 76);
				GetResourceManager().DrawImageFont("score", g, String.valueOf(lastScore), 20 + 132, 120 + 140);
			}
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
		isfadeMode = false;
		isHelpMode = false;
		alpha = 0;
		GetResourceManager().ClearResources();
	}
	
	@Override
	public void Touch(float x, float y, int pId, int event)
	{
		switch(event)
		{
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			{
				if(!isHelpMode)
				{
					if(Crash.IsRectCrash(60, 240, 200, 50, x, y, 0, 0))
					{
						nextState = "game";
						isfadeMode = false;
						PlaySelectSound();
					}
					if(Crash.IsRectCrash(60, 300, 200, 50, x, y, 0, 0))
					{
						nextState = "option";
						isfadeMode = false;	
						PlaySelectSound();
					}
					if(Crash.IsRectCrash(60, 360, 200, 50, x, y, 0, 0))
					{
						isHelpMode = true;
						file.InitInput();
						try {
							lastStage = file.ReadInteger();
							lastScore = file.ReadInteger();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
						file.DestInput();
						PlaySelectSound();
					}
				}
				else
				{
					if(Crash.IsRectCrash(10, 90, 150, 300, x, y, 0, 0))
					{
						helpPage--;
						if(helpPage < 0)
							helpPage = 3;
					}
					if(Crash.IsRectCrash(160, 90, 150, 300, x, y, 0, 0))
					{
						helpPage++;
						if(helpPage > 3)
							helpPage = 0;
					}
					if(Crash.IsRectCrash(10, 480 / 2 - 150 + 265, 300, 35, x, y, 0, 0))
					{
						isHelpMode = false;
						PlaySelectSound();
					}
				}
			}
			break;
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