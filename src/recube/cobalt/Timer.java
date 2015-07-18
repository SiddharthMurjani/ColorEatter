package recube.cobalt;

public class Timer 
{
	int elapsed;
	int delay;
	
	public Timer(int _delay)
	{
		delay = _delay;
		Reset();
	}
	
	public void Reset()
	{
		elapsed = 0;
	}
	
	public void Update(float d)
	{
		elapsed += d * 50.0f;
	}
	
	public boolean CheckTimer()
	{
		return (delay <= elapsed);
	}
}
