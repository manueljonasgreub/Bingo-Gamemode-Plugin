package com.github.manueljonasgreub.team;

import org.bukkit.entity.Player;

import java.util.*;

public class TeamManager {

    private final List<Team> teams;

    private final Map<UUID, Team> playerTeamMap = new HashMap<>();


    public TeamManager(){

        teams = new ArrayList<>(List.of(
                new Team("1", "red"),
                new Team("2", "yellow"),
                new Team("3", "green"),
                new Team("4", "blue")
        ));

    }


    public void addPlayer(Player player, Team team){
        team.addPlayer(player);
        playerTeamMap.put(player.getUniqueId(),team);
    }

    public void removePlayer(Player player){
        Team team = playerTeamMap.remove(player.getUniqueId());
        if(team != null){
            team.removePlayer(player);
        }
    }

    public void distributePlayersRandomly(Collection<? extends Player> players){
        List<Player> shuffled = new ArrayList<>(players);
        Collections.shuffle(shuffled);
        int teamIndex = 0;
        for (Player p : shuffled) {
            addPlayer(p, teams.get(teamIndex % teams.size()));
            p.sendMessage("§eNo teams were selected, so they were created randomly.");
            teamIndex++;
        }
    }

    public boolean allTeamsEmpty(){
        return teams.stream().allMatch(team -> team.players.isEmpty());
    }

    public Optional<Team> getTeamOf(Player player) {
        return Optional.ofNullable(playerTeamMap.get(player.getUniqueId()));
    }

    public List<Team> getTeams() { return teams; }

    public void setTeams(List<Team> newTeams) {
        teams.clear();
        teams.addAll(newTeams);
        rebuildLookup();
    }

    private void rebuildLookup() {
        playerTeamMap.clear();
        for (Team t : teams) {
            for (Player p : t.players) {
                playerTeamMap.put(p.getUniqueId(), t);
            }
        }
    }

}
