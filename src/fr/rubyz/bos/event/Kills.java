package fr.rubyz.bos.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import fr.rubyz.bos.BallsOfSteel;

public class Kills implements Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();

		int death = BallsOfSteel.death.get(p.getUniqueId());
		BallsOfSteel.death.put(p.getUniqueId(), death + 1);

		if (e.getEntity().getKiller() != null) {

			if (e.getEntity().getKiller() instanceof Player) {

				Player killer = (Player) e.getEntity().getKiller();

				BallsOfSteel.kills.put(killer.getUniqueId(), BallsOfSteel.kills.get(killer) + 1);
			}

		}

	}

}
