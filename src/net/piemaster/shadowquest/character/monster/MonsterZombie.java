/* 433-294 Object Oriented Software Development
 * Zombie (Monster)
 * Author: Oliver Lade <orlade>
 */

package net.piemaster.shadowquest.character.monster;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class MonsterZombie extends Monster
{
	/**
	 * Create a zombie
	 * 
	 * @param x
	 *            The monster's x coordinate
	 * @param y
	 *            The monster's y coordinate
	 * @param level
	 *            The monster's level
	 * @throws SlickException
	 */
	public MonsterZombie(double x, double y, int level) throws SlickException
	{
		// Set the fundamental monster data
		super(x, y, level);
		this.name = "Zombie";

		// Set up the monster's avatar
		this.avatar = new Image("assets/units/zombie.png");

		// Set the monster's stats
		this.maxHealth = 60;
		this.health = maxHealth;
		this.strength = 10;
		this.defence = 5;
		this.cooldown = 800;
	}
}
