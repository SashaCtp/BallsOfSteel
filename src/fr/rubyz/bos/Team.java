package fr.rubyz.bos;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import fr.rubyz.bos.utils.Cuboid;

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
		return spawn;
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
			Bukkit.getServer().getPlayer(uuid).teleport(this.spawn);
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
