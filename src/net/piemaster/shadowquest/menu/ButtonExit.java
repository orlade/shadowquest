package net.piemaster.shadowquest.menu;

import net.piemaster.shadowquest.MenuState;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ButtonExit extends Button
{
	public ButtonExit(int x, int y) throws SlickException
	{
		super("exit", x, y, new Image("assets/menu/btn-exit.png"), new Image(
				"assets/menu/btn-exit-light.png"));
	}

	@Override
	/** Exit the game
	 * 
	 * @param gc The game container
	 * @param sbg The state-based game
	 */
	public void action(GameContainer gc, StateBasedGame sbg, MenuState ms)
	{
		gc.exit();
	}

}
