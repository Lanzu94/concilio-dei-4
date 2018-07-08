package server.model;

/**
 * Save the different states the player can has in the server
 */
public enum EnumStatePlayer {

	online,
	disconnected,
	lobby,
	lobbyDisconnected,
	inGame,
	inGameDisconnected,
}
