package recube.cobalt;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Graphics 
{
	Canvas canvas;
	Paint paint;
	
	public void setCanvas(Canvas _canvas)
	{
		canvas = _canvas;
	}

	public Graphics()
	{
		canvas = null;
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		SetFillMode(false);
	}
	
	public void Clear(int color)
	{
		canvas.drawColor(color);
	}
	
	public void DrawImage(Bitmap image, int x, int y)
	{
		canvas.drawBitmap(image, x, y, paint);
	}
	
	public void DrawLine(int x, int y, int x1, int y1)
	{
		canvas.drawLine(x, y, x1, y1, paint);
	}
	
	public void DrawRectangle(float x, float y, float width, float height)
	{
		canvas.drawRect(x, y, x + width, y + height, paint);
	}
	
	public void DrawCircle(float x, float y, float r)
	{
		canvas.drawCircle(x, y, r, paint);
	}
	
	public void DrawString(String str, float x, float y)
	{
		canvas.drawText(str, x, y, paint);
	}
	
	public void SetAlpha(int alpha)
	{
		paint.setAlpha(alpha);
	}
	
	public int GetAlpha(int alpha)
	{
		return paint.getAlpha();
	}
	
	public void SetColor(int color)
	{
		paint.setColor(color);
	}
	
	public int GetColor()
	{
		return paint.getColor();
	}
	
	public void SetFillMode(boolean yes)
	{
		if(yes)
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
		else
			paint.setStyle(Paint.Style.STROKE);
	}
}