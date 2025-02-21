package com.github.manueljonasgreub;

import com.github.manueljonasgreub.commands.BingoCommand;
import com.github.manueljonasgreub.commands.PrintNameCommand;
import com.github.manueljonasgreub.game.Game;
import com.github.manueljonasgreub.listeners.PlayerEventListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class BingoMain extends JavaPlugin {

    private Game game;

    @Override
    public void onEnable() {

        game = new Game();



        this.getCommand("print").setExecutor(new PrintNameCommand());
        this.getCommand("bingo").setExecutor(new BingoCommand());

        getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static BingoMain getInstance(){
        return getPlugin(BingoMain.class);
    }

    public Game getGame(){
        return game;
    }


}
