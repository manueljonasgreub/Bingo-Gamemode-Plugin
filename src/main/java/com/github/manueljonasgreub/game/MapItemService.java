package com.github.manueljonasgreub.game;

import com.github.manueljonasgreub.BingoMain;
import com.github.manueljonasgreub.map.BingoMapRenderer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataType;


public class MapItemService {

    private static final NamespacedKey BINGO_MAP_KEY =
            new NamespacedKey(BingoMain.getInstance(), "bingo_preview_map");

    public static ItemStack createMapItem(String mapURL){

        MapView mapView = Bukkit.createMap(BingoMain.getInstance().getServer().getWorld("world"));
        mapView.getRenderers().clear();

        BingoMapRenderer renderer = new BingoMapRenderer(mapURL);
        mapView.addRenderer(renderer);

        ItemStack mapItem = new ItemStack(Material.FILLED_MAP);
        MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();
        mapMeta.setMapView(mapView);
        mapMeta.getPersistentDataContainer().set(BINGO_MAP_KEY, PersistentDataType.BYTE, (byte) 1);
        mapItem.setItemMeta(mapMeta);

        return mapItem;

    }

    public static void giveMapItemsToAll(ItemStack mapItem){
        for (var player : BingoMain.getInstance().getServer().getOnlinePlayers()){
            player.getInventory().addItem(mapItem);
        }
    }

    public static void updateMapItemsFromAll(String mapURL){

        MapView mapView = Bukkit.createMap(BingoMain.getInstance().getServer().getWorld("world"));
        mapView.getRenderers().clear();

        BingoMapRenderer renderer = new BingoMapRenderer(mapURL);
        mapView.addRenderer(renderer);

        ItemStack mapItem = new ItemStack(Material.FILLED_MAP);
        MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();
        mapMeta.setMapView(mapView);
        mapItem.setItemMeta(mapMeta);

        for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
            for (ItemStack currentMapItem : currentPlayer.getInventory()) {
                if (currentMapItem != null) {
                    if (currentMapItem.getType().equals(Material.FILLED_MAP)) {
                        currentMapItem.setItemMeta(mapMeta);
                    }
                }
            }
        }
    }

    public static boolean isBingoMap(ItemStack it) {
        if (it == null || it.getType() != Material.FILLED_MAP) return false;
        ItemMeta im = it.getItemMeta();
        if (im == null) return false;
        Byte v = im.getPersistentDataContainer().get(BINGO_MAP_KEY, PersistentDataType.BYTE);
        return v != null && v == 1;
    }

    public static void removeMapItems(Player player) {
        PlayerInventory inv = player.getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack it = inv.getItem(i);
            if (MapItemService.isBingoMap(it)) inv.setItem(i, null);
        }
    }

    public static void removeMapItemsFromAll(){
        for (var player : BingoMain.getInstance().getServer().getOnlinePlayers()){
            removeMapItems(player);
        }
    }


}
