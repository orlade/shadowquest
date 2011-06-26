package net.piemaster.shadowquest.world;

import java.util.ArrayList;

import net.piemaster.shadowquest.character.Bonus;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class WorldArmour extends WorldItem
{
	/** The armour rating of the piece of armour */
	private int armourRating;

	/**
	 * Create an new piece of armour in the world
	 * 
	 * @param x
	 *            The x coordinates of the starting position
	 * @param y
	 *            The y coordinates of the starting position
	 * @param name
	 *            The item's name
	 * @param avatar
	 *            The Image used to represent the item
	 * @param bonuses
	 *            The list of bonuses to be applied to the item
	 * @param armourRating
	 *            The armour rating of the armour
	 * @throws SlickException
	 */
	public WorldArmour(double x, double y, String name, Type type, Image avatar,
			ArrayList<Bonus> bonuses, int armourRating) throws SlickException
	{
		super(x, y, name, type, avatar, bonuses);
		this.armourRating = armourRating;
	}

	/**
	 * @return the armourRating
	 */
	public int getArmourRating()
	{
		return armourRating;
	}
}
