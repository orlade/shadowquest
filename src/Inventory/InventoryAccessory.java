package Inventory;

import java.util.ArrayList;

import org.newdawn.slick.Image;

import Character.Bonus;
import Character.Unit;
import Game.Item;

public class InventoryAccessory extends InventoryItem
{

	/** Create a new accessory
	 * 
	 * @param name The name of the accessory
	 * @param avatar The graphical representation of the accessory
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
		if(this.isEquipped())
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
