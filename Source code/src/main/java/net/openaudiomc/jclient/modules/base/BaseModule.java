package net.openaudiomc.jclient.modules.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import net.openaudiomc.jclient.OpenAudioMc;
import net.openaudiomc.jclient.modules.AModule;
import net.openaudiomc.jclient.modules.base.interfaces.SocketEvent;
import net.openaudiomc.jclient.modules.base.interfaces.SocketInjector;
import net.openaudiomc.jclient.modules.base.managers.SocketManager;

public class BaseModule extends AModule {

    private static @Getter SocketInjector socket;
	private Map<String, SocketEvent> eventsTo;

	public BaseModule(OpenAudioMc base, Map<String, SocketEvent> ev) {
		super(base);
		this.eventsTo = ev;
	}
	
	@Override
	public void onLoaded() {
		socket = new SocketInjector(getOAInstance(), "127.0.0.1", 18901, false);
		new SocketManager() {
			@Override
			public void registerEvents() {
				for (Entry<String, SocketEvent> a : eventsTo.entrySet()) {
					addEvent(a.getKey(), a.getValue());
				}
				this.done();
			}
		};
	}

	@Override
	public void onDestroy() {
		socket.disconnect();
	}

}
