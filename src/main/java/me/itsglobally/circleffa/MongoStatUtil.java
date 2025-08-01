package me.itsglobally.circleffa;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.util.UUID;

public class MongoStatUtil {
    private static final String URI = "mongodb://localhost:27017"; // Change if needed
    private static final String DB_NAME = "circleffa";
    private static final String COLLECTION_NAME = "playerStats";

    private static final MongoClient client;
    private static final MongoCollection<Document> collection;

    static {
        client = MongoClients.create(URI);
        MongoDatabase db = client.getDatabase(DB_NAME);
        collection = db.getCollection(COLLECTION_NAME);
    }

    private static Document getPlayerDoc(UUID uuid) {
        Document doc = collection.find(Filters.eq("uuid", uuid.toString())).first();
        return doc == null ? new Document("uuid", uuid.toString()) : doc;
    }

    private static void updateField(UUID uuid, String field, long value) {
        collection.updateOne(
                Filters.eq("uuid", uuid.toString()),
                new Document("$set", new Document(field, value)),
                new UpdateOptions().upsert(true)
        );
    }

    private static void incrementField(UUID uuid, String field, long value) {
        collection.updateOne(
                Filters.eq("uuid", uuid.toString()),
                new Document("$inc", new Document(field, value)),
                new UpdateOptions().upsert(true)
        );
    }

    // -------- STARS --------
    public static void setStars(UUID uuid, long stars) {
        updateField(uuid, "stars", stars);
    }

    public static void addStars(UUID uuid, long amount) {
        incrementField(uuid, "stars", amount);
    }

    public static long getStars(UUID uuid) {
        return getPlayerDoc(uuid).getOrDefault("stars", 0L) instanceof Number ?
                ((Number) getPlayerDoc(uuid).get("stars")).longValue() : 0L;
    }

    // -------- XP --------
    public static void setXp(UUID uuid, long xp) {
        updateField(uuid, "xp", xp);
        handleXpOverflow(uuid);
    }

    public static void addXp(UUID uuid, long xp) {
        incrementField(uuid, "xp", xp);
        handleXpOverflow(uuid);
    }

    public static long getXp(UUID uuid) {
        return getPlayerDoc(uuid).getOrDefault("xp", 0L) instanceof Number ?
                ((Number) getPlayerDoc(uuid).get("xp")).longValue() : 0L;
    }

    private static void handleXpOverflow(UUID uuid) {
        long xp = getXp(uuid);
        if (xp >= 100) {
            long starsToAdd = xp / 100;
            long remainingXp = xp % 100;
            addStars(uuid, starsToAdd);
            setXp(uuid, remainingXp);
        }
    }

    // -------- KILLS --------
    public static void addKill(UUID uuid) {
        incrementField(uuid, "kills", 1);
    }

    public static long getKills(UUID uuid) {
        return getPlayerDoc(uuid).getOrDefault("kills", 0L) instanceof Number ?
                ((Number) getPlayerDoc(uuid).get("kills")).longValue() : 0L;
    }

    public static void setKills(UUID uuid, long kills) {
        updateField(uuid, "kills", kills);
    }

    // -------- DIES --------
    public static void addDies(UUID uuid) {
        incrementField(uuid, "dies", 1);
    }

    public static long getDies(UUID uuid) {
        return getPlayerDoc(uuid).getOrDefault("dies", 0L) instanceof Number ?
                ((Number) getPlayerDoc(uuid).get("dies")).longValue() : 0L;
    }

    public static void setDies(UUID uuid, long dies) {
        updateField(uuid, "dies", dies);
    }
}
