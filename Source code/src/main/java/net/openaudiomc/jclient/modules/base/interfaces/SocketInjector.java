package net.openaudiomc.jclient.modules.base.interfaces;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import net.openaudiomc.jclient.OpenAudioMc;
import net.openaudiomc.jclient.modules.socket.objects.KeyHolder;

public class SocketInjector {
	
	private boolean connected;
	private Socket socket;
	private HashMap<String, SocketEvent> events;
	private String host;
	private int port;
	private boolean ssl;
	
	public SocketInjector(OpenAudioMc base, String host, int port, boolean ssl) {
		this.ssl = ssl;
		this.events = new HashMap<String,SocketEvent>();
		this.host = host;
		this.port = port;
	}

	public void connect() {
		if (!connected) {
            try {
                System.out.println("[OpenAudioMc] Starting SocketIO");
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new SecureRandom());
                IO.setDefaultSSLContext(sc);

                HttpsURLConnection.setDefaultHostnameVerifier((s, sslSession) -> true);

                IO.Options options = new IO.Options();
                options.sslContext = sc;
                options.secure = ssl;
                options.port = port;

                socket = IO.socket(host, options);

                listenForEvents();

                socket.connect();
                System.out.println("[OpenAudioMc] SocketIO started!");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
	}
	
	public void listenForEvents() {
		if(socket.connected()) {
			for (Entry<String, SocketEvent> event : events.entrySet()) {
				socket.on(event.getKey(), new Emitter.Listener() {
					@Override
					public void call(Object... args) {
						event.getValue().call(args);
					}
				});
			}
		}
	}
	
	private TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }
            }
    };

	public void listen(String string, SocketEvent socketEvent) {
		this.events.put(string, socketEvent);
	}

	public void disconnect() {
		socket.disconnect();
		socket.close();
	}

}
