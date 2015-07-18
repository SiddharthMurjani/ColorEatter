package recube.cobalt;

import android.graphics.PointF;
import android.graphics.RectF;

public class Crash 
{
	private Crash() { }
	
	public static boolean IsRectCrash(RectF rect1, RectF rect2)
	{
		return IsRectCrash(rect1.left, rect1.top, rect1.right - rect1.left, rect1.bottom - rect1.top,
				rect2.left, rect2.top, rect2.right - rect2.left, rect2.bottom - rect2.top);
	}
	
	public static boolean IsRectCrash(float x, float y, float width, float height,
										float x2, float y2, float width2, float height2)
	{
		if(x <= x2 + width2 && x2 <= x + width && y <= y2 + height2 && y2 <= y + height)
			return true;
		else
			return false;
	}
	
	public static boolean IsCircleCrash(float x, float y, float r, float x2, float y2, float r2)
	{
		if(Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2)) <= r + r2)
			return true;
		else
			return false;
	}
	
	public static boolean IsIsometricTileCrash(float touchx, float touchy, 
			float tilex, float tiley, float tilewidth, float tileheight)
	{
		tilex += tilewidth / 2;
		tiley += tileheight / 2;
		
		float tx = 0, ty = 0, tx1 = tilewidth / 2, ty1 = tileheight / 2;
		float tilt = (ty1 - ty) / (tx1 - tx);

		PointF p1 = new PointF(tilex - tilewidth / 2, tiley);
		PointF p2 = new PointF(tilex, tiley - tileheight / 2);
		PointF p3 = new PointF(tilex + tilewidth / 2, tiley);
		PointF p4 = new PointF(tilex, tiley + tileheight / 2);

		float d1 = p1.y - (-tilt * p1.x);
		float d2 = p2.y - (tilt * p2.x);
		float d3 = p3.y - (-tilt * p3.x);
		float d4 = p4.y - (tilt * p4.x);

		float result1 = -tilt * touchx - touchy + d1;
		float result2 = tilt * touchx - touchy + d2;
		float result3 = -tilt * touchx - touchy + d3;
		float result4 = tilt * touchx - touchy + d4;

		if(result1 <= 0 && result2 <= 0 && result3 >= 0 && result4 >= 0)
			return true;
		else
			return false;
	}
}