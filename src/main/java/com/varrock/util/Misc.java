package com.varrock.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.WordUtils;
import org.jboss.netty.buffer.ChannelBuffer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.varrock.GameSettings;
import com.varrock.model.Item;
import com.varrock.model.Position;
import com.varrock.model.container.ItemContainer;
import com.varrock.model.container.StackType;
import com.varrock.net.login.FloodCheckStatus;
import com.varrock.net.sql.batch.impl.OutputBatchUpdateEvent;
import com.varrock.net.sql.table.impl.OutputSQLTableEntry;
import com.varrock.saving.PlayerSaving;
import com.varrock.world.World;
import com.varrock.world.content.combat.CombatContainer.CombatHit;
import com.varrock.world.entity.impl.player.Player;
import com.varrock.world.entity.impl.player.PlayerLoading;

public class Misc {
	
	/**
	 * @param value        for example 13,615465
	 * @param decimalPlace for example 2,
	 * @return returns 13,61
	 */
	public static double round(double value, int decimalPlace) {
		double power_of_ten = 1;

		while (decimalPlace-- > 0)
			power_of_ten *= 10.0;
		return Math.round(value * power_of_ten) / power_of_ten;
	}

	public static String ordinal(int i) {
		int mod100 = i % 100;
		int mod10 = i % 10;
		if(mod10 == 1 && mod100 != 11) {
			return i + "st";
		} else if(mod10 == 2 && mod100 != 12) {
			return i + "nd";
		} else if(mod10 == 3 && mod100 != 13) {
			return i + "rd";
		} else {
			return i + "th";
		}
	}

	public static String removeLastChar(String str) {
		return str.substring(0, str.length() - 2);
	}

	public static int arrayMax(int[] arr) {
		int max = Integer.MIN_VALUE;

		for(int cur: arr)
			max = Math.max(max, cur);

		return max;
	}

	/**
	 * Fetches system time and formats it appropriately
	 *
	 * @return Formatted time
	 */
	public static String getTime() {
		Date getDate = new Date();
		String timeFormat = "M/d/yy hh:mma";
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
		return "[" + sdf.format(getDate) + "]";
	}

	public static void print(String string) {
		System.out.println(string);
	}

	public static void printWarning(String string) {
		print("[WARNING]: " + string);
	}

	public static void print(Throwable throwable) {
		throwable.printStackTrace();

		OutputBatchUpdateEvent.getInstance().submit(new OutputSQLTableEntry(System.currentTimeMillis(), throwable));
	}

	public static int arrayMin(int[] arr) {
		int min = Integer.MAX_VALUE;

		for(int cur: arr)
			min = Math.min(min, cur);

		return min;
	}

	public static String capitalize(String s) {
		s = s.toLowerCase();
		for (int i = 0; i < s.length(); i++) {
			if (i == 0) {
				s = String.format("%s%s", Character.toUpperCase(s.charAt(0)), s.substring(1));
			}
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				if (i + 1 < s.length()) {
					s = String.format("%s%s%s", s.subSequence(0, i + 1), Character.toUpperCase(s.charAt(i + 1)), s.substring(i + 2));
				}
			}
		}
		return s;
	}

	/**
	 * Formats the name by checking if it starts with a vowel.
	 * @param name	The string to format.
	 */
	public static String getArticle(String name) {
		char letter = Character.toLowerCase(name.charAt(0));
		boolean vowel = letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u';
		String other = vowel ? "an" : "a";
		if (name.endsWith("s")) {
			other += " pair of";
		}
		return other + " " + name;
	}

	public static boolean isFileUnlocked(File file) {
        try {
            FileInputStream in = new FileInputStream(file);
            if (in!=null) in.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
	
	public static Player accessPlayer(String username) {
		if(username == null)
			return null;
		username = Misc.formatText(username.toLowerCase());
		Player owner = World.getPlayerByName(username);
		
		if(owner == null && PlayerSaving.getSaving().exists(username)) {
			
			Player player = new Player(null);
			
			player.setUsername(username);
			player.setLongUsername(NameUtils.stringToLong(username));
			
			PlayerLoading.getResult(player, true);
			
			return player;
			
		}
		
		return owner;
		
	}


	/** Random instance, used to generate pseudo-random primitive types. */
	public static final SecureRandom RANDOM = new SecureRandom();

	private static ZonedDateTime zonedDateTime;
	public static final int HALF_A_DAY_IN_MILLIS = 43200000;
	
	public static final String sendCashToString(long j) {
        if (j >= 0 && j < 10000)
            return String.valueOf(j);
        else if (j >= 10000 && j < 10000000)
            return j / 1000 + "K";
        else if (j >= 10000000)
            return j / 1000000 + "M";
        else if (j >= 1000000000)
            return j / 1000000000 + "B";
        else
            return Long.toString(j);
    }

	public static int toCyclesOrDefault(long time, int def, TimeUnit unit) {
		if (time > Integer.MAX_VALUE) {
			time = def;
		}
		return (int) TimeUnit.MILLISECONDS.convert(time, unit) / 600;
	}
	
	public static final long stringToLong(String input){
		try {
		if (input.toLowerCase().contains("k")) {
			input = input.replaceAll("k", "000");
		} else if (input.toLowerCase().contains("m")) {
			input = input.replaceAll("m", "000000");
		} else if (input.toLowerCase().contains("b")) {
			input = input.replaceAll("b", "000000000");
		} else if (input.toLowerCase().contains("t")) {
			input = input.replaceAll("t", "000000000000");
		}
		if(Long.parseLong(input) > Long.MAX_VALUE){
			return Long.MAX_VALUE;
		}
	} catch(NumberFormatException e){
		return Misc.stringToLong("100t");
	}
		//System.out.println(input);
		return Long.parseLong(input);
	}
	
	public static CombatHit[] concat(CombatHit[] a, CombatHit[] b) {
		int aLen = a.length;
		int bLen = b.length;
		CombatHit[] c= new CombatHit[aLen+bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}

	public static List<Player> getCombinedPlayerList(Player p) {
		List<Player> plrs = new LinkedList<Player>();
		for(Player localPlayer : p.getLocalPlayers()) {
			if(localPlayer != null)
				plrs.add(localPlayer);
		}
		plrs.add(p);
		return plrs;
	}

	public static String getCurrentServerTime() {
		zonedDateTime = ZonedDateTime.now();
		int hour = zonedDateTime.getHour();
		String hourPrefix = hour < 10 ? "0"+hour+"" : ""+hour+"";
		int minute = zonedDateTime.getMinute();
		String minutePrefix = minute < 10 ? "0"+minute+"" : ""+minute+"";
		return ""+hourPrefix+":"+minutePrefix+"";
	}

    public static String getTimeMessage() {
        return "[<col=009933>" + getCurrentServerTime() + "</col>] ";
    }

    public static String getLootIcon() {
        return "<img=469> ";
    }

    public static String getBoxIcon() {
        return "<img=466> ";
    }

    public static String getEventIcon() {
        return "<img=471> <shad=ff0000> ";
    }

    public static String getWildywyrmIcon() {
        return "<img=467> <shad=ffffff> ";
    }

    public static String getHalloweenIcon() {
        return "<img=750> <shad=ffa500> ";
    }

    public static String getServerIcon() {
        return "<img=475> ";
    }

	public static String getTimePlayed(long totalPlayTime) {
		final int sec = (int) (totalPlayTime / 1000), h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
		return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
	}

	public static String longToPlayerName2(long l) {
		int i = 0;
		char ac[] = new char[99];
		while (l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = xlateTable[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}
	
	public static long nameToLong(String s) {
		s = s.toLowerCase();

		long l = 0L;

		for (int i = 0; i < s.length() && i < 12; i++) {
			char c = s.charAt(i);

			l *= 37L;

			if (c >= 'A' && c <= 'Z') {
				l += (1 + c) - 65;
			} else if (c >= 'a' && c <= 'z') {
				l += (1 + c) - 97;
			} else if (c >= '0' && c <= '9') {
				l += (27 + c) - 48;
			}
		}

		while (l % 37L == 0L && l != 0L) {
			l /= 37L;
		}

		return l;
	}

	  public static boolean isJSONValid(String jsonInString ) {
		    try {
		       final ObjectMapper mapper = new ObjectMapper();
		       mapper.readTree(jsonInString);
		       return true;
		    } catch (IOException e) {
		       return false;
		    }
		  }
	  
	  public static void addCompWhitelist(String mac) {
//			try {
//				URL apiurl = new URL("https://simplicityps.org/resources/compromisedWhitelist.php?type=add&mac="+mac);
//				//System.out.println(apiurl);
//				//System.out.println(base64Message);
//		        URLConnection con = apiurl.openConnection();
//		        con.setRequestProperty("User-Agent", "Mozilla/5.0");
//		        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//		        con.setRequestProperty("Accept", "*/*");
//		        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//		        in.close();
//				} catch(Exception e) {
//					e.printStackTrace();
//				}
		}
	  
	  
	  public static boolean checkCompWhitelist(String mac) {
//			try {
//				URL apiurl = new URL("https://simplicityps.org/resources/compromisedWhitelist.php?type=check&mac="+mac);
//				//System.out.println(apiurl);
//				//System.out.println(base64Message);
//		        URLConnection con = apiurl.openConnection();
//		        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.0.5) Gecko/2008120122 Firefox/3.0.5");
//		        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//		        con.setRequestProperty("Accept", "*/*");
//		        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//		        String inputLine;
//		        while ((inputLine = in.readLine()) != null) {
//		        	if(inputLine.contains("TRUE"))
//		        	return true;
//		        }
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
			return false;
		}
	  
	  
	static void discordSender(String type, String message) {
//		try {
//			String base64Message = Base64.getEncoder().encodeToString(message.getBytes());
//			URL apiurl = new URL("https://simplicityps.org/resources/sendDiscordLog.php?type="+type+"&content="+base64Message);
//			//System.out.println(apiurl);
//			//System.out.println(base64Message);
//	        URLConnection con = apiurl.openConnection();
//	        con.setRequestProperty("User-Agent", "Mozilla/5.0");
//	        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//	        con.setRequestProperty("Accept", "*/*");
//	        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//	        in.close();
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
	}
	
	public static void sendDiscordLog(String type, String message) {
		new Thread(() -> { discordSender(type, message); }).start();
    }
	
	public static void sendStaffIdle(String username) {
//		try {
//			String username64 = Base64.getEncoder().encodeToString(username.getBytes());
//			URL apiurl = new URL("https://simplicityps.org/resources/idleStaffReporter.php?u="+username64);
//	        URLConnection con = apiurl.openConnection();
//	        con.setRequestProperty("User-Agent", "Mozilla/5.0");
//	        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//	        con.setRequestProperty("Accept", "*/*");
//	        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//	        in.close();
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
	}
	public static int getMinutesPlayed(Player p) {
		long totalPlayTime = p.getTotalPlayTime() + (p.getRecordedLogin().elapsed());
		int sec = (int) (totalPlayTime / 1000), h = sec / 3600, m = sec / 60 % 60;
		for(int i = 0; i < h; i++)
			m+=60;
		return m;
	}

	public static String getHoursPlayed(long totalPlayTime) {
		final int sec = (int) (totalPlayTime / 1000), h = sec / 3600;
		return (h < 10 ? "0" + h : h) + "h";
	}

	public static int getMinutesPassed(long t) {
		int seconds=(int) ((t/1000)%60);
		int minutes=(int) (((t-seconds)/1000)/60);
		return minutes;
	}

	public static Item[] concat(Item[] a, Item[] b) {
		int aLen = a.length;
		int bLen = b.length;
		Item[] c= new Item[aLen+bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}
	public static String[] concat(String[] a, String[] b) {
		int aLen = a.length;
		int bLen = b.length;
		String[] c= new String[aLen+bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}
	public static ItemContainer concat(ItemContainer a, ItemContainer b) {
		ItemContainer c = new ItemContainer() {
			@Override
			public StackType stackType() {
				return a.stackType();
			}

			@Override
			public ItemContainer refreshItems() {
				return this;
			}

			@Override
			public ItemContainer full() {
				return this;
			}

			@Override
			public int capacity() {
				return a.capacity() + b.capacity();
			}
		};

		for (Item itemA : a.getValidItems()) {
			c.add(itemA);
		}

		for (Item itemB : b.getValidItems()) {
			c.add(itemB);
		}

		return c;
	}

	public static Player getCloseRandomPlayer(List<Player> plrs) {
		int index = Misc.getRandom(plrs.size() - 1);
		if(index > 0) 
			return plrs.get(index);
		return null;
	}

	public static byte directionDeltaX[] = new byte[]{ 0, 1, 1, 1, 0,-1,-1,-1 };
	public static byte directionDeltaY[] = new byte[]{ 1, 1, 0,-1,-1,-1, 0, 1 };	
	public static byte xlateDirectionToClient[] = new byte[]{ 1, 2, 4, 7, 6, 5, 3, 0 };

	public static int getRandom(int range) {
		return (int) (java.lang.Math.random() * (range + 1));
	}

	public static double randomFloat() {
		return java.lang.Math.random();
	}

	public static int direction(int srcX, int srcY, int x, int y) {
		double dx = (double) x - srcX, dy = (double) y - srcY;
		double angle = Math.atan(dy / dx);
		angle = Math.toDegrees(angle);
		if (Double.isNaN(angle))
			return -1;
		if (Math.signum(dx) < 0)
			angle += 180.0;
		return (int) ((((90 - angle) / 22.5) + 16) % 16);
		/*int changeX = x - srcX; int changeY = y - srcY;
		for (int j = 0; j < directionDeltaX.length; j++) {
			if (changeX == directionDeltaX[j] &&
				changeY == directionDeltaY[j])
				return j;

		}
		return -1;*/
	}

	public static String ucFirst(String str) {
		str = str.toLowerCase();
		if (str.length() > 1) {
			str = str.substring(0, 1).toUpperCase() + str.substring(1);
		} else {
			return str.toUpperCase();
		}
		return str.replaceAll("_", " ");
	}

	public static String format(int num) {
		return NumberFormat.getInstance().format(num);
	}

	public static String formatRunescapeStyle(long num) {
		if (num <= 0) {
			return Long.toString(num);
		}
		int length = String.valueOf(num).length();
		String number = Long.toString(num);
		String numberString = number;
		String end = "";
		if (length == 4) {
			numberString = number.substring(0, 1) + "k";
			//6400
			double doubleVersion = 0.0;
			doubleVersion = num / 1000.0;
			if (doubleVersion != getDoubleRoundedUp(doubleVersion)) {
				if (num - (1000 * getDoubleRoundedDown(doubleVersion)) > 100) {
					numberString = number.substring(0, 1) + "." + number.substring(1, 2) + "k";
				}
			}
		}
		else if (length == 5) {
			numberString = number.substring(0, 2) + "k";
		}
		else if (length == 6) {
			numberString = number.substring(0, 3) + "k";
		}
		else if (length == 7) {
			String sub = number.substring(1, 2);
			if (sub.equals("0")) {
				numberString = number.substring(0, 1) + "m";
			}
			else {
				numberString = number.substring(0, 1) + "." + number.substring(1, 2) + "m";
			}
		}
		else if (length == 8) {
			end = "." + number.substring(2, 3);
			if (end.equals(".0")) {
				end = "";
			}
			numberString = number.substring(0, 2) + end + "m";
		}
		else if (length == 9) {
			end = "." + number.substring(3, 4);
			if (end.equals(".0")) {
				end = "";
			}
			numberString = number.substring(0, 3) + end + "m";
		}
		else if (length == 10) {
			numberString = number.substring(0, 4) + "m";
		}
		else if (length == 11) {
			numberString = number.substring(0, 2) + "." + number.substring(2, 5) + "b";
		}
		else if (length == 12) {
			numberString = number.substring(0, 3) + "." + number.substring(3, 6) + "b";
		}
		else if (length == 13) {
			numberString = number.substring(0, 4) + "." + number.substring(4, 7) + "b";
		}

		return numberString;
	}

	public static double getDoubleRoundedUp(double doubleNumber) {
		return Math.ceil(doubleNumber);
	}

	public static double getDoubleRoundedDown(double doubleNumber) {
		return (double) ((int) doubleNumber);
	}

	public static String formatText(String s) {
		if(s == null) {
			return "null";
		}
		for (int i = 0; i < s.length(); i++) {
			if (i == 0) {
				s = String.format("%s%s", Character.toUpperCase(s.charAt(0)),
						s.substring(1));
			}
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				if (i + 1 < s.length()) {
					s = String.format("%s%s%s", s.subSequence(0, i + 1),
							Character.toUpperCase(s.charAt(i + 1)),
							s.substring(i + 2));
				}
			}
		}
		return s.replace("_", " ");
	}

	public static String getTotalAmount(int j) {
		if (j >= 10000 && j < 10000000) {
			return j / 1000 + "K";
		} else if (j >= 10000000 && j <= 2147483647) {
			return j / 1000000 + "M";
		} else {
			return "" + j + " ";
		}
	}

	public static String formatPlayerName(String str) {
		String str1 = Misc.ucFirst(str);
		str1.replace("_", " ");
		return str1;
	}
	
	public static String capitalizeFirstLetter(String string) {
		return Character.toUpperCase(string.charAt(0)) + string.substring(1);
	}
	
	public static String capitalizeFirstLetters(String string) {
		return WordUtils.capitalize(string);
	}

	public static String insertCommasToNumber(Object number2) {
		String number = number2.toString();
		return number.length() < 4 ? number : insertCommasToNumber(number.substring(0, number.length() - 3)) + "," + number.substring(number.length() - 3, number.length());
	}
	
	/**
	 * Checks if the players name is valid.
	 * 
	 * @param name
	 *            the name
	 * @return true if valid
	 */
	public static boolean isValidName(String name) {
		return name.matches("[A-Za-z0-9 ]+") && (name.length() > 0 && name.length() <= 12);
	}

	/**
	 * Checks if the host is valid.
	 * 
	 * @param host
	 *            The host
	 * @return <code>true</code> if valid
	 */
	public static boolean isValidHost(String host) {
		return host.matches("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
	}
	
	/**
	 * Checks if the host is valid.
	 * 
	 * @param host
	 *            The host
	 * @return <code>true</code> if valid
	 */
	public static boolean isValidMac(String mac) {
		return mac.matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
	}

	private static char decodeBuf[] = new char[4096];

	public static String textUnpack(byte packedData[], int size) {
		int idx = 0, highNibble = -1;
		for (int i = 0; i < size * 2; i++) {
			int val = packedData[i / 2] >> (4 - 4 * (i % 2)) & 0xf;
		if (highNibble == -1) {
			if (val < 13)
				decodeBuf[idx++] = xlateTable[val];
			else
				highNibble = val;
		} else {
			decodeBuf[idx++] = xlateTable[((highNibble << 4) + val) - 195];
			highNibble = -1;
		}
		}

		return new String(decodeBuf, 0, idx);
	}

	public static char xlateTable[] = { ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n',
		's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b',
		'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-',
		'&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"',
		'[', ']' };

	public static String anOrA(String s) {
		s = s.toLowerCase();
		if(s.equalsIgnoreCase("anchovies") || s.equalsIgnoreCase("soft clay") || s.equalsIgnoreCase("cheese") || s.equalsIgnoreCase("ball of wool") || s.equalsIgnoreCase("spice") || s.equalsIgnoreCase("steel nails") || s.equalsIgnoreCase("snape grass") || s.equalsIgnoreCase("coal"))
			return "some";
		if(s.startsWith("a") || s.startsWith("e") || s.startsWith("i") || s.startsWith("o") || s.startsWith("u")) 
			return "an";
		return "a";
	}

	@SuppressWarnings("rawtypes")
	public static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new Class[classes.size()]);
	}

	@SuppressWarnings("rawtypes")
	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

	public static int randomMinusOne(int length) {
		return getRandom(length - 1);
	}

	public static String removeSpaces(String s) {
		return s.replace(" ", "");
	}

	public static int getMinutesElapsed(int minute, int hour, int day, int year) {
		Calendar i = Calendar.getInstance();

		if (i.get(1) == year) {
			if (i.get(6) == day) {
				if (hour == i.get(11)) {
					return i.get(12) - minute;
				}
				return (i.get(11) - hour) * 60 + (59 - i.get(12));
			}

			int ela = (i.get(6) - day) * 24 * 60 * 60;
			return ela > 2147483647 ? 2147483647 : ela;
		}

		int ela = getElapsed(day, year) * 24 * 60 * 60;

		return ela > 2147483647 ? 2147483647 : ela;
	}

	public static int getDayOfYear() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int days = 0;
		int[] daysOfTheMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
			daysOfTheMonth[1] = 29;
		}
		days += c.get(Calendar.DAY_OF_MONTH);
		for (int i = 0; i < daysOfTheMonth.length; i++) {
			if (i < month) {
				days += daysOfTheMonth[i];
			}
		}
		return days;
	}

	public static int getYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}
	
	/**
	 * @param min
	 *            The minimum integer expected (inclusive)
	 * @param max
	 *            The maximum integer expected (inclusive)
	 * @return A random number min-max inclusive.
	 */
	public static int random(int min, int max) {
		return getRandom(max - min + 1) + min;
	}

	public static int randomInclusive(int minInclusive, int maxInclusive) {
		return ThreadLocalRandom.current().nextInt(maxInclusive - minInclusive + 1) + minInclusive;
	}


	public static int random(int i) {
		return getRandom(i);
	}
	/**
	 * Sets up money representing it as 1000K (1,000,000)
	 * 
	 * @param quantity
	 *            the money
	 * @return the money
	 */
	public static String setupMoney(long quantity) {
		if (quantity < -1) {
			quantity = Long.MAX_VALUE;
		}
		return currency(quantity) + " (" + formatLong(quantity) + " gp)";
	}

	/**
	 * Gets symbol for money
	 * 
	 * @param quantity
	 *            the amount
	 * @return the symbol
	 */
	public static String currency(final long quantity) {
		if (quantity >= 10000 && quantity < 10000000) {
			return quantity / 1000 + "K";
		} else if (quantity >= 10000000 && quantity <= Integer.MAX_VALUE) {
			return quantity / 1000000 + "M";
		} else if (quantity > Integer.MAX_VALUE && quantity <= Long.MAX_VALUE) {
			return quantity / 10000000 + "B";
		} else {
			return "" + quantity + " gp";
		}
	}

	/**
	 * Formats numerals
	 * 
	 * @param num
	 *            the number
	 * @return formated number
	 */
	public static String formatLong(final long num) {
		return NumberFormat.getInstance().format(num);
	}

	public static int random(final int[] collection) {
		return collection[random(collection.length - 1)];
	}

	public static int random(List<Integer> collection) {
		return collection.get(random(collection.size() - 1));
	}

	public static String random(final String[] collection) {
		return collection[random(collection.length - 1)];
	}

	public static Position random(final Position[] collection) {
		return collection[random(collection.length - 1)];
	}

	public static int getElapsed(int day, int year) {
		if (year < 2013) {
			return 0;
		}

		int elapsed = 0;
		int currentYear = Misc.getYear();
		int currentDay = Misc.getDayOfYear();

		if (currentYear == year) {
			elapsed = currentDay - day;
		} else {
			elapsed = currentDay;

			for (int i = 1; i < 5; i++) {
				if (currentYear - i == year) {
					elapsed += 365 - day;
					break;
				} else {
					elapsed += 365;
				}
			}
		}

		return elapsed;
	}

	public static boolean isWeekend() {
		int day = Calendar.getInstance().get(7);
		return (day == 1) || (day == 6) || (day == 7);
	}

	/**
	 * Returns a pseudo-random {@code int} value between inclusive
	 * <code>min</code> and exclusive <code>max</code>.
	 * 
	 * <br>
	 * <br>
	 * This method is thread-safe. </br>
	 * 
	 * @param min
	 *            The minimum inclusive number.
	 * @param max
	 *            The maximum exclusive number.
	 * @return The pseudo-random {@code int}.
	 * @throws IllegalArgumentException
	 *             If the specified range is less <tt>0</tt>
	 */
	public static int exclusiveRandom(int min, int max) {
		if (max <= min) {
			max = min + 1;
		}
		return RANDOM.nextInt((max - min)) + min;
	}

	/**
	 * Returns a pseudo-random {@code int} value between inclusive <tt>0</tt>
	 * and exclusive <code>range</code>.
	 * 
	 * <br>
	 * <br>
	 * This method is thread-safe. </br>
	 * 
	 * @param range
	 *            The exclusive range.
	 * @return The pseudo-random {@code int}.
	 * @throws IllegalArgumentException
	 *             If the specified range is less <tt>0</tt>
	 */
	public static int exclusiveRandom(int range) {
		return exclusiveRandom(0, range);
	}

	/**
	 * Returns a pseudo-random {@code int} value between inclusive
	 * <code>min</code> and inclusive <code>max</code>.
	 * 
	 * @param min
	 *            The minimum inclusive number.
	 * @param max
	 *            The maximum inclusive number.
	 * @return The pseudo-random {@code int}.
	 * @throws IllegalArgumentException
	 *             If {@code max - min + 1} is less than <tt>0</tt>.
	 * @see {@link #exclusiveRandom(int)}.
	 */
	public static int inclusiveRandom(int min, int max) {
		if (max < min) {
			max = min + 1;
		}
		return exclusiveRandom((max - min) + 1) + min;
	}

	/**
	 * Returns a pseudo-random {@code int} value between inclusive <tt>0</tt>
	 * and inclusive <code>range</code>.
	 * 
	 * @param range
	 *            The maximum inclusive number.
	 * @return The pseudo-random {@code int}.
	 * @throws IllegalArgumentException
	 *             If {@code max - min + 1} is less than <tt>0</tt>.
	 * @see {@link #exclusiveRandom(int)}.
	 */
	public static int inclusiveRandom(int range) {
		return inclusiveRandom(0, range);
	}

	public static byte[] readFile(File s) {
		try {
			FileInputStream fis = new FileInputStream(s);
			FileChannel fc = fis.getChannel();
			ByteBuffer buf = ByteBuffer.allocate((int) fc.size());
			fc.read(buf);
			buf.flip();
			fis.close();
			return buf.array();
		} catch (Exception e) {
			System.out.println("FILE : " + s.getName() + " missing.");
			return null;
		}
	}

	public static int getTimeLeft(long start, int timeAmount, TimeUnit timeUnit) {
		start -= timeUnit.toMillis(timeAmount);
		long elapsed = System.currentTimeMillis() - start;
		int toReturn = timeUnit == TimeUnit.SECONDS ? (int) ((elapsed / 1000) % 60) - timeAmount : (int) ((elapsed / 1000) / 60) - timeAmount;
		if(toReturn <= 0)
			toReturn = 1;
		return timeAmount - toReturn;
	}

	/**
	 * Converts an array of bytes to an integer.
	 * 
	 * @param data
	 *            the array of bytes.
	 * @return the newly constructed integer.
	 */
	public static int hexToInt(byte[] data) {
		int value = 0;
		int n = 1000;
		for (int i = 0; i < data.length; i++) {
			int num = (data[i] & 0xFF) * n;
			value += num;
			if (n > 1) {
				n = n / 1000;
			}
		}
		return value;
	}

	public static Position delta(Position a, Position b) {
		return new Position(b.getX() - a.getX(), b.getY() - a.getY());
	}

	/**
	 * Picks a random element out of any array type.
	 * 
	 * @param array
	 *            the array to pick the element from.
	 * @return the element chosen.
	 */
	public static <T> T randomElement(T[] array) {
		return array[(int) (RANDOM.nextDouble() * array.length - 1)];
	}

	/**
	 * Picks a random element out of any list type.
	 * 
	 * @param list
	 *            the list to pick the element from.
	 * @return the element chosen.
	 */
	public static <T> T randomElement(List<T> list) {
		return list.get((int) (RANDOM.nextDouble() * list.size()));
	}

	/**
	 * Reads string from a data input stream.
	 * @param inputStream 	The input stream to read string from.
	 * @return 				The String value.
	 */
	public static String readString(ChannelBuffer buffer) {
		StringBuilder builder = null;
		try {
			byte data;
			builder = new StringBuilder();
			while ((data = buffer.readByte()) != 10) {
				builder.append((char) data);
			}
		} catch(IndexOutOfBoundsException e) {

		}
		return builder.toString();
	}
	
	private static final Set<String> DOMAIN_EXTENSION = new HashSet<>(Arrays.asList(".com", ".org", ".net", ".tk", ".info", ".eu", ".de", ".co.uk", ".cc", ".us", ".io"));

	private static final String[] BLOCKED_WORDS = new String[]{
		"@cr", "<img=", 
		"<col=", "<shad=", ":tradereq:", ":duelreq:", 
		"alora", "arrav", "osbase", "ikov", "elkoy",
		"noblepk", "soulplay", "runeworld", 
		"nigger", "nigga", "cunt"
	};

	public static boolean blockedWord(String word) {

		if(!GameSettings.CENSOR_ENABLED) {
			return false;
		}

		word = filterWord(word);
		
		ArrayList<String> bwords = new ArrayList<>();
		
		for (String bw : BLOCKED_WORDS) {
			if (word.contains(bw)) {
				bwords.add(word);
			}
		}
		
		for (String ext : DOMAIN_EXTENSION) {
			if (word.contains(ext)) {
				
				if (word.length() > word.indexOf(ext) + ext.length() && Character.isLetter(word.charAt(word.indexOf(ext) + ext.length()))) {
					if (bwords.size() > 0) {
						return true;
					}
				} else {
					return true;
				}
			}
		}
		
		return bwords.size() > 0;
	}
	
	public static String filterWord(String word) {
		word = word.toLowerCase().replaceAll("\\s+", " ");
		word = word.replaceAll(",", ".");
		word = word.replaceAll("_", "");
		word = word.replaceAll(" ", "");
		word = word.replaceAll("[.]\\s+", ".");
		word = word.replaceAll("0", "o");
		word = word.replaceAll("1", "i");
		word = word.replaceAll("!", "i");
		word = word.replaceAll("@", "a");
		word = word.replaceAll("3", "e");
		word = word.replaceAll("-", "");
		
		return word;
	}
	
	public static String formatNumber(long amount) {
		if (amount == Integer.MIN_VALUE) {
			return formatNumber(Integer.MIN_VALUE + 1);
		}

		if (amount < 0) {
			return "-" + formatNumber(-amount);
		}

		if (amount < 1000) {
			return Long.toString(amount);
		}

		Entry<Long, String> e = SUFFIXES.floorEntry(amount);

		Long divideBy = e.getKey();
		String suffix = e.getValue();

		long truncated = amount / (divideBy / 10);
		
		boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
		
		return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
	}
	
	
	/**
	 * Checks if the sum of two integers is an integer.
	 * 
	 * @param base
	 *            The base value.
	 * @param value
	 *            The value to be added.
	 * @return <code>true</code> if can
	 */
	public static boolean canAddInteger(int base, int value) {
		long total = (long) base + (long) value;

		return total <= Integer.MAX_VALUE && total > 0;
	}

	/**
	 * Gets the amount that can be added to the base integer.
	 * 
	 * @param base
	 *            The base value.
	 * @param value
	 *            The value.
	 * @return The amount.
	 */
	public static int getAddInteger(int base, int value) {
		if (base >= Integer.MAX_VALUE) {
			return 0;
		}
		
		long total = (long) base + (long) value;
		
		if (total < 1) {
			return 0;
		}
		
		if (total > Integer.MAX_VALUE) {
			return Integer.MAX_VALUE - base;
		}

		return value;
	}
	
	private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	/**
	 * Generates a random alpha-numeric string of the specified length.
	 * 
	 * @param len
	 *            The length.
	 * @return The string.
	 */
	public static String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);

		for (int i = 0; i < len; i++) {
			sb.append(AB.charAt(RANDOM.nextInt(AB.length())));
		}

		return sb.toString();
	}
	
	/**
	 * Sorts the {@link Map} by values.
	 * @param map
	 * @param descending
	 * @return
	 */
	public static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map, boolean descending) {
		SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
				int res = e1.getValue().compareTo(e2.getValue());

				if (descending) {
					res = e2.getValue().compareTo(e1.getValue());
				}

				return res != 0 ? res : 1;
			}
		});
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}
	
	public static boolean startsWithVowel(String word) {
		return VOWELS.contains(Character.toUpperCase(word.charAt(0)));
	}
	
	public static String getAOrAn(String s) {
		return startsWithVowel(s) ? "an" : "a";
	}
	
	/**
	 * A hash collection of vowels.
	 */
	private static final Set<Character> VOWELS = new HashSet<>(Arrays.asList(
			'A', 'E', 'I', 'O', 'U'
	));
	
	private static final NavigableMap<Long, String> SUFFIXES = new TreeMap<>();

	public static String getSpecificTimeLeftForTimer(long duration, Stopwatch stopwatch) {
		long milliseconds = (duration - stopwatch.elapsed());
		int days = (int) (milliseconds / (1000*60*60*24));
		milliseconds =  milliseconds - (days * (1000*60*60*24));

		StringBuilder timeLeft = new StringBuilder();
		if(days > 0) {
			timeLeft.append(days);
			timeLeft.append(" day");
			if(days > 1)
				timeLeft.append("s");

			//timeLeft.append(", ");
		}

		timeLeft.append(Misc.getSpecificTimeLeft((int) milliseconds / 1000));

		return timeLeft.toString();
	}

	public static String getTimeLeftForTimer(long duration, Stopwatch stopwatch) {
		long milliseconds = (duration - stopwatch.elapsed());
		int days = (int) (milliseconds / (1000*60*60*24));
		milliseconds =  milliseconds - (days * (1000*60*60*24));

		StringBuilder timeLeft = new StringBuilder();
		if(days > 0) {
			timeLeft.append(days);
			timeLeft.append(" day");
			if(days > 1)
				timeLeft.append("s");

			//timeLeft.append(", ");
		}

		timeLeft.append(Misc.getTimeLeft((int) milliseconds / 1000));

		return timeLeft.toString();
	}

	public static String getSpecificTimeLeft(int seconds) {
		int hours = 0;
		int minutes = 0;
		int secondsLeft = 0;

		String time = "";
		if (seconds > 3600) {
			hours = seconds / 3600;
			time = hours + " hour" + Misc.getPluralS(hours);
		}
		int value1 = 0;
		value1 = seconds - (hours * 3600);
		if (value1 > 0) {
			minutes = value1 / 60;
			if (minutes > 0) {
				if (!time.isEmpty()) {
					time = time + " ";
				}
				time = time + minutes + " minute" + Misc.getPluralS(minutes);
			}
		}
		int value2 = value1 - (minutes * 60);
		if (value2 > 0) {
			secondsLeft = value2;

			if (!time.isEmpty()) {
				time = time + " ";
			}
			time = time + secondsLeft + " second" + Misc.getPluralS(secondsLeft);
		}

		//if (time.isEmpty()) {
			//time = "a moment";
		//}
		return time;
	}

	public static String getTimeLeft(int seconds) {
		int hours = 0;
		int minutes = 0;
		int secondsLeft = 0;

		String time = "";
		if (seconds > 3600) {
			hours = seconds / 3600;
			time = hours + " hour" + Misc.getPluralS(hours);
		}

		int value1 = 0;
		value1 = seconds - (hours * 3600);
		if (value1 > 0 && hours <= 0) {
			minutes = value1 / 60;
			if (minutes > 0) {
				if (!time.isEmpty()) {
					time = time + " ";
				}
				time = time + minutes + " minute" + Misc.getPluralS(minutes);
			}
		}

		int value2 = value1 - (minutes * 60);
		if (value2 > 0 && hours <= 0 && minutes <= 0) {
			secondsLeft = value2;

			if (!time.isEmpty()) {
				time = time + " ";
			}
			time = time + secondsLeft + " second" + Misc.getPluralS(secondsLeft);
		}

		//if (time.isEmpty()) {
			//time = "a moment";
		//}
		return time;
	}

	public static String getShortTimeLeft(int seconds) {
		int hours = 0;
		int minutes = 0;
		int secondsLeft = 0;

		String time = "";
		if (seconds > 3600) {
			hours = seconds / 3600;
			time = hours + " hour" + Misc.getPluralS(hours);
		}

		int value1 = 0;
		value1 = seconds - (hours * 3600);
		if (value1 > 0) {
			minutes = value1 / 60;
			if (minutes > 0) {
				if (!time.isEmpty()) {
					time = time + " ";
				}
				time = time + minutes + " minute" + Misc.getPluralS(minutes);
			}
		}

		int value2 = value1 - (minutes * 60);
		if (value2 > 0 && hours <= 0 && minutes <= 0) {
			secondsLeft = value2;

			if (!time.isEmpty()) {
				time = time + " ";
			}
			time = time + secondsLeft + " second" + Misc.getPluralS(secondsLeft);
		}

		//if (time.isEmpty()) {
			//time = "a moment";
		//}
		return time;
	}

	public static String getPluralS(int amount) {
		if (amount > 1) {
			return "s";
		}
		if (amount == 0) {
			return "s";
		}
		return "";
	}

	public static String getPluralS(long amount) {
		if (amount > 1) {
			return "s";
		}
		return "";
	}

	/**
	 * Creates a new file if one does not already exist for the given path. If one does, this
	 * does nothing.
	 *
	 * @param path  the path to the given file.
	 * @param lines the lines to add to the file if absent.
	 * @throws IOException thrown if we cannot write for some reason.
	 */
	public static void createFileIfAbsent(Path path, String... lines) throws IOException {
		if (Files.notExists(path)) {
			Files.write(path, Arrays.asList(lines), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
		}
	}

	static {
		SUFFIXES.put(1_000L, "k");
		SUFFIXES.put(1_000_000L, "m");
		SUFFIXES.put(1_000_000_000L, "b");
	}

	public static void detectVPN(String ip_address, String username){
		GameSettings.CHECKED_IPS.put(ip_address, FloodCheckStatus.CHECKING);
		try {
			URL obj = new URL("https://www.ipqualityscore.com/api/json/ip/SOMn2K4OZ6qqRAWupg4poacipkVhmFwQ/"+ip_address+"?strictness=1&fast=1&allow_public_access_points=true");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.0.5) Gecko/2008120122 Firefox/3.0.5");
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				String apiResponse = response.toString();
				if(apiResponse == null || !isJSONValid(apiResponse)) {
					System.out.println("VPN DETECTION API ERROR. Invalid Response");
					return;
				}
				JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();
				int ip_score = Integer.valueOf(jsonObject.get("fraud_score").getAsString());
				String is_proxy = jsonObject.get("proxy").getAsString();
				String is_vpn = jsonObject.get("vpn").getAsString();
				if(/*ip_score >= GameSettings.SUS_IP_SCORE || */is_proxy.equals("true") || is_vpn.contentEquals("true")){
					GameSettings.CHECKED_IPS.put(ip_address, FloodCheckStatus.IS_PROXY);
					World.sendStaffMessage("<img=483> [SUS ACCOUNT] " + "@red@" + username + " has logged in with high-risk ip-address [ "+ip_address+" ] | SCORE: "+ip_score+" - VPN: "+is_vpn);
				} else {
					GameSettings.CHECKED_IPS.put(ip_address, FloodCheckStatus.SAFE);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static String getYouTubeVideoID(String url) {
		String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";

		Pattern compiledPattern = Pattern.compile(pattern);
		Matcher matcher = compiledPattern.matcher(url); //url is youtube url for which you want to extract the id.
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

}