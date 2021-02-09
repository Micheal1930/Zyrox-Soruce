package com.zyrox.world.entity.impl.player;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zyrox.GameSettings;
import com.zyrox.world.World;

/**
 * 
 * @author Nick Hartskeerl <apachenick@hotmail.com>
 *
 */
public class PlayerLogout implements Runnable {
	
	/**
	 * 
	 */
	public static ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
	
	/**
	 * 
	 */
	public static void init() {
		EXECUTOR.submit(new PlayerLogout());
	}

	@Override
	public void run() {
		
		while (true) {
			try {
				long start = System.currentTimeMillis();
				int amount = 0;
				Iterator<Player> $it = World.getLogoutQueue().iterator();

				while ($it.hasNext()) {

					Player player = $it.next();

					if (player == null || amount >= GameSettings.LOGOUT_THRESHOLD) {
						break;
					}

					if (PlayerHandler.handleLogout(player)) {
						$it.remove();
						amount++;
					}

				}

				long sleep = (600 + start) - System.currentTimeMillis();

				if (sleep > 0) {
					try {
						Thread.sleep(sleep);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

}
