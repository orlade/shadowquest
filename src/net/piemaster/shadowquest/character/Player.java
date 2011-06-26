package net.piemaster.shadowquest.character;

/* 433-294 Object Oriented Software Development
 * RPG Game Player
 * Author: Oliver Lade <orlade>
 */

import java.util.ArrayList;
import java.util.EnumMap;

import net.piemaster.shadowquest.GUI;
import net.piemaster.shadowquest.GameplayState;
import net.piemaster.shadowquest.Item;
import net.piemaster.shadowquest.RPG;
import net.piemaster.shadowquest.character.monster.Monster;
import net.piemaster.shadowquest.inventory.InventoryAccessory;
import net.piemaster.shadowquest.inventory.InventoryArmour;
import net.piemaster.shadowquest.inventory.InventoryConsumable;
import net.piemaster.shadowquest.inventory.InventoryItem;
import net.piemaster.shadowquest.inventory.InventoryQuest;
import net.piemaster.shadowquest.inventory.InventoryWeapon;
import net.piemaster.shadowquest.world.ExitZone;
import net.piemaster.shadowquest.world.WorldAccessory;
import net.piemaster.shadowquest.world.WorldArmour;
import net.piemaster.shadowquest.world.WorldConsumable;
import net.piemaster.shadowquest.world.WorldGold;
import net.piemaster.shadowquest.world.WorldItem;
import net.piemaster.shadowquest.world.WorldQuest;
import net.piemaster.shadowquest.world.WorldWeapon;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Player extends Unit
{
	/** The range at which the player can pick up items */
	private float pickupRange = 30;
	/** The item the player is standing on, candidate for pickup */
	private WorldItem pickupCandidate = null;
	/** Whether the player is on an already used exit point (hasn't moved off) */
	private boolean onUsedExit = false;
	/** The last exit zone the user used */
	private ExitZone lastExit = null;

	/**
	 * The average chance that an item will drop when the player kills a monster
	 */
	protected double itemDrop = 0.1;
	/** The modifier to how much gold is dropped (constant chance) */
	protected double goldDrop = 1;

	/**
	 * The player's level schedule, how much xp is needed to level up from each
	 * level
	 */
	private int levelXps[];
	/** The maximum level the player can reach */
	private int maxLevel = 50;
	/** The amount of gold the player is carrying */
	protected int gold = 0;

	/**
	 * Create a new Player object.
	 * 
	 * @throws SlickException
	 */
	public Player() throws SlickException
	{
		// Initilialise the bonuses
		this.bonuses = new EnumMap<Bonus.Type, Double>(Bonus.Type.class);
		for (Bonus.Type type : Bonus.Type.values())
		{
			this.bonuses.put(type, 0.0);
		}

		// Initialise basic spawning properties
		this.init();

		// Call the player "Bartel", of course
		this.name = "Bartel";

		// Set the Player's avatar and physical size
		this.avatar = new Image("assets/units/player.png");
		this.physWidth = 34;
		this.physHeight = 44;

		// Set the player's stats
		this.strength = 50;
		this.defence = 50;
		this.cooldown = 600;
		this.gold = 50;

		// DEBUG: Give the player some items to test the inventory
		// ArrayList<Bonus> strengthBonus = new ArrayList<Bonus>(1);
		// strengthBonus.add(new Bonus(Bonus.Type.DAMAGE, 10));

		this.pickupItem(new InventoryWeapon("Short Sword", new Image("assets/items/sword.png"),
				Bonus.EMPTY_BONUS, 5, 10));

		ArrayList<Bonus> potionBonus = new ArrayList<Bonus>(1);
		potionBonus.add(new Bonus(Bonus.Type.HP, 25));
		this.pickupItem(new InventoryConsumable("Health Potion", new Image(
				"assets/items/potion.png"), potionBonus));

		// Fill out the Player's level progression schedule
		levelXps = new int[maxLevel + 1];
		levelXps[0] = 0;
		for (int i = 1; i <= maxLevel; i++)
		{
			levelXps[i] = (int) (levelXps[i - 1] + 100 + 0.1 * levelXps[i - 1]);
		}
	}

	/** Sets Player properties when spawning (i.e. at the start or after dying) */
	public void init()
	{
		// Set the Player's starting position
		this.x = 900;
		this.y = 590;

		// Give the player full health
		this.health = this.getMaxHealth();
	}

	/**
	 * Update the player's state for a frame.
	 * 
	 * @author Oliver
	 * 
	 * @param dir_x
	 *            The player's movement in the x axis (-1, 0 or 1).
	 * @param dir_y
	 *            The player's movement in the y axis (-1, 0 or 1).
	 * @param delta
	 *            Time passed since last frame (milliseconds).
	 * @param game
	 *            The game object, which acts as a facade.
	 */
	public void update(int dirX, int dirY, int delta, GameplayState gps) throws SlickException
	{
		// Handle player death
		if (this.getHealth() <= 0)
		{
			this.die(gps);
			return;
		}

		if (checkExit(gps))
			return;

		// Decrease the cooldown timer if necessary
		if (this.getCooldownTimer() > 0)
		{
			this.cooldownTimer -= delta;
			// If the attack is less than half cooled down, reset rotation
			if (this.getCooldownTimer() < this.getCooldown() / 2)
			{
				this.getAvatar().rotate(-this.getAvatar().getRotation());
			}
		}
		// Handle HP regen if necessary
		if (this.getHpRegenAmount() != 0)
		{
			this.hpRegenTimer -= delta;
			if (this.getHpRegenTimer() > 0)
			{
				this.hpRegenTimer -= delta;
			}
			else
			{
				this.heal(getHpRegenAmount());
				this.hpRegenTimer = this.hpRegenCooldown;
			}
		}

		// Level up if necessary
		if (this.getXp() >= this.getLevelXps()[this.level] && this.getLevel() < this.maxLevel)
		{
			this.level++;
			this.strength += 5;
			System.out.println(getName() + " leveled up! Now level " + this.level);
		}

		// Calculate the player's new position on successful move
		double newX = getX() + dirX * getSpeed() * delta;
		double newY = getY() + dirY * getSpeed() * delta;

		// Check for items near the new position
		WorldItem item = scanForItem(gps);
		// If there is one, set it as the pickup candidate, else null
		this.pickupCandidate = item;

		// Check for monsters near the new position
		Monster m = scanForMonster(gps);
		if (m != null)
		{
			this.attack(m);
			// If the monster was killed, add xp
			if (m.getHealth() <= 0)
			{
				int xpGain = m.getXp();
				if (level == maxLevel && xp + xpGain > levelXps[maxLevel])
				{
					this.xp = levelXps[maxLevel];
					System.out.println(this.getName() + " received " + xpGain + "XP!");
				}
				else
				{
					this.xp += xpGain;
					System.out.println(this.getName() + " received " + xpGain + "XP!");
				}
				// Kill off the monster
				m.die(gps);
			}
		}

		this.move(newX, newY, gps);
	}

	/**
	 * Check if the player is standing on an exit zone, and jump him if so
	 * 
	 * @param gps
	 *            The gameplay state to check in
	 * @return Whether the player has used an exit (to short out update)
	 */
	private boolean checkExit(GameplayState gps)
	{
		// Jump the player to another level if in a transition zone
		ExitZone z = gps.getMap().getCurrentExit(this.getX(), this.getY());
		if (z != null && !isOnUsedExit())
		{
			System.out.println("USING " + z);
			if (z.getTargetId() == -1)
			{
				z.setTargetId(gps.getID() + 1);
			}
			else if (z.getTargetId() == -2)
			{
				z.setTargetId(gps.getID() - 1);
			}
			gps.changeLevel(z);
			this.onUsedExit = true;

			return true;
		}
		// If not on an exit, reset the onUsedExit flag
		else if (z == null && isOnUsedExit())
		{
			this.onUsedExit = false;
		}
		return false;
	}

	@Override
	/** Required abstract override */
	public void update(int delta, GameplayState gps)
	{
		return;
	}

	/**
	 * Adds an item to the player's inventory.
	 * 
	 * @param item
	 *            The item to be added.
	 */
	public void pickupItem(InventoryItem item)
	{
		if (this.getInventory().getItems().size() < 20)
			this.getInventory().getItems().add(item);
		else
			System.out.println("Inventory full, pickup failed!");
	}

	/**
	 * Converts a WorldItem to an Item and adds it to the player's inventory.
	 * 
	 * @param item
	 *            The WorldItem to be converted and added.
	 * @param world
	 *            The world from which to remove the WorldItem.
	 */
	public void pickupItem(WorldItem item, GameplayState gps)
	{
		// If it's gold, add it to the gold stash
		if (item instanceof WorldGold)
		{
			this.gold += ((WorldGold) item).getAmount();
		}
		// Other items go in inventory
		else
		{
			// If it's a consumable
			if (item.getType() == Item.Type.CONSUMABLE)
			{
				WorldConsumable cons = (WorldConsumable) (item);
				this.getInventory()
						.addItem(
								new InventoryConsumable(cons.getName(), cons.getAvatar(), cons
										.getBonuses()));
			}
			// If it's an accessory
			else if (item.getType() == Item.Type.ACCESSORY)
			{
				this.getInventory()
						.addItem(
								new InventoryAccessory(item.getName(), item.getAvatar(), item
										.getBonuses()));
			}
			// If it's a weapon
			else if (item.getType() == Item.Type.WEAPON)
			{
				WorldWeapon weapon = (WorldWeapon) (item);
				this.getInventory().addItem(
						new InventoryWeapon(weapon.getName(), weapon.getAvatar(), weapon
								.getBonuses(), weapon.getMinDamage(), weapon.getMaxDamage()));
			}
			// If it's a quest item
			else if (item.getType() == Item.Type.QUEST)
			{
				WorldQuest quest = (WorldQuest) (item);
				this.getInventory().addItem(
						new InventoryQuest(quest.getName(), quest.getAvatar(), quest.getBonuses()));
			}
			// If it's a piece of armour
			else if (item.getType() == Item.Type.ARMOUR || item.getType() == Item.Type.SHIELD
					|| item.getType() == Item.Type.HELMET || item.getType() == Item.Type.GLOVES
					|| item.getType() == Item.Type.BOOTS)
			{
				WorldArmour armour = (WorldArmour) (item);
				this.getInventory().addItem(
						new InventoryArmour(armour.getName(), armour.getType(), armour.getAvatar(),
								armour.getBonuses(), armour.getArmourRating()));
			}
			else
			{
				System.out.println("ERROR: Failed to pickup WorldItem -> InventoryItem!");
				return;
			}
		}
		// Debug print
		System.out.println(getName() + " picked up " + item);
		// Remove the item from the world
		item.die(gps);
	}

	/**
	 * Instructs the player to pick up their candidate item (if any)
	 * 
	 * @param world
	 *            The world to pickup the item from
	 */
	public void pickupItem(GameplayState gps)
	{
		if (this.getPickupCandidate() != null)
		{
			this.pickupItem(this.getPickupCandidate(), gps);
		}
	}

	/**
	 * Scan the area around the player for monsters to attack.
	 * 
	 * @param gps
	 *            The game world, which holds the list of monsters.
	 * @return The target to be attacked.
	 */
	Monster scanForMonster(GameplayState gps)
	{
		// Check through every monster
		for (Monster m : gps.getMonsters())
		{
			// Early failure check - if diff in either axis is too great, move
			// on
			if (Math.abs(this.getX() - m.getX()) > this.getAttackRange()
					|| Math.abs(this.getY() - m.getY()) > this.getAttackRange())
			{
				continue;
			}

			// Find the actual distance from the player
			double monsterDist = GameplayState.distanceTo(this, m);

			// If within range, return the monster as a target
			if (monsterDist <= this.getAttackRange())
			{
				return m;
			}
		}
		// No monsters in range, return null
		return null;
	}

	/**
	 * Scan the area around the player for pickuppable items.
	 * 
	 * @param gps
	 *            The game world, which holds the list of world items.
	 * @return The item that may be picked up.
	 */
	WorldItem scanForItem(GameplayState gps)
	{
		// Check through every WorldItem
		for (WorldItem w : gps.getWorldItems())
		{
			// Early failure check - if diff in either axis is too great, move
			// on
			if (Math.abs(this.getX() - w.getX()) > this.getPickupRange()
					|| Math.abs(this.getY() - w.getY()) > this.getPickupRange())
			{
				continue;
			}

			// Find the actual distance from the player
			double itemDist = GameplayState.distanceTo(this, w);

			// If within range, set the item as a pickup candidate
			if (itemDist <= pickupRange)
			{
				return w;
			}
		}
		// No items in range, return null
		return null;
	}

	/**
	 * Select an item in the inventory panel.
	 * 
	 * @param gui
	 *            The GUI to select from
	 */
	public void selectInvItem(GUI gui)
	{
		// If the selected slot contains an item, perform its action()
		InventoryItem item = null;
		if (gui.getInvSelectIndex() < inventory.getItems().size())
		{
			item = inventory.getItems().get((int) gui.getInvSelectIndex());
		}

		if (item != null)
		{
			item.use(this);
		}
	}

	/**
	 * Drop an item from the inventory panel. Essentially reverses the
	 * pickupItem() method.
	 * 
	 * @param gps
	 *            Facade for GUI, and world to drop item into
	 * @throws SlickException
	 */
	public void dropInvItem(GameplayState gps) throws SlickException
	{
		// If the selected slot contains an item, perform its action()
		InventoryItem item = null;
		if (gps.getGui().getInvSelectIndex() < inventory.getItems().size())
		{
			item = inventory.getItems().get((int) gps.getGui().getInvSelectIndex());
		}

		if (item != null)
		{
			// Spawn the item in the world
			// If it's a consumable
			if (item.getType() == Item.Type.CONSUMABLE)
			{
				InventoryConsumable cons = (InventoryConsumable) (item);
				gps.spawnWorldItem(new WorldConsumable(this.getX(), this.getY(), cons.getName(),
						cons.getAvatar(), cons.getBonuses()));
			}
			// If it's an accessory
			else if (item.getType() == Item.Type.ACCESSORY)
			{
				gps.spawnWorldItem(new WorldAccessory(this.getX(), this.getY(), item.getName(),
						item.getAvatar(), item.getBonuses()));
			}
			// If it's a weapon
			else if (item.getType() == Item.Type.WEAPON)
			{
				InventoryWeapon weapon = (InventoryWeapon) (item);
				gps.spawnWorldItem(new WorldWeapon(this.getX(), this.getY(), weapon.getName(),
						weapon.getAvatar(), weapon.getBonuses(), weapon.getMinDamage(), weapon
								.getMaxDamage()));
			}
			// If it's a quest item
			else if (item.getType() == Item.Type.QUEST)
			{
				InventoryQuest quest = (InventoryQuest) (item);
				gps.spawnWorldItem(new WorldQuest(this.getX(), this.getY(), quest.getName(), quest
						.getAvatar(), quest.getBonuses()));
			}
			// If it's a piece of armour
			else if (item.getType() == Item.Type.ARMOUR || item.getType() == Item.Type.SHIELD
					|| item.getType() == Item.Type.HELMET || item.getType() == Item.Type.GLOVES
					|| item.getType() == Item.Type.BOOTS)
			{
				InventoryArmour armour = (InventoryArmour) (item);
				gps.spawnWorldItem(new WorldArmour(this.getX(), this.getY(), armour.getName(),
						armour.getType(), armour.getAvatar(), armour.getBonuses(), armour
								.getArmourRating()));
			}
			else
			{
				System.out.println("ERROR: Failed to drop InventoryItem -> WorldItem!");
				return;
			}

			// Remove the item from the inventory
			if (item.isEquipped())
			{
				this.getEquipment().unequipItem(item);
			}
			this.getInventory().getItems().remove(item);
		}
		// Debug print
		System.out.println(getName() + " dropped " + item + "[" + this.getX() + ", " + this.getY()
				+ "]");
	}

	/**
	 * Drops some amount of the player's gold on the ground (for death)
	 * 
	 * @param gps
	 *            The GPS to drop into
	 * @param amount
	 *            The amount to drop
	 */
	public void dropGold(GameplayState gps, int amount)
	{
		try
		{
			gps.spawnWorldItem(new WorldGold(getX(), getY(), amount));
		}
		// Handle image load failure
		catch (SlickException e)
		{
			System.out.println(e.getStackTrace());
		}
		// Reduce the player's gold balance
		this.gold -= amount;
		// Debug print
		System.out.println(getName() + " dropped " + amount + "gold @ [" + this.getX() + ", "
				+ this.getY() + "]");
	}

	@Override
	/** Handle death of the player, called from update() if health < 0.
	 * 
	 * @param world The game world to remove the player from
	 */
	public void die(GameplayState gps)
	{
		// Debug print
		System.out.println(this.getName() + " died!");
		// Create a corpse
		this.dropCorpse(gps);
		// Drop all the player's gold
		dropGold(gps, getGold());
		// Reset the player's rotation for respawning
		this.getAvatar().rotate(-this.getAvatar().getRotation());
		this.setX(-getPhysWidth());
		this.setY(-getPhysHeight());
		// Reset use of exit zones
		this.setLastExit(null);

		// Change level back to the World Map
		if (gps.getID() != RPG.GPSMAIN)
		{
			gps.changeLevel(RPG.GPSMAIN);
			// Respawning handled in changeLevel
		}
		// If already on world map, just reset to town
		else
		{
			this.init();
		}
	}

	@Override
	/** Gets the minimum damage done by the unit's weapon.
	 * 
	 * @return The minimum damage done by the unit's weapon
	 */
	public double getMinDamage()
	{
		// Get a reference to the unit's weapon
		InventoryWeapon weapon = (InventoryWeapon) (this.getEquipment().getItems()
				.get(Item.Type.WEAPON));
		double damage;
		// If holding a weapon
		if (weapon != null)
		{
			damage = weapon.getMinDamage() * (this.getStrength() + 100) / 100;
		}
		// Else unarmed
		else
		{
			damage = this.getStrength() / 15 * (this.getStrength() + 100) / 100;
		}
		damage *= (1 + this.getBonuses().get(Bonus.Type.PCDAMAGE));
		damage += this.getBonuses().get(Bonus.Type.DAMAGE);
		return damage;
	}

	@Override
	/** Gets the maximum damage done by the unit's weapon.
	 * 
	 * @return The maximum damage done by the unit's weapon
	 */
	public double getMaxDamage()
	{
		// Get a reference to the unit's weapon
		InventoryWeapon weapon = (InventoryWeapon) (this.getEquipment().getItems()
				.get(Item.Type.WEAPON));
		double damage;
		// If holding a weapon
		if (weapon != null)
		{
			damage = weapon.getMaxDamage() * (this.getStrength() + 100) / 100;
		}
		// Else unarmed
		else
		{
			damage = this.getStrength() / 10 * (this.getStrength() + 100) / 100;
		}
		damage *= (1 + this.getBonuses().get(Bonus.Type.PCDAMAGE));
		damage += this.getBonuses().get(Bonus.Type.DAMAGE);
		return damage;
	}

	/**
	 * @return the levelXps
	 */
	public int[] getLevelXps()
	{
		return levelXps;
	}

	/**
	 * @return the pickupRange
	 */
	public float getPickupRange()
	{
		return pickupRange;
	}

	/**
	 * @return the pickupCandidate
	 */
	public WorldItem getPickupCandidate()
	{
		return pickupCandidate;
	}

	/**
	 * @return the onUsedExit
	 */
	public boolean isOnUsedExit()
	{
		return onUsedExit;
	}

	/**
	 * @return the lastExit
	 */
	public ExitZone getLastExit()
	{
		return lastExit;
	}

	/**
	 * @param lastExit
	 *            the lastExit to set
	 */
	public void setLastExit(ExitZone lastExit)
	{
		this.lastExit = lastExit;
	}

	/**
	 * @return the gold
	 */
	public int getGold()
	{
		return gold;
	}

	public void adjustGold(int amount)
	{
		this.gold += amount;
	}

	/**
	 * @return the goldDrop
	 */
	public double getGoldDrop()
	{
		return goldDrop;
	}

	/**
	 * @return the itemDrop
	 */
	public double getItemDrop()
	{
		return itemDrop + this.getBonuses().get(Bonus.Type.ITEMDROP);
	}
}
