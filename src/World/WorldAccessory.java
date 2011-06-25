package World;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import Character.Bonus;
import Game.Item;

public class WorldAccessory extends WorldItem
{
    /** Create an new accessory in the world
     * 
     * @param x The x coordinates of the starting position
     * @param y The y coordinates of the starting position
     * @param name The item's name
     * @param avatar The Image used to represent the item
     * @param bonuses The list of bonuses to be applied to the item
     * @throws SlickException
     */
	public WorldAccessory(double x, double y, String name, Image avatar, ArrayList<Bonus> bonuses)
	throws SlickException
	{
		super(x, y, name, Item.Type.ACCESSORY, avatar, bonuses);
	}
}
