package fr.rubyz.bos.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import fr.rubyz.bos.BallsOfSteel;

public class Kills implements Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {

		BallsOfSteel.gameStats.addDeath(e.getEntity());

		if (e.getEntity().getKiller() != null) {

			BallsOfSteel.gameStats.addKill(e.getEntity().getKiller());

		}

	}

}
