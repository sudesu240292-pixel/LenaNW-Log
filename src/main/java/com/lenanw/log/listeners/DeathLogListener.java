package com.lenanw.log.listeners;

import com.lenanw.log.LenaNWLog;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DeathLogListener implements Listener {
    
    private final LenaNWLog plugin;
    
    public DeathLogListener(LenaNWLog plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String cause = getDeathCause(player);
        
        plugin.getLogManager().logDeath(player, cause, player.getLocation());
    }
    
    private String getDeathCause(Player player) {
        EntityDamageEvent lastDamage = player.getLastDamageCause();
        
        if (lastDamage == null) {
            return "Unknown";
        }
        
        EntityDamageEvent.DamageCause damageCause = lastDamage.getCause();
        
        switch (damageCause) {
            case FALL:
                return "Fall Damage";
            case DROWNING:
                return "Drowning";
            case FIRE:
            case FIRE_TICK:
                return "Fire";
            case LAVA:
                return "Lava";
            case VOID:
                return "Void";
            case SUICIDE:
                return "Suicide";
            case STARVATION:
                return "Starvation";
            case POISON:
                return "Poison";
            case WITHER:
                return "Wither";
            case LIGHTNING:
                return "Lightning";
            case EXPLOSION:
                return "Explosion";
            case PROJECTILE:
                return "Projectile";
            case ENTITY_ATTACK:
                if (lastDamage instanceof EntityDamageByEntityEvent) {
                    EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) lastDamage;
                    if (entityEvent.getDamager() instanceof Player) {
                        return "Killed by " + ((Player) entityEvent.getDamager()).getName();
                    } else {
                        return "Killed by " + entityEvent.getDamager().getType().name();
                    }
                }
                return "Entity Attack";
            default:
                return damageCause.name();
        }
    }
} 