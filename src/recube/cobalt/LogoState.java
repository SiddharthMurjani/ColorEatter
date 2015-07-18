package recube.cobalt;

import android.graphics.Bitmap;

public class LogoState extends State
{
	int[] logoes;
	int logoCount = 0;
	int backcolor;
	float fadealpha = 0;
	boolean isfadeMode;
	int current = 0;
	String nextState;
	
	public LogoState(String name, GameView gameView, int[] _logoes, 
			int _backcolor, String _nextState)
	{
		super(name, gameView);
		logoes = _logoes;
		logoCount = logoes.length;
		backcolor = _backcolor;
		nextState = _nextState;
	}
	
	@Override
	public void Initial() 
	{
		for(int i = 0; i < logoes.length; i++)
			GetResourceManager().AddBitmap(String.valueOf(i), logoes[i]);
		isfadeMode = false;
		current = 0;
		fadealpha = 0;
	}

	@Override
	public void Update(float d) 
	{
		if(current == logoCount)
		{
			GetGameView().GetStateManager().OutroState();
			GetGameView().GetStateManager().IntroState(nextState);
		}
		else
		{
			if(!isfadeMode)
			{
				fadealpha += 10 * d;
				if(fadealpha > 255)
				{
					fadealpha = 255;
					isfadeMode = !isfadeMode;
				}
			}
			else
			{
				fadealpha -= 10 * d;
				if(fadealpha < 0)
				{
					fadealpha = 0;
					isfadeMode = !isfadeMode;
					current ++;
				}
			}
		}
	}

	@Override
	public void Draw(Graphics g, float d) 
	{
		g.Clear(backcolor);
		g.SetAlpha((int)fadealpha);
		Bitmap img = GetResourceManager().GetBitmap(String.valueOf(current));
		g.DrawImage(img, 
				GetGameView().GetFixedWidth() / 2 - img.getWidth() / 2,
				GetGameView().GetFixedHeight() / 2 - img.getHeight() / 2);
	}

	@Override
	public void Destroy() 
	{
		GetResourceManager().ClearResources();
	}
}