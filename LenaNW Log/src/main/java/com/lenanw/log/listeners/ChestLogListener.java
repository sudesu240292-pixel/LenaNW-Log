package com.lenanw.log.listeners;

import com.lenanw.log.LenaNWLog;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;

public class ChestLogListener implements Listener {
    
    private final LenaNWLog plugin;
    
    public ChestLogListener(LenaNWLog plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            Inventory inventory = event.getInventory();
            
            if (isChestInventory(inventory)) {
                plugin.getLogManager().logChest(player, "OPENED", player.getLocation(), null);
            }
        }
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            Inventory inventory = event.getInventory();
            
            if (isChestInventory(inventory)) {
                plugin.getLogManager().logChest(player, "CLOSED", player.getLocation(), null);
            }
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && isChestInventory(event.getInventory())) {
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            ItemStack cursorItem = event.getCursor();
            
            if (clickedItem != null) {
                plugin.getLogManager().logChest(player, "TOOK_ITEM", player.getLocation(), clickedItem);
            }
            
            if (cursorItem != null && event.isRightClick()) {
                plugin.getLogManager().logChest(player, "PUT_ITEM", player.getLocation(), cursorItem);
            }
        }
    }
    
    private boolean isChestInventory(Inventory inventory) {
        String title = inventory.getTitle();
        return title != null && (title.contains("Chest") || title.contains("Sandık") || 
               title.contains("Chest") || title.contains("chest"));
    }
} 