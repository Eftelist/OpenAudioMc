package net.openaudiomc.jclient.modules;

import java.util.HashMap;
import java.util.Map.Entry;

public class ModuleManager {
	
	private HashMap<String, AModule> modules = new HashMap<>();
	
	public void injectModule(String id, AModule module) {
		modules.put(id, module);
	}
	
	public void unloadModules() {
		for (Entry<String, AModule> a : modules.entrySet()) {
			a.getValue().onDestroy();
		}
	}

}
