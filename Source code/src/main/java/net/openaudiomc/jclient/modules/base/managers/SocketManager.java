package net.openaudiomc.jclient.modules.base.managers;

import net.openaudiomc.jclient.modules.base.BaseModule;
import net.openaudiomc.jclient.modules.base.interfaces.SocketEvent;
import net.openaudiomc.jclient.modules.base.interfaces.SocketInjector;

public abstract class SocketManager {
	
	private SocketInjector inJ;

	public SocketManager() {
		this.inJ = BaseModule.getSocket();
	}
	
	public abstract void registerEvents();
	
	public void addEvent(String eventID, SocketEvent event) {
		this.inJ.listen(eventID, event);
	}
	
	public void done() {
		this.inJ.connect();
	}

}
