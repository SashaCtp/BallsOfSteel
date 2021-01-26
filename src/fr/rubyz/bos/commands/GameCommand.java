package fr.rubyz.bos.commands;

import fr.rubyz.bos.BallsOfSteel;
import fr.rubyz.bos.GameManager;
import fr.rubyz.bos.GameState;
import fr.rubyz.bos.inventory.ParamInventory;
import fr.rubyz.bos.utils.Util;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        switch (args[0]){

            case "start":
                gameStart(sender, cmd, label, args);
                break;
            case "stop":
                gameStop(sender, cmd, label, args);
                break;
            case "param":
                gameParam(sender, cmd, label, args);
                break;
            case "time":
                gameTime(sender, cmd, label, args);
                break;
            default:
                sendCommandHelp(sender);
                break;

        }

        return true;

    }

    /**
     * Send the help text for the command
     * @param sender Sender command's sender
     */
    public void sendCommandHelp(CommandSender sender){

    }

    /**
     * Send "You do not have the permission !" to the command sender
     * @param sender Command's sender
     */
    public void sendNoPermissionMessage(CommandSender sender){

        sender.sendMessage("§cYou do not have the permission !");

    }



    /**
     * Pause the game
     */
    public void gameStart(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender.isOp()){

            GameManager.start();

        }else{

            sendNoPermissionMessage(sender);

        }

    }

    /**
     * Stop the game
     */
    public void gameStop(CommandSender sender, Command cmd, String label, String[] args) {

        if(!sender.isOp()){
            sendNoPermissionMessage(sender);
            return;
        }

        BallsOfSteel.remainingTime = 5;

    }

    /**
     * Open the parameters menu
     */
    public void gameParam(CommandSender sender, Command cmd, String label, String[] args){

        if(!(sender instanceof Player))
            return;

        if(!sender.isOp()){
            sendNoPermissionMessage(sender);
            return;
        }

        Player p = (Player) sender;


        if(!BallsOfSteel.instance.getGameConfig().isHost())
            return;

        if(BallsOfSteel.gameState.equals(GameState.LOBBY)){
            ParamInventory.openParamInventory(0, p);
        }else{
            p.sendMessage(Util.getGamePrefix() + "The game already started !");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 5F, 5F);
        }

    }

    /**
     * Learn about the current game parameters
     */
    public void gameInfo(CommandSender sender, Command cmd, String label, String[] args) {



    }

    /**
     * Learn about the current game parameters
     *
     * The command looks like that : /game time add/remove/set value
     */
    public void gameTime(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player))
            return;

        if(!sender.isOp()) {
            sendNoPermissionMessage(sender);
            return;
        }

        Player p = (Player) sender;

        if(args.length != 3){
            p.sendMessage("§cUsage : /game time <add/remove/set> <time>");
            return;
        }

        // Action parsing
        String action = args[1];
        if(!(action.equals("set") || action.equals("add") || action.equals("remove"))){
            p.sendMessage("§cUsage : /game time <add/remove/set> <time>");
            return;
        }

        // Time parsing
        int time = 0;

        try{
            time = Integer.parseInt(args[2]);

            if(time < 0) {
                p.sendMessage("§cPlease give a positive value");
                return;
            }
        }catch(Exception e){
            p.sendMessage("§cPlease give a correct value");
            return;
        }

        switch (action){

            case "set":
                BallsOfSteel.remainingTime = time;
                break;
            case "add":
                BallsOfSteel.remainingTime = BallsOfSteel.remainingTime + time;
                break;
            case "remove":
                if(time > BallsOfSteel.remainingTime)
                    p.sendMessage("§cYou can not remove more time !");
                else
                    BallsOfSteel.remainingTime = BallsOfSteel.remainingTime - time;
                break;
        }

    }

}
