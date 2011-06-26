package net.piemaster.shadowquest.menu;

import net.piemaster.shadowquest.MenuState;
import net.piemaster.shadowquest.ShadowQuest;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class ButtonStart extends Button
{

	public ButtonStart(int x, int y) throws SlickException
	{
		super("start", x, y, new Image("assets/menu/btn-play.png"), new Image(
				"assets/menu/btn-play-light.png"));
	}

	@Override
	/** Begin/resume the game
	 * 
	 * @param gc The game container
	 * @param sbg The state-based game
	 */
	public void action(GameContainer gc, StateBasedGame sbg, MenuState ms)
	{
		sbg.enterState(ShadowQuest.GPSMAIN, new FadeOutTransition(Color.black), new FadeInTransition(
				Color.black));
	}

}
