package my.ce;

import java.io.IOException;
import java.util.Random;
import java.util.Stack;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Vibrator;
import android.view.MotionEvent;

import recube.cobalt.Crash;
import recube.cobalt.FileProc;
import recube.cobalt.GameView;
import recube.cobalt.Graphics;
import recube.cobalt.State;
import recube.cobalt.Timer;

public class CE_GameState extends State 
{
	Vibrator vib;
	int[][] board = new int[17][17];
	Random rand = null;
	int[][] color = {
						{Color.YELLOW, Color.WHITE, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.RED, Color.CYAN, Color.BLACK},
						{
							0xffffff66, 
							0xffffeeff,
							0xff6699ff,
							0xff99cc33,
							0xffff99ff,
							0xffcc3333,
							0xff99ccff,
							0xff000000,
						}
					};
	
	int count, time;
	
	int stageNum = 0, score;
	
	FileProc scoreFile;
	Timer timeTimer;
	
	boolean isClear;
	boolean isFail;
	boolean isfadeMode;
	float alpha;

	boolean[] optionValue;
	FileProc optionFile;

	public CE_GameState(GameView _gameView) 
	{
		super("game", _gameView);
		scoreFile = new FileProc(GetGameView(), "score.txt");
		rand = new Random();

		optionValue = new boolean[2];
		optionFile = new FileProc(GetGameView(), "option.txt");
		
        vib = (Vibrator)GetGameView().getContext().getSystemService(Context.VIBRATOR_SERVICE);
	}

	@Override
	public void Initial()
	{
		stageNum = 0;
		
		score = 0;
		
		DataInitialize();
		
		timeTimer = new Timer(1000);
		
		int[] numFont = {R.drawable.game_num0, R.drawable.game_num1, R.drawable.game_num2, R.drawable.game_num3,
				R.drawable.game_num4, R.drawable.game_num5, R.drawable.game_num6, R.drawable.game_num7,
				R.drawable.game_num8, R.drawable.game_num9};
		int[] etc = {R.drawable.game_char_slash, '/'};

		GetResourceManager().AddBitmap("bg", R.drawable.game_bg);
		GetResourceManager().AddImageFont("score", numFont, null, null, etc);
		GetResourceManager().AddBitmap("clear", R.drawable.game_clear);
		GetResourceManager().AddBitmap("over", R.drawable.game_over);
		GetResourceManager().AddBitmap("next", R.drawable.game_next_button);
		GetResourceManager().AddBitmap("title", R.drawable.game_title_button);
		GetResourceManager().AddSound("select", R.raw.color_select);

		isfadeMode = true;
		alpha = 0;

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
		
		if(!isClear && !isFail)
		{
			if(time > 0)
			{
				timeTimer.Update(d);
				if(timeTimer.CheckTimer())
				{
					time--;
					ScoreSub(1);
					if(time <= 0)
					{
						time = 0;
						isClear = false;
						isFail = true;
					}
					timeTimer.Reset();
				}
			}
			
			if(CheckBoardIsSameColor())
			{
				isClear = true;
			}
		}
	}

	@Override
	public void Draw(Graphics g, float d)
	{
		g.Clear(Color.BLACK);
		g.SetAlpha(255);
		
		g.DrawImage(GetResourceManager().GetBitmap("bg"), 0, 0);
		
		synchronized(board)
		{
			for(int i = 1; i < 16; i++)
			{
				for(int j = 1; j < 16; j++)
				{
					if(board[i][j] > 6)
						continue;
					g.SetColor(color[(optionValue[1]) ? 1 : 0][board[i][j]]);
					g.SetFillMode(true);
					g.DrawRectangle(10 + ((j - 1) * 20), 134 + ((i - 1) * 20), 20, 20);
				}
			}
		}

		for(int i = 0; i < 7; i++)
		{
			g.SetColor(color[(optionValue[1]) ? 1 : 0][i]);
			g.SetFillMode(true);
			g.DrawCircle(12 + (i * 45) + 12.5f, 444 + 12.5f, 12.5f);//25, 25);
		}

		GetResourceManager().DrawImageFont("score", g, String.valueOf(stageNum), 120, 16);
		GetResourceManager().DrawImageFont("score", g, String.valueOf(score), 82, 50);
		GetResourceManager().DrawImageFont("score", g, String.valueOf(count) + "/25", 30, 108);
		GetResourceManager().DrawImageFont("score", g, String.valueOf(time), 260, 108);
		
		if(isClear)
		{
			g.DrawImage(GetResourceManager().GetBitmap("clear"), 0, 165);
			g.DrawImage(GetResourceManager().GetBitmap("next"), 5, 270);
			g.DrawImage(GetResourceManager().GetBitmap("title"), 215, 270);
		}
		if(isFail)
		{
			g.DrawImage(GetResourceManager().GetBitmap("over"), 0, 170);
			g.DrawImage(GetResourceManager().GetBitmap("title"), 110, 270);
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
	}
	
	public void Touch(float x, float y, int pId, int event)
	{
		if(event == MotionEvent.ACTION_UP || event == MotionEvent.ACTION_CANCEL)
		{
			if(!isClear && !isFail)
			{
				for(int i = 0; i < 7; i++)
				{
					if(Crash.IsCircleCrash(x, y, 0, 
							12 + (i * 45) + 12.5f, 
							444 + 12.5f,
							17.5f))
					{
						synchronized(board)
						{
							SetBoardColor(i);
							vib.vibrate(50);
						}
					}
				}
			}
			else
			{
				if(isClear)
				{
					if(Crash.IsRectCrash(x, y, 0, 0, 
							5, 270, 100, 50))
					{
						DataInitialize();
						vib.vibrate(50);
					}
					else if(Crash.IsRectCrash(x, y, 0, 0,
							215, 270, 100, 50))
					{
						GameEnd();
						vib.vibrate(50);
					}
				}
				else if(isFail)
				{
					if(Crash.IsRectCrash(x, y, 0, 0, 
							110, 270, 100, 50))
					{
						GameEnd();
					}
				}
			}
		}
	}

	private void SetBoardColor(int color)
	{
		int lastColor = 0;
		
		synchronized (board) 
		{
			Stack<Point> stk = new Stack<Point>();
			stk.push(new Point(1, 1));
			lastColor = board[1][1];
			board[1][1] = color;
			
			if(lastColor == color) return;
			
			while(stk.size() != 0)
			{
				boolean notFound = true;
			
				for(int i = -1; i <= 1; i++)
				{
					if(!notFound) break;
					for(int j = -1; j <= 1; j++)
					{
						if((i == 0 && j == 0) || Math.abs(i) == Math.abs(j)) continue;
						if(board[stk.peek().x + i][stk.peek().y + j] == lastColor)
						{
							board[stk.peek().x + i][stk.peek().y + j] = color;
							stk.push(new Point(stk.peek().x + i, stk.peek().y + j));
							notFound = false;
							break;
						}
					}
				}
				
				if(notFound)
					stk.pop();
			}

			count++;
			if(count > 25)
				ScoreSub(5);
			
			PlaySelectSound();
		}
	}
	
	private void PlaySelectSound()
	{
		if(!optionValue[0])
		{
			GetResourceManager().PlaySound("select");
		}
	}
	
	private void DataInitialize()
	{
		for(int i = 0; i < 17; i++)
		{
			for(int j = 0; j < 17; j++)
				if(i == 0 || j == 0 || i == 16 || j == 16) board[i][j] = 9;
				else
				{
					int pattern = rand.nextInt(6);
					switch(pattern)
					{
					case 0: case 2: case 4: case 6:
						board[i][j] = rand.nextInt(7);
						break;
					case 1: case 3: case 5:
						while(true)
						{
							int pattern2 = rand.nextInt((stageNum >= 4) ? 5 : 4), color = 0;
							
							switch(pattern2)
							{
							case 0: color = board[i - 1][j]; break;
							case 1: color = board[i + 1][j]; break;
							case 2: color = board[i][j - 1]; break;
							case 3: color = board[i][j + 1]; break;
							case 4: color = 7; break;
							}
							
							if(color != 9) { board[i][j] = color; break; }
						}
						break;
					}
				}
		}
		
		if(board[0][0] == 7) board[0][0] = rand.nextInt(6);
		
		for(int i = 0; i < 17; i++)
		{
			for(int j = 0; j < 17; j++)
			{
				if(i != 0 && j != 0 && i != 16 && j != 16)
				{
					int count = 0;
					count += ((board[i][j - 1] == 7) ? 1 : 0);
					count += ((board[i][j + 1] == 7) ? 1 : 0);
					count += ((board[i - 1][j] == 7) ? 1 : 0);
					count += ((board[i + 1][j] == 7) ? 1 : 0);
					if(count >= 3)
					{
						board[i][j + 1] = rand.nextInt(6);
						board[i - 1][j] = rand.nextInt(6);
					}
				}
				else if(i == 0 && j != 0 && j != 16)
				{
					if(board[i][j - 1] == 7 && board[i][j + 1] == 7 && board[i + 1][j] == 7)
						board[i][j + 1] = rand.nextInt(6);
				}
				else if(i == 16 && j != 0 && j != 16)
				{
					if(board[i][j - 1] == 7 && board[i][j + 1] == 7 && board[i - 1][j] == 7)
						board[i][j + 1] = rand.nextInt(6);
				}
				else if(j == 0 && i != 0 && i != 16)
				{
					if(board[i][j + 1] == 7 && board[i - 1][j] == 7 && board[i + 1][j] == 7)
						board[i][j + 1] = rand.nextInt(6);
				}
				else if(j == 16 && i != 0 && i != 16)
				{
					if(board[i][j - 1] == 7 && board[i - 1][j] == 7 && board[i + 1][j] == 7)
						board[i][j + 1] = rand.nextInt(6);
				}
			}
		}
		
		count = 0;
		time = 90 - stageNum;
		
		score += 100;
		
		isClear = isFail = false;
		
		stageNum++;
	}
	
	private boolean CheckBoardIsSameColor()
	{
		int color = board[1][1];
		
		for(int i = 1; i < 16; i++)
		{
			for(int j = 1; j < 16; j++)
			{
				if(board[i][j] != color)
					if(board[i][j] != 7)
						return false;
			}
		}
		
		return true;
	}
	
	private void ScoreSum(int value)
	{
		score += value;
	}
	
	private void ScoreSub(int value)
	{
		score -= value;
		if(score < 0)
		{
			score = 0;
			isClear = false;
			isFail = true;
		}
	}
	
	private void GameEnd()
	{
		ScoreSum(stageNum * 10);

		int lastScore = 0;
		scoreFile.InitInput();
		try 
		{
			scoreFile.ReadInteger();
			lastScore = scoreFile.ReadInteger();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		scoreFile.DestInput();
		
		if(lastScore < score)
		{
			scoreFile.InitOutput();
			scoreFile.WriteInteger(stageNum);
			scoreFile.WriteInteger(score);
			scoreFile.DestOutput();
		}
		
		isfadeMode = false;
	}
}