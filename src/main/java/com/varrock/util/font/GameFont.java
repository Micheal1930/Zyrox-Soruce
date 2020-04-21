package com.varrock.util.font;

import java.io.File;

import com.varrock.util.ByteBuffer;
import com.varrock.util.FileOperations;

public final class GameFont {

	private static final double defaultItemZoom = 2D;
	private static double itemZoom = 2D;
	private static String endZoom;
	private static String startZoom;

	public int baseCharacterHeight = 0;
	private int[] characterDrawYOffsets;
	private int[] characterHeights;
	private int[] characterDrawXOffsets;
	private int[] characterWidths;
	private byte[][] fontPixels;
	private int[] characterScreenWidths;
	private static String aRSString_4135;
	private static String endShadow = "/shad";
	private static String endEffect;
	private static String aRSString_4147;
	private static String startImage;
	private static String startItem;
	private static String startIcon;
	private static String startClanImage;
	private static String startEffect;
	private static String aRSString_4162;
	private static String aRSString_4163;
	private static String endTransparency;
	private static String aRSString_4165;
	private static String startUnderline;
	private static String startDefaultUnderline;
	private static String aRSString_4169;

	public GameFont(boolean TypeFont, String s, String directory) {
		File dataFile = new File(directory + s + ".dat");
		File indexFile = new File(directory + "index" + ".dat");
		try {
			int length = (s.equals("regularhit") || s.equals("bighit")) ? 58 : 256;
			fontPixels = new byte[length][];
			characterWidths = new int[length];
			characterHeights = new int[length];
			characterDrawXOffsets = new int[length];
			characterDrawYOffsets = new int[length];
			characterScreenWidths = new int[length];
			ByteBuffer data = ByteBuffer.wrap(FileOperations.readFile(dataFile));
			ByteBuffer index = ByteBuffer.wrap(FileOperations.readFile(indexFile));
			index.position = data.getUnsignedShort() + 4;
			int k = index.getUnsignedByte();
			if (k > 0) {
				index.position += 3 * (k - 1);
			}
			for (int l = 0; l < length; l++) {
				characterDrawXOffsets[l] = index.getUnsignedByte();
				characterDrawYOffsets[l] = index.getUnsignedByte();
				int i1 = characterWidths[l] = index.getUnsignedShort();
				int j1 = characterHeights[l] = index.getUnsignedShort();
				int k1 = index.getUnsignedByte();
				int l1 = i1 * j1;
				fontPixels[l] = new byte[l1];
				if (k1 == 0) {
					for (int i2 = 0; i2 < l1; i2++) {
						fontPixels[l][i2] = data.getByte();
					}
				} else if (k1 == 1) {
					for (int j2 = 0; j2 < i1; j2++) {
						for (int l2 = 0; l2 < j1; l2++) {
							fontPixels[l][j2 + l2 * i1] = data.getByte();
						}
					}
				}
				if (j1 > baseCharacterHeight && l < 128) {
					baseCharacterHeight = j1;
				}
				characterDrawXOffsets[l] = 1;
				characterScreenWidths[l] = i1 + 2;
				int k2 = 0;
				for (int i3 = j1 / 7; i3 < j1; i3++) {
					k2 += fontPixels[l][i3 * i1];
				}
				if (k2 <= j1 / 7) {
					characterScreenWidths[l]--;
					characterDrawXOffsets[l] = 0;
				}
				k2 = 0;
				for (int j3 = j1 / 7; j3 < j1; j3++) {
					k2 += fontPixels[l][(i1 - 1) + j3 * i1];
				}
				if (k2 <= j1 / 7) {
					characterScreenWidths[l]--;
				}
			}
			if (TypeFont) {
				characterScreenWidths[32] = characterScreenWidths[73];
			} else {
				characterScreenWidths[32] = characterScreenWidths[105];
			}
		} catch (Exception e) {
		}
	}

	public static String getEndShadow() {
		return endShadow;
	}

	public static void setEndShadow(String endShadow) {
		GameFont.endShadow = endShadow;
	}

	public int getWidth(String string) {
		if (string == null) {
			return 0;
		}
		int startIndex = -1;
		int finalWidth = 0;
		final boolean containsEndEffect = string.contains(">");
		for (int currentCharacter = 0; currentCharacter < string.length(); currentCharacter++) {
			int character = string.charAt(currentCharacter);
			if (character > 255) {
				character = 32;
			}
			if (character == 60 && containsEndEffect) {
				startIndex = currentCharacter;
			} else {
				if (character == 62 && startIndex != -1) {
					String effectString = string.substring(startIndex + 1, currentCharacter);
					startIndex = -1;
					if (effectString.equals(startEffect)) {
						character = 60;
					} else if (effectString.equals(endEffect)) {
						character = 62;
					} else if (effectString.equals(aRSString_4135)) {
						character = 160;
					} else if (effectString.equals(aRSString_4162)) {
						character = 173;
					} else if (effectString.equals(aRSString_4165)) {
						character = 215;
					} else if (effectString.equals(aRSString_4147)) {
						character = 128;
					} else if (effectString.equals(aRSString_4163)) {
						character = 169;
					} else if (effectString.equals(aRSString_4169)) {
						character = 174;
					} else {
						if (effectString.startsWith(startImage)) {
							try {
								int iconId = Integer.valueOf(effectString.substring(4));
								finalWidth += 14;
							} catch (Exception exception) {
							}
						} else if (effectString.startsWith(startItem)) {
							try {
								int itemId = Integer.valueOf(effectString.substring(5));
									finalWidth += 32;
							} catch (Exception exception) {
							}
						} else if (effectString.startsWith(startClanImage)) {
							try {
								int iconId = Integer.valueOf(effectString.substring(5));
								finalWidth += 11;
							} catch (Exception exception) {
							}
						} else if (effectString.startsWith(startIcon)) {
							try {
								int iconId = Integer.valueOf(effectString.substring(5));
								finalWidth += 11;
							} catch (Exception exception) {
							}
						}
						continue;
					}
				}
				if (startIndex == -1) {
					finalWidth += characterScreenWidths[character];
				}
			}
		}
		return finalWidth;
	}

	static {
		startItem = "item=";
		startImage = "img=";
		startZoom = "zoom=";
		startIcon = "icon=";
		startClanImage = "friendChat=";
		startUnderline = "u=";
		endZoom = "/zoom";
		startDefaultUnderline = "u";
		endTransparency = "/trans";
		aRSString_4135 = "nbsp";
		aRSString_4169 = "reg";
		aRSString_4165 = "times";
		aRSString_4162 = "shy";
		aRSString_4163 = "copy";
		endEffect = "gt";
		aRSString_4147 = "euro";
		startEffect = "lt";
	}
}