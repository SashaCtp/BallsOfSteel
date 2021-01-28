package fr.rubyz.bos.commands;

import fr.rubyz.bos.BallsOfSteel;
import fr.rubyz.bos.GameConfiguration;
import fr.rubyz.bos.GameState;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEditBookEvent;

public class StatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // Display the sender's statistics
        if(args.length == 0){

            if(BallsOfSteel.gameState.equals(GameState.LOBBY)){
                sender.sendMessage("§cThe game hasn't started yet !");
                return true;
            }

            if(!(sender instanceof Player)){
                sender.sendMessage("§cThe console is not a player !");
                return true;
            }

            BallsOfSteel.gameStats.displayPlayerStats((Player) sender, (Player) sender);
            return true;

        }else if(args.length == 1){

            if(!sender.isOp()){
                sender.sendMessage("§cYou must be op to use this command !");
                return true;
            }

            if(BallsOfSteel.gameState.equals(GameState.LOBBY)){
                sender.sendMessage("§cThe game hasn't started yet !");
                return true;
            }

            try{

                Player statsPlayer = Bukkit.getServer().getPlayer(args[0]);
                BallsOfSteel.gameStats.displayPlayerStats(statsPlayer, (Player) sender);
                return true;

            }catch (Exception e){
                sender.sendMessage("§cThe given player is not connected on the server !");
                return true;
            }

        }else{

            if(sender.isOp())
                sender.sendMessage("§cUsage : /stats <Player>");
            else
                sender.sendMessage("§cUsage : /stats");

            return false;

        }

    }
}
