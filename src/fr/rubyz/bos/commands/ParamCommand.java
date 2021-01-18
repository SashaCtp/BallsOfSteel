package fr.rubyz.bos.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.rubyz.bos.BallsOfSteel;
import fr.rubyz.bos.GameState;
import fr.rubyz.bos.inventory.ParamInventory;
import fr.rubyz.bos.utils.Util;

public class ParamCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof Player){
				
			Player p = (Player) sender;
				
			if(p.isOp()){
					
				if(BallsOfSteel.instance.getGameConfig().isHost()){
					
					if(GameState.isState(GameState.LOBBY)){
						ParamInventory.openParamInventory(0, p);
					}else{
						p.sendMessage(Util.getGamePrefix() + "It's to late for it !");
						p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 5F, 5F);
					}
					
				}
					
			}else{
				p.sendMessage("§cYou don't have the permission !");
			}
			
		}
		
		return true;
	}

}
