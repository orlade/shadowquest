package net.piemaster.shadowquest.menu;

import net.piemaster.shadowquest.MenuState;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class ButtonOptions extends Button
{
	/**
	 * Create an options button
	 * 
	 * @param x
	 *            The x coord
	 * @param y
	 *            The y coord
	 * @param image
	 *            The image to use
	 * @throws SlickException
	 */
	public ButtonOptions(int x, int y, Image image) throws SlickException
	{
		super("exit", x, y, image);
	}

	@Override
	/** Exit the game
	 * 
	 * @param gc The game container
	 * @param sbg The state-based game
	 */
	public void action(GameContainer gc, StateBasedGame sbg, MenuState ms)
	{
		System.out.println("OPTIONS: Change screen size, dev mode");
	}

}
