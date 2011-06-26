package net.piemaster.shadowquest;

import java.util.ArrayList;

import net.piemaster.shadowquest.character.Corpse;
import net.piemaster.shadowquest.character.monster.Monster;
import net.piemaster.shadowquest.character.monster.MonsterBandit;
import net.piemaster.shadowquest.character.monster.MonsterSkeleton;
import net.piemaster.shadowquest.character.monster.MonsterZombie;
import net.piemaster.shadowquest.character.villager.Villager;
import net.piemaster.shadowquest.mapgen.MapFactory;
import net.piemaster.shadowquest.world.WorldItem;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class GameplayStateLevelDungeon extends GameplayState
{
	/** The monsters the currently inhabit the World */
	private ArrayList<Monster> monsters;
	/** The corpses present in the world */
	private ArrayList<Corpse> corpses;
	/** The items sitting around in the game world */
	private ArrayList<WorldItem> items;

	/**
	 * Create a GameplayState of the main level
	 * 
	 * @param ID
	 *            The state's ID
	 */
	public GameplayStateLevelDungeon(int ID)
	{
		super(ID);

		monsters = new ArrayList<Monster>();
		corpses = new ArrayList<Corpse>();
		items = new ArrayList<WorldItem>();
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
		int size = 15 + (int) (Math.random() * 10);
		// TODO: Fix it so maps can be generated with uneven sides and not fail
		try
		{
			if (this.getID() < RPG.GPSDUNGEON5)
				this.setMap(MapFactory.generateCave(size, size, true));
			else
				this.setMap(MapFactory.generateCave(size, size, false));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// Add some monsters
		initMonsters();

		// Add some items to the world
		// initItems();
	}

	/**
	 * Set up a random horde of monsters
	 * 
	 * @throws SlickException
	 * */
	public void initMonsters() throws SlickException
	{
		// A number giving a general scale of the map size
		double mapSize = Math.sqrt((this.getMap().getMapWidth() / this.getMap().getTileWidth())
				* (this.getMap().getMapHeight() / this.getMap().getTileHeight()));
		System.out.println("mapsize=" + mapSize + ", [" + this.getMap().getMapWidth() + ", "
				+ this.getMap().getMapHeight() + "]");
		// num = mapSize +/- 1/8*mapSize
		int numMonsters = (int) (mapSize / 2 + Math.random() * (mapSize / 4) - mapSize / 8);
		System.out.println("Generating " + numMonsters + " monsters...");

		for (int i = 0; i < numMonsters; i++)
		{
			// Create a new monster at a random position with mlvl based on plvl
			Monster monster;
			double mx = Math.random() * (getMap().getMapWidth() - 2 * getMap().getTileWidth())
					+ getMap().getTileWidth();
			double my = Math.random() * (getMap().getMapHeight() - 2 * getMap().getTileHeight())
					+ getMap().getTileHeight();
			// System.out.println("MAP = ("+getMap().getMapWidthTiles()+", "+getMap().getMapHeightTiles()+"), MONSTER = ("+mx/72+", "+my/72+")");

			// Chose monster type
			switch ((int) (Math.random() * 3))
			{
			// Zombie (mlvl = [plvl-4, plvl+2])
			case 0:
				monster = new MonsterZombie(0, 0,
						(int) (getPlayer().getLevel() + Math.random() * 7 - 4));
				break;
			// Bandit (mlvl = [plvl-2, plvl+4])
			case 1:
				monster = new MonsterBandit(0, 0,
						(int) (getPlayer().getLevel() + Math.random() * 7 - 2));
				break;
			// Skeleton (mlvl = [plvl+1, plvl+7])
			case 2:
				monster = new MonsterSkeleton(0, 0, (int) (getPlayer().getLevel() + Math.random()
						* 7 + 1));
				break;
			// Default: Zombie (mlvl = [plvl-4, plvl+2])
			default:
				System.out.println("WARNING: Monster creation default hit - creating zombie!");
				monster = new MonsterZombie(0, 0,
						(int) (getPlayer().getLevel() + Math.random() * 7 - 4));
				break;
			}

			// Place the monster in a non-blocked tile, outside of player
			// detection range
			while (getMap().isBoxBlocked(mx, my, monster.getPhysWidth(), monster.getPhysHeight())
					|| distanceTo(getPlayer(), monster) <= monster.getDetectionRange())
			{
				mx = Math.random() * (getMap().getMapWidth() - 2 * getMap().getTileWidth())
						+ getMap().getTileWidth();
				my = Math.random() * (getMap().getMapHeight() - 2 * getMap().getTileHeight())
						+ getMap().getTileHeight();
				// System.out.println("MAP = ("+getMap().getMapWidthTiles()+", "+getMap().getMapHeightTiles()+"), MONSTER = ("+mx/72+", "+my/72+")");
			}
			monster.setX(mx);
			monster.setY(my);
			getMonsters().add(monster);
		}
	}

	@Override
	/** Update the game state for a frame.
	 * @param gc The Slick game container object.
	 * @param sbg The Game object.
	 * @param delta Time passed since last frame (milliseconds).
	 */
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		super.update(gc, sbg, delta);
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
		/*
		 * // Render the Player's status panel getGui().renderStatusPanel(g);
		 * if(isRenderInvPanel()) getGui().renderInventoryPanel(g);
		 */
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
	 * @return the villagers
	 */
	@Override
	public ArrayList<Villager> getVillagers()
	{
		return new ArrayList<Villager>(0);
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
