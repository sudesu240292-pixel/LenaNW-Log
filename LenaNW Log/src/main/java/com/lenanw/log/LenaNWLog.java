package com.lenanw.log;

import com.lenanw.log.listeners.*;
import com.lenanw.log.managers.LogManager;
import com.lenanw.log.commands.LogCommand;
import com.lenanw.log.commands.LogsCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class LenaNWLog extends JavaPlugin {
    
    private static LenaNWLog instance;
    private LogManager logManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Config dosyasını oluştur
        saveDefaultConfig();
        
        // Log Manager'ı başlat
        logManager = new LogManager(this);
        
        // Event listener'ları kaydet
        registerListeners();
        
        // Komutları kaydet
        registerCommands();
        
        getLogger().info("LenaNW Log Sistemi başarıyla yüklendi!");
    }
    
    @Override
    public void onDisable() {
        if (logManager != null) {
            logManager.closeAllWriters();
        }
        getLogger().info("LenaNW Log Sistemi kapatıldı!");
    }
    
    private void registerListeners() {
        // Kullanıcı - Şifre Log
        getServer().getPluginManager().registerEvents(new AuthLogListener(this), this);
        
        // İtem Log'ları
        getServer().getPluginManager().registerEvents(new ItemLogListener(this), this);
        
        // Sandık Log
        getServer().getPluginManager().registerEvents(new ChestLogListener(this), this);
        
        // Ölüm Log
        getServer().getPluginManager().registerEvents(new DeathLogListener(this), this);
        
        // Claim Log
        getServer().getPluginManager().registerEvents(new ClaimLogListener(this), this);
        
        // Kinas (Para) Log
        getServer().getPluginManager().registerEvents(new MoneyLogListener(this), this);
        
        // Item Silme Log (1 dakikada bir)
        getServer().getScheduler().runTaskTimer(this, new ItemCleanupTask(this), 1200L, 1200L); // 1200 tick = 1 dakika
    }
    
    private void registerCommands() {
        getCommand("log").setExecutor(new LogCommand(this));
        getCommand("logs").setExecutor(new LogsCommand(this));
    }
    
    public static LenaNWLog getInstance() {
        return instance;
    }
    
    public LogManager getLogManager() {
        return logManager;
    }
} 