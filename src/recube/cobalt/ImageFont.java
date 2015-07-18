package recube.cobalt;

import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageFont
{
	class FontType
	{
		Bitmap image;
		char chr;
		
		public FontType(Bitmap bmp, char c)
		{
			image = bmp;
			chr = c;
		}
		
		public FontType(GameView gameView, int res, char c)
		{
			image = BitmapFactory.decodeResource(gameView.getResources(), res);
			chr = c;
		}
	}
	
	Vector<FontType> bitmap;
	int fwi = 0;
	int fhe = 0;
	
	public ImageFont(GameView gameView, int[] numfont, int[] toword, int[] hiword, int[] els)
	{
		Bitmap tmp = null;
		
		bitmap = new Vector<FontType>();
		
		if(numfont != null)
		{
			for(int i = 0; i <= 9; i++)
			{
				tmp = BitmapFactory.decodeResource(gameView.getResources(), numfont[i]);
				if(tmp == null) continue;
				bitmap.add(new FontType(tmp, (char)('0' + i)));
				if(fwi < tmp.getWidth())
					fwi = tmp.getWidth();
				if(fhe < tmp.getHeight())
					fhe = tmp.getHeight();
			}
		}
		if(toword != null)
		{
			for(int i = 0; i < 26; i++)
			{
				tmp = BitmapFactory.decodeResource(gameView.getResources(), toword[i]);
				if(tmp == null) continue;
				bitmap.add(new FontType(tmp, (char)('a' + i)));
				if(fwi < tmp.getWidth())
					fwi = tmp.getWidth();
				if(fhe < tmp.getHeight())
					fhe = tmp.getHeight();
			}
		}
		if(hiword != null)
		{
			for(int i = 0; i < 26; i++)
			{
				tmp = BitmapFactory.decodeResource(gameView.getResources(), hiword[i]);
				if(tmp == null) continue;
				bitmap.add(new FontType(tmp, (char)('A' + i)));
				if(fwi < tmp.getWidth())
					fwi = tmp.getWidth();
				if(fhe < tmp.getHeight())
					fhe = tmp.getHeight();
			}
		}
		if(els != null)
		{
			for(int i = 0; i < els.length; i+= 2)
				bitmap.add(new FontType(gameView, els[i], (char)els[i + 1]));
			
		}
	}
	
	private Bitmap GetFontImage(char c)
	{
		for(int i = 0; i < bitmap.size(); i++)
		{
			if(bitmap.get(i).chr == c)
				return bitmap.get(i).image;
		}
		
		return null;
	}
	
	public void DrawText(String text, int x, int y, Graphics g)
	{
		char[] ch = text.toCharArray();
		int xoff = x, yoff = y;
		
		for(int i = 0; i < ch.length; i++)
		{
			if(ch[i] == ' ')
			{
				xoff += fwi;
				continue;
			}
			
			if(ch[i] == '\n')
			{
				yoff += fhe;
				continue;
			}
			
			Bitmap b = GetFontImage(ch[i]);
			if(b == null)
				continue;

			g.DrawImage(b, xoff, yoff);
			xoff += b.getWidth();
		}
	}
}