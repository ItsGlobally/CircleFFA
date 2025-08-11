package me.itsglobally.circleffa;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.util.UUID;

public class MongoStatUtil {
    private static final String URI = "mongodb://172.20.0.1:27017";
    private static final String DB_NAME = "circleffa";
    private static final String COLLECTION_NAME = "playerStats";

    private static final MongoClient client;
    private static final MongoCollection<Document> collection;

    static {
        client = MongoClients.create(URI);
        MongoDatabase db = client.getDatabase(DB_NAME);
        collection = db.getCollection(COLLECTION_NAME);
    }

    // ===== Helper Methods =====

    private static Document getPlayerDoc(UUID uuid) {
        Document doc = collection.find(Filters.eq("uuid", uuid.toString())).first();
        if (doc == null) {
            doc = new Document("uuid", uuid.toString());
            collection.insertOne(doc);
        }
        return doc;
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

    private static long safeGetLong(Document doc, String key) {
        Object value = doc.get(key);
        return value instanceof Number ? ((Number) value).longValue() : 0L;
    }

    // ===== STARS =====

    public static void setStars(UUID uuid, long stars) {
        updateField(uuid, "stars", stars);
    }

    public static void addStars(UUID uuid, long amount) {
        incrementField(uuid, "stars", amount);
    }

    public static long getStars(UUID uuid) {
        return safeGetLong(getPlayerDoc(uuid), "stars");
    }

    // ===== XP =====

    public static void setXp(UUID uuid, long xp) {
        updateField(uuid, "xp", xp);
        handleXpOverflow(uuid);
    }

    public static void addXp(UUID uuid, long xp) {
        incrementField(uuid, "xp", xp);
        handleXpOverflow(uuid);
    }

    public static long getXp(UUID uuid) {
        return safeGetLong(getPlayerDoc(uuid), "xp");
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

    // ===== KILLS =====

    public static void setKills(UUID uuid, long kills) {
        updateField(uuid, "kills", kills);
    }

    public static void addKill(UUID uuid) {
        incrementField(uuid, "kills", 1);
    }

    public static long getKills(UUID uuid) {
        return safeGetLong(getPlayerDoc(uuid), "kills");
    }

    // ===== DIES =====

    public static void setDies(UUID uuid, long dies) {
        updateField(uuid, "dies", dies);
    }

    public static void addDies(UUID uuid) {
        incrementField(uuid, "dies", 1);
    }

    public static long getDies(UUID uuid) {
        return safeGetLong(getPlayerDoc(uuid), "dies");
    }
}
