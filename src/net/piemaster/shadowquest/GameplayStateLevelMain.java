package net.piemaster.shadowquest;

import java.util.ArrayList;

import net.piemaster.shadowquest.character.Bonus;
import net.piemaster.shadowquest.character.Corpse;
import net.piemaster.shadowquest.character.monster.Monster;
import net.piemaster.shadowquest.character.monster.MonsterBandit;
import net.piemaster.shadowquest.character.monster.MonsterNecromancer;
import net.piemaster.shadowquest.character.monster.MonsterSkeleton;
import net.piemaster.shadowquest.character.monster.MonsterZombie;
import net.piemaster.shadowquest.character.villager.Villager;
import net.piemaster.shadowquest.character.villager.VillagerPeasant;
import net.piemaster.shadowquest.character.villager.VillagerPrince;
import net.piemaster.shadowquest.character.villager.VillagerShaman;
import net.piemaster.shadowquest.character.villager.VillagerShopkeeper;
import net.piemaster.shadowquest.inventory.InventoryConsumable;
import net.piemaster.shadowquest.world.ExitZone;
import net.piemaster.shadowquest.world.WorldAccessory;
import net.piemaster.shadowquest.world.WorldConsumable;
import net.piemaster.shadowquest.world.WorldItem;
import net.piemaster.shadowquest.world.WorldMap;
import net.piemaster.shadowquest.world.WorldQuest;
import net.piemaster.shadowquest.world.WorldWeapon;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class GameplayStateLevelMain extends GameplayState
{
	/** The monsters the currently inhabit the World */
	private ArrayList<Monster> monsters;
	/** The corpses present in the world */
	private ArrayList<Corpse> corpses;
	/** The items sitting around in the game world */
	private ArrayList<WorldItem> items;
	/** The villagers currently living in the game world */
	private ArrayList<Villager> villagers;

	/**
	 * Create a GameplayState of the main level
	 * 
	 * @param ID
	 *            The state's ID
	 */
	public GameplayStateLevelMain(int ID)
	{
		super(ID);

		monsters = new ArrayList<Monster>();
		corpses = new ArrayList<Corpse>();
		items = new ArrayList<WorldItem>();
		villagers = new ArrayList<Villager>();
	}

	@Override
	/** Initialise the state
	 * 
	 * @param gc The Slick game container object
	 * @param sbg The Game object
	 */
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{
		super.init(gc, sbg);
		// Initialise the map
		this.setMap(new WorldMap("assets/map.tmx", "assets"));
		this.getMap().setExit(
				new ExitZone(13.0 * getMap().getTileWidth(), 6.0 * getMap().getTileHeight(),
						getMap().getTileWidth() * 2, getMap().getTileHeight() * 2, ShadowQuest.GPSDUNGEON));

		// Add some villagers
		initVillagers();

		// Add some monsters
		initMonsters();

		// Add some items to the world
		initItems();
	}

	@Override
	/** Update the game state for a frame.
	 * @param gc The Slick game container object.
	 * @param sbg The Game object.
	 * @param delta Time passed since last frame (milliseconds).
	 */
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		// Handle buying from shopkeeper - should be implemented somewhere else
		// probably
		if (getInput().isKeyPressed(Input.KEY_B))
		{
			for (Villager v : villagers)
			{
				if (v instanceof VillagerShopkeeper && v.isInteracting())
				{
					if (getPlayer().getGold() >= VillagerShopkeeper.potionPrice)
					{
						// Buy the potion
						getPlayer().adjustGold(-VillagerShopkeeper.potionPrice);
						ArrayList<Bonus> potionBonus = new ArrayList<Bonus>(1);
						potionBonus.add(new Bonus(Bonus.Type.HP, 25));
						getPlayer().pickupItem(
								new InventoryConsumable("Health Potion", new Image(
										"assets/items/potion.png"), potionBonus));

						// Set purchase dialogue
						v.setDialogue(((VillagerShopkeeper) v).getPurchaseDialogue());
					}
					else
					{
						// Warn that it costs too much
						v.setDialogue(((VillagerShopkeeper) v).getPriceReject());
					}
				}
			}
		}
		super.update(gc, sbg, delta);

		// Update each villager
		for (Villager v : villagers)
		{
			v.update(delta, this);
		}
	}

	@Override
	/** Render the entire screen, so it reflects the current game state.
	 * @param gc The Slick game container object.
	 * @param g The Slick graphics object, used for drawing.
	 */
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
	{
		// Render the game world
		super.render(gc, sbg, g);
	}

	/**
	 * Adds the villagers to the world
	 * 
	 * @throws SlickException
	 */
	public void initVillagers() throws SlickException
	{
		villagers.add(new VillagerShaman(900, 540, "Elvira"));
		villagers.add(new VillagerPeasant(828, 825, "Garth"));
		villagers.add(new VillagerShopkeeper(662, 764, "Harold"));
		villagers.add(new VillagerPrince(545, 610, "Prince Aldric"));
	}

	/**
	 * Adds some monsters to the world
	 * 
	 * @throws SlickException
	 */
	public void initMonsters() throws SlickException
	{
		// Zombies
		monsters.add(new MonsterZombie(1260, 540, (int) (Math.random() * 1 + 1)));
		monsters.add(new MonsterZombie(1476, 900, (int) (Math.random() * 1 + 1)));
		monsters.add(new MonsterZombie(1548, 1332, (int) (Math.random() * 2 + 1)));
		monsters.add(new MonsterZombie(756, 2556, (int) (Math.random() * 2 + 1)));
		monsters.add(new MonsterZombie(900, 2844, (int) (Math.random() * 2 + 1)));
		monsters.add(new MonsterZombie(1980, 2412, (int) (Math.random() * 3 + 1)));
		monsters.add(new MonsterZombie(2844, 1548, (int) (Math.random() * 3 + 1)));
		monsters.add(new MonsterZombie(2196, 1044, (int) (Math.random() * 4 + 1)));
		monsters.add(new MonsterZombie(2988, 396, (int) (Math.random() * 5 + 1)));

		// Skeleton
		monsters.add(new MonsterSkeleton(2916, 974, (int) (Math.random() * 5 + 1)));
		monsters.add(new MonsterSkeleton(1980, 612, (int) (Math.random() * 5 + 1)));
		monsters.add(new MonsterSkeleton(2052, 972, (int) (Math.random() * 10 + 1)));
		monsters.add(new MonsterSkeleton(2772, 540, (int) (Math.random() * 10 + 1)));

		// Bandits
		monsters.add(new MonsterBandit(1116, 1476, (int) (Math.random() * 1 + 1)));
		monsters.add(new MonsterBandit(1260, 1908, (int) (Math.random() * 2 + 1)));
		monsters.add(new MonsterBandit(540, 1476, (int) (Math.random() * 2 + 1)));
		monsters.add(new MonsterBandit(1404, 2484, (int) (Math.random() * 3 + 1)));
		monsters.add(new MonsterBandit(2772, 2556, (int) (Math.random() * 3 + 1)));
		monsters.add(new MonsterBandit(2052, 1548, (int) (Math.random() * 5 + 1)));
		monsters.add(new MonsterBandit(2052, 1404, (int) (Math.random() * 5 + 1)));

		// Bosses
		monsters.add(new MonsterNecromancer(2052, 468, "Draelic", 20));
	}

	/**
	 * Adds some quest items to the world
	 * 
	 * @throws SlickException
	 */
	public void initItems() throws SlickException
	{
		// DEBUG
		/*
		 * // Create the bonus list ArrayList<Bonus> debugBonus = new
		 * ArrayList<Bonus>(1); debugBonus.add(new Bonus(Bonus.Type.HPREGEN,
		 * 1)); debugBonus.add(new Bonus(Bonus.Type.SPEED, 0.25));
		 * debugBonus.add(new Bonus(Bonus.Type.PCDAMAGE, 0.8)); // Create the
		 * item items.add( new WorldArmour(this.getPlayer().getX() + 100,
		 * this.getPlayer().getY()+100, "Armour of Velocity", Item.Type.ARMOUR,
		 * new Image("assets/items/elven_shield.png"), debugBonus, 50 ) );
		 */

		// DEBUG
		// Create the bonus list
		ArrayList<Bonus> epicDebugBonus = new ArrayList<Bonus>(1);
		epicDebugBonus.add(new Bonus(Bonus.Type.DAMAGE, 100));
		epicDebugBonus.add(new Bonus(Bonus.Type.ACCURACY, 100));
		epicDebugBonus.add(new Bonus(Bonus.Type.ITEMDROP, 1));
		epicDebugBonus.add(new Bonus(Bonus.Type.SPEED, 1));
		// Create the item
		items.add(new WorldAccessory(this.getPlayer().getX(), this.getPlayer().getY() + 100,
				"Amulet of Epic Debug", new Image("assets/items/amulet.png"), epicDebugBonus));

		// Create the bonus list
		ArrayList<Bonus> vitalityBonus = new ArrayList<Bonus>(1);
		vitalityBonus.add(new Bonus(Bonus.Type.MAXHP, 40));
		// Create the item
		items.add(new WorldAccessory(972, 2916, "Amulet of Vitality", new Image(
				"assets/items/amulet.png"), vitalityBonus));

		// Create the bonus list
		ArrayList<Bonus> strengthBonus = new ArrayList<Bonus>(1);
		strengthBonus.add(new Bonus(Bonus.Type.DAMAGE, 10));
		// Create the item
		items.add(new WorldWeapon(1980, 1476, "Sword of Strength", new Image(
				"assets/items/sword.png"), strengthBonus, 15, 30));

		// Create the bonus list
		ArrayList<Bonus> agilityBonus = new ArrayList<Bonus>(1);
		agilityBonus.add(new Bonus(Bonus.Type.COOLDOWN, 200));
		// Create the item
		items.add(new WorldConsumable(2052, 900, "Tome of Agility", new Image(
				"assets/items/book.png"), agilityBonus));

		// Create the item (no bonus)
		items.add(new WorldQuest(2052, 396, "Elixir of Life", new Image("assets/items/elixir.png"),
				Bonus.EMPTY_BONUS));
	}

	/**
	 * @return the monsters
	 */
	@Override
	public ArrayList<Monster> getMonsters()
	{
		return monsters;
	}

	/**
	 * @return the monsters
	 */
	@Override
	public ArrayList<Villager> getVillagers()
	{
		return villagers;
	}

	/**
	 * @return the corpses
	 */
	@Override
	public ArrayList<Corpse> getCorpses()
	{
		return corpses;
	}

	/**
	 * @return the worldItems
	 */
	@Override
	public ArrayList<WorldItem> getWorldItems()
	{
		return items;
	}
}
