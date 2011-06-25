package Menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import Game.MenuState;

public class ButtonAbout extends Button
{
	/** Create an about button
	 * 
	 * @param x The x coord
	 * @param y The y coord
	 * @param image The image to use
	 * @throws SlickException
	 */
	public ButtonAbout(int x, int y) throws SlickException
	{
		super("start", x, y, new Image("assets/menu/btn-about.png"),
				 			 new Image("assets/menu/btn-about-light.png"));
	}

	@Override
    /** Begin/resume the game
     * 
     * @param gc The game container
     * @param sbg The state-based game
     */
	public void action(GameContainer gc, StateBasedGame sbg, MenuState ms)
	{
		if(ms.getCurrentBox() != ms.getAboutBox())
			ms.setCurrentBox(ms.getAboutBox());
		else
			ms.setCurrentBox(null);
		//System.out.println("ABOUT: Brief stuff about how this is an RPG, and a bit of story");
		//System.out.println("FEATURES: XP and levels, random item drops, inventory, PG dungeons");
		//System.out.println("OTHER: unit blocking, damage calcs, commerce, armour");
	}
	
}
