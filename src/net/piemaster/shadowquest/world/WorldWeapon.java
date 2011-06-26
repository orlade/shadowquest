package net.piemaster.shadowquest.world;

import java.util.ArrayList;

import net.piemaster.shadowquest.Item;
import net.piemaster.shadowquest.character.Bonus;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class WorldWeapon extends WorldItem
{
	/** The minimum damage dealt by the weapon */
	private double minDamage;
	/** The maximum damage dealt by the weapon */
	private double maxDamage;

	/**
	 * Create an new weapon in the world
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
	 * @param minDamage
	 *            The minimum damage dealt by the weapon
	 * @param maxDamage
	 *            The maximum damage dealt by the weapon
	 * @throws SlickException
	 */
	public WorldWeapon(double x, double y, String name, Image avatar, ArrayList<Bonus> bonuses,
			double minDamage, double maxDamage) throws SlickException
	{
		super(x, y, name, Item.Type.WEAPON, avatar, bonuses);
		this.minDamage = minDamage;
		this.maxDamage = maxDamage;
	}

	/**
	 * @return the minDamage
	 */
	public double getMinDamage()
	{
		return minDamage;
	}

	/**
	 * @return the maxDamage
	 */
	public double getMaxDamage()
	{
		return maxDamage;
	}
}
