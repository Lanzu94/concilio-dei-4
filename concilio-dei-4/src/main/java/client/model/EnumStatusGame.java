package client.model;

/**
 * save in this class the game status that are shown during the game
 */
public enum EnumStatusGame { 
	gameStart,
	map,//print the map
	permitCards,//print the permit card visible and the number of remain permit card
	kingBoard,//print status of kingboard with the councillor balcony
	nobilityRoad,//print the bonus of nobility road
	opponentStatus,//print the opponent status (number of resource)
	playerStatus,//print the status of the currently player
	opponentTurn,
	
	legend,//print the legend of letter
	
	//action 1 Change Councillor
	listCouncillorOutOfBalconies,//print the list of Councillor out of balconies that player can chose
	balconyChose,//print the balcony that player can chose
	
	//action 2 Buy Permit Card
	balconyChoseForCorrupt,//player must chose which balcony wants corrupt
	politicalCardChosen,//player must chose which politic cards he want to use to corrupt a balcony
	whichPermitCardGet,//the player chose wich permit card want
	
	//action 3 Build an Emporium By Permit
	indexPermitCard,//player chose which permit card he wants to use to build an emporium
	townNameChosed,//player chose the town where he want build
	
	//action 4 Build an Emporium By King
	politicCardsUseToKing,//player chose the politic cards he wants to use to corrupt king balcony
	townChosenToKing,//player chose the town where he want build
	routeToFollow,//player chose the road that want to go with king
	
	//action 6 Change Permit Build Card
	regionChose,//player chose in which region he wants to change the visible permit cards
	
	//action 7 Use an Helper To Change Councillor
	listCouncillorFreeByHelper,//print the list of Councillor out of balconies
	balconyChoseByHelper,//print the balcony that player can chose
	
	//action for chose the bonus in nobility road
	select1BonusToken,
	select2BonusToken,
	select1BonusPermitCard,
	select1PermitCard,
	
	timerTurn,//print the time left
	errorInsertedDate,//print the page error if the player insert invalid date	
	resourceNotEnought,//print the page error if the player try to do something but don't have enought resources	
	
	marketStart,//notify the market starts
	turnDoneOffert,//player can insert his offert
	waitTurnDoneOffertFinish,
	resourcesNotEnoughtToOffert,
	timerMarket,//print time left for buy the things
	yourTurnToBuy,//player can buy opponents offert
	opponentTurnToBuy,//player must wait opponent finish buy offert
	resourcesNotEnoughtToBuy,
	
	sleep;
}
