package net.piemaster.shadowquest.character;

/* 433-294 Object Oriented Software Development
 * RPG Game Player
 * Author: Oliver Lade <orlade>
 */

import net.piemaster.shadowquest.GameplayState;

import org.newdawn.slick.Image;

public class Corpse extends Unit
{
	/**
	 * Create a new corpse at a given position with a given image
	 * 
	 * @param x
	 *            The x coord to create at
	 * @param y
	 *            The y coord to create at
	 * @param avatar
	 *            The image to represent the corpse
	 */
	public Corpse(double x, double y, Image avatar)
	{
		this.x = x;
		this.y = y;
		this.avatar = avatar;
	}

	@Override
	/** 'Kill' the corpse, removing it from the world
	 * 
	 * @param gps The GameplayState holding the list of corpses
	 */
	public void die(GameplayState gps)
	{
		gps.getCorpses().remove(this);
	}

	@Override
	public double getMaxDamage()
	{
		return 0;
	}

	@Override
	public double getMinDamage()
	{
		return 0;
	}

	@Override
	public void update(int delta, GameplayState gps)
	{
		return;
	}
}
