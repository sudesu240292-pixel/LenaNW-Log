package com.lenanw.log.listeners;

import com.lenanw.log.LenaNWLog;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerKickEvent;

public class AuthLogListener implements Listener {
    
    private final LenaNWLog plugin;
    
    public AuthLogListener(LenaNWLog plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        String playerName = event.getPlayer().getName();
        String address = event.getAddress().getHostAddress();
        
        plugin.getLogManager().logAuth(playerName, "LOGIN_ATTEMPT", 
            "Address: " + address + ", Result: " + event.getResult());
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        String address = event.getPlayer().getAddress().getAddress().getHostAddress();
        
        plugin.getLogManager().logAuth(playerName, "JOIN", 
            "Address: " + address + ", UUID: " + event.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        String address = event.getPlayer().getAddress().getAddress().getHostAddress();
        
        plugin.getLogManager().logAuth(playerName, "QUIT", 
            "Address: " + address + ", Playtime: " + getPlaytime(event.getPlayer()));
    }
    
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        String playerName = event.getPlayer().getName();
        String reason = event.getReason();
        String address = event.getPlayer().getAddress().getAddress().getHostAddress();
        
        plugin.getLogManager().logAuth(playerName, "KICK", 
            "Address: " + address + ", Reason: " + reason);
    }
    
    private String getPlaytime(org.bukkit.entity.Player player) {
        // Bu kısım sunucunuzun playtime sistemine göre değiştirilebilir
        return "N/A";
    }
} 