package com.varrock.model.definitions;

import com.varrock.world.clip.stream.ByteStreamExt;
import com.varrock.world.clip.stream.MemoryArchive;


// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class GameObjectDefinition {
	
	public static GameObjectDefinition class46;
	
	public boolean rangableObject() {
		int[] rangableObjects = {3007, 980, 4262, 14437, 14438, 4437, 4439, 3487, 3457};
		for (int i : rangableObjects) {
			if (i == id) {
				return true;
			}
		}
		if (name != null) {
			final String name1 = name.toLowerCase();
			String[] rangables = {"altar", "pew", "log", "stump", "stool", "sign", "cart", "chest", "rock", "bush", "hedge", "chair", "table", "crate", "barrel", "box", "skeleton", "corpse", "vent", "stone", "rockslide"};
			for (String i : rangables) {
				if (name1.contains(i)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean removedObject(int id) {
		return id == 102102 || id == 102104 || id == 111369 || id == 102100 || id == 7332 || id == 7326 || id == 7325 || id == 7385  || id == 125383 || id == 7331 || id == 7385 || id == 7320 || id == 7317 || id == 7323 || id == 7354;
	}

	private static GameObjectDefinition getOldschoolDefinition(int id) {
		if(id < 0) {
			return null;
		}
		for(int j = 0; j < 20; j++) {
			if(cache[j].id == id) {
				return cache[j];
			}
		}
		cacheIndex = (cacheIndex + 1) % 20;
		GameObjectDefinition object = cache[cacheIndex];
		/*Removing doors etc*/
		if(removedObject(id)) {
			object.unwalkable = false;
			return object;
		}
		dataBufferOSRS.currentOffset = streamIndicesOSRS[id - 100_000];
		object.id = id;
		object.nullLoader();
		try {
			object.readValuesOSRS(dataBufferOSRS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (id == 34573) {
			object.unwalkable = false;
	    }

		if (id == 104470) {
			object.unwalkable = false;
	    }
		
		if (id == 131561) {
			object.tileSizeX = 2;
			object.tileSizeY = 2;
		}
		
		return object;
	}
	
	private static GameObjectDefinition get667Definition(int id) {
		if (id > totalObjects667 || id > streamIndices667.length - 1) {
			id = 0;
		}
		for(int j = 0; j < 20; j++) {
			if(cache[j].id == id) {
				return cache[j];
			}
		}
		cacheIndex = (cacheIndex + 1) % 20;
		GameObjectDefinition object = cache[cacheIndex];
		/*Removing doors etc*/
		if(removedObject(id)) {
			object.unwalkable = false;
			return object;
		}
		dataBuffer667.currentOffset = streamIndices667[id];
		object.id = id;
		object.nullLoader();
		try {
		object.readValues667(dataBuffer667);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}

	public static GameObjectDefinition getDefinition(int i) {

		if (i != -1) {

			if (i >= 100_000){
				return getOldschoolDefinition(i);
			}

			if (i >= streamIndices525.length) {
				return get667Definition(i);
			}
		}

		for (int j = 0; j < 20; j++) {
			if (cache[j].id == i) {
				return cache[j];
			}
		}
		
		cacheIndex = (cacheIndex + 1) % 20;
		class46 = cache[cacheIndex];

		if (i == -1) {
			i = 6951;
		}
		
		if (i > streamIndices525.length - 1 || i < 0) {
			return null;
		}
		
		dataBuffer525.currentOffset = streamIndices525[i];

		class46.id = i;
		class46.nullLoader();
		class46.readValues(dataBuffer525);
		return class46;
	}
	
	   public int[] solidObjects = {1902,1903,1904,1905,
			   1906,1907,1908,1909,1910,1911,1912,1536,1535,1537,1538,5139,5140,5141,5142,5143,5144,5145,5146,5147,5148,5149,5150,
			   1534,1533,1532,1531,1530,1631,1624,733,1658,1659,1631,1620,14723,14724,14726,14622,14625,14627,11668,11667,
			   14543,14749,14561,14750,14752,14751,1547,1548,1415,1508,1506,1507,1509,1510,1511,1512,1513,1514,1515,1516,
			   1517,1518,1519,1520,1521,1522,1523,1524,1525,1526,1527,1528,1529,1505,1504,3155,3154,3152,10748,9153,9154,
			   9473,1602,1603,1601,1600,9544,9563,9547,2724,6966,6965,9587,9588,9626,9627,9596,9598,11712,11713,11773,11776,
			   11652,11818,11716,11721,14409,11715,11714,11825,11409,11826,11819,14411,14410,11719,11717,14402,11828,11772,
			   11775,11686,12278,1853,11611,11610,11609,11608,11607,11561,11562,11563,11564,11558,11616,11617,11625,11624,12990,
			   12991,5634,1769,1770,135,134,11536,11512,11529,11513,11521,11520,11519,11518,11517,11516,11514,11509,11538,11537,
			   11470,11471,136,11528,11529,11530,11531,1854,1000,9265,9264,1591,11708,11709,11851, 29885};
			       public void setSolid(int type) {
			   	aBoolean779 = false;
			   		for(int i = 0; i < solidObjects.length;i++) {
			   			if(type == solidObjects[i]) {
			   				unwalkable = true;
			   				aBoolean779 = true;
			   				continue;
			   			}
			   		}

			       }

	public void nullLoader() {
		modelArray = null;
		objectModelType = null;
		name = null;
		description = null;
		modifiedModelColors = null;
		originalModelColors = null;
		tileSizeX = 1;
		tileSizeY = 1;
		unwalkable = true;
		impenetrable = true;
		interactive = false;
		aBoolean762 = false;
		aBoolean769 = false;
		aBoolean764 = false;
		anInt781 = -1;
		anInt775 = 16;
		aByte737 = 0;
		aByte742 = 0;
		actions = null;
		anInt746 = -1;
		anInt758 = -1;
		aBoolean751 = false;
		aBoolean779 = true;
		anInt748 = 128;
		anInt772 = 128;
		anInt740 = 128;
		anInt768 = 0;
		anInt738 = 0;
		anInt745 = 0;
		anInt783 = 0;
		aBoolean736 = false;
		aBoolean766 = false;
		anInt760 = -1;
		anInt774 = -1;
		anInt749 = -1;
		childrenIDs = null;
	}
	
	private static ByteStreamExt dataBuffer525;
	private static ByteStreamExt dataBuffer667;
	private static ByteStreamExt dataBufferOSRS;
	
	public static int totalObjects667;
	public static int totalObjectsOSRS;

	public static void init() {
		//long startup = System.currentTimeMillis();
		//System.out.println("Loading cache game object definitions...");
		
		dataBuffer525 = new ByteStreamExt(getBuffer("loc.dat"));
		ByteStreamExt idxBuffer525 = new ByteStreamExt(getBuffer("loc.idx"));
		
		dataBuffer667 = new ByteStreamExt(getBuffer("667loc.dat"));
		ByteStreamExt idxBuffer667 = new ByteStreamExt(getBuffer("667loc.idx"));

		dataBufferOSRS = new ByteStreamExt(getBuffer("OSRSloc.dat"));
		ByteStreamExt idxBufferOSRS = new ByteStreamExt(getBuffer("OSRSloc.idx"));
		
		int totalObjects525 = idxBuffer525.readUnsignedWord();
		totalObjects667 = idxBuffer667.readUnsignedWord();
		totalObjectsOSRS = idxBufferOSRS.readUnsignedWord();
				
		streamIndices525 = new int[totalObjects525];
		int i = 2;
		for (int j = 0; j < totalObjects525; j++) {
			streamIndices525[j] = i;
			i += idxBuffer525.readUnsignedWord();
		}
		
		streamIndices667 = new int[totalObjects667];
		
		i = 2;
		for (int j = 0; j < totalObjects667; j++) {
			streamIndices667[j] = i;
			i += idxBuffer667.readUnsignedWord();
		}

		streamIndicesOSRS = new int[totalObjectsOSRS];

		i = 2;
		for (int j = 0; j < totalObjectsOSRS; j++) {
			streamIndicesOSRS[j] = i;
			i += idxBufferOSRS.readUnsignedWord();
		}
		
		cache = new GameObjectDefinition[20];
		for (int k = 0; k < 20; k++) {
			cache[k] = new GameObjectDefinition();
		}
	}

	public static byte[] getBuffer(String s) {
		try {
			java.io.File f = new java.io.File("./data/clipping/objects/" + s);
			if (!f.exists())
				return null;
			byte[] buffer = new byte[(int) f.length()];
			java.io.DataInputStream dis = new java.io.DataInputStream(new java.io.FileInputStream(f));
			dis.readFully(buffer);
			dis.close();
			return buffer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void readValues(ByteStreamExt buffer) {
		int i = -1;
		label0: do {
			int opcode;
			do {
				opcode = buffer.readUnsignedByte();
				if (opcode == 0)
					break label0;
				if (opcode == 1) {
					int k = buffer.readUnsignedByte();
					if (k > 0)
						if (modelArray == null || lowMem) {
							objectModelType = new int[k];
							modelArray = new int[k];
							for (int k1 = 0; k1 < k; k1++) {
								modelArray[k1] = buffer.readUnsignedWord();
								objectModelType[k1] = buffer.readUnsignedByte();
							}
						} else {
							buffer.currentOffset += k * 3;
						}
				} else if (opcode == 2)
					name = buffer.readString();
				else if (opcode == 3)
					description = buffer.readBytes();
				else if (opcode == 5) {
					int l = buffer.readUnsignedByte();
					if (l > 0)
						if (modelArray == null || lowMem) {
							objectModelType = null;
							modelArray = new int[l];
							for (int l1 = 0; l1 < l; l1++)
								modelArray[l1] = buffer.readUnsignedWord();
						} else {
							;// buffer.currentOffset += l * 2;
						}
				} else if (opcode == 14)
					tileSizeX = buffer.readUnsignedByte();
				else if (opcode == 15)
					tileSizeY = buffer.readUnsignedByte();
				else if (opcode == 17)
					unwalkable = false;
				else if (opcode == 18)
					impenetrable = false;
				else if (opcode == 19) {
					i = buffer.readUnsignedByte();
					if (i == 1)
						interactive = true;
				} else if (opcode == 21)
					aBoolean762 = true;
				else if (opcode == 22)
					aBoolean769 = false;//
				else if (opcode == 23)
					aBoolean764 = true;
				else if (opcode == 24) {
					anInt781 = buffer.readUnsignedWord();
					if (anInt781 == 65535)
						anInt781 = -1;
				} else if (opcode == 28)
					anInt775 = buffer.readUnsignedByte();
				else if (opcode == 29)
					aByte737 = buffer.readSignedByte();
				else if (opcode == 39)
					aByte742 = buffer.readSignedByte();
				else if (opcode >= 30 && opcode < 39) {
					if (actions == null)
						actions = new String[10];
					actions[opcode - 30] = buffer.readString();
					if (actions[opcode - 30].equalsIgnoreCase("hidden"))
						actions[opcode - 30] = null;
				} else if (opcode == 40) {
					int i1 = buffer.readUnsignedByte();
					modifiedModelColors = new int[i1];
					originalModelColors = new int[i1];
					for (int i2 = 0; i2 < i1; i2++) {
						modifiedModelColors[i2] = buffer.readUnsignedWord();
						originalModelColors[i2] = buffer.readUnsignedWord();
					}
				} else if (opcode == 60)
					anInt746 = buffer.readUnsignedWord();
				else if (opcode == 62)
					aBoolean751 = true;
				else if (opcode == 64)
					aBoolean779 = false;
				else if (opcode == 65)
					anInt748 = buffer.readUnsignedWord();
				else if (opcode == 66)
					anInt772 = buffer.readUnsignedWord();
				else if (opcode == 67)
					anInt740 = buffer.readUnsignedWord();
				else if (opcode == 68)
					anInt758 = buffer.readUnsignedWord();
				else if (opcode == 69)
					anInt768 = buffer.readUnsignedByte();
				else if (opcode == 70)
					anInt738 = buffer.readSignedWord();
				else if (opcode == 71)
					anInt745 = buffer.readSignedWord();
				else if (opcode == 72)
					anInt783 = buffer.readSignedWord();
				else if (opcode == 73)
					aBoolean736 = true;
				else if (opcode == 74) {
					aBoolean766 = true;
				} else {
					if (opcode != 75)
						continue;
					anInt760 = buffer.readUnsignedByte();
				}
				continue label0;
			} while (opcode != 77);
			anInt774 = buffer.readUnsignedWord();
			if (anInt774 == 65535)
				anInt774 = -1;
			anInt749 = buffer.readUnsignedWord();
			if (anInt749 == 65535)
				anInt749 = -1;
			int j1 = buffer.readUnsignedByte();
			childrenIDs = new int[j1 + 1];
			for (int j2 = 0; j2 <= j1; j2++) {
				childrenIDs[j2] = buffer.readUnsignedWord();
				if (childrenIDs[j2] == 65535)
					childrenIDs[j2] = -1;
			}

		} while (true);
		if (i == -1) {
			interactive = modelArray != null
					&& (objectModelType == null || objectModelType[0] == 10);
			if (actions != null)
				interactive = true;
		}
		if (aBoolean766) {
			unwalkable = false;
			impenetrable = false;
		}
		if (anInt760 == -1)
			anInt760 = unwalkable ? 1 : 0;
	}

	private void readValuesOSRS(ByteStreamExt stream) {
		int i = -1;
		label0: do {
			int opcode;
			do {
				opcode = stream.readUnsignedByte();
				if (opcode == 0)
					break label0;
				if (opcode == 1) {
					int k = stream.readUnsignedByte();
					if (k > 0)
						if (modelArray == null) {
							objectModelType = new int[k];
							modelArray = new int[k];
							for (int k1 = 0; k1 < k; k1++) {
								modelArray[k1] = stream.readUnsignedWord();
								objectModelType[k1] = stream.readUnsignedByte();
							}
						} else {
							stream.currentOffset += k * 3;
						}
				} else if (opcode == 2)
					name = stream.readString();
				else if (opcode == 3)
					description = stream.readBytes();
				else if (opcode == 5) {
					int l = stream.readUnsignedByte();
					if (l > 0)
						if (modelArray == null) {
							objectModelType = null;
							modelArray = new int[l];
							for (int l1 = 0; l1 < l; l1++) {
								modelArray[l1] = stream.readUnsignedWord();
							}
						} else {
							stream.currentOffset += l * 2;
						}
				} else if (opcode == 14)
					tileSizeX = stream.readUnsignedByte();
				else if (opcode == 15)
					tileSizeY = stream.readUnsignedByte();
				else if (opcode == 17)
					unwalkable = false;
				else if (opcode == 18)
					impenetrable = false;
				else if (opcode == 19) {
					i = stream.readUnsignedByte();
					if (i == 1)
						interactive = true;
				} else if (opcode == 21) {
				} else if (opcode == 22) {
				} else if (opcode == 23)
					aBoolean764 = true;
				else if (opcode == 24) {
					stream.readUnsignedWord();
				} else if (opcode == 28)
					stream.readUnsignedByte();
				else if (opcode == 29)
					aByte737 = stream.readSignedByte();
				else if (opcode == 39)
					aByte742 = stream.readSignedByte();
				else if (opcode >= 30 && opcode < 39) {
					if (actions == null)
						actions = new String[10];
					actions[opcode - 30] = stream.readString();
					if (actions[opcode - 30].equalsIgnoreCase("hidden"))
						actions[opcode - 30] = null;
				} else if (opcode == 40) {
					int i1 = stream.readUnsignedByte();
					modifiedModelColors = new int[i1];
					originalModelColors = new int[i1];
					for (int i2 = 0; i2 < i1; i2++) {
						modifiedModelColors[i2] = stream.readUnsignedWord();
						originalModelColors[i2] = stream.readUnsignedWord();
					}
				} else if (opcode == 60)
					stream.readUnsignedWord();
				else if (opcode == 62)
					aBoolean751 = true;
				else if (opcode == 64)
					aBoolean779 = false;
				else if (opcode == 65)
					anInt748 = stream.readUnsignedWord();
				else if (opcode == 66)
					anInt772 = stream.readUnsignedWord();
				else if (opcode == 67)
					anInt740 = stream.readUnsignedWord();
				else if (opcode == 68)
					stream.readUnsignedWord();
				else if (opcode == 69)
					anInt768 = stream.readUnsignedByte();
				else if (opcode == 70)
					anInt738 = stream.readSignedWord();
				else if (opcode == 71)
					anInt745 = stream.readSignedWord();
				else if (opcode == 72)
					anInt783 = stream.readSignedWord();
				else if (opcode == 73)
					aBoolean736 = true;
				else if (opcode == 74) {
					aBoolean766 = true;
				} else {
					if (opcode != 75)
						continue;
					anInt760 = stream.readUnsignedByte();
				}
				continue label0;
			} while (opcode != 77);
			anInt774 = stream.readUnsignedWord();
			if (anInt774 == 65535)
				anInt774 = -1;
			anInt749 = stream.readUnsignedWord();
			if (anInt749 == 65535)
				anInt749 = -1;
			int j1 = stream.readUnsignedByte();
			childrenIDs = new int[j1 + 1];
			for (int j2 = 0; j2 <= j1; j2++) {
				childrenIDs[j2] = stream.readUnsignedWord();
				if (childrenIDs[j2] == 65535)
					childrenIDs[j2] = -1;
			}

		} while (true);
		if (i == -1) {
			interactive = modelArray != null && (objectModelType == null || objectModelType[0] == 10);
			if (actions != null)
				interactive = true;
		}
		if (aBoolean766) {
			unwalkable = false;
			impenetrable = false;
		}
		if (anInt760 == -1)
			anInt760 = unwalkable ? 1 : 0;
	}
	
	private void readValues667(ByteStreamExt buffer) {
		int i = -1;
		label0: do {
			int opcode;
			do {
				opcode = buffer.readUnsignedByte();
				if (opcode == 0)
					break label0;
				if (opcode == 1) {
					int k = buffer.readUnsignedByte();
					if (k > 0)
						if (modelArray == null || lowMem) {
							objectModelType = new int[k];
							modelArray = new int[k];
							for (int k1 = 0; k1 < k; k1++) {
								modelArray[k1] = buffer.readUnsignedWord();
								objectModelType[k1] = buffer.readUnsignedByte();
							}
						} else {
							buffer.currentOffset += k * 3;
						}
				} else if (opcode == 2)
					name = buffer.readString();
				else if (opcode == 3)
					description = buffer.readBytes();
				else if (opcode == 5) {
					int l = buffer.readUnsignedByte();
					if (l > 0)
						if (modelArray == null || lowMem) {
							objectModelType = null;
							modelArray = new int[l];
							for (int l1 = 0; l1 < l; l1++)
								modelArray[l1] = buffer.readUnsignedWord();
						} else {
							;//buffer.offset += l * 2;
						}
				} else if (opcode == 14)
					tileSizeX = buffer.readUnsignedByte();
				else if (opcode == 15)
					tileSizeY = buffer.readUnsignedByte();
				else if (opcode == 17)
					unwalkable = false;
				else if (opcode == 18)
					impenetrable = false;
				else if (opcode == 19) {
					i = buffer.readUnsignedByte();
					if (i == 1)
						interactive = true;
				} else if (opcode == 21) {
					//conformable = true;
				} else if (opcode == 22) {
					//blackModel = false;//
				} else if (opcode == 23)
					aBoolean764 = true;
				else if (opcode == 24) {
					buffer.readUnsignedWord();
				} else if (opcode == 28)
					buffer.readUnsignedByte();
				else if (opcode == 29)
					aByte737 = buffer.readSignedByte();
				else if (opcode == 39)
					aByte742 = buffer.readSignedByte();
				else if (opcode >= 30 && opcode < 39) {
					if (actions == null)
						actions = new String[10];
					actions[opcode - 30] = buffer.readString();
					if (actions[opcode - 30].equalsIgnoreCase("hidden")
							|| actions[opcode - 30].equalsIgnoreCase("null"))
						actions[opcode - 30] = null;
				} else if (opcode == 40) {
					int i1 = buffer.readUnsignedByte();
					for (int i2 = 0; i2 < i1; i2++) {
						buffer.readUnsignedWord();
						buffer.readUnsignedWord();
					}
				} else if (opcode == 60)
					buffer.readUnsignedWord();
				else if (opcode == 62)
					aBoolean751 = true;
				else if (opcode == 64)
					aBoolean779 = false;
				else if (opcode == 65)
					anInt748 = buffer.readUnsignedWord();
				else if (opcode == 66)
					anInt772 = buffer.readUnsignedWord();
				else if (opcode == 67)
					anInt740 = buffer.readUnsignedWord();
				else if (opcode == 68)
					buffer.readUnsignedWord();
				else if (opcode == 69)
					anInt768 = buffer.readUnsignedByte();
				else if (opcode == 70)
					anInt738 = buffer.readSignedWord();
				else if (opcode == 71)
					anInt745 = buffer.readSignedWord();
				else if (opcode == 72)
					anInt783 = buffer.readSignedWord();
				else if (opcode == 73)
					aBoolean736 = true;
				else if (opcode == 74) {
					aBoolean766 = true;
				} else {
					if (opcode != 75)
						continue;
					anInt760 = buffer.readUnsignedByte();
				}
				continue label0;
			} while (opcode != 77);
				anInt774 = buffer.readUnsignedWord();
			if (anInt774 == 65535)
				anInt774 = -1;
				anInt749 = buffer.readUnsignedWord();
			if (anInt749 == 65535)
				anInt749 = -1;
			int j1 = buffer.readUnsignedByte();
			childrenIDs = new int[j1 + 1];
			for (int j2 = 0; j2 <= j1; j2++) {
				childrenIDs[j2] = buffer.readUnsignedWord();
				if (childrenIDs[j2] == 65535)
					childrenIDs[j2] = -1;
			}

		} while (true);
		if (i == -1) {
			interactive = modelArray != null && (objectModelType == null || objectModelType[0] == 10);
			if (actions != null)
				interactive = true;
		}
		if (aBoolean766) {
			unwalkable = false;
			impenetrable = false;
		}
		if (anInt760 == -1)
			anInt760 = unwalkable ? 1 : 0;
    }

	public GameObjectDefinition() {
		id = -1;
	}

	public boolean hasActions() {
		return interactive;
	}

	public boolean hasName() {
		return name != null && name.length() > 1;
	}

	public int xLength() {
		return tileSizeX;
	}

	public int yLength() {
		return tileSizeY;
	}

	public boolean aBoolean736;
	public byte aByte737;
	public int anInt738;
	public String name;
	public int anInt740;
	public byte aByte742;
	public int tileSizeX;
	public int anInt745;
	public int anInt746;
	public int[] originalModelColors;
	public int anInt748;
	public int anInt749;
	public boolean aBoolean751;
	public static boolean lowMem;
	public int id;
	public static int[] streamIndices525;
	public static int[] streamIndices667;
	public static int[] streamIndicesOSRS;
	public boolean impenetrable;
	public int anInt758;
	public int childrenIDs[];
	public int anInt760;
	public int tileSizeY;
	public boolean aBoolean762;
	public boolean aBoolean764;
	public boolean aBoolean766;
	public boolean unwalkable;
	public int anInt768;
	public boolean aBoolean769;
	public static int cacheIndex;
	public int anInt772;
	public int[] modelArray;
	public int anInt774;
	public int anInt775;
	public int[] objectModelType;
	public byte description[];
	public boolean interactive;
	public boolean aBoolean779;
	public int anInt781;
	public static GameObjectDefinition[] cache;
	public int anInt783;
	public int[] modifiedModelColors;
	public String actions[];
	public static MemoryArchive archive;

	public int actionCount() {
		return interactive ? 1 : 0;
	}

	public String getName() {
		return name;
	}
	
	public int getSizeX() {
		return tileSizeX;
	}
	
	public int getSizeY() {
		return tileSizeY;
	}

}
