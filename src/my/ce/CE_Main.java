package my.ce;

import recube.cobalt.GameView;
import recube.cobalt.LogoState;
import recube.cobalt.State;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;

public class CE_Main extends Activity 
{
	GameView gameView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        
        gameView = new GameView(this, 320, 480);
        int logoes[] = {R.drawable.alchelogo};
        gameView.GetStateManager().AddState(new LogoState("logo", gameView, logoes, Color.WHITE, "menu"));
        gameView.GetStateManager().AddState(new CE_MenuState(gameView));
        gameView.GetStateManager().AddState(new CE_GameState(gameView));
        gameView.GetStateManager().AddState(new CE_OptionState(gameView));
        gameView.GetStateManager().IntroState("logo");
        
        layout.addView(gameView);
    }
    
    @Override
    public void onBackPressed()
    {
    	if(gameView == null) { this.finish(); return; }
    	State curState = gameView.GetStateManager().GetCurrentState();
    	if(curState == null || curState.GetName() == "menu" || curState.GetName() == "logo")
    		this.finish();
    	else if(curState.GetName() == "game")
    		((CE_GameState)curState).isfadeMode = false;
    	else if(curState.GetName() == "option")
    		((CE_OptionState)curState).isfadeMode = false;
    }
}