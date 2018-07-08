package server.controller;

import java.awt.Color;
import java.io.IOException;
import java.rmi.RemoteException;

import client.controller.RMIClientInterface;

/**
 *	This interface implements RMIClientInterface and it will be implemented by the different connection modes
 */
public interface Connection extends RMIClientInterface{
}