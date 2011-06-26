/* 433-294 Object Oriented Software Development
 * Prince Aldric (Villager NPC)
 * Author: Oliver Lade <orlade>
 */

package net.piemaster.shadowquest.character.villager;

import net.piemaster.shadowquest.character.Player;
import net.piemaster.shadowquest.character.Unit;
import net.piemaster.shadowquest.inventory.InventoryQuest;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class VillagerPrince extends Villager
{
	/** The completion status of the Prince's quest */
	private boolean questComplete = false;

	/**
	 * Create Prince Aldric
	 * 
	 * @param x
	 *            Prince's x coordinate
	 * @param y
	 *            Prince's y coordinate
	 * @param name
	 *            The prince's name
	 * @throws SlickException
	 */
	public VillagerPrince(double x, double y, String name) throws SlickException
	{
		super(x, y, name, new Image("assets/units/prince.png"));
	}

	@Override
	/** The action to take when interacting with another Unit (usually the player)
	 * 
	 * @param world The game world, acting as a facade
	 * @param target The target to interact with
	 */
	public void interact(Unit target)
	{
		// Player interacting with this Villager
		if (target instanceof Player)
		{
			if (questComplete)
			{
				setDialogue("My father requested I deliver you this message: \"You're Winner!\"");
			}
			else
			{
				InventoryQuest elixir = (InventoryQuest) (target.getInventory()
						.hasItem("Elixir of Life"));
				// Player has the elixir
				if (!questComplete && elixir != null)
				{
					// Consume the elixir
					elixir.use(target, this);
					this.questComplete = true;
					setDialogue("The elixir! My father is cured! Thank you!");
				}
				else
				{
					setDialogue("Please seek out the Elixir of Life to cure the king.");
				}
			}
		}
		// NPC interacting with this Villager
		else
		{
			setDialogue("Sup, " + target.getName() + "!");
		}
	}
}
