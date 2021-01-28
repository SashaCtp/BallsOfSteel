package fr.rubyz.bos.utils;

import java.util.*;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import fr.rubyz.bos.BallsOfSteel;
import fr.rubyz.bos.GameState;
import fr.rubyz.bos.Team;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagList;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle.EnumTitleAction;

public class Util {
	
	
	//Chat Util
	
	private static String gamePrefix = "§7[§6BallsOfSteel§7]§r ";
	public static String getGamePrefix(){
		return gamePrefix;
	}
	
	public static ItemStack make(Material material, int amount, String name, List<String> lore){
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        assert(itemMeta != null);
        if(name != null){
        	itemMeta.setDisplayName(name);
        }
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        item.setAmount(amount);
        return item;
    }
	
	public static ItemStack makePotionHidePotionEffect(Material material, byte data, int amount, String name, List<String> lore){
        @SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(material, amount, data);
        ItemMeta itemMeta = item.getItemMeta();
        assert(itemMeta != null);
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(itemMeta);
        item.setAmount(amount);
        return item;
    }
    
    //Item Util
    public static ItemStack addGlow(ItemStack item) {
        net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) tag = nmsStack.getTag();
        NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }
		
	private static void sendTime(Player player, int ticks) {
		PacketPlayOutTitle p = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, 20, ticks, 20);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(p);
	}

	// Infinite Block functions ============----------

    /**
     * Make the infinite item if infinite option is on
     * @return Infinite item
     */
    public static ItemStack getInfiniteItem(){
        if(BallsOfSteel.gameConfig.isInfiniteBuildBlock())
            return Util.make(BallsOfSteel.gameConfig.getBuildBlockMaterial(), 1, null, Collections.singletonList("§7Infinite block"));
        else
            return null;
    }

    /**
     * Checks if the ItemStack is the infinite construction block
     * @param itemStack Item to verify
     * @return boolean
     */
	public static boolean isTheInfiniteBlock(ItemStack itemStack){

	    if(!itemStack.getType().equals(BallsOfSteel.gameConfig.getBuildBlockMaterial()))
	        return false;

	    if(itemStack.getItemMeta() == null)
            return false;

	    if(itemStack.getItemMeta().getLore() == null)
	        return false;

        return itemStack.getItemMeta().getLore().contains("§7Infinite block");

    }
	
	// Tab Util ============----------
	
	public static void updateTab(){
		
		String state = null;
		StringBuilder ranking = new StringBuilder(" \n");
		
		for(Player pls : Bukkit.getOnlinePlayers()){
		
			if(BallsOfSteel.gameState.equals(GameState.LOBBY)){
				state = "Waiting for players - " + Bukkit.getOnlinePlayers().size() + "/" + BallsOfSteel.gameMaxplayers + " joueurs";
			}else if(BallsOfSteel.gameState.equals(GameState.GAME)){
			    Team playerTeam = Team.getPlayerTeam(pls);

				if(playerTeam != null){
					if(playerTeam.getDiamonds() <= 1){
						state = "In game - " + Bukkit.getOnlinePlayers().size() + "/" + BallsOfSteel.gameMaxplayers + " joueurs - " + playerTeam.getColor() + playerTeam.getDiamonds() + " diamond";
					}else{
						state = "In game - " + Bukkit.getOnlinePlayers().size() + "/" + BallsOfSteel.gameMaxplayers + " joueurs - " + playerTeam.getColor() + playerTeam.getDiamonds() + " diamonds";
					}
				}else{
					state = "In game";
				}
				
				//Classement en fonction du nombre de diamants
				for(Team t : BallsOfSteel.teams){
                    ranking.append(t.getColor()).append("§l█ §b").append(t.getDiamonds()).append(" §l♦ §r ");
				}
				
				
			}
			
			pls.setPlayerListHeader("§c§lRubyzGames §7- §6Balls Of Steel\n §7" + state + " \n");
			pls.setPlayerListFooter(ranking +" \n§6 play.rubyzgames.fr  -  www.rubyzgames.fr  -  ts.rubyzgames.fr ");
		
		}
		
	}

    /**
     *  Display an important message to a player
     * @param title     Title of the message
     * @param message   Array of strings, each string beeing a line
     * @param dest      CommandSender who need to receive the message
     */
    public static void sendImportantMessage(String title, ArrayList<String> message, CommandSender dest){

        dest.sendMessage("");
        dest.sendMessage("§7----------- [§6" + title + "§7] -----------");

        for(String s : message)
            dest.sendMessage(s);

        dest.sendMessage("§7------------------------" + "-".repeat(title.length()));
        dest.sendMessage("");

        if(dest instanceof Player) {

            Player p = (Player) dest;
            p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.4F, 1F);

        }

    }

    /**
     * Spawn a firework on the given location
     * @param loc Location of the firework
     */
    public static void spawnFirework(Location loc){

        Random r = new Random();

        Firework firework1 = (Firework) Objects.requireNonNull(loc.getWorld()).spawn(loc, Firework.class);
        FireworkEffect effect1 = FireworkEffect.builder()
                .withColor(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)))
                .withColor(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)))
                .withColor(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)))
                .flicker(true)
                .withFade(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)))
                .withFade(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)))
                .withFade(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)))
                .with(FireworkEffect.Type.BURST)
                .build();
        FireworkMeta meta1 = firework1.getFireworkMeta();
        meta1.addEffect(effect1);
        meta1.setPower(1);
        firework1.setFireworkMeta(meta1);

    }
	
}
