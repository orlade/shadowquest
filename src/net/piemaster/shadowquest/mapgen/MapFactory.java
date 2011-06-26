package net.piemaster.shadowquest.mapgen;

import net.piemaster.shadowquest.world.DungeonMap;
import net.piemaster.shadowquest.world.Map;

/** Procedurally generates a cave given a few variables */
public abstract class MapFactory
{
	private static final int TILE_FLOOR = 3;
	private static final int TILE_WALL = 2;

	GenParams params;
	GenParams[] params_set;

	/**
	 * Procedurally generates a cave map
	 * 
	 * @param width
	 *            The width of the map (tiles)
	 * @param height
	 *            The height of the map (tiles)
	 * @return The generated map
	 * @throws Exception
	 */
	public static Map generateCave(int width, int height, boolean hasExit) throws Exception
	{
		// Probability of a tile starting off blocked
		int fillProb = 40;

		// Set up params (r1, r2, reps)
		GenParams[] paramsList = new GenParams[2];
		paramsList[0] = new GenParams(5, 2, 4);
		paramsList[1] = new GenParams(5, 0, 3);

		// Initialise the map
		int[][] currentGrid = initMapRandom(width, height, fillProb);

		// Run the procedure on the map
		for (int i = 0; i < paramsList.length; i++)
		{
			GenParams params = paramsList[i];
			for (int j = 0; j < params.reps; j++)
			{
				currentGrid = runCave(width, height, currentGrid, params);
			}
		}
		currentGrid = insertEdges(width, height, currentGrid);

		// Create an entrance
		System.out.println("[MapFactory] Creating entrance...");
		int entranceX, entranceY;
		do
		{
			entranceX = (int) (Math.random() * (width - 2) + 1);
			entranceY = (int) (Math.random() * (height - 2) + 1);
		}
		while (!isPassable(currentGrid[entranceY][entranceX]));
		// Insert the entrance tile
		currentGrid[entranceY][entranceX] = 17;
		System.out.println("MapFactory] Entrance created @ [" + entranceX + ", " + entranceY + "]");

		int exitX = 0;
		int exitY = 0;
		if (hasExit)
		{
			// Create an exit
			System.out.println("[MapFactory] Creating entrance...");

			do
			{
				exitX = (int) (Math.random() * (width - 2) + 1);
				exitY = (int) (Math.random() * (height - 2) + 1);
			}
			while (!isPassable(currentGrid[exitY][exitX]));
			// Insert the entrance tile
			currentGrid[exitY][exitX] = 17;
			System.out.println("MapFactory] Exit created @ [" + exitX + ", " + exitY + "]");
		}

		// Create a raw map of the current grid
		RawMap raw = new RawMap(currentGrid, 72, 72);
		// DEBUG: Print the map
		// System.out.println(raw.toGraphicString());

		// Pass it to be serialised
		writeMap(raw, "assets/auto_dungeon.tmx");

		return new DungeonMap("assets/auto_dungeon.tmx", "assets", entranceX, entranceY, exitX,
				exitY);
	}

	/** Returns a random tile based on the fillProb */
	private static int pickRandomTile(int fillProb)
	{
		if ((int) (Math.random() * 100) < fillProb)
			return TILE_WALL;
		else
			return TILE_FLOOR;
	}

	/** Initialises the map with random tiles */
	private static int[][] initMapRandom(int width, int height, int fillProb)
	{
		// Create a grid to store the tiles in
		int[][] mapGrid = new int[height][width];

		// Set tiles randomly
		for (int y = 1; y < height - 1; y++)
			for (int x = 1; x < width - 1; x++)
				mapGrid[y][x] = pickRandomTile(fillProb);

		mapGrid = insertEdges(width, height, mapGrid);

		return mapGrid;
	}

	/** Runs the generation procedure for a single generation, evolving the map */
	private static int[][] runCave(int width, int height, int[][] currentGrid, GenParams params)
	{
		// int[][] newGrid = currentGrid;
		int[][] newGrid = new int[height][width];

		for (int y = 1; y < height - 1; y++)
			for (int x = 1; x < width - 1; x++)
			{
				int adjcount_r1 = 0, adjcount_r2 = 0;

				for (int i = -1; i <= 1; i++)
				{
					for (int j = -1; j <= 1; j++)
					{
						if (currentGrid[y + i][x + j] != TILE_FLOOR)
							adjcount_r1++;
					}
				}

				for (int i = y - 2; i <= y + 2; i++)
					for (int j = x - 2; j <= x + 2; j++)
					{
						if (Math.abs(i - y) == 2 && Math.abs(j - x) == 2)
							continue;
						if (i < 0 || j < 0 || i >= height || j >= width)
							continue;
						if (currentGrid[i][j] != TILE_FLOOR)
							adjcount_r2++;
					}
				if (adjcount_r1 >= params.r1_cutoff || adjcount_r2 <= params.r2_cutoff)
					newGrid[y][x] = TILE_WALL;
				else
					newGrid[y][x] = TILE_FLOOR;
			}

		return newGrid;
	}

	private static int[][] insertEdges(int width, int height, int[][] mapGrid)
	{
		// Create walls along the edges
		for (int y = 0; y < height; y++)
		{
			mapGrid[y][0] = TILE_WALL;
			mapGrid[y][width - 1] = TILE_WALL;
		}
		for (int x = 0; x < width; x++)
		{
			mapGrid[0][x] = TILE_WALL;
			mapGrid[height - 1][x] = TILE_WALL;
		}

		return mapGrid;
	}

	/**
	 * Checks whether a tile ID is passable or not
	 * 
	 * @param id
	 *            The id to check
	 * @return whether a tile of that id is passable
	 */
	private static boolean isPassable(int id)
	{
		final int[] PASSABLE = { 3, 4, 5, 6, 8, 9, 10, 11, 16, 17, 18 };
		// final int[] IMPASSABLE = {1,2,7,12,13,14,15,19,20};

		for (int i : PASSABLE)
		{
			if (i == id)
				return true;
		}
		return false;
	}

	/**
	 * Serialises the map to a Slick-readable .tmx file
	 * 
	 * @throws Exception
	 */
	private static void writeMap(RawMap raw, String filename) throws Exception
	{
		XMLMapWriter.writeMap(raw, filename);
	}
}