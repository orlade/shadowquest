package net.piemaster.shadowquest.inventory;

import java.util.ArrayList;

import net.piemaster.shadowquest.Item;
import net.piemaster.shadowquest.character.Bonus;
import net.piemaster.shadowquest.character.Unit;

import org.newdawn.slick.Image;

public class InventoryWeapon extends InventoryItem
{
	/** The minimum damage the weapon can do on a successful hit */
	private double minDamage;
	/** The maximum damage the weapon can do on a successful hit */
	private double maxDamage;

	/**
	 * Create a new weapon
	 * 
	 * @param name
	 *            The name of the weapon
	 * @param avatar
	 *            The graphical representation of the weapon
	 * @param bonuses
	 *            The bonuses to be applied (if any)
	 * @param min
	 *            The weapon's minimum damage
	 * @param max
	 *            The weapon's maximum damage
	 */
	public InventoryWeapon(String name, Image avatar, ArrayList<Bonus> bonuses, double min,
			double max)
	{
		super(name, Item.Type.WEAPON, avatar, bonuses);
		this.minDamage = min;
		this.maxDamage = max;
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
		}
		// Otherwise, equip
		else
		{
			unit.getEquipment().equipItem(this);
		}
	}

	/**
	 * @return the minDamage
	 */
	public double getMinDamage()
	{
		return minDamage;
	}

	/**
	 * @return the maxDamage
	 */
	public double getMaxDamage()
	{
		return maxDamage;
	}
}
