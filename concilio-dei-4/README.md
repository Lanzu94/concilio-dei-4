For the project "The Council of 4" we have developed these things:

* RMI 
* Socket
* CLI
* GUI
* 2 advanced features: lobby and persistent game

To play to The Council you have to follow these steps:

* Run the "StartServer.java" class in the server package (the server will start and it will work independently).
* Run the "StartClient.java" class in the client package.
* Follow the instructions that are shown in the client. An example:
* The first step is to choose both the connection type (RMI or Socket) and the graphics type (CLI or GUI) and then press the play button.
* Then type the name and the password of the player (in this way in the CLI: "name password", while in the GUI there will be specific fields).
* The player can choose to play online or to change his connection mode pressing the button in the GUI or typing the number of the option in the CLI.
* If the player chooses to play online, then he will choose if starting a new game, or joining to a match already existent, or visualizing the list of the already made maps.
* NEW GAME- if the player chooses to start a new game, he will have to enter the maximum and the minimum number of players he wants in the game and the map he wants to adopt (in the CLI "minimum-maximum-numberMap"). Then, the player will wait for other players to connect to his game (after the achievement of the minimum number, a  timer is set up).
* JOIN A LOBBY- in this option, the player will visualize the list of the already created games and waiting for more players (with their code, and the number of players already in), and will choose to join one, selecting the number of the game he wants.
* When the game starts (by reaching the max number of players allowed in the specific game or after e certain amount of time after having reached the minimum number of players allowed), the
* client will guide the player to choose what actions to do.

Something about the player:

1. When the player chooses "play online" he must insert name and password.
	The purpose of the password is to prevent the access to unauthorized people to join with your identity and play with your resources.
2. If the player disconnects he can join again with his user name and the password he used and he will find himself in the position where he was before.
3. In the online menu the player can create a new game or see the list of the already created games that are waiting for more players.

Something about the game:

1. Each game turn is divided in 3 different parts:
	-"normal game turn" where each player, following an order, can do the main actions;
	-"market:offer" where the players, at the same time, can do any offer about what they want to sell to the other players;
	-"market:buy" where each player, following an order that changes at every turn, can buy the things that other players are offering.
2. In every turn the player has a limited time to do the actions.
3. If the time ends, a timer in the client passes the player's turn automatically.
4. If the client disconnects during his turn, a new timer on the server will pass his turn after a short time and puts the player in "inGameDisconnected" mode, that will skip instantly the 	player's turn if he doesn't connect in time.
5. When a player completes his action, the opponents can see who is playing and the consequences of his actions.
6. When the game ends, the players can see the final ranking and, if they press enter, they will leave the game and go back to the online menu.
7. In order to facilitate the visualization of the game, in the CLI there are special options that show the legend of the acronyms used for maps and game's components and options that allow 	to print the player's status and the opponents' one.

Something about the connection:

1. The player can close the game to disconnect himself.
2. The name and the password of the players remain saved on the server, so only the same player can connect with his name (preventing other players to access someone else's game).
3. When someone tries to connect with an already existing name and the right password, the server kicks out the previously playing player with that name. In this way there can't be 2 players with the same name online or more people using the same player together.