package com.lenanw.log.listeners;

import com.lenanw.log.LenaNWLog;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public class ItemLogListener implements Listener {
    
    private final LenaNWLog plugin;
    
    public ItemLogListener(LenaNWLog plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        
        plugin.getLogManager().logItemDrop(player, item, event.getItemDrop().getLocation());
    }
    
    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem().getItemStack();
        
        plugin.getLogManager().logItemPickup(player, item, event.getItem().getLocation());
    }
    
    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ItemStack item = event.getItem().getItemStack();
            
            plugin.getLogManager().logItemPickup(player, item, event.getItem().getLocation());
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            ItemStack cursorItem = event.getCursor();
            
            if (clickedItem != null) {
                logInventoryAction(player, "CLICK", clickedItem, event.getInventory().getTitle());
            }
            
            if (cursorItem != null && event.isRightClick()) {
                logInventoryAction(player, "RIGHT_CLICK", cursorItem, event.getInventory().getTitle());
            }
        }
    }
    
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            ItemStack draggedItem = event.getOldCursor();
            
            if (draggedItem != null) {
                logInventoryAction(player, "DRAG", draggedItem, event.getInventory().getTitle());
            }
        }
    }
    
    private void logInventoryAction(Player player, String action, ItemStack item, String inventoryTitle) {
        // Yetkili log'u için özel kontrol
        if (player.hasPermission("lenanw.log.admin") || player.isOp()) {
            plugin.getLogManager().logItemPickup(player, item, player.getLocation());
        }
    }
} 