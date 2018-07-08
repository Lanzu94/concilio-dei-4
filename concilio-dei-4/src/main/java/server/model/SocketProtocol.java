package server.model;
//each initial in the socket protocol corresponds to an action
/**
 * This class has the communication protocol for the socket connection mode
 */
public enum  SocketProtocol {
PCG, //player connection game    							C/S
UNP, //update new player									S
CNG, //create new game (return long= code of game match)	C/S
LOL, //list of lobbies										C/S
JAL, //join a lobby											C
UPL, //update player in lobby								S
LOM,// list of maps											C/S
PMG,  //print map of the game								S
GIS, //game is started-> start a game						S
PPC,//print permit card										S
SKC,  //status kingboard 									S
SNR,  //status nobility road								S
PSO, //print status opponents								S
PSP, //print status player									S
TUR,  //print Player turn									S
EAH,  //engage an helper									C
OMA, //other main action									C
CHC, //change councillor									C
BPC, //buy permit card										C
BEP, //build emporium by permit								C
BEK, //build emporium by king								C
CPC, //change permit card									C
CCH, //change councillor by helper							C
SBT, //select 1 bonus token									C/S
STT, //select 2 bonus token									C/S
SBP, //select bonus permit card								C/S
SPC, //Select permit card									C/S
PTT, //pass turn											-
STA, //start turn again										-
SMK, //start market											-
ADO, //add offer											C/S
FOF, //finish offer											C
SBM, //start buy market										-
BOF, //buy offer											C
PMT, //pass market turn										C
EAG, //enter again in game									-
RGM,  //remove game											-
UAP, //update action player									S
EID, //error insert data									S
RNE, //resource not enough									S
EIO, //error insert offer									S
RNO, //resource not enough for offer						S
ROO, //receive other offer									S
OTB, //opponent turn to buy									S
RNB, //resource not enough to buy offer						S
EIB, //error insert bought									S
FGM, //end game											S
GCG, //give code game										S
HRT, //check if the player is connected        				-
}
