package recube.cobalt;

import java.util.Vector;
import java.util.Stack;

public class StateManager 
{
	Vector<State> state;
	Stack<State> current;
	
	public StateManager()
	{
		state = new Vector<State>();
		current = new Stack<State>();
	}
	
	public void AddState(State _state)
	{
		state.add(_state);
	}
	
	public State GetCurrentState()
	{
		return current.peek();
	}
	
	public void IntroState(String name)
	{
		for(int i = 0; i < state.size(); i++)
			if(state.get(i).GetName() == name)
			{
				if(current.size() != 0)
					current.peek().Destroy();
				current.push(state.get(i));
				current.peek().Initial();
				break;
			}
	}
	
	public void OutroState()
	{
		if(current.size() == 0) return;
		current.peek().Destroy();
		current.pop();
		if(current.size() != 0)
			current.peek().Initial();
	}
	
	public void Update(float d)
	{
		if(current.size() == 0) return;
		current.peek().Update(d);
	}
	
	public void Draw(Graphics g, float d)
	{
		if(current.size() == 0) return;
		try
		{
			current.peek().Draw(g, d);
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void Touch(float x, float y, int pointerId, int touchEvent)
	{
		if(current.size() == 0) return;
		current.peek().Touch(x, y, pointerId, touchEvent);
	}
	
	public void Key(int keyCode, int downState)
	{
		if(current.size() == 0) return;
		current.peek().Key(keyCode, downState);
	}
	
	public State GetState(String name)
	{
		for(int i = 0; i < state.size(); i++)
			if(state.get(i).GetName() == name)
			{
				return state.get(i);
			}
		return null;
	}
}
