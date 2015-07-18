package recube.cobalt;

public abstract class State 
{
	String name;
	GameView gameView;
	ResourceManager resMan;
	
	public State(String _name, GameView _gameView)
	{
		name = _name;
		gameView = _gameView;
		resMan = new ResourceManager(_gameView);
	}
	
	public String GetName() 
	{
		return name;
	}
	
	public GameView GetGameView()
	{
		return gameView;
	}
	
	public ResourceManager GetResourceManager()
	{
		return resMan;
	}
	
	public abstract void Initial();
	public abstract void Update(float d);
	public abstract void Draw(Graphics g, float d);
	public abstract void Destroy();
	
	public void Touch(float x, float y, int pointerId, int touchEvent)
	{
		
	}
	
	public void Key(int keyCode, int downState)
	{
		
	}
}