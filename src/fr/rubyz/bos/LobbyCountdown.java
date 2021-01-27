package fr.rubyz.bos;

import fr.rubyz.bos.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class LobbyCountdown {

    private int initialValue;
    public int countdown;
    private boolean started = false;
    private boolean forcedStart = false;

    public LobbyCountdown(int initialValue){
        this.initialValue = initialValue;
    }

    /**
     * Starts the lobby countdown
     */
    public void start(boolean forcedStart){

        if(!this.started() && BallsOfSteel.gameState.equals(GameState.LOBBY)){

            this.started = true;
            this.forcedStart = forcedStart;

            if(this.forcedStart)
                this.countdown = 10;
            else
                this.countdown = this.initialValue;

        }

    }

    /**
     * Pause the countdown
     */
    public void pause(){

        if(this.started() && BallsOfSteel.gameState.equals(GameState.LOBBY))
            this.started = false;

    }

    /**
     * Stop and reset the countdown
     * @param message Message to display to players (null = nothing displayed)
     */
    public void stop(String message){

        if(this.started()) {
            this.started = false;
            this.countdown = this.initialValue;
        }

    }


    /**
     * Decrement the countdown
     */
    public void decrement(){

        if(this.started() && this.countdown > 0)
            this.countdown--;

    }

    /**
     * Display the countdown to other people
     */
    public void displayCountdown(){

        if(this.started() && this.getCountdown() >= 0){

            for(Player pls : Bukkit.getOnlinePlayers())
                pls.setLevel(this.getCountdown());

            String message = null;
            String title = null;
            String subtitle = null;

            if(this.getCountdown() == 60) {
                message = " Game starts in §c1 minute§f !";
                title = "§l1";
                subtitle = "minute";
            }

            if(this.getCountdown() == 10 || (this.getCountdown() <= 5 && this.getCountdown() > 1)) {
                message = "§fGame starts in §c" + this.getCountdown() + " seconds§f !";
                title = "§a" + this.getCountdown();
                subtitle = "§7§fseconds";
            }

            if(this.getCountdown() == 1) {
                message = "§fGame starts in §c" + this.getCountdown() + " second§f !";
                title = "§6" + this.getCountdown();
                subtitle = "§7§fsecond";
            }

            if(message != null){

                Bukkit.broadcastMessage(Util.getGamePrefix() + message);

                for(Player pls : Bukkit.getOnlinePlayers()) {
                    pls.playSound(pls.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.3F, 1F);
                    pls.sendTitle(title, subtitle, 20, 2,2);
                }

            }

        }

    }

    /**
     * Decrement the countdown
     */
    public boolean finished(){
        return (this.started() && this.getCountdown() == 0);
    }

    /**
     * Checks if the countdown started
     * @return True if it started, else False
     */
    public boolean started(){
        return this.started;
    }

    /**
     * Get the countdown value
     */
    public int getCountdown(){
        return this.countdown;
    }

    /**
     * Checks if the countdown was force started or not
     * @return boolean
     */
    public boolean forceStarted(){
        return this.forcedStart;
    }

}
