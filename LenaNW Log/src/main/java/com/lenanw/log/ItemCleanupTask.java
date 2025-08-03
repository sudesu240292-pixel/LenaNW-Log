package com.lenanw.log;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

public class ItemCleanupTask extends BukkitRunnable {
    
    private final LenaNWLog plugin;
    
    public ItemCleanupTask(LenaNWLog plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void run() {
        Map<Location, Integer> locationItemCounts = new HashMap<>();
        Map<Location, StringBuilder> locationItemTypes = new HashMap<>();
        
        // Tüm dünyalarda yerdeki itemleri tara
        for (World world : plugin.getServer().getWorlds()) {
            for (Item item : world.getEntitiesByClass(Item.class)) {
                Location location = item.getLocation();
                String locationKey = formatLocationKey(location);
                
                // Item sayısını artır
                locationItemCounts.put(location, 
                    locationItemCounts.getOrDefault(location, 0) + item.getItemStack().getAmount());
                
                // Item türlerini kaydet
                if (!locationItemTypes.containsKey(location)) {
                    locationItemTypes.put(location, new StringBuilder());
                }
                
                StringBuilder types = locationItemTypes.get(location);
                String itemType = item.getItemStack().getType().name();
                
                if (!types.toString().contains(itemType)) {
                    if (types.length() > 0) {
                        types.append(", ");
                    }
                    types.append(itemType);
                }
            }
        }
        
        // Her lokasyon için log yaz
        for (Location location : locationItemCounts.keySet()) {
            int itemCount = locationItemCounts.get(location);
            String itemTypes = locationItemTypes.get(location).toString();
            
            plugin.getLogManager().logItemCleanup(location, itemCount, itemTypes);
        }
        
        // Eğer hiç item yoksa genel bir log yaz
        if (locationItemCounts.isEmpty()) {
            plugin.getLogManager().logItemCleanup(null, 0, "No items found");
        }
    }
    
    private String formatLocationKey(Location location) {
        return String.format("%s_%.0f_%.0f_%.0f", 
            location.getWorld().getName(), 
            location.getX(), location.getY(), location.getZ());
    }
} 