package com.lenanw.log.managers;

import com.lenanw.log.LenaNWLog;
import org.bukkit.configuration.ConfigurationSection;
import com.lenanw.log.libs.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiscordWebhookManager {
    
    private final LenaNWLog plugin;
    private final boolean enabled;
    private final ConfigurationSection webhookConfig;
    private final ConfigurationSection embedConfig;
    private final SimpleDateFormat timeFormat;
    
    public DiscordWebhookManager(LenaNWLog plugin) {
        this.plugin = plugin;
        this.enabled = plugin.getConfig().getBoolean("discord.enabled", false);
        this.webhookConfig = plugin.getConfig().getConfigurationSection("discord.webhooks");
        this.embedConfig = plugin.getConfig().getConfigurationSection("discord.embed");
        this.timeFormat = new SimpleDateFormat("HH:mm:ss");
    }
    
    public void sendWebhook(String logType, String title, String description, String... fields) {
        if (!enabled) return;
        
        String webhookUrl = getWebhookUrl(logType);
        if (webhookUrl == null || webhookUrl.equals("https://discord.com/api/webhooks/YOUR_WEBHOOK_URL_HERE")) {
            return; // Webhook URL ayarlanmamış
        }
        
        try {
            JSONObject embed = createEmbed(logType, title, description, fields);
            JSONObject payload = new JSONObject();
            payload.put("embeds", new JSONObject[]{embed});
            
            sendWebhookRequest(webhookUrl, payload.toString());
            
        } catch (Exception e) {
            plugin.getLogger().warning("Discord webhook gönderilemedi: " + e.getMessage());
        }
    }
    
    private JSONObject createEmbed(String logType, String title, String description, String... fields) {
        JSONObject embed = new JSONObject();
        
        // Başlık
        String embedTitle = getEmbedTitle(logType);
        if (embedTitle != null) {
            embed.put("title", embedTitle);
        }
        
        // Açıklama
        embed.put("description", description);
        
        // Renk
        String color = getEmbedColor(logType);
        if (color != null) {
            embed.put("color", Integer.parseInt(color, 16));
        }
        
        // Zaman damgası
        embed.put("timestamp", new Date().toInstant().toString());
        
        // Footer
        JSONObject footer = new JSONObject();
        footer.put("text", "LenaNW Log Sistemi • " + timeFormat.format(new Date()));
        embed.put("footer", footer);
        
        // Alanlar (fields)
        if (fields.length > 0) {
            JSONObject[] fieldObjects = new JSONObject[fields.length / 2];
            for (int i = 0; i < fields.length; i += 2) {
                if (i + 1 < fields.length) {
                    JSONObject field = new JSONObject();
                    field.put("name", fields[i]);
                    field.put("value", fields[i + 1]);
                    field.put("inline", true);
                    fieldObjects[i / 2] = field;
                }
            }
            embed.put("fields", fieldObjects);
        }
        
        return embed;
    }
    
    private String getWebhookUrl(String logType) {
        if (webhookConfig == null) return null;
        return webhookConfig.getString(logType);
    }
    
    private String getEmbedColor(String logType) {
        if (embedConfig == null) return null;
        ConfigurationSection colors = embedConfig.getConfigurationSection("colors");
        if (colors == null) return null;
        return colors.getString(logType, "ffffff");
    }
    
    private String getEmbedTitle(String logType) {
        if (embedConfig == null) return null;
        ConfigurationSection titles = embedConfig.getConfigurationSection("titles");
        if (titles == null) return null;
        return titles.getString(logType);
    }
    
    private void sendWebhookRequest(String webhookUrl, String payload) throws IOException {
        URL url = new URL(webhookUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "LenaNW-Log/1.0");
        connection.setDoOutput(true);
        
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = payload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        int responseCode = connection.getResponseCode();
        if (responseCode != 204) {
            plugin.getLogger().warning("Discord webhook yanıt kodu: " + responseCode);
        }
        
        connection.disconnect();
    }
    
    // Özel webhook metodları
    public void sendAuthWebhook(String playerName, String action, String details) {
        sendWebhook("auth", "Kullanıcı " + action, 
            "**" + playerName + "** " + action.toLowerCase() + " oldu",
            "Detaylar", details,
            "Zaman", timeFormat.format(new Date()));
    }
    
    public void sendItemDropWebhook(String playerName, String itemName, String location) {
        sendWebhook("item_drop", "İtem Atıldı", 
            "**" + playerName + "** " + itemName + " attı",
            "İtem", itemName,
            "Konum", location,
            "Zaman", timeFormat.format(new Date()));
    }
    
    public void sendItemPickupWebhook(String playerName, String itemName, String location) {
        sendWebhook("item_pickup", "İtem Toplandı", 
            "**" + playerName + "** " + itemName + " topladı",
            "İtem", itemName,
            "Konum", location,
            "Zaman", timeFormat.format(new Date()));
    }
    
    public void sendChestWebhook(String playerName, String action, String location, String itemInfo) {
        sendWebhook("chest", "Sandık İşlemi", 
            "**" + playerName + "** sandık " + action.toLowerCase(),
            "İşlem", action,
            "Konum", location,
            "İtem", itemInfo,
            "Zaman", timeFormat.format(new Date()));
    }
    
    public void sendDeathWebhook(String playerName, String cause, String location) {
        sendWebhook("death", "Oyuncu Öldü", 
            "**" + playerName + "** öldü",
            "Neden", cause,
            "Konum", location,
            "Zaman", timeFormat.format(new Date()));
    }
    
    public void sendClaimWebhook(String playerName, String action, String location, String details) {
        sendWebhook("claim", "Claim İşlemi", 
            "**" + playerName + "** claim " + action.toLowerCase(),
            "İşlem", action,
            "Konum", location,
            "Detaylar", details,
            "Zaman", timeFormat.format(new Date()));
    }
    
    public void sendMoneyWebhook(String playerName, String action, double amount, String details) {
        sendWebhook("money", "Para İşlemi", 
            "**" + playerName + "** " + action.toLowerCase() + " " + amount + " Kinas",
            "İşlem", action,
            "Miktar", amount + " Kinas",
            "Detaylar", details,
            "Zaman", timeFormat.format(new Date()));
    }
    
    public void sendItemCleanupWebhook(String location, int itemCount, String itemTypes) {
        sendWebhook("item_cleanup", "Item Temizleme", 
            "**" + itemCount + "** item temizlendi",
            "Sayı", String.valueOf(itemCount),
            "Türler", itemTypes,
            "Konum", location,
            "Zaman", timeFormat.format(new Date()));
    }
} 