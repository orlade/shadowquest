package net.piemaster.shadowquest.world;

import java.io.InputStream;

import net.piemaster.shadowquest.RPG;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class DungeonMap extends Map
{

	/**
	 * Create a dungeon map given a path to a map file
	 * 
	 * @param mapFile
	 *            The filepath to the .tmx map file
	 * @param mapFolder
	 *            The folder containing the map information
	 * @throws SlickException
	 */
	public DungeonMap(String mapFile, String mapFolder) throws SlickException
	{
		super(mapFile, mapFolder);
	}

	/**
	 * Create a dungeon map from an existing TiledMap
	 * 
	 * @param map
	 *            The existing TiledMap
	 * @throws SlickException
	 */
	public DungeonMap(TiledMap map)
	{
		super(map);
	}

	/**
	 * Create a dungeon map with an entrance, given a path to a map file
	 * 
	 * @param mapFile
	 *            The filepath to the .tmx map file
	 * @param mapFolder
	 *            The folder containing the map information
	 * @param x
	 *            The horizontal tile index of the entrance
	 * @param y
	 *            The vertical tile index of the entrance
	 * @throws SlickException
	 */
	public DungeonMap(String mapFile, String mapFolder, int entranceX, int entranceY)
			throws SlickException
	{
		super(mapFile, mapFolder);

		// Save the entrance index
		// this.entranceX = entranceX;
		// this.entranceY = entranceY;
		this.entrance = new ExitZone(entranceX, entranceY, getTileWidth(), getTileHeight(),
				RPG.GPSMAIN);
	}

	/**
	 * Create a dungeon map with an entrance, given a path to a map file
	 * 
	 * @param mapFile
	 *            The filepath to the .tmx map file
	 * @param mapFolder
	 *            The folder containing the map information
	 * @param entranceX
	 *            The horizontal tile index of the entrance
	 * @param entranceY
	 *            The vertical tile index of the entrance
	 * @param exitX
	 *            The horizontal tile index of the exit
	 * @param exitY
	 *            The vertical tile index of the exit
	 * @throws SlickException
	 */
	public DungeonMap(String mapFile, String mapFolder, int entranceX, int entranceY, int exitX,
			int exitY) throws SlickException
	{
		// Create the map normally
		super(mapFile, mapFolder);

		// Save the entrance index
		// this.entranceX = entranceX;
		// this.entranceY = entranceY;
		// this.exitX = exitX;
		// this.exitY = exitY;
		this.entrance = new ExitZone(entranceX, entranceY, getTileWidth(), getTileHeight(), -2);
		this.exit = new ExitZone(exitX, exitY, getTileWidth(), getTileHeight(), -1);
	}

	/**
	 * Create a dungeon map with an entrance, given an input stream.
	 * @throws SlickException
	 */
	public DungeonMap(InputStream mapStream, int entranceX, int entranceY, int exitX,
			int exitY) throws SlickException
	{
		// Create the map normally
		
		super(mapStream, "assets");

		// Save the entrance and exit indexes
		this.entrance = new ExitZone(entranceX, entranceY, getTileWidth(), getTileHeight(), -2);
		this.exit = new ExitZone(exitX, exitY, getTileWidth(), getTileHeight(), -1);
	}

	/**
	 * Create a dungeon map from an existing TiledMap with an entrance
	 * 
	 * @param map
	 *            The existing TiledMap
	 * @param x
	 *            The horizontal tile index of the entrance
	 * @param y
	 *            The vertical tile index of the entrance
	 * @throws SlickException
	 */
	public DungeonMap(TiledMap map, int entranceX, int entranceY)
	{
		// Create the map normally
		super(map);

		// Save the entrance index
		// this.entranceX = entranceX;
		// this.entranceY = entranceY;
		this.entrance = new ExitZone(entranceX, entranceY, getTileWidth(), getTileHeight(),
				RPG.GPSMAIN);
		// this.getExits().add( new ExitZone(entranceX, entranceY,
		// getTileWidth(), getTileHeight(), RPG.GPSMAIN));
	}

	/**
	 * Create a dungeon map from an existing TiledMap with an entrance
	 * 
	 * @param map
	 *            The existing TiledMap
	 * @param entranceX
	 *            The horizontal tile index of the entrance
	 * @param entranceY
	 *            The vertical tile index of the entrance
	 * @param exitX
	 *            The horizontal tile index of the exit
	 * @param exitY
	 *            The vertical tile index of the exit
	 * @throws SlickException
	 */
	public DungeonMap(TiledMap map, int entranceX, int entranceY, int exitX, int exitY)
	{
		// Create the map normally
		super(map);

		// Save the entrance index
		// this.entranceX = entranceX;
		// this.entranceY = entranceY;
		// this.exitX = exitX;
		// this.exitY = exitY;
		this.entrance = new ExitZone(entranceX, entranceY, getTileWidth(), getTileHeight(), -2);
		this.exit = new ExitZone(exitX, exitY, getTileWidth(), getTileHeight(), -1);
	}
}
