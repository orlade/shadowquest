package net.piemaster.shadowquest.inventory;

import java.util.ArrayList;

import net.piemaster.shadowquest.Item;
import net.piemaster.shadowquest.character.Bonus;
import net.piemaster.shadowquest.character.Unit;

import org.newdawn.slick.Image;

public class InventoryItem extends Item
{
	/** Flag whether the item is currently equipped */
	protected boolean equipped = false;

	/**
	 * Create an inventory item object with optional bonuses
	 * 
	 * @param name
	 *            Name of the item
	 * @param type
	 *            Item.Type of the item
	 * @param avatar
	 *            Image representing the item
	 * @param bonuses
	 *            Bonuses to be applied to the item (null for none)
	 */
	public InventoryItem(String name, Type type, Image avatar, ArrayList<Bonus> bonuses)
	{
		super(name, type, avatar, bonuses);
	}

	/**
	 * The action that occurs when the item is used.
	 * 
	 * @param unit
	 *            The unit using the item
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
	 * @return whether the item is equipped
	 */
	public boolean isEquipped()
	{
		return equipped;
	}

	/**
	 * @param equipped
	 *            the equipped to set
	 */
	public void setEquipped(boolean equipped)
	{
		this.equipped = equipped;
	}
}
