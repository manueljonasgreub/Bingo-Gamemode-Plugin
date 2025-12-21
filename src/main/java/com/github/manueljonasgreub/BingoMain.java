package com.github.manueljonasgreub;

import com.github.manueljonasgreub.commands.BingoCommand;
import com.github.manueljonasgreub.commands.PrintNameCommand;
import com.github.manueljonasgreub.game.Game;
import com.github.manueljonasgreub.listeners.PlayerEventListener;
import com.github.manueljonasgreub.utils.BatchFileCreator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.FileUtil;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;

import static com.github.manueljonasgreub.utils.BatchFileCreator.createBatchFileIfNotExists;

public final class BingoMain extends JavaPlugin {

    private Game game;

    @Override
    public void onLoad() {
        saveConfig();
        if(!getConfig().contains("isReset")){
            getConfig().set("isReset", false);
            saveConfig();
        }

        if (!getConfig().contains("api-base-url")) {
            getConfig().set("api-base-url", "http://api.vrmarek.me");
            saveConfig();
        }

        if(getConfig().getBoolean("isReset")){
            try {

                File world = new File(Bukkit.getWorldContainer(), "world");
                File world_nether = new File(Bukkit.getWorldContainer(), "world_nether");
                File world_the_end = new File(Bukkit.getWorldContainer(), "world_the_end");


                FileUtils.deleteDirectory(world);
                FileUtils.deleteDirectory(world_nether);
                FileUtils.deleteDirectory(world_the_end);

                world.mkdirs();
                world_nether.mkdirs();
                world_the_end.mkdirs();

                new File(world, "data").mkdirs();
                new File(world, "datapacks").mkdirs();
                new File(world, "playerdata").mkdirs();
                new File(world, "poi").mkdirs();
                new File(world, "region").mkdirs();

                new File(world_nether, "data").mkdirs();
                new File(world_nether, "datapacks").mkdirs();
                new File(world_nether, "playerdata").mkdirs();
                new File(world_nether, "poi").mkdirs();
                new File(world_nether, "region").mkdirs();

                new File(world_the_end, "data").mkdirs();
                new File(world_the_end, "datapacks").mkdirs();
                new File(world_the_end, "playerdata").mkdirs();
                new File(world_the_end, "poi").mkdirs();
                new File(world_the_end, "region").mkdirs();


            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            getConfig().set("isReset", false);
            saveConfig();
        }
    }

    @Override
    public void onEnable() {

        BatchFileCreator.createBatchFileIfNotExists();
        game = new Game();

        this.getCommand("print").setExecutor(new PrintNameCommand());
        this.getCommand("bingo").setExecutor(new BingoCommand());

        getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);

    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public static BingoMain getInstance(){
        return getPlugin(BingoMain.class);
    }

    public Game getGame(){
        return game;
    }

    public void restartServer() {
        try {
            Runtime.getRuntime().exec("cmd /c start restart.bat");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bukkit.shutdown();
    }

    public String getApiBaseUrl() {
        return getConfig().getString("api-base-url");
    }


}
