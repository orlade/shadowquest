package net.piemaster.shadowquest.character;

import java.util.EnumMap;

import net.piemaster.shadowquest.Item;
import net.piemaster.shadowquest.inventory.InventoryItem;

public class Equipment
{
	/** The Unit who's Equipment this is */
	private Unit owner;
	/** The map that associates each equipped item with an EquipType enum value */
	private EnumMap<Item.Type, InventoryItem> items;
	/** Simple ArrayList of the equipped accessories */
	private InventoryItem[] accessories;

	/** Create a new set of equipment. */
	public Equipment(Unit owner)
	{
		this.owner = owner;
		// Create the EnumMap
		items = new EnumMap<Item.Type, InventoryItem>(Item.Type.class);
		accessories = new InventoryItem[4];
	}

	/**
	 * Equip an item from the unit's inventory
	 * 
	 * @param item
	 *            The item to be equipped
	 */
	public void equipItem(InventoryItem item)
	{
		if (item.getType() != Item.Type.ACCESSORY)
		{
			// Unequip existing item if necessary
			if (items.get(item.getType()) != null)
			{
				this.unequipItem(items.get(item.getType()));
			}

			// Equip
			items.put(item.getType(), item);
			// Apply the item bonuses to the unit
			if (!item.getBonuses().isEmpty())
			{
				for (Bonus bonus : item.getBonuses())
				{
					this.owner.adjustBonus(bonus.getType(), bonus.getValue());
				}
			}
			item.setEquipped(true);
			System.out.println(item.getName() + " equipped in slot " + item.getType());

		}
		else if (item.getType() == Item.Type.ACCESSORY)
		{
			for (int i = 0; i < accessories.length; i++)
			{
				if (accessories[i] == null)
				{
					// Equip
					accessories[i] = item;
					// Apply the item bonuses to the unit
					if (!item.getBonuses().isEmpty())
					{
						for (Bonus bonus : item.getBonuses())
						{
							this.owner.adjustBonus(bonus.getType(), bonus.getValue());
						}
					}
					System.out.println(item.getName() + " equipped in accessory slot " + i);
					return;
				}
			}
			System.out.println("Could not equip " + item.getName() + ": accessory slots full!");
		}
	}

	/**
	 * Remove an item from the set of equipped items
	 * 
	 * @param item
	 *            The item to be unequipped
	 */
	public void unequipItem(InventoryItem item)
	{
		if (item.getType() != Item.Type.ACCESSORY)
		{
			// Unequip
			items.remove(item.getType());
			// Remove the item bonuses from the unit
			if (!item.getBonuses().isEmpty())
			{
				for (Bonus bonus : item.getBonuses())
				{
					this.owner.adjustBonus(bonus.getType(), -bonus.getValue());
				}
			}
			item.setEquipped(false);
			System.out.println(item.getName() + " unequipped from slot " + item.getType());
		}
		else if (item.getType() == Item.Type.ACCESSORY)
		{
			for (int i = 0; i < accessories.length; i++)
			{
				if (accessories[i] != null && accessories[i].equals(item))
				{
					// Unequip
					accessories[i] = null;
					// Remove the item bonuses from the unit
					if (!item.getBonuses().isEmpty())
					{
						for (Bonus bonus : item.getBonuses())
						{
							this.owner.adjustBonus(bonus.getType(), -bonus.getValue());
						}
					}
					System.out.println(item.getName() + " unequipped from accessory slot " + i);
					return;
				}
			}
			System.out.println(item.getName() + " not found in accessory list!");
		}
	}

	/** Generate a String listing the equipment items */
	@Override
	public String toString()
	{
		String str = "";

		str += "Weapon: " + items.get(Item.Type.WEAPON) + "\n";
		str += "Shield: " + items.get(Item.Type.SHIELD) + "\n";
		str += "Armour: " + items.get(Item.Type.ARMOUR) + "\n";
		str += "Helmet: " + items.get(Item.Type.HELMET) + "\n";
		str += "Boots: " + items.get(Item.Type.BOOTS) + "\n";
		str += "Gloves: " + items.get(Item.Type.GLOVES) + "\n";

		str += "Accesories: ";
		for (int i = 0; i < accessories.length; i++)
		{
			InventoryItem acc = accessories[i];
			if (acc != null)
			{
				str += acc.getName() + (i < accessories.length ? ", " : "\n");
			}
		}

		return str;
	}

	/**
	 * @return the owner
	 */
	public Unit getOwner()
	{
		return owner;
	}

	/**
	 * @return the items
	 */
	public EnumMap<Item.Type, InventoryItem> getItems()
	{
		return items;
	}

	/**
	 * @return the accessories
	 */
	public InventoryItem[] getAccessories()
	{
		return accessories;
	}
}
