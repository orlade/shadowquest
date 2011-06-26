package net.piemaster.shadowquest.world;

import java.util.ArrayList;

import net.piemaster.shadowquest.Item;
import net.piemaster.shadowquest.character.Bonus;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class WorldQuest extends WorldItem
{
	/**
	 * Create an new quest item in the world
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
	 * @throws SlickException
	 */
	public WorldQuest(double x, double y, String name, Image avatar, ArrayList<Bonus> bonuses)
			throws SlickException
	{
		super(x, y, name, Item.Type.QUEST, avatar, bonuses);
	}
}
