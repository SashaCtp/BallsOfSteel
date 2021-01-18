package fr.rubyz.bos;

public enum GameState {

	LOBBY(true), GAME(false), FINISH(false);

	private boolean canJoin;
	
	private static GameState currentState;
	
	GameState(boolean canJoin){
		this.canJoin = canJoin;
	}
	
	public static void setState(GameState state){
		GameState.currentState = state;
	}
	
	public static boolean isState(GameState state){
		return GameState.currentState == state;
	}
	
	public static GameState getState() {
		return currentState;
	}
	
	public boolean canJoin(){
		return canJoin;
	}
}
