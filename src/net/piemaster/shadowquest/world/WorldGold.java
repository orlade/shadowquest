package net.piemaster.shadowquest.world;

import net.piemaster.shadowquest.Item;
import net.piemaster.shadowquest.character.Bonus;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class WorldGold extends WorldItem
{
	/** The amount of gold in the pile */
	private int amount;

	/**
	 * Create some gold in the world.
	 * 
	 * @param x
	 *            The x coordinate to spawn the gold.
	 * @param y
	 *            The y coordinate to spawn the gold.
	 * @param amount
	 *            The amount of gold in the pile.
	 * @throws SlickException
	 */
	public WorldGold(double x, double y, int amount) throws SlickException
	{
		super(x, y, amount + " gold", Item.Type.GOLD, new Image("assets/items/gold.png"),
				Bonus.EMPTY_BONUS);
		this.amount = amount;
	}

	/**
	 * @return the amount
	 */
	public int getAmount()
	{
		return amount;
	}
}
