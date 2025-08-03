package com.lenanw.log.managers;

import com.lenanw.log.LenaNWLog;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LogManager {
    
    private final LenaNWLog plugin;
    private final Map<String, BufferedWriter> writers;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat timeFormat;
    private final DiscordWebhookManager discordManager;
    
    public LogManager(LenaNWLog plugin) {
        this.plugin = plugin;
        this.writers = new HashMap<>();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.timeFormat = new SimpleDateFormat("HH:mm:ss");
        this.discordManager = new DiscordWebhookManager(plugin);
        
        // Log klasörünü oluştur
        createLogDirectory();
        
        // Writer'ları başlat
        initializeWriters();
    }
    
    private void createLogDirectory() {
        File logDir = new File(plugin.getDataFolder(), "logs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
    }
    
    private void initializeWriters() {
        String[] logTypes = {
            "auth", "item_drop", "item_pickup", "chest", "death", 
            "claim", "money", "item_cleanup"
        };
        
        for (String type : logTypes) {
            createWriter(type);
        }
    }
    
    private void createWriter(String logType) {
        try {
            String fileName = logType + "_" + dateFormat.format(new Date()) + ".log";
            File logFile = new File(plugin.getDataFolder(), "logs/" + fileName);
            
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
            writers.put(logType, writer);
            
        } catch (IOException e) {
            plugin.getLogger().severe("Log dosyası oluşturulamadı: " + logType);
            e.printStackTrace();
        }
    }
    
    public void logAuth(String playerName, String action, String details) {
        writeLog("auth", String.format("[%s] %s - %s: %s", 
            timeFormat.format(new Date()), playerName, action, details));
        
        // Discord webhook gönder
        discordManager.sendAuthWebhook(playerName, action, details);
    }
    
    public void logItemDrop(Player player, ItemStack item, Location location) {
        String itemInfo = item.getType().name() + " x" + item.getAmount();
        String locationInfo = formatLocation(location);
        
        writeLog("item_drop", String.format("[%s] %s dropped %s at %s", 
            timeFormat.format(new Date()), player.getName(), itemInfo, locationInfo));
        
        // Discord webhook gönder
        discordManager.sendItemDropWebhook(player.getName(), itemInfo, locationInfo);
    }
    
    public void logItemPickup(Player player, ItemStack item, Location location) {
        String itemInfo = item.getType().name() + " x" + item.getAmount();
        String locationInfo = formatLocation(location);
        
        writeLog("item_pickup", String.format("[%s] %s picked up %s at %s", 
            timeFormat.format(new Date()), player.getName(), itemInfo, locationInfo));
        
        // Discord webhook gönder
        discordManager.sendItemPickupWebhook(player.getName(), itemInfo, locationInfo);
    }
    
    public void logChest(Player player, String action, Location location, ItemStack item) {
        String locationInfo = formatLocation(location);
        String itemInfo = item != null ? item.getType().name() + " x" + item.getAmount() : "N/A";
        
        writeLog("chest", String.format("[%s] %s %s chest at %s - Item: %s", 
            timeFormat.format(new Date()), player.getName(), action, locationInfo, itemInfo));
        
        // Discord webhook gönder
        discordManager.sendChestWebhook(player.getName(), action, locationInfo, itemInfo);
    }
    
    public void logDeath(Player player, String cause, Location location) {
        String locationInfo = formatLocation(location);
        
        writeLog("death", String.format("[%s] %s died at %s - Cause: %s", 
            timeFormat.format(new Date()), player.getName(), locationInfo, cause));
        
        // Discord webhook gönder
        discordManager.sendDeathWebhook(player.getName(), cause, locationInfo);
    }
    
    public void logClaim(Player player, String action, Location location, String details) {
        String locationInfo = formatLocation(location);
        
        writeLog("claim", String.format("[%s] %s %s claim at %s - %s", 
            timeFormat.format(new Date()), player.getName(), action, locationInfo, details));
        
        // Discord webhook gönder
        discordManager.sendClaimWebhook(player.getName(), action, locationInfo, details);
    }
    
    public void logMoney(Player player, String action, double amount, String details) {
        writeLog("money", String.format("[%s] %s %s %.2f Kinas - %s", 
            timeFormat.format(new Date()), player.getName(), action, amount, details));
        
        // Discord webhook gönder
        discordManager.sendMoneyWebhook(player.getName(), action, amount, details);
    }
    
    public void logItemCleanup(Location location, int itemCount, String itemTypes) {
        String locationInfo = location != null ? formatLocation(location) : "Tüm dünya";
        
        writeLog("item_cleanup", String.format("[%s] Cleaned up %d items at %s - Types: %s", 
            timeFormat.format(new Date()), itemCount, locationInfo, itemTypes));
        
        // Discord webhook gönder
        discordManager.sendItemCleanupWebhook(locationInfo, itemCount, itemTypes);
    }
    
    private void writeLog(String logType, String message) {
        BufferedWriter writer = writers.get(logType);
        if (writer != null) {
            try {
                writer.write(message);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                plugin.getLogger().severe("Log yazılamadı: " + logType);
                e.printStackTrace();
            }
        }
    }
    
    private String formatLocation(Location location) {
        return String.format("World: %s, X: %.2f, Y: %.2f, Z: %.2f", 
            location.getWorld().getName(), location.getX(), location.getY(), location.getZ());
    }
    
    public void closeAllWriters() {
        for (BufferedWriter writer : writers.values()) {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                plugin.getLogger().severe("Writer kapatılamadı");
                e.printStackTrace();
            }
        }
        writers.clear();
    }
    
    public String getLogContent(String logType, String playerName) {
        try {
            String fileName = logType + "_" + dateFormat.format(new Date()) + ".log";
            File logFile = new File(plugin.getDataFolder(), "logs/" + fileName);
            
            if (!logFile.exists()) {
                return "Log dosyası bulunamadı: " + fileName;
            }
            
            StringBuilder content = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(logFile));
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (playerName == null || line.contains(playerName)) {
                    content.append(line).append("\n");
                }
            }
            
            reader.close();
            return content.toString();
            
        } catch (IOException e) {
            return "Log dosyası okunamadı: " + e.getMessage();
        }
    }
    
    public DiscordWebhookManager getDiscordManager() {
        return discordManager;
    }
} 