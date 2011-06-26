/* 433-294 Object Oriented Software Development
 * Bandit (Monster)
 * Author: Oliver Lade <orlade>
 */

package net.piemaster.shadowquest.character.monster;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class MonsterBandit extends Monster
{
	/**
	 * Create a bandit
	 * 
	 * @param x
	 *            The monster's x coordinate
	 * @param y
	 *            The monster's y coordinate
	 * @param level
	 *            The monster's level
	 * @throws SlickException
	 */
	public MonsterBandit(double x, double y, int level) throws SlickException
	{
		// Set the fundamental monster data
		super(x, y, level);
		this.name = "Bandit";

		// Set up the monster's avatar
		this.avatar = new Image("assets/units/bandit.png");

		// Set the monster's stats
		this.maxHealth = 40;
		this.health = maxHealth;
		this.strength = 15;
		this.defence = 10;
		this.cooldown = 200;
	}
}
