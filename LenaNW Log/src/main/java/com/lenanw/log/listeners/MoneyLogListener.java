package com.lenanw.log.listeners;

import com.lenanw.log.LenaNWLog;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerChatEvent;

public class MoneyLogListener implements Listener {
    
    private final LenaNWLog plugin;
    
    public MoneyLogListener(LenaNWLog plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().toLowerCase();
        
        // Para ile ilgili komutları yakala
        if (command.contains("money") || command.contains("balance") || 
            command.contains("pay") || command.contains("give") || 
            command.contains("set") || command.contains("add") || 
            command.contains("remove") || command.contains("kinas")) {
            
            // Komut parametrelerini parse et
            String[] args = command.split(" ");
            if (args.length >= 2) {
                String action = args[0].substring(1); // / işaretini kaldır
                String target = args.length > 1 ? args[1] : "";
                double amount = 0.0;
                
                try {
                    if (args.length > 2) {
                        amount = Double.parseDouble(args[2]);
                    }
                } catch (NumberFormatException e) {
                    // Sayı değilse amount 0 kalır
                }
                
                plugin.getLogManager().logMoney(player, action, amount, 
                    "Target: " + target + ", Command: " + command);
            }
        }
    }
    
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().toLowerCase();
        
        // Chat'te para ile ilgili mesajları yakala
        if (message.contains("money") || message.contains("balance") || 
            message.contains("pay") || message.contains("kinas")) {
            
            plugin.getLogManager().logMoney(player, "CHAT_MENTION", 0.0, 
                "Message: " + event.getMessage());
        }
    }
    
    // Bu metod dışarıdan çağrılabilir (diğer plugin'lerden)
    public void logMoneyTransaction(Player player, String action, double amount, String details) {
        plugin.getLogManager().logMoney(player, action, amount, details);
    }
} 