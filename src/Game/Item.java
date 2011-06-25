package Game;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import Character.Bonus;


public abstract class Item
{
	/** Every different type of item */
	public static enum Type {WEAPON, SHIELD, ARMOUR, HELMET, BOOTS, GLOVES, ACCESSORY, CONSUMABLE, QUEST, GOLD}
	
	/** The item's name */
	protected String name;
	/** The type of the item (Weapon, Helmet, etc.) */
	protected Type type;
	/** The Image object representing the item in the world */
	protected Image avatar;
	/** A list of the bonuses applied to the item */
	protected ArrayList<Bonus> bonuses;
	
	/** Returns a random armour type for item drop rolls
	 * 
	 * @return a random integer between 1 and 5 inclusive
	 */
	public static Item.Type chooseRandomArmourType()
	{ 
		return Item.Type.values()[(int)(Math.random()*5 + 1)];
	}
	
	/** Create an item object with optional bonuses
	 *
	 * @param name Name of the item
	 * @param type Item.Type of the item
	 * @param avatar Image representing the item
	 * @param bonuses Bonuses to be applied to the item (null for none)
	 */
	public Item(String name, Type type, Image avatar, ArrayList<Bonus> bonuses)
	{
		this.name = name;
		this.type = type;
		this.avatar = avatar;
		
		// Add the bonuses
		this.bonuses = bonuses;
	}
	
	
	/** Draw the item at the given coordinates.
	 * 
	 * @param g The graphics device to draw with
	 * @param x The x coordinates to draw at
	 * @param y The y coordinates to draw at
	 */
	public void render(Graphics g, float x, float y)
	{
		this.avatar.draw(x, y);
	}
	
	/** Draw the item at the given coordinates and scale
	 * 
	 * @param g The graphics device to draw with
	 * @param x The x coordinates to draw at
	 * @param y The y coordinates to draw at
	 * @param scale The scale to draw the avatar with
	 */
	public void render(Graphics g, float x, float y, float scale)
	{
		this.avatar.draw(x, y, scale);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the avatar
	 */
	public Image getAvatar() {
		return avatar;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @return the bonuses
	 */
	public ArrayList<Bonus> getBonuses() {
		return bonuses;
	}
}
