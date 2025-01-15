package com.github.manueljonasgreub;

import com.github.manueljonasgreub.commands.PrintNameCommand;
import com.github.manueljonasgreub.listeners.PlayerEventListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class BingoMain extends JavaPlugin {

    @Override
    public void onEnable() {


        this.getCommand("print").setExecutor(new PrintNameCommand());
        getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
