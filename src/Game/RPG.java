package Game;
/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Author: Matt Giuca <mgiuca>
 */

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;

/** Main class for the Role-Playing Game engine.
 * Handles initialisation, input and rendering.
 * Effectively acts as the GamePlayState, holding entities and providing a facade.
 */
public class RPG extends StateBasedGame
{
    /** Screen width (px) */
    public static final int SCREENWIDTH = 800;
    /** Screen height (px) */
    public static final int SCREENHEIGHT = 600;
    
    /** The ID of the menu state */
    public static final int MENUSTATE = 0;
    /** The ID of the main world level state */
    public static final int GPSMAIN = 1;
    /** The ID of the dungeon level state */
    public static final int GPSDUNGEON = 2;
    public static final int GPSDUNGEON2 = 3;
    public static final int GPSDUNGEON3 = 4;
    public static final int GPSDUNGEON4 = 5;
    public static final int GPSDUNGEON5 = 6;

    /** Create a new RPG object. 
     * @throws SlickException */
    public RPG() throws SlickException
    {
        super("Shadow Quest");
        
        this.addState(new MenuState(MENUSTATE));
        this.addState(new GameplayStateLevelMain(GPSMAIN));
        this.addState(new GameplayStateLevelDungeon(GPSDUNGEON));
        this.addState(new GameplayStateLevelDungeon(GPSDUNGEON2));
        this.addState(new GameplayStateLevelDungeon(GPSDUNGEON3));
        this.addState(new GameplayStateLevelDungeon(GPSDUNGEON4));
        this.addState(new GameplayStateLevelDungeon(GPSDUNGEON5));
        this.enterState(MENUSTATE, null, new FadeInTransition(Color.black));
    }

    /** Start-up method. Creates the game and runs it.
     * @param args Command-line arguments (ignored).
     */
    public static void main(String[] args)
    throws SlickException
    {
        AppGameContainer app = new AppGameContainer(new RPG());
        // setShowFPS(true), to show frames-per-second.
        app.setShowFPS(true);
        app.setTargetFrameRate(100);
        app.setDisplayMode(SCREENWIDTH, SCREENHEIGHT, false);
        app.start();
    }

	@Override
	/** Initialise the game states */
	public void initStatesList(GameContainer gc) throws SlickException
	{
        //this.getState(MAINMENUSTATE).init(gc, this);
        //this.getState(GPSMAIN).init(gc, this);
        //this.getState(GPSDUNGEON).init(gc, this);
	}
}
