package net.piemaster.shadowquest;

import java.util.ArrayList;

import net.piemaster.shadowquest.menu.Button;
import net.piemaster.shadowquest.menu.ButtonAbout;
import net.piemaster.shadowquest.menu.ButtonControls;
import net.piemaster.shadowquest.menu.ButtonExit;
import net.piemaster.shadowquest.menu.ButtonStart;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class MenuState extends BasicGameState
{
	/** The state's ID */
	private int ID;

	private Image background;
	// private Image subtitle;
	private Image aboutBox;
	private Image controlsBox;

	private Image currentBox;

	private ArrayList<Button> buttons;

	private GameState lastState = null;

	/**
	 * Create a GameplayState
	 * 
	 * @param ID
	 *            The state's ID
	 * @throws SlickException
	 */
	public MenuState(int ID) throws SlickException
	{
		this.ID = ID;

		buttons = new ArrayList<Button>();
	}

	@Override
	/** Initialise the state
	 * 
	 * @param gc The Slick game container object
	 * @param sbg The Game object
	 */
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{
		// Load the menu background
		this.background = new Image("assets/menu/rock-bg.jpg");
		// Load the subtitle
		// this.subtitle = new Image("assets/menu/subtitle.png");
		// Load the info boxes
		this.aboutBox = new Image("assets/menu/box-about.png");
		this.controlsBox = new Image("assets/menu/box-controls.png");

		// Add the buttons to the list
		buttons.add(new ButtonStart(30, 230));

		buttons.add(new ButtonAbout(30, 380));
		buttons.add(new ButtonControls(30, 490));
		// buttons.add(new ButtonOptions(30, 520, new
		// Image("assets/menu/btn-options.png")));

		buttons.add(new ButtonExit(ShadowQuest.SCREENWIDTH - 200 - 50, ShadowQuest.SCREENHEIGHT - 71 - 50));
	}

	/**
	 * Update the menu
	 * 
	 * @param gc
	 *            The game container
	 * @param sbg
	 *            The state-based game
	 * @param delta
	 *            Number of ms since the last frame
	 */
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		// Use ESC to jump back to menu
		if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE) && getLastState() != null)
		{
			// Jump to the menu
			sbg.enterState(getLastState().getID());
		}

		// Update each button
		for (Button b : buttons)
		{
			b.update(delta, gc, sbg, this);
		}
	}

	/**
	 * Render the menu items
	 * 
	 * @param gc
	 *            The game container
	 * @param sbg
	 *            The state-based game
	 * @param gr
	 *            The graphics driver to draw with
	 */
	public void render(GameContainer gc, StateBasedGame sbg, Graphics gr) throws SlickException
	{
		// Draw the background
		background.draw(0, 0);
		// subtitle.draw(RPG.SCREENWIDTH - subtitle.getWidth() - 5, 5);
		if (getCurrentBox() != null)
		{
			getCurrentBox().draw(322, 198);
		}

		// Draw the buttons
		for (Button b : buttons)
		{
			b.render();
		}
	}

	/**
	 * @return the iD
	 */
	@Override
	public int getID()
	{
		return ID;
	}

	/**
	 * @return the lastState
	 */
	public GameState getLastState()
	{
		return lastState;
	}

	/**
	 * @param lastState
	 *            the lastState to set
	 */
	public void setLastState(GameState lastState)
	{
		this.lastState = lastState;
	}

	/**
	 * @return the aboutBox
	 */
	public Image getAboutBox()
	{
		return aboutBox;
	}

	/**
	 * @return the controlsBox
	 */
	public Image getControlsBox()
	{
		return controlsBox;
	}

	/**
	 * @return the currentBox
	 */
	public Image getCurrentBox()
	{
		return currentBox;
	}

	/**
	 * @param currentBox
	 *            the currentBox to set
	 */
	public void setCurrentBox(Image currentBox)
	{
		this.currentBox = currentBox;
	}
}
