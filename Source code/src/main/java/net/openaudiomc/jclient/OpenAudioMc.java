package net.openaudiomc.jclient;

import lombok.Getter;
import net.openaudiomc.jclient.modules.ModuleManager;
import net.openaudiomc.jclient.modules.base.BaseModule;
import net.openaudiomc.jclient.modules.base.interfaces.SocketEvent;
import net.openaudiomc.jclient.modules.commands.CommandsModule;
import net.openaudiomc.jclient.modules.media.MediaModule;
import net.openaudiomc.jclient.modules.player.PlayerModule;
import net.openaudiomc.jclient.modules.socket.SocketModule;
import net.openaudiomc.jclient.modules.socket.objects.ApiEndpoints;
import net.openaudiomc.jclient.utils.config.Config;
import net.openaudiomc.jclient.utils.ESMap;
import net.openaudiomc.jclient.utils.MapBuilder;
import net.openaudiomc.jclient.utils.Reflection;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Predicate;

public final class OpenAudioMc extends JavaPlugin {

    @Getter private static OpenAudioMc instance;

    @Getter private Config conf;

    @Getter private PlayerModule playerModule;
    @Getter private SocketModule socketModule;
    @Getter private CommandsModule commandsModule;
    @Getter private ApiEndpoints apiEndpoints;
    @Getter private Reflection reflection;
    @Getter private MediaModule mediaModule;

	@Getter private ModuleManager moduleManager;

    @Override
    public void onEnable() {
        instance = this;

        conf = new Config();
        conf.load();

        moduleManager = new ModuleManager();
        Map<String, SocketEvent> ev = MapBuilder.<String,SocketEvent>ordered()
        		.put("test_event",(o) -> {
        			System.out.println("Recieved test_event");
        		}
        ).build();
        
        moduleManager.injectModule("base_with_socket", new BaseModule(this,ev));
        
        apiEndpoints = new ApiEndpoints();

        playerModule = new PlayerModule(this);
        socketModule = new SocketModule(this);
        commandsModule = new CommandsModule(this);
        reflection = new Reflection(this);
        mediaModule = new MediaModule(this);
    }

    @Override
    public void onDisable() {
        socketModule.closeConnection();
    }
}
