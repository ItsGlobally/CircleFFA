package me.itsglobally.circleffa;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.UUID;

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
                    Bukkit.getLogger().info("a");
                    // String player = json.has("player") ? json.get("player").getAsString() : "";
                    String player;
                    if (json.has("player")) {
                        player = json.get("player").getAsString();
                    } else {
                        JsonObject obj = basic();
                        obj.addProperty("error", "noplayer");
                        return;
                    }
                    Bukkit.getLogger().info("a");
                    UUID puid;
                    String pname;
                    Player onlinePlayer = Bukkit.getPlayer(player);

                    if (onlinePlayer != null) {
                        puid = onlinePlayer.getUniqueId();
                        pname = onlinePlayer.getName();
                    } else {
                        JsonObject obj = basic();
                        obj.addProperty("error", "playernotfound");
                        send(gson.toJson(obj));
                        return;
                    }

                    Long kills = MongoStatUtil.getKills(puid);
                    Bukkit.getLogger().info("a");
                    Long stars = MongoStatUtil.getStars(puid);
                    Bukkit.getLogger().info("a");
                    Long deaths = MongoStatUtil.getDies(puid);
                    Bukkit.getLogger().info("a");
                    Long ks = data.getks(puid);
                    Bukkit.getLogger().info("a");
                    Long xp = MongoStatUtil.getXp(puid);
                    Bukkit.getLogger().info("a");
                    JsonObject obj = basic();
                    obj.addProperty("name", pname);
                    obj.addProperty("xp", xp);
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
            e.printStackTrace();
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
