package me.itsglobally.circleffa;

import java.util.HashMap;
import java.util.UUID;

public class data {
    private static final HashMap<UUID, Boolean> bm = new HashMap<>();

    public static void setBm(UUID p) {
        bm.put(p, !bm.get(p));
    }

    public static Boolean getBm(UUID p) {
        return bm.get(p);
    }
}
