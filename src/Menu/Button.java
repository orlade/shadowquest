package Menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import Game.MenuState;

public abstract class Button
{
	private String name;
	private int x;
	private int y;
	private Image image;
	private Image imageLight;
	private boolean moused = false;
	
	private final float minScale = 1;
	private final float maxScale = 1.05f;
	private float scale = minScale;
	private final float scaleStep = 0.0001f;
	
	/** Create a button
	 * 
	 * @param x The x coord
	 * @param y The y coord
	 * @param image The image representing the button
	 */
	public Button(int x, int y, Image image)
	{
		this.name = "";
		this.x = x;
		this.y = y;
		this.image = image;
	}
	
	/** Create a named button
	 * 
	 * @param name The name of the button
	 * @param x The x coord
	 * @param y The y coord
	 * @param image The image representing the button
	 */
	public Button(String name, int x, int y, Image image)
	{
		this.name = name;
		this.x = x;
		this.y = y;
		this.image = image;
	}
	
	/** Create a named button
	 * 
	 * @param name The name of the button
	 * @param x The x coord
	 * @param y The y coord
	 * @param image The image representing the button
	 */
	public Button(String name, int x, int y, Image image, Image imageLight)
	{
		this.name = name;
		this.x = x;
		this.y = y;
		this.image = image;
		this.imageLight = imageLight;
	}
	
	/** Checks whether the mouse is within the boundaries of the button
	 * 
	 * @param mouseX The mouse's X coord
	 * @param mouseY The mouse's Y coord
	 * @return Whether the button is hovered over
	 */
    public boolean isMouseOver(float mouseX, float mouseY)
    {
    	if( ( mouseX >= getX() && mouseX <= getX() + getImage().getWidth()) &&
    		( mouseY >= getY() && mouseY <= getY() + getImage().getHeight()) )
    		return true;
    	return false;
    }

    /** Update the button given mouse interaction
     * 
     * @param delta 
     * @param gc
     * @param sbg
     */
    public void update(int delta, GameContainer gc, StateBasedGame sbg, MenuState ms)
    {
    	if(isMouseOver(gc.getInput().getMouseX(), gc.getInput().getMouseY()))
    	{
    		// If clicked, call the action
			if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON))
    		{
    			action(gc, sbg, ms);
    		}
    		
    		if(scale < maxScale)
    			scale += scaleStep * delta;
    		else if(scale > maxScale)
    		{
    			scale = maxScale;
    		}
    		moused = true;
    	}
    	else
    	{
    		if(scale > minScale)
    			scale -= scaleStep * delta;
    		else if(scale < minScale)
    			scale = minScale;
    		moused = false;
    	}
    }
	
	/** Render the button at its position
	 * 
	 * @param g The graphics device to draw with
	 */
	public void render()
	{
		if(moused && getImageLight() != null)
			getImageLight().draw(getX(), getY(), scale);
		else
			getImage().draw(getX(), getY(), scale);
	}
    
    /** Execute the button's action
     * 
     * @param gc The game container
     * @param sbg The state-based game
     */
    public abstract void action(GameContainer gc, StateBasedGame sbg, MenuState ms);
    
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return the image
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * @return the imageLight
	 */
	public Image getImageLight() {
		return imageLight;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
