package net.piemaster.shadowquest.inventory;

import java.util.ArrayList;

import net.piemaster.shadowquest.Item;
import net.piemaster.shadowquest.character.Bonus;
import net.piemaster.shadowquest.character.Unit;

import org.newdawn.slick.Image;

public class InventoryConsumable extends InventoryItem
{
	/**
	 * Create a new consumable item
	 * 
	 * @param name
	 *            The name of the item
	 * @param avatar
	 *            The graphical representation of the item
	 */
	public InventoryConsumable(String name, Image avatar, ArrayList<Bonus> bonuses)
	{
		super(name, Item.Type.CONSUMABLE, avatar, bonuses);
	}

	@Override
	/** The action that occurs when the item is used (consume).
	 * 
	 * @param unit The unit using the item
	 */
	public void use(Unit unit)
	{
		// Consume the item
		for (Bonus bonus : this.getBonuses())
		{
			// Apply the correct alterations
			if (bonus.getType() == Bonus.Type.HP)
			{
				unit.heal(bonus.getValue());
			}
			else if (bonus.getType() == Bonus.Type.MP)
			{
				unit.recharge(bonus.getValue());
			}
			else if (bonus.getType() == Bonus.Type.COOLDOWN)
			{
				unit.increaseCooldown(-(int) bonus.getValue());
			}
			else if (bonus.getType() == Bonus.Type.STRENGTH)
			{
				unit.increaseStrength((int) bonus.getValue());
			}
			// None of the above
			else
			{
				System.out.println("WARNING: Unhadled consumable bonus [from " + this.getName()
						+ "]: " + bonus.getType() + "!");
			}
		}
		unit.getInventory().getItems().remove(this);
		System.out.println(this.getName() + " consumed");
	}
}
