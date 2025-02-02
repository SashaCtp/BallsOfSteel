package fr.rubyz.bos;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import fr.rubyz.bos.utils.Cuboid;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class Team {

	private String name;
	private ArrayList<UUID> players;
	private int diamonds;
	private int maxplayers;
	private Location spawn;
	private Block chest;
	private String color;
	private Material colorBlock;
	private DiamondIndicator diamondIndicator;
	private Cuboid base;
	
	public Team(String name, ArrayList<UUID> players, int maxplayers, Location spawn, Block chest, Location armorStandLoc, String color, Material colorBlock, Location loc1, Location loc2){
		
		this.name = name;
		this.players = players;
		this.diamonds = 0;
		this.maxplayers = maxplayers;
		this.spawn = spawn;
		this.chest = chest;
		this.color = color;
		this.colorBlock = colorBlock;
		this.diamondIndicator = new DiamondIndicator(this , armorStandLoc);
		
		this.base = new Cuboid(loc1, loc2);

	}

	/**
	 * Return the list the team's players
	 *
	 * @return Array<Player> List of the team's player
	 */
	public ArrayList<Player> getPlayers() {
		
		ArrayList<Player> toReturn = new ArrayList<>();
		
		for(UUID uuid : this.players){
			toReturn.add(Bukkit.getServer().getPlayer(uuid));
		}
		
		return toReturn;
	}

	/**
	 * Return the the owner of a given chest
	 *
	 * @param block Block of which we want to get the owning team
	 * @return Owning team
	 */
	public static Team getTeamChest(Block block){
		
		Team team = null;
		
		for(int i = 0; i < BallsOfSteel.teams.size(); i++){
			
			if(block.equals(BallsOfSteel.teams.get(i).getChest())){
				team = BallsOfSteel.teams.get(i);
			}
			
		}
		
		return team;
	}

	/**
	 * Count and refresh the number of diamonds in the team's chest
	 */
	public void refreshDiamondCount(){

		Block chestBlock = this.getChest();

		if(chestBlock.getType().equals(Material.CHEST)){

			Chest chest = (Chest) chestBlock.getState();

			int diamondsAmout = 0;
			ItemStack[] items = chest.getInventory().getContents();

			for(ItemStack item : items){
				if(item != null){
					if(item.getType().equals(Material.DIAMOND)){
						diamondsAmout += item.getAmount();
					}
				}
			}

			this.setDiamonds(diamondsAmout);

		}else{
			System.out.println("§cError : The " + this.getName() + " team's chest is not correctly defined.");
		}

	}

	// TODO : Check if the function needs a return statement
	/**
	 * Sort the list (ASC)
	 * @param list List to sort
	 */
	public static ArrayList<Team> sortTeamList(ArrayList<Team> list){

		for(int k = 1; k < list.size()-1; k++) {

			for (int i = 0; i < list.size() - k; i++) {

				if(list.get(i).getDiamonds() > list.get(i+1).getDiamonds()){
					Team tmp = list.get(i);
					list.set(i, list.get(i+1));
					list.set(i+1, tmp);
				}

			}

		}

		return list;

	}

	/**
	 * Checks if a team is at maximum capacity
	 * @return True if the team is full, else False
	 */
	public boolean isFull(){

		return (this.players.size() == this.maxplayers);

	}

	/**
	 * Checks if all the teams are full
	 *
	 * @return True if all the teams are full, else False
	 */
	public static boolean allTeamFull(){
		
		for(Team t : BallsOfSteel.getTeams()){
			if(!t.isFull()){
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Get the team of player (null if in no team)
	 * @param p Player
	 */
	public static Team getPlayerTeam(Player p){

		for(Team t : BallsOfSteel.teams) {
			if (t.getPlayers().contains(p))
				return t;
		}

		return null;

	}

	/**
	 * 	Give the correct team colored armor
	 */
	public static void giveTeamArmor(Player p){
		Color color = null;
		Team playerTeam = getPlayerTeam(p);

		if(playerTeam == null)
			return;

		String playerTeamColor = playerTeam.getColor();

		switch (playerTeamColor) {
			case "§1" -> color = Color.fromRGB(0, 0, 170);
			case "§9" -> color = Color.fromRGB(83, 85, 255);
			case "§3" -> color = Color.fromRGB(1, 169, 170);
			case "§b" -> color = Color.fromRGB(84, 255, 255);
			case "§c" -> color = Color.fromRGB(255, 80, 82);
			case "§2" -> color = Color.fromRGB(0, 170, 3);
			case "§e" -> color = Color.fromRGB(255, 254, 87);
			case "§a" -> color = Color.fromRGB(85, 255, 88);
		}

		ItemStack lhelmet = new ItemStack(Material.LEATHER_HELMET, 1);
		LeatherArmorMeta lhelmetmeta = (LeatherArmorMeta)lhelmet.getItemMeta();
		assert lhelmetmeta != null;
		lhelmetmeta.setDisplayName("Default armor");
		lhelmetmeta.setColor(color);
		lhelmet.setItemMeta(lhelmetmeta);

		ItemStack lchestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta lchestplatemeta = (LeatherArmorMeta)lhelmet.getItemMeta();
		lchestplatemeta.setDisplayName("Default armor");
		lchestplatemeta.setColor(color);
		lchestplate.setItemMeta(lchestplatemeta);

		ItemStack lboots = new ItemStack(Material.LEATHER_BOOTS, 1);
		LeatherArmorMeta lbootsmeta = (LeatherArmorMeta)lhelmet.getItemMeta();
		lbootsmeta.setDisplayName("Default armor");
		lbootsmeta.setColor(color);
		lboots.setItemMeta(lbootsmeta);

		p.getInventory().setHelmet(lhelmet);
		p.getInventory().setChestplate(lchestplate);
		p.getInventory().setBoots(lboots);

	}

	// Getters and setters


	/**
	 * Add a player to the team
	 * @param p Player to add
	 */
	public void addPlayer(Player p) {
		this.players.add(p.getUniqueId());
	}

	/**
	 * Remove a player from the team
	 * @param p Player to remove
	 */
	public void removePlayer(Player p){
		this.players.remove(p.getUniqueId());
	}

	/**
	 * Get the number of diamonds the team owns
	 * @return Number of diamonds
	 */
	public int getDiamonds() {
		return diamonds;
	}

	/**
	 * Set the number of diamonds the team owns
	 * @param diamonds Number of diamonds
	 */
	public void setDiamonds(int diamonds) {
		this.diamonds = diamonds;
	}

	/**
	 * Get the maximum capacity of the team
	 * @return Maximum capacity
	 */
	public int getMaxplayers() {
		return maxplayers;
	}

	/**
	 * Set the maximum capacity of the team
	 * @param maxplayers Maximum capacity
	 */
	public void setMaxplayers(int maxplayers) {
		this.maxplayers = maxplayers;
	}

	/**
	 * Get the spawn location of the team
	 * @return Location of the spawn
	 */
	public Location getSpawn() {
		return this.spawn;
	}

	/**
	 * Set the spawn location of team
	 * @param spawn Location of the spawn
	 */
	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}

	/**
	 * Get the diamond chest of the team
	 * @return Block
	 */
	public Block getChest() {
		return chest;
	}

	/**
	 * Get the diamond indicator of the team
	 * @return DiamondIndicator of the team
	 */
	public DiamondIndicator getDiamondIndicator() {
		return diamondIndicator;
	}

	/**
	 * Get the name of the team
	 * @return Name of the team
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the color code of the team
	 * @return Color of the team
	 */
	public String getColor(){
		return this.color;
	}

	/**
	 * Get the Material color of the team
	 * @return Material of the team
	 */
	public Material getColorBlock(){
		return this.colorBlock;
	}

	/**
	 * Teleport the players to starting location
	 * Location must be different than NULL
	 */
	public void teleportPlayers(){

		assert(this.spawn != null);

		for(UUID uuid : this.players){
			Objects.requireNonNull(Bukkit.getPlayer(uuid)).teleport(this.spawn);
		}

	}

	/**
	 * Get the cuboid protecting the base of the team
	 * @return Base of the tram
	 */
	public Cuboid getBase(){
		return this.base;
	}
	
}
