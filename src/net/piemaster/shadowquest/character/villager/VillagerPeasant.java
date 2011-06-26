/* 433-294 Object Oriented Software Development
 * Peasant (Villager NPC)
 * Author: Oliver Lade <orlade>
 */

package net.piemaster.shadowquest.character.villager;

import net.piemaster.shadowquest.character.Player;
import net.piemaster.shadowquest.character.Unit;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class VillagerPeasant extends Villager
{
	// /** The completion status of the peasant's quest */
	// private boolean questComplete = false;

	/**
	 * Create farmer
	 * 
	 * @param x
	 *            The peasant's x coordinate
	 * @param y
	 *            The peasant's y coordinate
	 * @param name
	 *            The peasant's name
	 * @throws SlickException
	 */
	public VillagerPeasant(double x, double y, String name) throws SlickException
	{
		super(x, y, name, new Image("assets/units/peasant.png"));
	}

	@Override
	/** The action to take when interacting with another Unit (usually the player)
	 * 
	 * @param target The target to interact with
	 */
	public void interact(Unit target)
	{
		// Player interacting with this Villager
		if (target instanceof Player)
		{
			// Give amulet quest
			if (target.getInventory().hasItem("Amulet of Vitality") == null)
			{
				setDialogue("Find the Amulet of Vitality, across the river to the west.");
			}
			// Give sword quest
			else if (target.getInventory().hasItem("Sword of Strength") == null)
			{
				setDialogue("Find the Sword of Strength - cross the river and back, on the east side.");
			}
			// Give tome quest
			else if (target.getInventory().hasItem("Tome of Agility") == null)
			{
				setDialogue("Find the Tome of Agility, in the Land of Shadows.");
			}
			// Out of quests
			else
			{
				setDialogue("You have found all the treasure I know of.");
			}
		}
		// NPC interacting with this Villager
		else
		{
			setDialogue("Sup, " + target.getName() + "!");
		}
	}

}
