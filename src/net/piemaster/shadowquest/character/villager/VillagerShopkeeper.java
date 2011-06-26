/* 433-294 Object Oriented Software Development
 * Peasant (Villager NPC)
 * Author: Oliver Lade <orlade>
 */

package net.piemaster.shadowquest.character.villager;

import net.piemaster.shadowquest.character.Player;
import net.piemaster.shadowquest.character.Unit;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class VillagerShopkeeper extends Villager
{
	/** The global price of a health potion */
	public static int potionPrice = 50;

	/** What the shopkeeper says when something is too expensive */
	private String priceReject = "'Fraid you can't afford that, stranger.";
	/** What the shopkeeper says when something is too expensive */
	private String purchaseDialogue = "Ah, thank ye stranger!";

	/**
	 * Create shopkeeper
	 * 
	 * @param x
	 *            The shopkeeper x coordinate
	 * @param y
	 *            The shopkeeper y coordinate
	 * @param name
	 *            The shopkeeper name
	 * @throws SlickException
	 */
	public VillagerShopkeeper(double x, double y, String name) throws SlickException
	{
		super(x, y, name, new Image("assets/units/peasant.png"));
	}

	@Override
	/** The action to take when interacting with another Unit (usually the player)
	 * 
	 * @param target The target to interact with
	 */
	public void interact(Unit target)
	{
		// Player interacting with this Villager
		if (target instanceof Player)
		{
			setDialogue("Press B to buy a health potion (50 gold).");
		}
		// NPC interacting with this Villager
		else
		{
			setDialogue("Sup, " + target.getName() + "!");
		}
	}

	/**
	 * @return the priceReject
	 */
	public String getPriceReject()
	{
		return priceReject;
	}

	/**
	 * @return the purchaseDialogue
	 */
	public String getPurchaseDialogue()
	{
		return purchaseDialogue;
	}
}
