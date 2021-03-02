package com.zyrox.world.clip.region;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPInputStream;

import com.zyrox.model.Direction;
import com.zyrox.model.GameObject;
import com.zyrox.model.Position;
import com.zyrox.model.Locations.Location;
import com.zyrox.model.definitions.GameObjectDefinition;
import com.zyrox.world.World;
import com.zyrox.world.content.CustomObjects;
import com.zyrox.world.content.doors.DoorManager;
import com.zyrox.world.entity.impl.GameCharacter;

/**
 * A highly modified version of the released clipping.
 *
 * @author Relex lawl and Palidino: Gave me (Gabbe) the base.
 * @editor Gabbe: Rewrote the system, now loads regions when they're actually
 *         needed etc.
 */
public final class RegionClipping {

	private static final Map<Integer, RegionClipping> regions = new ConcurrentHashMap<>();

	private int id;

	private int[][][] clips = new int[4][][];

	public static Map<Integer, ArrayList<GameObject>> gameObjects = new HashMap<>();

	public RegionClipping(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public boolean isOSRSRegion() {
		return OSRS_REGIONS.contains(id);
	}

	public void removeClip(int x, int y, int height, int shift) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (height < 0 || height >= 4)
			height = 0;
		if (clips[height] == null) {
			clips[height] = new int[64][64];
		}
		clips[height][x - regionAbsX][y - regionAbsY] = /* 16777215 - shift */0;
	}

	public void addClip(int x, int y, int height, int shift) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (height < 0 || height >= 4)
			height = 0;
		if (clips[height] == null) {
			clips[height] = new int[64][64];
		}
		clips[height][x - regionAbsX][y - regionAbsY] |= shift;
	}

	// TODO: Move to a utility class
	static byte[] degzip(Path path) throws IOException {
		byte[] bytes = Files.readAllBytes(path);
		if (bytes.length == 0) {
			return bytes;
		}

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try (GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(bytes))) {
			byte[] buffer = new byte[Byte.BYTES * 1024];

			while (true) {
				int read = gzip.read(buffer);
				if (read == -1) {
					break;
				}

				os.write(buffer, 0, read);
			}
		}
		return os.toByteArray();
	}

	/**
	 * Reads a 'smart' (either a {@code byte} or {@code short} depending on the
	 * value) from the specified buffer.
	 * 
	 * @param buffer The buffer.
	 * @return The 'smart'.
	 */
	// TODO: Move to a utility class
	public static int readSmart(ByteBuffer buffer) {
		int peek = buffer.get(buffer.position()) & 0xFF;
		if (peek < 128) {
			return buffer.get() & 0xFF;
		}
		return (buffer.getShort() & 0xFFFF) - 32768;
	}

	public static void init() throws IOException {

		GameObjectDefinition.init();

		Path path = Paths.get("data", "clipping");

		// Decodes pre eoc definitions
		ByteBuffer buffer = ByteBuffer.wrap(Files.readAllBytes(path.resolve("map_index")));
		Map<Integer, MapDefinition> preEocMapDefinitions = new HashMap<>();
		int size = buffer.getShort() & 0xFFFF;
		for (int i = 0; i < size; i++) {
			int id = buffer.getShort() & 0xFFFF;
			int terrain = buffer.getShort() & 0xFFFF;
			int objects = buffer.getShort() & 0xFFFF;
			if (id == 14682) {
				terrain = 1870;
				objects = 1871;
			}
			if (id == 9007) {
				terrain = 1938;
				objects = 1939;
			}
			preEocMapDefinitions.put(id, new MapDefinition(id, objects, terrain));
			regions.computeIfAbsent(id, RegionClipping::new);
		}

		int[] regionIds = new int[] { 10791,11815,4919, 5536, 5945, 5946, 6201, 12701, 12702, 12703, 12957, 12958, 12959, 12961, 5023, 5280, 5279, 5022, 5535 };
		int[][] mapData = new int[][] {{ 1666, 1667 },{ 1690, 1691 }, { 2820, 2821 },{ 3472, 3473 }, { 2206, 2207 }, { 2208, 2209 }, { 2242, 2243 }, { 10002, 10003 }, { 10004, 10005 }, { 10006, 10007 }, { 10008, 10009 }, { 10010, 10011 }, { 10012, 10013 }, { 10020, 10021 }, { 10022, 10023 }, { 10024, 10025 }, { 10026, 10027 }, { 10028, 10029 }, { 10030, 10031 } };

		for (int i = 0; i < regionIds.length; i++) {
			preEocMapDefinitions.put(regionIds[i], new MapDefinition(regionIds[i], mapData[i][1], mapData[i][0]));
			regions.computeIfAbsent(regionIds[i], RegionClipping::new);
		}

		// Decodes osrs map definitions
		buffer = ByteBuffer.wrap(Files.readAllBytes(path.resolve("osrs_map_index")));
		Map<Integer, MapDefinition> osrsMapDefinitions = new HashMap<>();
		size = buffer.getShort() & 0xFFFF;
		for (int i = 0; i < size; i++) {
			int id = buffer.getShort() & 0xFFFF;
			int terrain = buffer.getShort() & 0xFFFF;
			int objects = buffer.getShort() & 0xFFFF;
			osrsMapDefinitions.put(id, new MapDefinition(id, objects, terrain));
			regions.computeIfAbsent(id, RegionClipping::new);

			if (OldschoolMaps.isOldschoolRegion(id)) {
				preEocMapDefinitions.put(id, new MapDefinition(id, objects, terrain));
			}

		}

		// Decodes terrain and objects
		for (MapDefinition definition : preEocMapDefinitions.values()) {
			int id = definition.getId();
			int x = (id >> 8) * 64;
			int y = (id & 0xFF) * 64;

			byte[] objects = degzip(path.resolve("maps/pre-eoc").resolve(definition.getObjects() + ".gz"));
			byte[] terrain = degzip(path.resolve("maps/pre-eoc").resolve(definition.getTerrain() + ".gz"));

			boolean oldschool = false;

			if (OldschoolMaps.isOldschoolRegion(id)) {
				MapDefinition oldschoolDefinition = osrsMapDefinitions.get(id);
				try {
					objects = degzip(path.resolve("maps/osrs").resolve(oldschoolDefinition.getObjects() + ".gz"));
					terrain = degzip(path.resolve("maps/osrs").resolve(oldschoolDefinition.getTerrain() + ".gz"));
				} catch (Exception e) {
					System.out.println("Objects for region: [x, y, id, file] - [" + x + ", " + y + ", " + id + ", " + definition.getObjects() + ", "+definition.getTerrain()+")] do not exist.");
					//e.printStackTrace();
				}
				oldschool = true;
			}

			if (objects.length == 0) {
				// System.out.println("Objects for region: [x, y, id, file] - ["
				// + x + ", " + y + ", " + id
				// + ", " + definition.objects + "] do not exist.");
				continue;
			}

			if (terrain.length == 0) {
				// System.out.println("Terrain for region: [x, y, id, file] - ["
				// + x + ", " + y + ", " + id
				// + ", " + definition.terrain + "] does not exist.");
				continue;
			}
			loadMaps(x, y, ByteBuffer.wrap(objects), ByteBuffer.wrap(terrain), oldschool);
		}

		System.out.println("Loaded "+ osrsMapDefinitions.size() + " pre-eoc map definitions and " + osrsMapDefinitions.size() + " osrs map definitions.");
	}

	private static void loadMaps(int absX, int absY, ByteBuffer objectStream, ByteBuffer groundStream, boolean oldschool) {
		byte[][][] heightMap = new byte[4][64][64];
		try {
			for (int z = 0; z < 4; z++) {
				for (int tileX = 0; tileX < 64; tileX++) {
					for (int tileY = 0; tileY < 64; tileY++) {
						while (true) {
							int tileType = groundStream.get() & 0xFF;
							if (tileType == 0) {
								break;
							} else if (tileType == 1) {
								groundStream.get();
								break;
							} else if (tileType <= 49) {
								groundStream.get();
							} else if (tileType <= 81) {
								heightMap[z][tileX][tileY] = (byte) (tileType - 49);
							}
						}
					}
				}
			}
			for (int i = 0; i < 4; i++) {
				for (int i2 = 0; i2 < 64; i2++) {
					for (int i3 = 0; i3 < 64; i3++) {
						if ((heightMap[i][i2][i3] & 1) == 1) {
							int height = i;
							if ((heightMap[1][i2][i3] & 2) == 2) {
								height--;
							}
							if (height >= 0 && height <= 3) {
								addClipping(absX + i2, absY + i3, height, 0x200000);
							}
						}
					}
				}
			}
			int objectId = -1;
			if(oldschool) {
				objectId = 99_999;
			}
			int incr;
			while ((incr = readSmart(objectStream)) != 0) {
				objectId += incr;
				int location = 0;
				int incr2;
				while ((incr2 = readSmart(objectStream)) != 0) {
					location += incr2 - 1;
					int localX = location >> 6 & 0x3f;
					int localY = location & 0x3f;
					int height = location >> 12;
					int objectData = objectStream.get() & 0xFF;
					int type = objectData >> 2;
					int direction = objectData & 0x3;
					if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64) {
						continue;
					}
					if ((heightMap[1][localX][localY] & 2) == 2) {
						height--;
					}

					int regionId = ((((absX >> 3) / 8) << 8) + ((absY >> 3) / 8));

					if (height >= 0 && height <= 3) {
						addObject(objectId, absX + localX, absY + localY, height, type, direction);
						DoorManager.addDoor(objectId, direction, type, new Position(absX + localX, absY + localY, height));
					}
				}
			}
		} catch (Exception cause) {
			System.out.println("Unable to load maps in region: " + ((((absX >> 3) / 8) << 8) + ((absY >> 3) / 8))
					+ " pos: " + absX + ", " + absY);
		}
	}

	public static void addClipping(int x, int y, int height, int shift) {
		 //System.out.println("Added clip at "+x+" and "+y+"");
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = (regionX / 8 << 8) + regionY / 8;
		RegionClipping r = regions.get(regionId);
		if (r != null) {
			r.addClip(x, y, height, shift);
		}
	}

	public static void removeClipping(int x, int y, int height, int shift) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = (regionX / 8 << 8) + regionY / 8;

		RegionClipping r = regions.get(regionId);
		if (r != null) {
			r.removeClip(x, y, height, shift);
		}
	}

	public static void removeClipping(int x, int y, int height) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		if (regionId < 0) {
			return;
		}

		RegionClipping r = regions.get(regionId);
		if (r != null) {
			r.removeClip(x, y, height, 0);
		}
	}

	public static RegionClipping forPosition(Position position) {
		int regionX = position.getX() >> 3;
		int regionY = position.getY() >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		return regions.get(regionId);
	}

	public static boolean objectExists(GameObject object) {
		Position pos = object.getPosition();

		ArrayList<GameObject> objects = gameObjects.getOrDefault(pos.getX(), null);

		if(objects == null)
			return false;

		for(GameObject gameObject : objects) {
			if(gameObject.getPosition().getX() == pos.getX() && gameObject.getPosition().getY() == pos.getY() && gameObject.getId() == object.getId()) {
				return true;
			}
		}

		if (CustomObjects.objectExists(pos)) {
			return true;
		}

		return false;
	}

	public static GameObject getGameObject(Position position) {
		final RegionClipping clipping = forPosition(position);
		if (clipping != null) {
			int x = position.getX();
			int y = position.getY();
			int height = position.getZ();
			int regionAbsX = (clipping.id >> 8) * 64;
			int regionAbsY = (clipping.id & 0xff) * 64;
			if (height < 0 || height >= 4)
				height = 0;

			ArrayList<GameObject> objects = gameObjects.get(x);

			for(GameObject gameObject : objects) {
				if(gameObject.getType() == 22)
					continue;
				if(gameObject.getPosition().equals(position)) {
					return gameObject;
				}
			}

			return null;
		} else {
			return null;
		}
	}

	public static void deleteObjectsOnFloor(int objectId, int floor) {
		ArrayList<GameObject> objectsToDelete = new ArrayList<>();
		for(ArrayList<GameObject> gameObjects : gameObjects.values()) {
			for(GameObject gameObject : gameObjects) {
				if(gameObject == null)
					continue;

				if(gameObject.getPosition().getZ() != floor)
					continue;

				if(gameObject.getId() != objectId)
					continue;

				objectsToDelete.add(gameObject);
			}
		}

		for(GameObject objectToDelete : objectsToDelete) {
			World.deregister(objectToDelete);
		}
	}

	public static GameObject getGameObject(Position position, int... types) {
		final RegionClipping clipping = forPosition(position);
		if (clipping != null) {
			int x = position.getX();
			int y = position.getY();
			int height = position.getZ();
			int regionAbsX = (clipping.id >> 8) * 64;
			int regionAbsY = (clipping.id & 0xff) * 64;
			if (height < 0 || height >= 4)
				height = 0;

			ArrayList<GameObject> objects = gameObjects.get(x);

			for(GameObject gameObject : objects) {
				if(gameObject.getPosition().equals(position)) {
					for(int type : types) {
						if(type == gameObject.getType()) {
							return gameObject;
						}
					}
				}
			}

			return null;
		} else {
			return null;
		}
	}

	private static void addClippingForVariableObject(int x, int y, int height, int type, int direction, boolean flag) {
		if (type == 0) {
			if (direction == 0) {
				addClipping(x, y, height, 128);
				addClipping(x - 1, y, height, 8);
			} else if (direction == 1) {
				addClipping(x, y, height, 2);
				addClipping(x, y + 1, height, 32);
			} else if (direction == 2) {
				addClipping(x, y, height, 8);
				addClipping(x + 1, y, height, 128);
			} else if (direction == 3) {
				addClipping(x, y, height, 32);
				addClipping(x, y - 1, height, 2);
			}
		} else if (type == 1 || type == 3) {
			if (direction == 0) {
				addClipping(x, y, height, 1);
				addClipping(x - 1, y, height, 16);
			} else if (direction == 1) {
				addClipping(x, y, height, 4);
				addClipping(x + 1, y + 1, height, 64);
			} else if (direction == 2) {
				addClipping(x, y, height, 16);
				addClipping(x + 1, y - 1, height, 1);
			} else if (direction == 3) {
				addClipping(x, y, height, 64);
				addClipping(x - 1, y - 1, height, 4);
			}
		} else if (type == 2) {
			if (direction == 0) {
				addClipping(x, y, height, 130);
				addClipping(x - 1, y, height, 8);
				addClipping(x, y + 1, height, 32);
			} else if (direction == 1) {
				addClipping(x, y, height, 10);
				addClipping(x, y + 1, height, 32);
				addClipping(x + 1, y, height, 128);
			} else if (direction == 2) {
				addClipping(x, y, height, 40);
				addClipping(x + 1, y, height, 128);
				addClipping(x, y - 1, height, 2);
			} else if (direction == 3) {
				addClipping(x, y, height, 160);
				addClipping(x, y - 1, height, 2);
				addClipping(x - 1, y, height, 8);
			}
		}
		if (flag) {
			if (type == 0) {
				if (direction == 0) {
					addClipping(x, y, height, 65536);
					addClipping(x - 1, y, height, 4096);
				} else if (direction == 1) {
					addClipping(x, y, height, 1024);
					addClipping(x, y + 1, height, 16384);
				} else if (direction == 2) {
					addClipping(x, y, height, 4096);
					addClipping(x + 1, y, height, 65536);
				} else if (direction == 3) {
					addClipping(x, y, height, 16384);
					addClipping(x, y - 1, height, 1024);
				}
			}
			if (type == 1 || type == 3) {
				if (direction == 0) {
					addClipping(x, y, height, 512);
					addClipping(x - 1, y + 1, height, 8192);
				} else if (direction == 1) {
					addClipping(x, y, height, 2048);
					addClipping(x + 1, y + 1, height, 32768);
				} else if (direction == 2) {
					addClipping(x, y, height, 8192);
					addClipping(x + 1, y + 1, height, 512);
				} else if (direction == 3) {
					addClipping(x, y, height, 32768);
					addClipping(x - 1, y - 1, height, 2048);
				}
			} else if (type == 2) {
				if (direction == 0) {
					addClipping(x, y, height, 66560);
					addClipping(x - 1, y, height, 4096);
					addClipping(x, y + 1, height, 16384);
				} else if (direction == 1) {
					addClipping(x, y, height, 5120);
					addClipping(x, y + 1, height, 16384);
					addClipping(x + 1, y, height, 65536);
				} else if (direction == 2) {
					addClipping(x, y, height, 20480);
					addClipping(x + 1, y, height, 65536);
					addClipping(x, y - 1, height, 1024);
				} else if (direction == 3) {
					addClipping(x, y, height, 81920);
					addClipping(x, y - 1, height, 1024);
					addClipping(x - 1, y, height, 4096);
				}
			}
		}
	}

	private static void addClippingForSolidObject(int x, int y, int height, int xLength, int yLength, boolean flag) {
		int clipping = 256;
		if (flag) {
			clipping += 0x20000;
		}
		for (int i = x; i < x + xLength; i++) {
			for (int i2 = y; i2 < y + yLength; i2++) {
				addClipping(i, i2, height, clipping);
			}
		}
	}

	/**
	 * A hash collection of object {@link Position}'s that have been removed
	 * client-sided and therefore no longer need to be included in clipping.
	 */
	private static final Set<Position> REMOVED_OBJECT_POSITIONS = new HashSet<>(Arrays.asList(
			/* Edgeville */
			new Position(3088, 3509), // Wheelbarrow
			new Position(3090, 3503), // Tree
			new Position(3095, 3498), // Table
			new Position(3091, 3495), // Bank chairs
			new Position(3092, 3488) // Table
	));

	public static void addObject(int objectId, int x, int y, int height, int type, int direction) {
		if (GameObjectDefinition.removedObject(objectId)) {
			return;
		}

		final Position position = new Position(x, y, height);
		final RegionClipping clipping = forPosition(position);

		GameObjectDefinition def = GameObjectDefinition.getDefinition(objectId);

		if (def == null) {
			return;
		}

		if (REMOVED_OBJECT_POSITIONS.contains(new Position(x, y, height))) {
			return;
		}

		switch (objectId) {
		case 14233: // pest control gates
		case 14235: // pest control gates
			return;
		}

		if (clipping != null) {

			if(!gameObjects.containsKey(position.getX())) {
				gameObjects.put(position.getX(), new ArrayList<GameObject>());
			}

			gameObjects.get(position.getX()).add(new GameObject(objectId, new Position(x, y, height), type, direction));

		}

		if (objectId == -1) {
			removeClipping(x, y, height, 0x000000);
			return;
		}

		int xLength;
		int yLength;
		if (direction != 1 && direction != 3) {
			xLength = def.xLength();
			yLength = def.yLength();
		} else {
			yLength = def.xLength();
			xLength = def.yLength();
		}
		if (type == 22) {
			if (def.hasActions() && def.unwalkable) {
				addClipping(x, y, height, 0x200000);
			}
		} else if (type >= 9) {
			if (def.unwalkable) {
				addClippingForSolidObject(x, y, height, xLength, yLength, def.aBoolean779);
			}
		} else if (type >= 0 && type <= 3) {
			if (def.unwalkable) {
				addClippingForVariableObject(x, y, height, type, direction, def.aBoolean779);
			}
		}
	}

	public static void addObject(GameObject gameObject) {
		if (gameObject.getId() != 65535 && gameObject.getId() != 32000 && gameObject.getId() != 30032)
			addObject(gameObject.getId(), gameObject.getPosition().getX(), gameObject.getPosition().getY(),
					gameObject.getPosition().getZ(), gameObject.getType(), gameObject.getFace());
	}

	public static void removeObject(GameObject gameObject) {
		if (gameObject.getId() != 32000 && gameObject.getId() != 30032)
			addObject(-1, gameObject.getPosition().getX(), gameObject.getPosition().getY(),
					gameObject.getPosition().getZ(), gameObject.getType(), gameObject.getFace());
	}

	public static int getClipping(int x, int y, int height) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		if (height >= 4)
			height = 0;
		else if (height == -1 || Location.inLocation(x, y, Location.PURO_PURO))
			return 0;
		RegionClipping r = regions.get(regionId);
		if (r != null) {
			return r.getClip(x, y, height);
		}
		return 0;
	}

	private int getClip(int x, int y, int height) {
		// height %= 4;
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (clips[height] == null) {
			clips[height] = new int[64][64];
		}
		return clips[height][x - regionAbsX][y - regionAbsY];
	}

	public static boolean canMove(int startX, int startY, int endX, int endY, int height, int xLength, int yLength) {
		int diffX = endX - startX;
		int diffY = endY - startY;
		int max = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (int ii = 0; ii < max; ii++) {
			int currentX = endX - diffX;
			int currentY = endY - diffY;
			for (int i = 0; i < xLength; i++) {
				for (int i2 = 0; i2 < yLength; i2++)
					if (diffX < 0 && diffY < 0) {
						if ((getClipping((currentX + i) - 1, (currentY + i2) - 1, height) & 0x128010e) != 0
								|| (getClipping((currentX + i) - 1, currentY + i2, height) & 0x1280108) != 0
								|| (getClipping(currentX + i, (currentY + i2) - 1, height) & 0x1280102) != 0)
							return false;
					} else if (diffX > 0 && diffY > 0) {
						if ((getClipping(currentX + i + 1, currentY + i2 + 1, height) & 0x12801e0) != 0
								|| (getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0
								|| (getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0)
							return false;
					} else if (diffX < 0 && diffY > 0) {
						if ((getClipping((currentX + i) - 1, currentY + i2 + 1, height) & 0x1280138) != 0
								|| (getClipping((currentX + i) - 1, currentY + i2, height) & 0x1280108) != 0
								|| (getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0)
							return false;
					} else if (diffX > 0 && diffY < 0) {
						if ((getClipping(currentX + i + 1, (currentY + i2) - 1, height) & 0x1280183) != 0
								|| (getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0
								|| (getClipping(currentX + i, (currentY + i2) - 1, height) & 0x1280102) != 0)
							return false;
					} else if (diffX > 0 && diffY == 0) {
						if ((getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0)
							return false;
					} else if (diffX < 0 && diffY == 0) {
						if ((getClipping((currentX + i) - 1, currentY + i2, height) & 0x1280108) != 0)
							return false;
					} else if (diffX == 0 && diffY > 0) {
						if ((getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0)
							return false;
					} else if (diffX == 0 && diffY < 0
							&& (getClipping(currentX + i, (currentY + i2) - 1, height) & 0x1280102) != 0)
						return false;

			}

			if (diffX < 0)
				diffX++;
			else if (diffX > 0)
				diffX--;
			if (diffY < 0)
				diffY++;
			else if (diffY > 0)
				diffY--;
		}

		return true;
	}

	public static boolean canMove(Position start, Position end, int xLength, int yLength) {
		return canMove(start.getX(), start.getY(), end.getX(), end.getY(), start.getZ(), xLength, yLength);
	}

	public static boolean canMove(Position position, Direction direction) {
		switch (direction) {
		case NORTH_WEST:
			return !blockedNorthWest(position) && !blockedNorth(position) && !blockedWest(position);
		case NORTH:
			return !blockedNorth(position);
		case NORTH_EAST:
			return !blockedNorthEast(position) && !blockedNorth(position) && !blockedEast(position);
		case WEST:
			return !blockedWest(position);
		case EAST:
			return !blockedEast(position);
		case SOUTH_WEST:
			return !blockedSouthWest(position) && !blockedSouth(position) && !blockedWest(position);
		case SOUTH:
			return !blockedSouth(position);
		case SOUTH_EAST:
			return !blockedSouthEast(position) && !blockedSouth(position) && !blockedEast(position);
		default:
			return false;
		}
	}

	public static boolean blockedProjectile(Position position) {
		return (getClipping(position.getX(), position.getY(), position.getZ()) & 0x20000) == 0;
	}

	public static boolean blocked(Position pos) {
		return (getClipping(pos.getX(), pos.getY(), pos.getZ()) & 0x1280120) != 0;
	}

	public static boolean blockedNorth(Position pos) {
		return (getClipping(pos.getX(), pos.getY() + 1, pos.getZ()) & 0x1280120) != 0;
	}

	public static boolean blockedEast(Position pos) {
		return (getClipping(pos.getX() + 1, pos.getY(), pos.getZ()) & 0x1280180) != 0;
	}

	public static boolean blockedSouth(Position pos) {
		return (getClipping(pos.getX(), pos.getY() - 1, pos.getZ()) & 0x1280102) != 0;
	}

	public static boolean blockedWest(Position pos) {
		return (getClipping(pos.getX() - 1, pos.getY(), pos.getZ()) & 0x1280108) != 0;
	}

	public static boolean blockedNorthEast(Position pos) {
		return (getClipping(pos.getX() + 1, pos.getY() + 1, pos.getZ()) & 0x12801e0) != 0;
	}

	public static boolean blockedNorthWest(Position pos) {
		return (getClipping(pos.getX() - 1, pos.getY() + 1, pos.getZ()) & 0x1280138) != 0;
	}

	public static boolean blockedSouthEast(Position pos) {
		return (getClipping(pos.getX() + 1, pos.getY() - 1, pos.getZ()) & 0x1280183) != 0;
	}

	public static boolean blockedSouthWest(Position pos) {
		return (getClipping(pos.getX() - 1, pos.getY() - 1, pos.getZ()) & 0x128010e) != 0;
	}

	public static boolean canProjectileAttack(GameCharacter a, GameCharacter b) {
		if (!a.isPlayer()) {
			if (b.isPlayer()) {
				return canProjectileMove(b.getPosition().getX(), b.getPosition().getY(), a.getPosition().getX(),
						a.getPosition().getY(), a.getPosition().getZ(), 1, 1);
			}
		}
		return canProjectileMove(a.getPosition().getX(), a.getPosition().getY(), b.getPosition().getX(),
				b.getPosition().getY(), a.getPosition().getZ(), 1, 1);
	}

	public static boolean canProjectileMove(int startX, int startY, int endX, int endY, int height, int xLength,
			int yLength) {
		int diffX = endX - startX;
		int diffY = endY - startY;
		// height %= 4;
		int max = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (int ii = 0; ii < max; ii++) {
			int currentX = endX - diffX;
			int currentY = endY - diffY;
			for (int i = 0; i < xLength; i++) {
				for (int i2 = 0; i2 < yLength; i2++) {
					if (diffX < 0 && diffY < 0) {
						if ((RegionClipping.getClipping(currentX + i - 1, currentY + i2 - 1, height) & (UNLOADED_TILE
								| /* BLOCKED_TILE | */UNKNOWN | PROJECTILE_TILE_BLOCKED | PROJECTILE_EAST_BLOCKED
								| PROJECTILE_NORTH_EAST_BLOCKED | PROJECTILE_NORTH_BLOCKED)) != 0
								|| (RegionClipping.getClipping(currentX + i - 1, currentY + i2, height)
										& (UNLOADED_TILE | /* BLOCKED_TILE | */UNKNOWN | PROJECTILE_TILE_BLOCKED
												| PROJECTILE_EAST_BLOCKED)) != 0
								|| (RegionClipping.getClipping(currentX + i, currentY + i2 - 1, height)
										& (UNLOADED_TILE | /* BLOCKED_TILE | */UNKNOWN | PROJECTILE_TILE_BLOCKED
												| PROJECTILE_NORTH_BLOCKED)) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY > 0) {
						if ((RegionClipping.getClipping(currentX + i + 1, currentY + i2 + 1, height) & (UNLOADED_TILE
								| /* BLOCKED_TILE | */UNKNOWN | PROJECTILE_TILE_BLOCKED | PROJECTILE_WEST_BLOCKED
								| PROJECTILE_SOUTH_WEST_BLOCKED | PROJECTILE_SOUTH_BLOCKED)) != 0
								|| (RegionClipping.getClipping(currentX + i + 1, currentY + i2, height)
										& (UNLOADED_TILE | /* BLOCKED_TILE | */UNKNOWN | PROJECTILE_TILE_BLOCKED
												| PROJECTILE_WEST_BLOCKED)) != 0
								|| (RegionClipping.getClipping(currentX + i, currentY + i2 + 1, height)
										& (UNLOADED_TILE | /* BLOCKED_TILE | */UNKNOWN | PROJECTILE_TILE_BLOCKED
												| PROJECTILE_SOUTH_BLOCKED)) != 0) {
							return false;
						}
					} else if (diffX < 0 && diffY > 0) {
						if ((RegionClipping.getClipping(currentX + i - 1, currentY + i2 + 1, height) & (UNLOADED_TILE
								| /* BLOCKED_TILE | */UNKNOWN | PROJECTILE_TILE_BLOCKED | PROJECTILE_SOUTH_BLOCKED
								| PROJECTILE_SOUTH_EAST_BLOCKED | PROJECTILE_EAST_BLOCKED)) != 0
								|| (RegionClipping.getClipping(currentX + i - 1, currentY + i2, height)
										& (UNLOADED_TILE | /* BLOCKED_TILE | */UNKNOWN | PROJECTILE_TILE_BLOCKED
												| PROJECTILE_EAST_BLOCKED)) != 0
								|| (RegionClipping.getClipping(currentX + i, currentY + i2 + 1, height)
										& (UNLOADED_TILE | /* BLOCKED_TILE | */UNKNOWN | PROJECTILE_TILE_BLOCKED
												| PROJECTILE_SOUTH_BLOCKED)) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY < 0) {
						if ((RegionClipping.getClipping(currentX + i + 1, currentY + i2 - 1, height) & (UNLOADED_TILE
								| /* BLOCKED_TILE | */UNKNOWN | PROJECTILE_TILE_BLOCKED | PROJECTILE_WEST_BLOCKED
								| PROJECTILE_NORTH_BLOCKED | PROJECTILE_NORTH_WEST_BLOCKED)) != 0
								|| (RegionClipping.getClipping(currentX + i + 1, currentY + i2, height)
										& (UNLOADED_TILE | /* BLOCKED_TILE | */UNKNOWN | PROJECTILE_TILE_BLOCKED
												| PROJECTILE_WEST_BLOCKED)) != 0
								|| (RegionClipping.getClipping(currentX + i, currentY + i2 - 1, height)
										& (UNLOADED_TILE | /* BLOCKED_TILE | */UNKNOWN | PROJECTILE_TILE_BLOCKED
												| PROJECTILE_NORTH_BLOCKED)) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY == 0) {
						if ((RegionClipping.getClipping(currentX + i + 1, currentY + i2, height)
								& (UNLOADED_TILE | /* BLOCKED_TILE | */UNKNOWN | PROJECTILE_TILE_BLOCKED
										| PROJECTILE_WEST_BLOCKED)) != 0) {
							return false;
						}
					} else if (diffX < 0 && diffY == 0) {
						if ((RegionClipping.getClipping(currentX + i - 1, currentY + i2, height)
								& (UNLOADED_TILE | /* BLOCKED_TILE | */UNKNOWN | PROJECTILE_TILE_BLOCKED
										| PROJECTILE_EAST_BLOCKED)) != 0) {
							return false;
						}
					} else if (diffX == 0 && diffY > 0) {
						if ((RegionClipping.getClipping(currentX + i, currentY + i2 + 1, height) & (UNLOADED_TILE
								| /*
									 * BLOCKED_TILE |
									 */UNKNOWN | PROJECTILE_TILE_BLOCKED | PROJECTILE_SOUTH_BLOCKED)) != 0) {
							return false;
						}
					} else if (diffX == 0 && diffY < 0) {
						if ((RegionClipping.getClipping(currentX + i, currentY + i2 - 1, height) & (UNLOADED_TILE
								| /*
									 * BLOCKED_TILE |
									 */UNKNOWN | PROJECTILE_TILE_BLOCKED | PROJECTILE_NORTH_BLOCKED)) != 0) {
							return false;
						}
					}
				}
			}
			if (diffX < 0) {
				diffX++;
			} else if (diffX > 0) {
				diffX--;
			}
			if (diffY < 0) {
				diffY++; // change
			} else if (diffY > 0) {
				diffY--;
			}
		}
		return true;
	}

	public static void removeClippingForVariableObject(int x, int y, int height, int type, int direction, boolean flag) {
		if (type == 0) {
			if (direction == 0) {
				removeClipping(x, y, height);
				if(flag)
					removeClipping(x, y + 1, height);
				else
					removeClipping(x - 1, y, height);
			} else if (direction == 1) {
				removeClipping(x, y, height);
				if(flag)
					removeClipping(x - 1, y, height);
				else
					removeClipping(x, y + 1, height);
			} else if (direction == 2) {
				removeClipping(x, y, height);
				if(flag)
					removeClipping(x, y - 1, height);
				else
					removeClipping(x + 1, y, height);
			} else if (direction == 3) {
				removeClipping(x, y, height);
				if(flag)
					removeClipping(x + 1, y, height);
				else
					removeClipping(x, y - 1, height);
			}
		} else if (type == 1 || type == 3) {
			if (direction == 0) {
				removeClipping(x, y, height);
				removeClipping(x - 1, y, height);
			} else if (direction == 1) {
				removeClipping(x, y, height);
				removeClipping(x + 1, y + 1, height);
			} else if (direction == 2) {
				removeClipping(x, y, height);
				removeClipping(x + 1, y - 1, height);
			} else if (direction == 3) {
				removeClipping(x, y, height);
				removeClipping(x - 1, y - 1, height);
			}
		} else if (type == 2) {
			if (direction == 0) {
				removeClipping(x, y, height);
				removeClipping(x - 1, y, height);
				removeClipping(x, y + 1, height);
			} else if (direction == 1) {
				removeClipping(x, y, height);
				removeClipping(x, y + 1, height);
				removeClipping(x + 1, y, height);
			} else if (direction == 2) {
				removeClipping(x, y, height);
				removeClipping(x + 1, y, height);
				removeClipping(x, y - 1, height);
			} else if (direction == 3) {
				removeClipping(x, y, height);
				removeClipping(x, y - 1, height);
				removeClipping(x - 1, y, height);
			}
		}
	}

	public final static boolean isInDiagonalBlock(GameCharacter attacked, GameCharacter attacker) {
		return attacked.getPosition().getX() - 1 == attacker.getPosition().getX()
				&& attacked.getPosition().getY() + 1 == attacker.getPosition().getY()
				|| attacker.getPosition().getX() - 1 == attacked.getPosition().getX()
						&& attacker.getPosition().getY() + 1 == attacked.getPosition().getY()
				|| attacked.getPosition().getX() + 1 == attacker.getPosition().getX()
						&& attacked.getPosition().getY() - 1 == attacker.getPosition().getY()
				|| attacker.getPosition().getX() + 1 == attacked.getPosition().getX()
						&& attacker.getPosition().getY() - 1 == attacked.getPosition().getY()
				|| attacked.getPosition().getX() + 1 == attacker.getPosition().getX()
						&& attacked.getPosition().getY() + 1 == attacker.getPosition().getY()
				|| attacker.getPosition().getX() + 1 == attacked.getPosition().getX()
						&& attacker.getPosition().getY() + 1 == attacked.getPosition().getY();
	}

	public static final int PROJECTILE_NORTH_WEST_BLOCKED = 0x200;
	public static final int PROJECTILE_NORTH_BLOCKED = 0x400;
	public static final int PROJECTILE_NORTH_EAST_BLOCKED = 0x800;
	public static final int PROJECTILE_EAST_BLOCKED = 0x1000;
	public static final int PROJECTILE_SOUTH_EAST_BLOCKED = 0x2000;
	public static final int PROJECTILE_SOUTH_BLOCKED = 0x4000;
	public static final int PROJECTILE_SOUTH_WEST_BLOCKED = 0x8000;
	public static final int PROJECTILE_WEST_BLOCKED = 0x10000;
	public static final int PROJECTILE_TILE_BLOCKED = 0x20000;
	public static final int UNKNOWN = 0x80000;
	public static final int BLOCKED_TILE = 0x200000;
	public static final int UNLOADED_TILE = 0x1000000;
	public static final int OCEAN_TILE = 2097152;


	private static Set<Integer> OSRS_REGIONS = new HashSet<>(Arrays.asList(4919, 5945, 5946, 6201, 5536, 4663, 6810, 9007, 9023, 9043, 11850, 11851, 12090, 12106, 12347, 12362, 12363, 12605, 12611,
			12701, 12702, 12703, 12861, 12887, 12889, 12957, 12958, 12959, 12961));
}