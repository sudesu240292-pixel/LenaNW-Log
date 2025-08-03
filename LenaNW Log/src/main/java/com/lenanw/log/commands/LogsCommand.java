package com.lenanw.log.commands;

import com.lenanw.log.LenaNWLog;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class LogsCommand implements CommandExecutor {
    
    private final LenaNWLog plugin;
    private final List<String> validLogTypes = Arrays.asList(
        "auth", "item_drop", "item_pickup", "chest", "death", 
        "claim", "money", "item_cleanup"
    );
    
    public LogsCommand(LenaNWLog plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("lenanw.log.view")) {
            sender.sendMessage("§cBu komutu kullanma yetkiniz yok!");
            return true;
        }
        
        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }
        
        String logType = args[0].toLowerCase();
        String playerName = args.length > 1 ? args[1] : null;
        
        if (!validLogTypes.contains(logType)) {
            sender.sendMessage("§cGeçersiz log türü! Geçerli türler:");
            for (String type : validLogTypes) {
                sender.sendMessage("§f- " + type);
            }
            return true;
        }
        
        String logContent = plugin.getLogManager().getLogContent(logType, playerName);
        
        if (logContent.isEmpty() || logContent.startsWith("Log dosyası bulunamadı")) {
            sender.sendMessage("§eLog dosyası bulunamadı veya boş: " + logType);
            return true;
        }
        
        // Log içeriğini sayfalara böl
        String[] lines = logContent.split("\n");
        int pageSize = 10;
        int totalPages = (lines.length + pageSize - 1) / pageSize;
        
        int page = 1;
        if (args.length > 2) {
            try {
                page = Integer.parseInt(args[2]);
                if (page < 1) page = 1;
                if (page > totalPages) page = totalPages;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, lines.length);
        
        sender.sendMessage("§6=== " + logType.toUpperCase() + " Logları ===");
        if (playerName != null) {
            sender.sendMessage("§eFiltre: §f" + playerName);
        }
        sender.sendMessage("§eSayfa: §f" + page + "/" + totalPages);
        sender.sendMessage("");
        
        for (int i = startIndex; i < endIndex; i++) {
            if (lines[i].trim().length() > 0) {
                sender.sendMessage("§f" + lines[i]);
            }
        }
        
        if (totalPages > 1) {
            sender.sendMessage("");
            sender.sendMessage("§eSayfa değiştirmek için: §f/logs " + logType + 
                (playerName != null ? " " + playerName : "") + " <sayfa>");
        }
        
        return true;
    }
    
    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage("§6=== Log Görüntüleme Komutları ===");
        sender.sendMessage("§e/logs <type> [player] [page]");
        sender.sendMessage("");
        sender.sendMessage("§eGeçerli Log Türleri:");
        for (String type : validLogTypes) {
            sender.sendMessage("§f- " + type);
        }
        sender.sendMessage("");
        sender.sendMessage("§eÖrnekler:");
        sender.sendMessage("§f/logs auth");
        sender.sendMessage("§f/logs death PlayerName");
        sender.sendMessage("§f/logs money PlayerName 2");
    }
} 