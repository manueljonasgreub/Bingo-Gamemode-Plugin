package com.github.manueljonasgreub.inventory;

import com.github.manueljonasgreub.BingoMain;
import com.github.manueljonasgreub.game.Game;
import com.github.manueljonasgreub.team.Team;
import com.github.manueljonasgreub.utils.PlacementMode;
import com.github.manueljonasgreub.utils.TeamColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.*;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class TeamsView implements Listener {

    private final Inventory gui;

    private final Game game = BingoMain.getInstance().getGame();

    private List<Team> teams = game.getTeams();


    private final int[] playerSlots = {
            10,11,12,13,14,15,16,
            19,20,21,22,23,24,25,
            28,29,30,31,32,33,34,
            37,38,39,40,41,42,43
    };

    public TeamsView() {
        gui = Bukkit.createInventory(null, 54, "Team Settings");
    }

    public void openGUI(Player player) {

        setItems();
        player.openInventory(gui);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (!event.getView().getTitle().equals("Team Settings")) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        switch (event.getSlot()) {
            case 2:
                handleTeamClick(player, 0);
                break;
            case 3:
                handleTeamClick(player, 1);
                break;
            case 5:
                handleTeamClick(player, 2);
                break;
            case 6:
                handleTeamClick(player, 3);
                break;
                case 10,11,12,13,14,15,16,
                     19,20,21,22,23,24,25,
                     28,29,30,31,32,33,34,
                     37,38,39,40,41,42,43:
                    handlePlayerClick(player, clickedItem);


            default:
                break;
        }


    }

    private void setItems(){

        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        if (fillerMeta != null) {
            fillerMeta.displayName(Component.empty());
            fillerMeta.setHideTooltip(true);
            filler.setItemMeta(fillerMeta);
        }

        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, filler);
        }


        setTeamItem(gui, 0);
        setTeamItem(gui, 1);
        setTeamItem(gui, 2);
        setTeamItem(gui, 3);

        setPlayerItems(gui);


    }


    private void setTeamItem(Inventory inv, int teamIndex) {

        ItemStack item = GetBannerItemStack(teams.get(teamIndex));
        BannerMeta meta = (BannerMeta)item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Edit Team " + (teamIndex + 1) + " Color");
        meta.lore(List.of(
                Component.text(ChatColor.GRAY + ""),
                Component.text(ChatColor.GRAY + "Click to cycle through"),
                Component.text(ChatColor.GRAY + "available colors."),
                Component.text(ChatColor.GRAY + "")
        ));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ITEM_SPECIFICS);

        if (teamIndex == 0) meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.CREEPER));
        if (teamIndex == 1) meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.FLOWER));
        if (teamIndex == 2) meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.GLOBE));
        if (teamIndex == 3) meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.FLOW));

        item.setItemMeta(meta);
        if(teamIndex == 0) inv.setItem(2, item);
        if(teamIndex == 1) inv.setItem(3, item);
        if(teamIndex == 2) inv.setItem(5, item);
        if(teamIndex == 3) inv.setItem(6, item);
    }

    private void setPlayerItems(Inventory inv) {

        List<Player> players = new java.util.ArrayList<>(Bukkit.getOnlinePlayers());

        int index = 0;

        for (Player player : players)
        {
            if (index >= playerSlots.length) break;

            int teamIndex = getTeamIndex(player);

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwningPlayer(player);
            meta.displayName(Component.text(ChatColor.WHITE + player.getName()));
            meta.lore(List.of(
                    Component.text(ChatColor.DARK_GRAY + "Modify the team of the "),
                    Component.text(ChatColor.DARK_GRAY + "current player."),
                    Component.text(ChatColor.DARK_GRAY + ""),
                    optionLine("Auto", teamIndex == -1, ChatColor.GRAY),
                    optionLine("Team 1", teamIndex == 0, ChatColor.RED),
                    optionLine("Team 2", teamIndex == 1, ChatColor.YELLOW),
                    optionLine("Team 3", teamIndex == 2, ChatColor.GREEN),
                    optionLine("Team 4", teamIndex == 3, ChatColor.BLUE),
                    Component.text(ChatColor.DARK_GRAY + "")
            ));


            head.setItemMeta(meta);


            inv.setItem(playerSlots[index], head);
            index++;
        }

        for (int i = index; i < playerSlots.length; i++) {
            inv.setItem(playerSlots[i], new ItemStack(Material.AIR));
        }

    }

    private void handleTeamClick(Player player, int teamIndex)
    {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1.0f, 1.0f);

        Team team = teams.get(teamIndex);
        //team.color = TeamColor.values()[(team.color.ordinal() + 1) % TeamColor.values().length];

        TeamColor[] values = TeamColor.values();
        TeamColor start = team.color;
        TeamColor next = start;

        do {
            next = values[(next.ordinal() + 1) % values.length];
        } while (next != start && isColorUsed(teams, team, next));

        team.color = next;


        InventoryView view = player.getOpenInventory();
        Inventory inv = view.getTopInventory();
        setTeamItem(inv, teamIndex);
    }

    private void handlePlayerClick(Player player, ItemStack item)
    {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 1.0f, 1.0f);

        Component comp = item.getItemMeta().displayName();
        String name = "";

        if(comp != null)
        {
            name = ChatColor.stripColor(PlainTextComponentSerializer.plainText().serialize(comp));
        }



        Player currentPlayer = Bukkit.getPlayerExact(name);
        if (currentPlayer == null) return;


        Team currentTeam = null;


        for (Team team : teams) {
            if (team.players.contains(currentPlayer)) {
                currentTeam = team;
                break;
            }
        }


        if (currentTeam == null) {
            teams.get(0).addPlayer(currentPlayer);
        } else {

            int currentIndex = teams.indexOf(currentTeam);

            currentTeam.removePlayer(currentPlayer);


            int nextIndex = currentIndex + 1;
            if (nextIndex < teams.size()) {
                teams.get(nextIndex).addPlayer(currentPlayer);
            }

        }


        InventoryView view = player.getOpenInventory();
        Inventory inv = view.getTopInventory();
        setPlayerItems(inv);
    }


    private ItemStack GetBannerItemStack(Team team){

        Material m = Material.valueOf(team.color.toString().toUpperCase() + "_BANNER");
        return new ItemStack(m);
    }

    private boolean isColorUsed(List<Team> teams, Team self, TeamColor color) {
        for (Team t : teams) {
            if (t != self && t.color == color) return true;
        }
        return false;
    }

    private int getTeamIndex(Player p) {
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).players.contains(p)) return i; // 0..3
        }
        return -1; // Auto
    }

    private Component optionLine(String text, boolean selected, ChatColor selectedColor) {
        ChatColor c = selected ? selectedColor : ChatColor.DARK_GRAY;
        return Component.text( c + text);
    }

}
