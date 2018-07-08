package client.controller;

import java.io.Serializable;
import server.controller.RMIServerInterface;

/**
 * This interface extends the RMI_ServerInterface. It will be implemented in 2 different way: ClientConnectionRMI and ClientConnectionSocket
 */
public interface ClientConnection extends RMIServerInterface, Serializable{
}
