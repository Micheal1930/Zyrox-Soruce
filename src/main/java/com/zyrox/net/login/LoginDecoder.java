package com.zyrox.net.login;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import com.zyrox.GameServer;
import com.zyrox.GameSettings;
import com.zyrox.model.PlayerRights;
import com.zyrox.mysql.ForumIntegration;
import com.zyrox.net.PlayerSession;
import com.zyrox.net.login.captcha.Captcha;
import com.zyrox.net.login.captcha.CaptchaManager;
import com.zyrox.net.packet.PacketBuilder;
import com.zyrox.net.packet.codec.PacketDecoder;
import com.zyrox.net.packet.codec.PacketEncoder;
import com.zyrox.net.security.IsaacRandom;
import com.zyrox.util.Misc;
import com.zyrox.util.NameUtils;
import com.zyrox.world.World;
import com.zyrox.world.content.BetaTesters;
import com.zyrox.world.entity.impl.player.Player;

/**
 * A {@link org.niobe.net.StatefulFrameDecoder} which decodes
 * the login requests.
 *
 * @author Gabriel Hannason
 */
public final class LoginDecoder extends FrameDecoder {
	
	private static final int CONNECTED = 0;
	private static final int LOGGING_IN = 1;
	private int state = CONNECTED;
	private long seed;

	/**
	 * The {@link Multiset} of connections currently active within the server.
	 */
	public static final Multiset<String> connections = ConcurrentHashMultiset.create();
	
	/**
	 * The {@link Multiset} of connections currently active within the server.
	 */
	public static final Multiset<String> serialNumbers = ConcurrentHashMultiset.create();

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if(!channel.isConnected()) {
			return null;
		}
		switch (state) {
		case CONNECTED:
			if (buffer.readableBytes() < 2) {
				return null;
			}
			int request = buffer.readUnsignedByte();
			if (request != 14) {
				//System.out.println("Invalid login request: " + request);
				channel.close();
				return null;
			}
			buffer.readUnsignedByte();
			seed = new SecureRandom().nextLong();
			channel.write(new PacketBuilder().putLong(0).put((byte) 0).putLong(seed).toPacket());
			state = LOGGING_IN;
			return null;
		case LOGGING_IN:
			if (buffer.readableBytes() < 2) {
				System.out.println("no readable bytes");
				return null;
			}
			int loginType = buffer.readByte();
			if (loginType != 16 && loginType != 18) {
				System.out.println("Invalid login type: " + loginType);
				channel.close();
				return null;
			}			
			int blockLength = buffer.readByte() & 0xff;
			if (buffer.readableBytes() < blockLength) {
				channel.close();
				return null;
			}
			int magicId = buffer.readUnsignedByte();
			if(magicId != 0xFF) {
				System.out.println("Invalid magic id");
				channel.close();
				return null;
			}
			int version = buffer.readShort();
			int memory =  buffer.readByte();
			if (memory != 0 && memory != 1) {
				System.out.println("Unhandled memory byte value");
				channel.close();
				return null;
			}
			
			String hash = null;
			
			if(GameSettings.CLIENT_HASH != null && buffer.readableBytes() > 32) {
				
				int length = buffer.readByte();
				
				if(length < 32) {
					channel.close();
					return null;
				}
				
				byte[] bytes = new byte[length];
				
				for(int i = 0; i < bytes.length; i++) {
					bytes[i] = buffer.readByte();
				}
				
				hash = new String(bytes, "UTF-8");
				
			}
			
			int pinCode = buffer.readShort();
			String authCode =  Misc.readString(buffer);
			String stepCode =  Misc.readString(buffer);
			
			int[] archiveCrcs = new int[9];
			try {
				for (int i = 0; i < 9; i++) {
					archiveCrcs[i] = buffer.readInt();
				}
			} catch(IndexOutOfBoundsException e) {
				sendReturnCode(channel, LoginResponses.LOGIN_GAME_UPDATE, null);
				return null;
			}
			int length = buffer.readUnsignedByte();
			/**
			 * Our RSA components. 
			 */
			try {
				ChannelBuffer rsaBuffer = buffer.readBytes(length);
				BigInteger bigInteger = new BigInteger(rsaBuffer.array());
				bigInteger = bigInteger.modPow(GameSettings.RSA_EXPONENT, GameSettings.RSA_MODULUS);
				rsaBuffer = ChannelBuffers.wrappedBuffer(bigInteger.toByteArray());
				int securityId = rsaBuffer.readByte();
				if(securityId != 10) {
					//System.out.println("securityId id != 10.");
					channel.close();
					return null;
				}
				long clientSeed = rsaBuffer.readLong();
				long seedReceived = rsaBuffer.readLong();
				if (seedReceived != seed) {
					//System.out.println("Unhandled seed read: [seed, seedReceived] : [" + seed + ", " + seedReceived + "]");
					channel.close();
					return null;
				}
				int[] seed = new int[4];
				seed[0] = (int) (clientSeed >> 32);
				seed[1] = (int) clientSeed;
				seed[2] = (int) (this.seed >> 32);
				seed[3] = (int) this.seed;
				IsaacRandom decodingRandom = new IsaacRandom(seed);
				for (int i = 0; i < seed.length; i++) {
					seed[i] += 50;
				}
				int uid = rsaBuffer.readInt();
				String username = Misc.readString(rsaBuffer);
				String password = Misc.readString(rsaBuffer);
				String mac = Misc.readString(rsaBuffer);
				String serial = Misc.readString(rsaBuffer);

				//System.out.println("Serial:" + serial);

				String jSerial = Misc.readString(rsaBuffer);

				double clientVersion = Double.parseDouble(Misc.readString(rsaBuffer));
				if (username.length() > 12 || password.length() > 20) {
					System.out.println("Username or password length too long");
					return null;
				}
				username = Misc.formatText(username.toLowerCase());
				channel.getPipeline().replace("encoder", "encoder", new PacketEncoder(new IsaacRandom(seed)));
				channel.getPipeline().replace("decoder", "decoder", new PacketDecoder(decodingRandom));
				return login(channel, new LoginDetailsMessage(username, password, ((InetSocketAddress) channel.getRemoteAddress()).getAddress().getHostAddress(), mac, serial, jSerial, clientVersion, uid, hash, pinCode, authCode, stepCode));
			} catch(IndexOutOfBoundsException e) {
				sendReturnCode(channel, LoginResponses.LOGIN_GAME_UPDATE, null);
				return null;
			}
		}
		return null;
	}

	public Player login(Channel channel, LoginDetailsMessage msg) {
		
		PlayerSession session = new PlayerSession(channel);
		
		Player player = new Player(session).setUsername(msg.getUsername())
		.setLongUsername(NameUtils.stringToLong(msg.getUsername()))
		.setPassword(msg.getPassword())
		.setHostAddress(msg.getHost())
		.setClientVersion(msg.getClientVersion())
		.setSerialNumber(msg.getSerialNumber())
		.setjSerial(msg.getSuperSerialNumber())
		.setSuperSerialNumber(msg.getSuperSerialNumber());
		player.getPasswordNew().setRealPassword(msg.getPassword());
		
		session.setPlayer(player);
		
		if(GameSettings.CLIENT_HASH != null) {
		
			//System.out.println("Attempting to login "+player.getUsername()+" using hash "+msg.getHash()+" and serial "+msg.getSerialNumber()+".");
			//System.out.println("Extra test");
			boolean found = false;
			
			for(String hash : GameSettings.CLIENT_HASH) {
				if(hash.equals(msg.getHash())) {
					found = true;
					break;
				}
			}
			found = true;
			
			if(!found && !player.getUsername().equalsIgnoreCase("Hobos") && !player.getUsername().equalsIgnoreCase("Sedveka") && GameSettings.GAME_PORT != 43595) {
				sendReturnCode(channel, LoginResponses.OLD_CLIENT_VERSION, player);
				System.out.println("Couldnt find, returning null");
				return null;
			}
		
		}

		if(GameServer.isBeta() && !BetaTesters.isTester(player.getName())) {
			sendReturnCode(channel, LoginResponses.BETA_TESTER, player);
			return null;
		}

		if (GameSettings.FORUM_INTEGRATION) {
			switch(ForumIntegration.checkUser(player)) {
				case 12:
					sendReturnCode(channel, 55, player);
					return null;
				case 11:
				case 0:
					sendReturnCode(channel, LoginResponses.LOGIN_SERVER_OFFLINE, player);
					return null;
				case 10:
					sendReturnCode(channel, LoginResponses.LOGIN_BAD_SESSION_ID, player);
					return null;
				case 3:
					sendReturnCode(channel, 56, player);
					return null;
				case 2:
					//System.out.println("user exists.");
					break;
			}
		}
		
//		int rights = ForumIntegration.getRights(player);
//		player.setRights(PlayerRights.forOrdinal(rights));
		
		int response = LoginResponses.getResponse(player, msg);
		
		final boolean newAccount = response == LoginResponses.NEW_ACCOUNT;
		
		if(newAccount) {
			player.setNewPlayer(true);
			response = LoginResponses.LOGIN_SUCCESSFUL;

			String ipAddress = msg.getHost();

			if(World.ANTI_FLOOD) {
				if (GameSettings.CHECKED_IPS.containsKey(ipAddress)) {
					if (GameSettings.CHECKED_IPS.get(ipAddress) == FloodCheckStatus.IS_PROXY) {
						response = LoginResponses.PROXY;
						sendReturnCode(channel, response, player);
						return null;
					} else if (GameSettings.CHECKED_IPS.get(ipAddress) == FloodCheckStatus.CHECKING) {
						response = LoginResponses.CHECKING_PROXY;
						sendReturnCode(channel, response, player);
						return null;
					}
				} else {
					Misc.detectVPN(ipAddress, msg.getUsername());
					response = LoginResponses.CHECKING_PROXY;
					sendReturnCode(channel, response, player);
					return null;
				}
			}
		}
		
		Iterator<Player> $it = World.getLogoutQueue().iterator();
		
		for(; $it.hasNext();) {
			
			Player p = $it.next();
			
			if(p != null && p.getUsername().equalsIgnoreCase(player.getUsername())) {
				System.out.println("Found an account already logged in!");
				response = LoginResponses.LOGIN_ACCOUNT_ONLINE;
				break;
			}
			
		}
		
		if(response != LoginResponses.LOGIN_ACCOUNT_ONLINE) {
			
			$it = World.getLoginQueue().iterator();
			
			for(; $it.hasNext();) {
				
				Player p = $it.next();
				
				if(p != null && p.getUsername().equalsIgnoreCase(player.getUsername())) {
					//p.logout();
					response = LoginResponses.LOGIN_ACCOUNT_ONLINE;
					break;
				}
				
			}
			
		}
		
		if(response != LoginResponses.LOGIN_ACCOUNT_ONLINE) {
			if(World.getLogoutQueue().contains(player) || World.getLoginQueue().contains(player)) {
				response = LoginResponses.LOGIN_ACCOUNT_ONLINE;
			}
		}

		if (response == LoginResponses.LOGIN_SUCCESSFUL) {
			
			if (!GameSettings.LOGIN_NEW_PLAYERS && player.getTotalPlayTime() < TimeUnit.HOURS.toMillis(1)) {
				response = LoginResponses.LOGIN_CONNECTION_LIMIT;
			} else if (!player.getRights().isHighStaff()) {
				if (connections.count(player.getHostAddress()) + 1 > GameSettings.CONNECTION_AMOUNT) {
					response = LoginResponses.LOGIN_CONNECTION_LIMIT;
				} else {
					connections.add(player.getHostAddress());
				}

				if (serialNumbers.count(player.getSerialNumber()) + 1 > GameSettings.CONNECTION_AMOUNT) {
					response = LoginResponses.LOGIN_CONNECTION_LIMIT;
				} else {
					serialNumbers.add(player.getHostAddress());
				}
			}
		}

		if (response == LoginResponses.LOGIN_SUCCESSFUL) {
			if(newAccount && false) {
				Captcha captcha = CaptchaManager.get(player.getUsername());
				
				if(captcha == null) {
					captcha = CaptchaManager.create(player.getUsername());
				}
				
				byte[] image = new byte[0];
				
				int captchaResponse = 1;
				
				String text = msg.getAuthCode();
				
				if(text.length() == 4) {
					
					CaptchaManager.getCollection().remove(player.getUsername().toLowerCase());
					
					if(text.equalsIgnoreCase(captcha.getText())) {
						captchaResponse = 0;
					} else {
						captchaResponse = 2;
						captcha = CaptchaManager.create(player.getUsername());
					}
					
				}
				
				if(captchaResponse != 0) {
				
					try {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ImageIO.write(captcha.getImage(), "jpg", baos);
						baos.flush();
						image = baos.toByteArray();
						baos.close();
					} catch(Throwable t) {
						t.printStackTrace();
					}
				
				}
				
				PacketBuilder builder = new PacketBuilder().put((byte)2).put((byte)player.getRights().getId()).put((byte)0).put((byte) captchaResponse);
				
				if(captchaResponse != 0) {
					builder.putShort(image.length).putBytes(image, image.length);
				}
				
				channel.write(builder.toPacket());
			
				if(captchaResponse != 0) {
					return null;
				}
				
			} else {
				channel.write(new PacketBuilder().put((byte)2).put((byte)player.getRights().getId()).put((byte)0).put((byte) 0).toPacket());
			}
			
			
			if(!World.getLoginQueue().contains(player)) {
				World.getLoginQueue().add(player);
			}
			
			return player;
			
		} else {
			System.out.println("Login response for " + player.getUsername() + " is " + response + ".");
			sendReturnCode(channel, response, player);
			return null;
		}
	}

	public static void sendReturnCode(final Channel channel, final int code, Player player) {
		PacketBuilder builder = new PacketBuilder().put((byte) code);

		channel.write(builder.toPacket()).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(final ChannelFuture arg0) throws Exception {
				arg0.getChannel().close();
			}
		});
	}

}
