package net.piemaster.shadowquest.inventory;

import java.util.ArrayList;

import net.piemaster.shadowquest.Item;
import net.piemaster.shadowquest.character.Bonus;
import net.piemaster.shadowquest.character.Unit;

import org.newdawn.slick.Image;

public class InventoryArmour extends InventoryItem
{
	/** The armour rating of the piece of armour */
	private int armourRating;

	/**
	 * Create a new piece of armour
	 * 
	 * @param name
	 *            The name of the armour
	 * @param avatar
	 *            The graphical representation of the armour
	 * @param armourRating
	 *            The armour rating of the armour
	 */
	public InventoryArmour(String name, Item.Type type, Image avatar, ArrayList<Bonus> bonuses,
			int armourRating)
	{
		super(name, type, avatar, bonuses);
		this.armourRating = armourRating;
	}

	@Override
	/** The action that occurs when the item is used.
	 * 
	 * @param unit The unit using the item
	 */
	public void use(Unit unit)
	{
		// If already equipped, unequip
		if (this.isEquipped())
		{
			unit.getEquipment().unequipItem(this);
			this.equipped = false;
		}
		// Otherwise, equip
		else
		{
			unit.getEquipment().equipItem(this);
			this.equipped = true;
		}
	}

	/**
	 * @return the armourRating
	 */
	public int getArmourRating()
	{
		return armourRating;
	}
}
