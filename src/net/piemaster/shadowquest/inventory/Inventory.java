package net.piemaster.shadowquest.inventory;

import java.util.ArrayList;

public class Inventory
{
	/** The max number of items the inventory can contain */
	protected int capacity;
	/** List of the items the unit owns */
	protected ArrayList<InventoryItem> items = new ArrayList<InventoryItem>();

	public Inventory(int capacity)
	{
		this.capacity = capacity;
	}

	public Inventory()
	{
		this.capacity = 20;
	}

	/**
	 * @param name
	 *            The name of the item to find in the inventory
	 * @return whether the item is present
	 */
	public InventoryItem hasItem(String name)
	{
		for (InventoryItem item : items)
		{
			if (item.getName().equals(name))
				return item;
		}
		return null;
	}

	/**
	 * Add an inventory item to the list of items in the inventory
	 * 
	 * @param item
	 *            the item to add
	 */
	public void addItem(InventoryItem item)
	{
		items.add(item);
	}

	/**
	 * @return the items
	 */
	public ArrayList<InventoryItem> getItems()
	{
		return items;
	}
}
