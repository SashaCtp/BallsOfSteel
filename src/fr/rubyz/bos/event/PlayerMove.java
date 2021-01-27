package fr.rubyz.bos.event;

import fr.rubyz.bos.BallsOfSteel;
import fr.rubyz.bos.GameState;
import fr.rubyz.bos.Team;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.EventListener;

public class PlayerMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e){

        Player p = e.getPlayer();

        if((BallsOfSteel.gameState.equals(GameState.LOBBY) || BallsOfSteel.gameState.equals(GameState.FINISH)) && p.getFoodLevel() < 20)
            p.setFoodLevel(20);

        if(p.getLocation().getX() > -197 || p.getLocation().getY() < 0 || p.getLocation().getY() > 216 || p.getLocation().getX() < -227 || p.getLocation().getZ() > 589 || p.getLocation().getZ() < 559){
            if(BallsOfSteel.gameState.equals(GameState.LOBBY)){
                Location loc = new Location(BallsOfSteel.getSpawn().getWorld(), BallsOfSteel.getSpawn().getX(), BallsOfSteel.getSpawn().getY(), BallsOfSteel.getSpawn().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
                p.teleport(loc);
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
            }
        }

        if(p.getLocation().getY() <= 0){

            if(BallsOfSteel.gameState.equals(GameState.GAME)){

                p.setHealth(0);

            }else if(BallsOfSteel.gameState.equals(GameState.FINISH)){

                Team t = Team.getPlayerTeam(p);

                if(t == null)
                    p.teleport(BallsOfSteel.getSpawn());
                else
                    p.teleport(t.getSpawn());

            }

        }

    }

}