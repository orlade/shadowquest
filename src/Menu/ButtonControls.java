package Menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import Game.MenuState;

public class ButtonControls extends Button
{
	/** Create a controls button
	 * 
	 * @param x The x coord
	 * @param y The y coord
	 * @param image The image to use
	 * @throws SlickException
	 */
	public ButtonControls(int x, int y) throws SlickException
	{
		super("start", x, y, new Image("assets/menu/btn-controls.png"),
							 new Image("assets/menu/btn-controls-light.png"));
	}

	@Override
    /** Display the game controls
     *  
     * @param gc The game container
     * @param sbg The state-based game
     */
	public void action(GameContainer gc, StateBasedGame sbg, MenuState ms)
	{
		if(ms.getCurrentBox() != ms.getControlsBox())
			ms.setCurrentBox(ms.getControlsBox());
		else
			ms.setCurrentBox(null);
		//System.out.println("CONTROLS: Arrow keys to move, Space to pick up, X for inventory Escape for menu");
		//System.out.println("In inventory: WASD to browse, Enter to equip/use, Backspace to drop");
		//System.out.println("Dev controls: N = noclip, B = tile blocking mask, E = exit zone masks");
	}
	
}
