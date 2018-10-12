package net.openaudiomc.jclient.modules;

import net.openaudiomc.jclient.OpenAudioMc;

public abstract class AModule {
	
	private OpenAudioMc instance = null;
	
	public AModule(OpenAudioMc base) {
		this.instance = base;
		onLoaded();
	}
	
	public abstract void onLoaded();
	
	public abstract void onDestroy();
	
	public OpenAudioMc getOAInstance() {
		return instance;
	}

}
