package fr.rubyz.bos.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import fr.rubyz.bos.utils.Util;
import fr.rubyz.bos.BallsOfSteel;

public class ParamInventory implements Listener{
	
	private static Inventory inv;
	
	/*
	 * Parameters menu pages :
	 *   0 => Main page
	 *   1 => Default block selection
	 *   2 => Scenarios
	 *   3 => Effects
	 */
	
	public static void openParamInventory(int page, Player p){
		
		if(page == 0){
			inv = Bukkit.createInventory(null,27, "Parameters");
			
			inv.setItem(10, Util.make(BallsOfSteel.gameConfig.getBuildBlockMaterial(), 1, "§7Default Block", null));
			
			if(BallsOfSteel.gameConfig.isMidProtected())
				inv.setItem(11, Util.addGlow(Util.make(Material.BEACON, 1, "§bMid protection : §aEnabled", null)));
			else
				inv.setItem(11, Util.make(Material.BEACON, 1, "§bProtection du Mid : §cDisabled", null));
			
			if(BallsOfSteel.gameConfig.isInfiniteBuildBlock())
				inv.setItem(12, Util.addGlow(Util.make(Material.CRAFTING_TABLE, 1, "§eInfinite Block : §aEnabled", null)));
			else
				inv.setItem(12, Util.make(Material.CRAFTING_TABLE, 1, "§eInfinite Block : §cDisabled", null));
			
			inv.setItem(13, Util.make(Material.LEATHER_HELMET, 1, "§2Equipment", null));
			
			inv.setItem(14, Util.make(Material.PAPER, 1, "§fScenarios", null));
			
			inv.setItem(16, Util.make(Material.BLAZE_POWDER, 1, "§9Effects", null));
			
			inv.setItem(26, Util.make(Material.BARRIER, 1, "§cClose", null));
		}else if(page == 1){
			inv = Bukkit.createInventory(null,27, "Parameters > Blocks");
			
			
			if(BallsOfSteel.gameConfig.getBuildBlockMaterial().equals(Material.DIRT))
				inv.setItem(11, Util.make(Material.DIRT, 1, "§aDirt", null));
			else
				inv.setItem(11, Util.make(Material.DIRT, 1, "§fDirt", null));
			
			if(BallsOfSteel.gameConfig.getBuildBlockMaterial().equals(Material.SANDSTONE))
				inv.setItem(12, Util.make(Material.SANDSTONE, 1, "§aSandstone", null));
			else
				inv.setItem(12, Util.make(Material.SANDSTONE, 1, "§fSandstone", null));
			
			if(BallsOfSteel.gameConfig.getBuildBlockMaterial().equals(Material.RED_SANDSTONE))
				inv.setItem(13, Util.make(Material.RED_SANDSTONE, 1, "§aRed Sandstone", null));
			else
				inv.setItem(13, Util.make(Material.RED_SANDSTONE, 1, "§fRed Sandstone", null));
			
			if(BallsOfSteel.gameConfig.getBuildBlockMaterial().equals(Material.WHITE_STAINED_GLASS))
				inv.setItem(14, Util.make(Material.WHITE_STAINED_GLASS, 1, "§aGlass", null));
			else
				inv.setItem(14, Util.make(Material.WHITE_STAINED_GLASS, 1, "§fGlass", null));
			
			if(BallsOfSteel.gameConfig.getBuildBlockMaterial().equals(Material.QUARTZ_BLOCK))
				inv.setItem(15, Util.make(Material.QUARTZ_BLOCK, 1, "§aQuartz", null));
			else
				inv.setItem(15, Util.make(Material.QUARTZ_BLOCK, 1, "§fQuartz", null));
			
			
			inv.setItem(26, Util.make(Material.ARROW, 1, "Back", null));
		}else if(page == 2){
			inv = Bukkit.createInventory(null,27, "Parameters > Scenarios");
			inv.setItem(26, Util.make(Material.ARROW, 1, "Back", null));
		}else if(page == 3){
			inv = Bukkit.createInventory(null,27, "Parameters > Effects");
			
			if(BallsOfSteel.effectList.contains(ParamInventoryEvent.regen1))
				inv.setItem(0, Util.makePotionHidePotionEffect(Material.POTION, (byte) 8193, 1, "§5Regeneration I - §aActive", null));
			else
				inv.setItem(0, Util.makePotionHidePotionEffect(Material.POTION, (byte) 8193, 1, "§5Regeneration I", null));
			
			if(BallsOfSteel.effectList.contains(ParamInventoryEvent.regen2))
				inv.setItem(1, Util.makePotionHidePotionEffect(Material.POTION, (byte) 8193, 2, "§5Regeneration II - §aActive", null));
			else
				inv.setItem(1, Util.makePotionHidePotionEffect(Material.POTION, (byte) 8193, 2, "§5Regeneration II", null));
			
			if(BallsOfSteel.effectList.contains(ParamInventoryEvent.regen3))
				inv.setItem(2, Util.makePotionHidePotionEffect(Material.POTION, (byte) 8193, 3, "§5Regeneration III - §aActive", null));
			else
				inv.setItem(2, Util.makePotionHidePotionEffect(Material.POTION, (byte) 8193, 3, "§5Regeneration III", null));
			
			
			if(BallsOfSteel.effectList.contains(ParamInventoryEvent.strength1))
				inv.setItem(9, Util.makePotionHidePotionEffect(Material.POTION, (byte) 8201, 1, "§cStrength I - §aActive", null));
			else
				inv.setItem(9, Util.makePotionHidePotionEffect(Material.POTION, (byte) 8201, 1, "§cStrength I", null));
			
			if(BallsOfSteel.effectList.contains(ParamInventoryEvent.strength2))
				inv.setItem(10, Util.makePotionHidePotionEffect(Material.POTION, (byte) 8201, 2, "§cStrength II - §aActive", null));
			else
				inv.setItem(10, Util.makePotionHidePotionEffect(Material.POTION, (byte) 8201, 2, "§cStrength II", null));
			
			if(BallsOfSteel.effectList.contains(ParamInventoryEvent.strength3))
				inv.setItem(11, Util.makePotionHidePotionEffect(Material.POTION, (byte) 8201, 3, "§cStrength III - §aActive", null));
			else
				inv.setItem(11, Util.makePotionHidePotionEffect(Material.POTION, (byte) 8201, 3, "§cStrength III", null));
			
			if(BallsOfSteel.effectList.contains(ParamInventoryEvent.speed1))
				inv.setItem(18, Util.makePotionHidePotionEffect(Material.POTION, (byte) 8194, 1, "§3Speed I - §aActive", null));
			else
				inv.setItem(18, Util.makePotionHidePotionEffect(Material.POTION, (byte) 8194, 1, "§3Speed I", null));
				
			if(BallsOfSteel.effectList.contains(ParamInventoryEvent.speed2))
				inv.setItem(19, Util.makePotionHidePotionEffect(Material.POTION, (byte) 8194, 2, "§3Speed II - §aActive", null));
			else
				inv.setItem(19, Util.makePotionHidePotionEffect(Material.POTION, (byte) 8194, 2, "§3Speed II", null));
				
			if(BallsOfSteel.effectList.contains(ParamInventoryEvent.speed3))
				inv.setItem(20, Util.makePotionHidePotionEffect(Material.POTION, (byte) 8194, 3, "§3Speed III - §aActive", null));
			else
				inv.setItem(20, Util.makePotionHidePotionEffect(Material.POTION, (byte) 8194, 3, "§3Speed III", null));	
						
			
			inv.setItem(26, Util.make(Material.ARROW, 1, "Back", null));
		}
		
		p.openInventory(inv);
		
	}

}
