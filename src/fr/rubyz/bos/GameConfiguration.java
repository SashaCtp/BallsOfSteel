package fr.rubyz.bos;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffect;


public class GameConfiguration {
	
	/*
	 * Default variables :
	 * 
	 * Team size : 3 players
	 * Match lenght : 30 minutes
	 *  
	 */
	
	private static FileConfiguration config = BallsOfSteel.getInstance().getConfig();
	
	private boolean midProtection = false;
	private boolean host = config.getBoolean("host");
	private int teamsize = config.getInt("teamsize");
	private boolean infiniteBuildBlock = config.getBoolean("infiniteBuildBlock");
	private int time = config.getInt("gametime");
	private Material buildBlockMaterial = Material.DIRT;
	
	/*
	 * Scenarios :
	 * 
	 * 	SilkTouch
	 *  NoKnockBack
	 *  NoBow
	 *  EnderDragon
	 *  Guardian
	 *  TnTBoost
	 *  
	 * Effects :
	 *  
	 *  Strength
	 *  Regeneration
	 *  JumpBoost
	 *  Speed
	 *  HealthBoost
	 *  Saturation
	 * 
	 */
	
	private boolean silkTouchMod = false;
	private boolean antiKbMod = false;
	private boolean noBowMod = false;
	private boolean enderDragonMod = false;
	private boolean guardianMod = false;
	private boolean tntBoostMod = false;
	
	private ArrayList<PotionEffect> effects = null;

	public GameConfiguration(int teamsize, boolean infiniteBuildBlock, int time, Material mat){
		if(teamsize != 0)
			this.teamsize = teamsize;
		
		this.infiniteBuildBlock = infiniteBuildBlock;
		
		if(time != 0)
			this.time = time;
		
		if(buildBlockMaterial == null)
			this.buildBlockMaterial = mat;
		
	}

	//Getters et setters � ne pas toucher
	
	public boolean isMidProtected(){
		return midProtection;
	}
	
	public void setMidProtected(boolean midProtection){
		this.midProtection = midProtection;
	}
	
	public boolean isHost(){
		return host;
	}
	
	public int getTeamsize() {
		return teamsize;
	}

	public void setTeamsize(int teamsize) {
		this.teamsize = teamsize;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	public Material getBuildBlockMaterial() {
		return buildBlockMaterial;
	}

	public void setBuildBlockMaterial(Material buildBlockMaterial) {
		this.buildBlockMaterial = buildBlockMaterial;
	}
	
	
	public boolean isSilkTouchMod() {
		return silkTouchMod;
	}

	public void setSilkTouchMod(boolean silkTouchMod) {
		this.silkTouchMod = silkTouchMod;
	}

	public boolean isAntiKbMod() {
		return antiKbMod;
	}

	public void setAntiKbMod(boolean antiKbMod) {
		this.antiKbMod = antiKbMod;
	}

	public boolean isNoBowMod() {
		return noBowMod;
	}

	public void setNoBowMod(boolean noBowMod) {
		this.noBowMod = noBowMod;
	}

	public boolean isEnderDragonMod() {
		return enderDragonMod;
	}

	public void setEnderDragonMod(boolean enderDragonMod) {
		this.enderDragonMod = enderDragonMod;
	}

	public boolean isGuardianMod() {
		return guardianMod;
	}

	public void setGuardianMod(boolean guardianMod) {
		this.guardianMod = guardianMod;
	}

	public boolean isTntBoostMod() {
		return tntBoostMod;
	}

	public void setTntBoostMod(boolean tntBoostMod) {
		this.tntBoostMod = tntBoostMod;
	}

	public ArrayList<PotionEffect> getEffects() {
		return effects;
	}

	public void setEffects(ArrayList<PotionEffect> effects) {
		this.effects = effects;
	}

	public boolean isInfiniteBuildBlock() {
		return infiniteBuildBlock;
	}

	public void setInfiniteBuildBlock(boolean infiniteBuildBlock) {
		this.infiniteBuildBlock = infiniteBuildBlock;
	}

	
}
