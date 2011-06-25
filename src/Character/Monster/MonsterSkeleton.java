/* 433-294 Object Oriented Software Development
 * Skeleton (Monster)
 * Author: Oliver Lade <orlade>
 */

package Character.Monster;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class MonsterSkeleton extends Monster
{	
	/** Create a skeleton
	 * 
	 * @param x The monster's x coordinate
	 * @param y The monster's y coordinate
	 * @param level The monster's level
	 * @throws SlickException
	 */
	public MonsterSkeleton(double x, double y, int level)
	throws SlickException
	{
		// Set the fundamental monster data
		super(x, y, level);
		this.name = "Skeleton";
		
		// Set up the monster's avatar
		this.avatar = new Image("assets/units/skeleton.png");
		
		// Set the monster's stats
		this.maxHealth = 125;
		this.health = maxHealth;
		this.strength = 20;
		this.defence = 20;
		this.cooldown = 500;
	}
}
