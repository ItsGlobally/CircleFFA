package me.itsglobally.circleffa;

import org.bukkit.Bukkit;

import java.lang.reflect.Method;

public class circleCoreApiUtils {
    public static void setChatHandleByCore(Boolean s) {
        try {
            Class<?> apiClass = Class.forName("me.itsglobally.circlecore.api");

            Method method = apiClass.getMethod("setChatFormat", Boolean.class);

            method.invoke(null, s);

        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().warning("CircleCore api class not found!");
        } catch (NoSuchMethodException e) {
            Bukkit.getLogger().warning("setChatHandleByCore method not found in CircleCore api!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
