package me.itsglobally.circleffa;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class daemon extends WebSocketClient {

    private final Gson gson = new Gson();

    public daemon(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Bukkit.getLogger().info("Connected to server");
        JsonObject obj = new JsonObject();
        obj.addProperty("server", "ffa");
        obj.addProperty("message", "connected");
        send(gson.toJson(obj));
    }

    @Override
    public void onMessage(String message) {
        try {
            JsonObject json = gson.fromJson(message, JsonObject.class);

            String server = json.has("server") ? json.get("server").getAsString() : "";
            String msg = json.has("cmd") ? json.get("cmd").getAsString() : "";
            if (!server.equals("ffa")) return;
            switch (msg) {
                case "playerstats":
                    String player = json.has("player") ? json.get("player").getAsString() : "";
                    Long kills = MongoStatUtil.getKills(Bukkit.getOfflinePlayer(player).getUniqueId());
                    Long stars = MongoStatUtil.getKills(Bukkit.getOfflinePlayer(player).getUniqueId());
                    Long deaths = MongoStatUtil.getKills(Bukkit.getOfflinePlayer(player).getUniqueId());
                    Long ks = MongoStatUtil.getKills(Bukkit.getOfflinePlayer(player).getUniqueId());
                    JsonObject obj = basic();
                    obj.addProperty("name", Bukkit.getPlayer(player).getName());
                    obj.addProperty("xp", MongoStatUtil.getXp(Bukkit.getOfflinePlayer(player).getUniqueId()));
                    obj.addProperty("kills", kills);
                    obj.addProperty("stars", stars);
                    obj.addProperty("deaths", deaths);
                    obj.addProperty("ks", ks);
                    send(gson.toJson(obj));

                    break;
                default:
                    Bukkit.getLogger().info("Unknown server: " + server + " | " + msg);
            }
        } catch (Exception e) {
            Bukkit.getLogger().info("Invalid JSON: " + message);
            JsonObject obj = basic();
            obj.addProperty("message", "fuck u not json r u retarded");
            send(gson.toJson(obj));
        }
    }
    private static JsonObject basic() {
        JsonObject obj = new JsonObject();
        obj.addProperty("server", "ffa");
        return obj;
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Bukkit.getLogger().warning("Connection closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public static void main(String[] args) throws Exception {
        daemon client = new daemon(new URI("ws://localhost:8080"));
        client.connectBlocking();
    }
}
