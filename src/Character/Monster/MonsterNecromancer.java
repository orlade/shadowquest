/* 433-294 Object Oriented Software Development
 * Necromancer (Monster)
 * Author: Oliver Lade <orlade>
 */

package Character.Monster;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class MonsterNecromancer extends Monster
{	
	/** Create a necromancer
	 * 
	 * @param x The monster's x coordinate
	 * @param y The monster's y coordinate
	 * @param level The monster's level
	 * @throws SlickException
	 */
	public MonsterNecromancer(double x, double y, int level)
	throws SlickException
	{
		// Set the fundamental monster data
		super(x, y, level);
		this.name = "Necromancer";
		
		// Set up the monster's avatar
		this.avatar = new Image("assets/units/necromancer.png");
		
		// Set the monster's stats
		this.maxHealth = 300;
		this.health = maxHealth;
		this.strength = 20;
		this.defence = 20;
		this.cooldown = 400;
	}
	

	/** Create a named necromancer
	 * 
	 * @param x The monster's x coordinate
	 * @param y The monster's y coordinate
	 * @param name The monster's name 
	 * @param level The monster's level
	 * @throws SlickException
	 */
	public MonsterNecromancer(double x, double y, String name, int level)
	throws SlickException
	{
		// Set the fundamental monster data
		super(x, y, level);
		this.name = name;
		
		// Set up the monster's avatar
		this.avatar = new Image("assets/units/necromancer.png");
		
		// Set the monster's stats
		this.maxHealth = 140;
		this.health = maxHealth;
		this.strength = 20;
		this.defence = 20;
		this.cooldown = 400;
	}
}
