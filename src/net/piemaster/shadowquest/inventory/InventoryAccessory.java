package net.piemaster.shadowquest.inventory;

import java.util.ArrayList;

import net.piemaster.shadowquest.Item;
import net.piemaster.shadowquest.character.Bonus;
import net.piemaster.shadowquest.character.Unit;

import org.newdawn.slick.Image;

public class InventoryAccessory extends InventoryItem
{

	/**
	 * Create a new accessory
	 * 
	 * @param name
	 *            The name of the accessory
	 * @param avatar
	 *            The graphical representation of the accessory
	 */
	public InventoryAccessory(String name, Image avatar, ArrayList<Bonus> bonuses)
	{
		super(name, Item.Type.ACCESSORY, avatar, bonuses);
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
}
