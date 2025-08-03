package com.lenanw.log.commands;

import com.lenanw.log.LenaNWLog;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LogCommand implements CommandExecutor {
    
    private final LenaNWLog plugin;
    
    public LogCommand(LenaNWLog plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lenanw.log.admin")) {
            sender.sendMessage("§cBu komutu kullanma yetkiniz yok!");
            return true;
        }
        
        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "reload":
                handleReload(sender);
                break;
            case "info":
                handleInfo(sender);
                break;
            case "testwebhook":
                handleTestWebhook(sender, args);
                break;
            default:
                sendHelpMessage(sender);
                break;
        }
        
        return true;
    }
    
    private void handleReload(CommandSender sender) {
        plugin.reloadConfig();
        sender.sendMessage("§aLog sistemi yeniden yüklendi!");
    }
    
    private void handleInfo(CommandSender sender) {
        sender.sendMessage("§6=== LenaNW Log Sistemi Bilgileri ===");
        sender.sendMessage("§eVersiyon: §f1.0.0");
        sender.sendMessage("§eAktif Log Türleri:");
        sender.sendMessage("§f- Kullanıcı Giriş/Çıkış");
        sender.sendMessage("§f- İtem Atma/Toplama");
        sender.sendMessage("§f- Sandık İşlemleri");
        sender.sendMessage("§f- Ölüm Logları");
        sender.sendMessage("§f- Claim İşlemleri");
        sender.sendMessage("§f- Para İşlemleri");
        sender.sendMessage("§f- Item Temizleme (1 dakikada bir)");
        sender.sendMessage("§eLog Dosyaları: §fplugins/LenaNW-Log/logs/");
        
        // Discord webhook durumu
        boolean discordEnabled = plugin.getConfig().getBoolean("discord.enabled", false);
        sender.sendMessage("§eDiscord Webhook: §f" + (discordEnabled ? "§aAktif" : "§cPasif"));
    }
    
    private void handleTestWebhook(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cKullanım: /log testwebhook <type>");
            sender.sendMessage("§eGeçerli türler: auth, item_drop, item_pickup, chest, death, claim, money, item_cleanup");
            return;
        }
        
        String webhookType = args[1].toLowerCase();
        String testPlayer = sender.getName();
        
        switch (webhookType) {
            case "auth":
                plugin.getLogManager().getDiscordManager().sendAuthWebhook(testPlayer, "TEST", "Test webhook mesajı");
                break;
            case "item_drop":
                plugin.getLogManager().getDiscordManager().sendItemDropWebhook(testPlayer, "DIAMOND_SWORD x1", "Test konumu");
                break;
            case "item_pickup":
                plugin.getLogManager().getDiscordManager().sendItemPickupWebhook(testPlayer, "IRON_SWORD x1", "Test konumu");
                break;
            case "chest":
                plugin.getLogManager().getDiscordManager().sendChestWebhook(testPlayer, "OPENED", "Test konumu", "DIAMOND x64");
                break;
            case "death":
                plugin.getLogManager().getDiscordManager().sendDeathWebhook(testPlayer, "Test ölüm", "Test konumu");
                break;
            case "claim":
                plugin.getLogManager().getDiscordManager().sendClaimWebhook(testPlayer, "TEST_ACTION", "Test konumu", "Test detayları");
                break;
            case "money":
                plugin.getLogManager().getDiscordManager().sendMoneyWebhook(testPlayer, "TEST", 1000.0, "Test para işlemi");
                break;
            case "item_cleanup":
                plugin.getLogManager().getDiscordManager().sendItemCleanupWebhook("Test konumu", 50, "DIAMOND, IRON, GOLD");
                break;
            default:
                sender.sendMessage("§cGeçersiz webhook türü: " + webhookType);
                return;
        }
        
        sender.sendMessage("§a" + webhookType + " webhook test mesajı gönderildi!");
    }
    
    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage("§6=== Log Komutları ===");
        sender.sendMessage("§e/log reload §f- Sistemi yeniden yükle");
        sender.sendMessage("§e/log info §f- Sistem bilgilerini göster");
        sender.sendMessage("§e/log testwebhook <type> §f- Discord webhook test et");
        sender.sendMessage("§e/logs <type> [player] §f- Log dosyalarını görüntüle");
    }
} 