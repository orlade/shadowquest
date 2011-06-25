package Game;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeOutTransition;

import Character.Corpse;
import Character.Player;
import Character.Unit;
import Character.Monster.Monster;
import Character.Villager.Villager;
import World.ExitZone;
import World.Map;
import World.WorldItem;
import World.WorldObject;

public abstract class GameplayState extends BasicGameState
{
	/** The state's ID */
	private int ID;
	/** The game the state belongs to */
	private StateBasedGame parentGame;
    /** Gets data about the current input (keyboard state) */
	private static Input input;
	
	/** Whether to run the game in devmode */
    private final boolean devMode = true;
	
	/** X co-ordinate of the 'camera' */
	private static double cameraX;
	/** Y co-ordinate of the 'camera' */
	private static double cameraY;
	
	/** The map of the game world */
	private Map map;
    /** The game's Player object */
    private static Player player;
    /** The in-game GUI */
    private static GUI gui;
	
	/** The font used by the floating health bar */
	protected static Font labelFont = null;
	/** Determines whether item bars will be shown above items */
	protected boolean showItemBars = false;
	/** Determines whether the inventory panel should be rendered */
	private static boolean renderInvPanel = false;

	
    
    /** Create a GameplayState
     * 
     * @param ID The state's ID
     */
	public GameplayState(int ID)
	{
		this.ID = ID;
	}

	@Override
	/** Initialise the state
	 * 
     * @param gc The Slick game container object
     * @param sbg The Game object
	 */
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException
	{
		// Store the parent game for changing levels
		parentGame = sbg;
		// Set the static input handler
		input = gc.getInput();
		
        // Store the entities
        player = new Player();
        gui = new GUI(player);
        
        // Initialise the camera on the player's position
        cameraX = player.getX() - RPG.SCREENWIDTH/2;
        cameraY = player.getY() - RPG.SCREENHEIGHT/2;
        
        // Create the DejaVu font
        labelFont = FontLoader.loadFont("assets/DejaVuSans-Bold.ttf", 12);
        
        // Load the font -- ISN'T RETAINED
        //gc.getGraphics().setFont(labelFont);
	}

	@Override
    /** Update the game state for a frame.
     * @param gc The Slick game container object.
     * @param sbg The Game object.
     * @param delta Time passed since last frame (milliseconds).
     */
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
	throws SlickException
	{   // Update the player's movement direction based on keyboard presses.
        int dirX = 0;
        int dirY = 0;
        if (getInput().isKeyDown(Input.KEY_DOWN))
        	dirY += 1;
        if (getInput().isKeyDown(Input.KEY_UP))
        	dirY -= 1;
        if (getInput().isKeyDown(Input.KEY_LEFT))
        	dirX -= 1;
        if (getInput().isKeyDown(Input.KEY_RIGHT))
        	dirX += 1;
        
        // Use ESC to jump back to menu
        if (getInput().isKeyPressed(Input.KEY_ESCAPE))
        {
        	// Save the current state to return to when closing menu
        	((MenuState)sbg.getState(RPG.MENUSTATE)).setLastState(this);
        	// Jump to the menu
        	sbg.enterState(RPG.MENUSTATE);
        }
        
        if(isDevMode())
        {
	        // Toggle tile block mask rendering
	        if (getInput().isKeyPressed(Input.KEY_B))
	        	getMap().toggleRenderBlockMask();
	        
	        // Toggle tile exit mask rendering
	        if (getInput().isKeyPressed(Input.KEY_E))
	        	getMap().toggleRenderExitMask();
	        
	        // Toggle noclip
	        if(getInput().isKeyPressed(Input.KEY_N))
	    		getPlayer().toggleNoclip();
	        
	        // Toggle god mode
	        if(getInput().isKeyPressed(Input.KEY_G))
	    		getPlayer().toggleGod();
	        
	        /*
	        // Jump to/from dungoen
	        if(getInput().isKeyPressed(Input.KEY_J))
	        {
	        	switch(sbg.getCurrentStateID())
	        	{
	        	case RPG.GPSMAIN: sbg.enterState(RPG.GPSDUNGEON, null, new FadeInTransition(Color.black)); break;
	        	case RPG.GPSDUNGEON: sbg.enterState(RPG.GPSMAIN, null, new FadeInTransition(Color.black)); break;
	        	}
	        }
	        // Reset the current level
	        if(getInput().isKeyPressed(Input.KEY_R))
	        {
	        	// Clear the existing entities
	        	getMonsters().clear();
	        	getCorpses().clear();
	        	getVillagers().clear();
	        	getWorldItems().clear();
	        	
	        	sbg.getCurrentState().init(gc, sbg);
	        	System.out.println("Resetting level...");
	        }
	        */
        }

        // Toggle inventory panel rendering
        if (getInput().isKeyPressed(Input.KEY_X))
        	toggleRenderInvPanel();
        
    	if( getInput().isKeyPressed(Input.KEY_SPACE) &&
    		getPlayer().getPickupCandidate() != null )
    	{
    		getPlayer().pickupItem(player.getPickupCandidate(), this);
    	}
        
        if(isRenderInvPanel())
        {
        	if(getInput().isKeyPressed(Input.KEY_ENTER))
        		getPlayer().selectInvItem(getGui());
        	if(getInput().isKeyPressed(Input.KEY_BACK))
        		getPlayer().dropInvItem(this);
        	
        	if(getInput().isKeyPressed(Input.KEY_W))
        		moveInvSelectIndex('u');
        	else if(getInput().isKeyDown(Input.KEY_W) && !getInput().isKeyDown(Input.KEY_S) &&
        			getGui().isInvScrollVerticalCool())
        		moveInvSelectIndex('u');
        	
        	if(getInput().isKeyPressed(Input.KEY_S))
        		moveInvSelectIndex('d');
        	else if(getInput().isKeyDown(Input.KEY_S) && !getInput().isKeyDown(Input.KEY_W) &&
        			getGui().isInvScrollVerticalCool())
        		moveInvSelectIndex('d');
        	
        	if(getInput().isKeyPressed(Input.KEY_A))
        		moveInvSelectIndex('l');
        	else if(getInput().isKeyDown(Input.KEY_A) && !getInput().isKeyDown(Input.KEY_D) &&
        			getGui().isInvScrollHorizCool())
        		moveInvSelectIndex('l');
        	
        	if(getInput().isKeyPressed(Input.KEY_D))
        		moveInvSelectIndex('r');
        	else if(getInput().isKeyDown(Input.KEY_D) && !getInput().isKeyDown(Input.KEY_A) &&
        			getGui().isInvScrollHorizCool())
        		moveInvSelectIndex('r');
        }
        else
        {
        	getInput().clearKeyPressedRecord();
        }
        
        // ALT to show item names
        if (getInput().isKeyDown(Input.KEY_LALT) || getInput().isKeyDown(Input.KEY_RALT))
        {
        	setShowItemBars(true);
        }
        else
        {
        	setShowItemBars(false);
        }
        
        // Update the contents of the world
		getPlayer().update(dirX, dirY, delta, this);

		// Update the position of the camera based on updated player position
		// If the inventory panel is open, squash the camera so the player is still centred
		if(isRenderInvPanel())
		{
			cameraX = this.getPlayer().getX() - (RPG.SCREENWIDTH - GUI.INVPANELWIDTH) / 2;
		}
		else
		{
			cameraX = this.getPlayer().getX() - RPG.SCREENWIDTH / 2;
		}
		cameraY = this.getPlayer().getY() - RPG.SCREENHEIGHT / 2;

		// Lock the camera on the map if it's going off
		forceCameraOnMap();

		// Update each monster
		for(Monster m : getMonsters())
		{
			m.update(delta, this);
		}

		// Update the GUI
		getGui().update(delta);
	}
	
	@Override
    /** Render the entire screen, so it reflects the current game state.
     * @param gc The Slick game container object.
     * @param g The Slick graphics object, used for drawing.
     */
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException
	{
		// Font for drawing
		gc.getGraphics().setFont(this.getLabelFont());
		
		// First, render the map
		getMap().render(g, getCameraX(), getCameraY());

		// Translate the scene to allow drawing at real co-ordinates
		g.translate((float) -this.getCameraX(), (float) -this.getCameraY());

		// Render each corpse
		for(Corpse c : getCorpses())
		{
			if(isOnCamera(c))
			{
				c.render(g);
			}
		}

		// Render each world item
		for(WorldItem w : getWorldItems())
		{
			if(isOnCamera(w))
			{
				w.render(g);
			}
		}
		
		// Render each villager
		for(Villager v : getVillagers())
		{
			if(isOnCamera(v))
			{
				v.render(g);
			}
		}

		// Render each villager's health and dialogue  (if necessary)
		for(Villager v : getVillagers())
		{
			if(isOnCamera(v))
			{
				v.renderSecondary(g);
			}
		}

		// Render each monster
		for(Monster m : getMonsters())
		{
			if(isOnCamera(m))
			{
				m.render(g);
			}
		}

		// Render the Player
		getPlayer().render(g);

		// Render each world item bar (if necessary)
		for(WorldItem w : getWorldItems())
		{
			if(isOnCamera(w))
			{
				// Requires the world as a facade for the player
				w.renderSecondary(g, this);
			}
		}

		// Render each monster's health bar
		for(Monster m : getMonsters())
		{
			if(isOnCamera(m))
			{
				m.renderSecondary(g);
			}
		}

		// Untranslate the graphics before rendering HUD elements
		g.translate((float) this.getCameraX(), (float) this.getCameraY());
		
		// Render the Player's status panel
		getGui().renderStatusPanel(g);
		if(isRenderInvPanel())
			getGui().renderInventoryPanel(g);
	}
	
	/** Locks the camera position on the map if it is off */
	// If the camera is at the right or left edge, lock it to prevent a black bar
	public void forceCameraOnMap()
	{
		if(getCameraX() < 0)
			cameraX = 0;
		if(getCameraX() + RPG.SCREENWIDTH > this.getMap().getMapWidth())
			cameraX = this.getMap().getMapWidth() - RPG.SCREENWIDTH;
	
		// If the camera is at the top or bottom edge, lock it to prevent a black bar 
		if(getCameraY() < 0)
			cameraY = 0; 
		if(getCameraY() + RPG.SCREENHEIGHT > this.getMap().getMapHeight())
			cameraY = this.getMap().getMapHeight() - RPG.SCREENHEIGHT; 
	}

	/** 'Fall-through' method for rendering, return whether an object should be rendered.
	 * 
	 * @param obj The WorldObject to test (Unit or WorldItem) 
	 * @return whether the object is on camera (and should be rendered)
	 */
	public boolean isOnCamera(WorldObject obj)
	{

		if( obj.getX() + obj.getAvatar().getWidth()/2 > getCameraX() &&
				obj.getX() - obj.getAvatar().getWidth()/2 < getCameraX() + RPG.SCREENWIDTH &&
				obj.getY() + obj.getAvatar().getHeight()/2 > getCameraY() &&
				obj.getY() - obj.getAvatar().getHeight()/2 < getCameraY() + RPG.SCREENHEIGHT )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/** Switches the GPS to a new level, transferring the player object to the map 'entrance'
	 * 
	 * @param levelId The ID of the new level
	 * @param player The player object for persistence
	 */
	public void changeLevel(int levelId)
	{
		// Enter the new state
		this.getParentGame().enterState(levelId,
				new FadeOutTransition(Color.black),
				new RefocusFadeInTransition(Color.black));
	}
	
	/** Switches the GPS to a new level, transferring the player object to the map 'entrance'
	 * 
	 * @param levelId The ID of the new level
	 * @param player The player object for persistence
	 */
	public void changeLevel(ExitZone z)
	{
		System.out.println("SETTING LASTEXIT = "+z);
		// Enter the new state
		this.getParentGame().enterState(z.getTargetId(),
				new FadeOutTransition(Color.black),
				new RefocusFadeInTransition(Color.black, z));
	}
	
	/** Updates the target state to set up the world correctly given the player's expected position
	 * 
	 * @param fs The first state, i.e. the state being switch INTO
	 * @param ss The second state, i.e. the state being switch OUT FROM
	 * @param z The exit zone being jumped to, if any
	 */
	public void changeLevelRefocus(GameplayState fs, GameplayState ss, ExitZone z)
	{
		System.out.println("ENTERING FLOOR "+(getID()-1));
		fs.setPlayer(this.getPlayer());
		// If the player hasn't been through an exit, use the passed one
		if(getPlayer().getLastExit() == null && z != null)
		{
			getPlayer().setLastExit(z);
		}
		
		// If going UP, set the player's position to that of the last exit he used
		if(fs.getID() < ss.getID())
		{
			// If no exit passed (probably died in a dungeon) reset to town
			if(fs.getPlayer().getLastExit() == null)
			{
				// Town coords (unmagic later)
				fs.getPlayer().setX(900);
				fs.getPlayer().setY(590);
				fs.getPlayer().init();
			}
			else
			{
				fs.getPlayer().setX(fs.getMap().getExit().getX() + fs.getMap().getTileWidth()/2);
				fs.getPlayer().setY(fs.getMap().getExit().getY() + fs.getMap().getTileHeight()/2);
			}
		}
		// If going DOWN, figure out the spawn point from the entrance values
		else
		{
			// Try to start the player at the entrance position
			fs.getPlayer().setX( fs.getMap().getEntrance().getX() + fs.getMap().getTileWidth()/2 );
			fs.getPlayer().setY( fs.getMap().getEntrance().getY() +	fs.getMap().getTileHeight()/2 );
			System.out.println("FADEIN: Player set to ("+fs.getPlayer().getX()+", "+fs.getPlayer().getY()+")");
			System.out.println("FADEIN: NOT ("+fs.getMap().getEntrance().getX()+", "+fs.getMap().getEntrance().getX()+") ["+
					fs.getMap().isBoxBlocked(fs.getPlayer().getX(), fs.getPlayer().getY(),
							fs.getPlayer().getPhysWidth(), fs.getPlayer().getPhysHeight())+"]");
			
			// If the entrance tile is blocked (probably because it's undefined, defaulting to (0,0)), find a new one
			if(fs.getMap().isBoxBlocked(fs.getPlayer().getX(), fs.getPlayer().getY(),
					fs.getPlayer().getPhysWidth(), fs.getPlayer().getPhysHeight()))
			{
				System.out.println("ENTRANCE APPARENTLY BLOCKED");
				// Start the player in the centre of a non-blocked tile
				do
				{
					fs.getPlayer().setX((int)(Math.random() * (fs.getMap().getMapWidthTiles()-2) + 1) * 
							fs.getMap().getTileWidth() - fs.getMap().getTileWidth()/2);
					fs.getPlayer().setY((int)(Math.random() * (fs.getMap().getMapHeightTiles()-2) + 1) * 
							fs.getMap().getTileHeight() - fs.getMap().getTileHeight()/2);
				}
				while(fs.getMap().isBoxBlocked(fs.getPlayer().getX(), fs.getPlayer().getY(),
						fs.getPlayer().getPhysWidth(), fs.getPlayer().getPhysHeight()));
			}
		}
		
		GameplayState.setCameraX(fs.getPlayer().getX() - RPG.SCREENWIDTH/2);
        GameplayState.setCameraY(fs.getPlayer().getY() - RPG.SCREENHEIGHT/2);
        fs.forceCameraOnMap();
        
        // Set the exit just passed through as the player's last
		getPlayer().setLastExit(z);
	}

	/** Get the distance between two units */
	public static double distanceTo(WorldObject from, WorldObject to)
	{
		// Find the distance from the player
		double distX = to.getX() - from.getX();
		double distY = to.getY() - from.getY();
		return Math.sqrt(distX*distX + distY*distY);
	}
	
	/** Tests whether a unit would be colliding with any other unit at given position
	 * 
	 * @param unit The unit to test
	 * @param x The x coord to check
	 * @param y The y coord to check
	 * @return Whether the unit is colliding with another unit at given position
	 */
	public boolean isUnitBlocked(Unit unit, double x, double y)
	{
		// Get values of testing unit edges
		double left = x - unit.getPhysWidth()/2;
		double right = x + unit.getPhysWidth()/2;
		double top = y - unit.getPhysHeight()/2;
		double bottom = y + unit.getPhysHeight()/2;
		
		// Build all game units into a list for checking
		ArrayList<Unit> allUnits = new ArrayList<Unit>(getMonsters().size() + getVillagers().size());
		allUnits.add(getPlayer());
		allUnits.addAll(getMonsters());
		allUnits.addAll(getVillagers());
		
		// Check each unit
		for(Unit u : allUnits)
		{
			// Return false if any axes are not colliding (CST) or unit checking itself
			if( left >= u.getX() + u.getPhysWidth()/2 ||
				right <= u.getX() - u.getPhysWidth()/2 ||
				top >= u.getY() + u.getPhysHeight()/2 ||
				bottom <= u.getY() - u.getPhysHeight()/2 ||
				unit.equals(u) )
			{
				continue;
			}
			
			// Check possible collisions
			if( left < u.getX() + u.getPhysWidth()/2 &&
				right > u.getX() - u.getPhysWidth()/2 &&
				top < u.getY() + u.getPhysHeight()/2 &&
				bottom > u.getY() - u.getPhysHeight()/2 )
			{
				System.out.println(unit.getName()+" colliding with "+u.getName()+"!");
				return true;
			}
		}
		return false;
	}

	/** Toggle the inventory panel rendering flag. */
	public void toggleRenderInvPanel()
	{
		renderInvPanel = !renderInvPanel;
	}

	/** Toggle the inventory panel rendering flag.
	 * 
	 * @param dir A char representing the direction of movement (u, d, l, r)
	 */
	public void moveInvSelectIndex(char dir)
	{
		this.getGui().moveInvSelectIndex(dir, this.getPlayer().getInventory().getItems().size());
	}

	/** Creates a new WorldItem with the given values */
	public void spawnWorldItem(WorldItem item)
	{
		this.getWorldItems().add(item);
	}
	
	@Override
	/**
	 * @return the id
	 */
	public int getID()
	{
		return ID;
	}
	
	/**
	 * @return the parentGame
	 */
	public StateBasedGame getParentGame() {
		return parentGame;
	}

	/**
	 * @return the cameraX
	 */
	public double getCameraX() {
		return cameraX;
	}

	/**
	 * @return the cameraY
	 */
	public double getCameraY() {
		return cameraY;
	}

	/**
	 * @param cameraX the cameraX to set
	 */
	public static void setCameraX(double cameraX) {
		GameplayState.cameraX = cameraX;
	}

	/**
	 * @param cameraY the cameraY to set
	 */
	public static void setCameraY(double cameraY) {
		GameplayState.cameraY = cameraY;
	}

	/**
	 * @return the map
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(Map map) {
		this.map = map;
	}

	/**
	 * @return the labelFont
	 */
	public Font getLabelFont() {
		return labelFont;
	}

	/**
	 * @return the showItemBars
	 */
	public boolean isShowItemBars() {
		return showItemBars;
	}

	/**
	 * @return the renderInvPanel
	 */
	public boolean isRenderInvPanel() {
		return renderInvPanel;
	}

	/**
	 * @return the devMode
	 */
	public boolean isDevMode() {
		return devMode;
	}

	/**
	 * @param showItemBars the showItemBars to set
	 */
	public void setShowItemBars(boolean showItemBars) {
		this.showItemBars = showItemBars;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/** Set the player, for moving between states
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		GameplayState.player = player;
	}

	/**
	 * @return the gui
	 */
	public GUI getGui() {
		return gui;
	}

	/**
	 * @return the input
	 */
	public static Input getInput() {
		return input;
	}

	/**
	 * @return the monsters
	 */
	public abstract ArrayList<Monster> getMonsters();
	
	/**
	 * @return the villagers
	 */
	public abstract ArrayList<Villager> getVillagers();

	/**
	 * @return the corpses
	 */
	public abstract ArrayList<Corpse> getCorpses();

	/**
	 * @return the worldItems
	 */
	public abstract ArrayList<WorldItem> getWorldItems();
}
