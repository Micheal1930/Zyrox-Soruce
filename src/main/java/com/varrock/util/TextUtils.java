package com.varrock.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Text utility class.
 *
 * @author Graham Edgecombe
 */
public class TextUtils {

	/**
	 * Unpacks text.
	 *
	 * @param packedData The packet text.
	 * @param size       The length.
	 * @return The string.
	 */
	public static String textUnpack(byte packedData[], int size) {
		byte[] decodeBuf = new byte[4096];
		int idx = 0, highNibble = -1;
		for (int i = 0; i < size * 2; i++) {
			int val = packedData[i / 2] >> (4 - 4 * (i % 2)) & 0xf;
			if (highNibble == -1) {
				if (val < 13) {
					decodeBuf[idx++] = (byte) Constants.XLATE_TABLE[val];
				} else {
					highNibble = val;
				}
			} else {
				decodeBuf[idx++] = (byte) Constants.XLATE_TABLE[((highNibble << 4) + val) - 195];
				highNibble = -1;
			}
		}
		return new String(decodeBuf, 0, idx);
	}

	public static String shortIp(String fullIp) {
		String[] parts = fullIp.split(":");
		return parts[0].replace("/", "");
	}
	
/*	public static String textUnpack(byte[] stream, int i) {
		int j = 0;
		int k = -1;
		char[] decodeBuf = new char[4096];
		for(int l = 0; l < i; l++) {
			int i1 = stream[l];
			decodeBuf[j++] = validChars[i1];//oo gotta fix that i guess
		}
		boolean flag1 = true;
		for(int k1 = 0; k1 < j; k1++) {
			char c = decodeBuf[k1];
			if(flag1 && c >= 'a' && c <= 'z') {
				decodeBuf[k1] += '\uFFE0';
				flag1 = false;
			}
			if(c == '.' || c == '!' || c == '?')
				flag1 = true;
		}
		return new String(decodeBuf, 0, j);
	}*/

	private static char validChars[] = {
			' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r',
			'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p',
			'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2',
			'3', '4', '5', '6', '7', '8', '9', ' ', '!', '?',
			'.', ',', ':', ';', '(', ')', '-', '&', '*', '\\',
			'\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[',
			']', '>', '<', '^', '/', '_'
	};

	/**
	 * Optimises text.
	 *
	 * @param text The text to optimise.
	 * @return The text.
	 */
	public static String optimizeText(String text) {
		char buf[] = text.toCharArray();
		boolean endMarker = true;
		for (int i = 0; i < buf.length; i++) {
			char c = buf[i];
			if (endMarker && c >= 'a' && c <= 'z') {
				buf[i] -= 0x20;
				endMarker = false;
			}
			if (c == '.' || c == '!' || c == '?') {
				endMarker = true;
			}
		}
		return new String(buf, 0, buf.length);
	}

	/**
	 * Gets the Username with the first GameCharacter capitalized.
	 *
	 * @param text
	 * @return
	 */
	public static String ucFirst(String text) {
		return java.lang.Character.toUpperCase(text.charAt(0)) + text.substring(1);
	}

	/**
	 * Packs text.
	 *
	 * @param packedData The destination of the packed text.
	 * @param text       The unpacked text.
	 */
	/*public static void textPack(byte packedData[], String text) {
		if(text.length() > 80) {
			text = text.substring(0, 80);
		}
		text = text.toLowerCase();
		int carryOverNibble = -1;
		int ofs = 0;
		for(int idx = 0; idx < text.length(); idx++) {
			char c = text.charAt(idx);
			int tableIdx = 0;
			for(int i = 0; i < Constants.XLATE_TABLE.length; i++) {
				if(c == (byte) Constants.XLATE_TABLE[i]) {
					tableIdx = i;
					break;
				}
			}
			if(tableIdx > 12) {
				tableIdx += 195;
			}
			if(carryOverNibble == -1) {
				if(tableIdx < 13) {
					carryOverNibble = tableIdx;
				} else {
					packedData[ofs++] = (byte) (tableIdx);
				}
			} else if (tableIdx < 13) {
				packedData[ofs++] = (byte) ((carryOverNibble << 4) + tableIdx);
				carryOverNibble = -1;
			} else {
				packedData[ofs++] = (byte) ((carryOverNibble << 4) + (tableIdx >> 4));
				carryOverNibble = tableIdx & 0xf;
			}
		}
		if(carryOverNibble != -1) {
			packedData[ofs++] = (byte) (carryOverNibble << 4);
		}
	}*/
	public static void textPack(byte[] stream, String s) {
		if (s.length() > 80)
			s = s.substring(0, 80);
		s = s.toLowerCase();
		int i = -1;
		for (int j = 0; j < s.length(); j++) {
			char c = s.charAt(j);
			int k = 0;
			for (int l = 0; l < validChars.length; l++) {
				if (c != validChars[l])
					continue;
				k = l;
				break;
			}
			stream[++i] = (byte) k;
		}
	}


	/**
	 * Filters invalid characters out of a string.
	 *
	 * @param s The string.
	 * @return The filtered string.
	 */
	public static String filterText(String s) {
		StringBuilder bldr = new StringBuilder();
		for (char c : s.toLowerCase().toCharArray()) {
			boolean valid = false;
			for (char validChar : Constants.XLATE_TABLE) {
				if (validChar == c) {
					valid = true;
				}
			}
			if (valid) {
                bldr.append(c);
			}
		}
		return bldr.toString();
	}

	public static void writeToFile(String file, String line) {
		writeToFile(new File(file), line);
	}

	public static void writeToFile(File file, String... lines) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file, true));
			for (String line : lines) {
				bw.write(line);
				bw.newLine();
			}
			bw.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void writeToTxtFile(String write) {
		try {
			FileWriter outFile = new FileWriter("itemnames.txt");
			PrintWriter out = new PrintWriter(outFile);

			// Also could be written as follows on one line
			// Printwriter out = new PrintWriter(new FileWriter(args[0]));

			// Write text to file
			out.println(write);
			out.println("This is line 2");
			out.print("This is line3 part 1, ");
			out.println("this is line 3 part 2");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
