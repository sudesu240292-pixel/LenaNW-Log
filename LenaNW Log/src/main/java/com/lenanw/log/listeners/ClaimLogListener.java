package com.lenanw.log.listeners;

import com.lenanw.log.LenaNWLog;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.block.Block;

public class ClaimLogListener implements Listener {
    
    private final LenaNWLog plugin;
    
    public ClaimLogListener(LenaNWLog plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        
        if (isInClaim(player, block.getLocation())) {
            plugin.getLogManager().logClaim(player, "BROKE_BLOCK", block.getLocation(), 
                "Block: " + block.getType().name());
        }
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        
        if (isInClaim(player, block.getLocation())) {
            plugin.getLogManager().logClaim(player, "PLACED_BLOCK", block.getLocation(), 
                "Block: " + block.getType().name());
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        
        if (block != null && isInClaim(player, block.getLocation())) {
            String action = event.getAction().name();
            plugin.getLogManager().logClaim(player, "INTERACTED", block.getLocation(), 
                "Action: " + action + ", Block: " + block.getType().name());
        }
    }
    
    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player) {
            Player player = (Player) event.getRemover();
            
            if (isInClaim(player, event.getEntity().getLocation())) {
                plugin.getLogManager().logClaim(player, "BROKE_HANGING", event.getEntity().getLocation(), 
                    "Hanging: " + event.getEntity().getType().name());
            }
        }
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            
            if (isInClaim(player, event.getEntity().getLocation())) {
                plugin.getLogManager().logClaim(player, "DAMAGED_ENTITY", event.getEntity().getLocation(), 
                    "Entity: " + event.getEntity().getType().name());
            }
        }
    }
    
    private boolean isInClaim(Player player, org.bukkit.Location location) {
        // Bu kısım sunucunuzun claim sistemine göre değiştirilebilir
        // Örnek: WorldGuard, GriefPrevention, vb.
        
        // Şimdilik basit bir kontrol yapıyoruz
        // Gerçek claim sistemi entegrasyonu için bu kısmı güncelleyin
        return false; // Varsayılan olarak false döndürüyoruz
    }
} 