package me.itsglobally.circleffa;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
            Bukkit.getLogger().info(json.toString());
            String server = json.has("server") ? json.get("server").getAsString() : "";
            Bukkit.getLogger().info(server);
            String cmd = json.has("cmd") ? json.get("cmd").getAsString() : "";
            Bukkit.getLogger().info(cmd);
            if (cmd.isEmpty()) return;
            switch (cmd) {
                case "playerstats":
                    // String player = json.has("player") ? json.get("player").getAsString() : "";
                    String player;
                    if (json.has("player")) {
                        player = json.get("player").getAsString();
                    } else {
                        JsonObject obj = basic();
                        obj.addProperty("error", "noplayer");
                        return;
                    }
                    OfflinePlayer ofp = Bukkit.getOfflinePlayer(player);
                    if (ofp == null) {
                        JsonObject obj = basic();
                        obj.addProperty("error", "playernotfound");
                        return;
                    }
                    Long kills = MongoStatUtil.getKills(ofp.getUniqueId());
                    Long stars = MongoStatUtil.getKills(ofp.getUniqueId());
                    Long deaths = MongoStatUtil.getKills(ofp.getUniqueId());
                    Long ks = data.getks(ofp.getUniqueId());
                    JsonObject obj = basic();
                    obj.addProperty("name", ofp.getName());
                    obj.addProperty("xp", MongoStatUtil.getXp(ofp.getUniqueId()));
                    obj.addProperty("kills", kills);
                    obj.addProperty("stars", stars);
                    obj.addProperty("deaths", deaths);
                    obj.addProperty("ks", ks);
                    send(gson.toJson(obj));
                    break;
                default:
                    Bukkit.getLogger().info("Unknown cmd: " + cmd);
            }
        } catch (Exception e) {
            Bukkit.getLogger().info("Invalid JSON: " + message);
            Bukkit.getLogger().info(e.getMessage());
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
