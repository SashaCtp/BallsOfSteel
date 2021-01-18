package fr.rubyz.bos.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Storage implements Listener{

	/*
	 * The Storage will be used to get materials, tools in-game
	 * Every team have a large chest in their base, right click and get
	 * the storage menu (Infinite resources)
	 */
	
	public Storage(){
		
		
		
	}
	
	//Event Section
	@EventHandler
	public void on(PlayerInteractEvent e){
		
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			
			
			
		}
		
	}
	
	
	
}
