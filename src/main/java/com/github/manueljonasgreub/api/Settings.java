package com.github.manueljonasgreub.api;

import com.github.manueljonasgreub.team.Team;

import java.util.List;

public class Settings {
    private int grid_size;
    private String gamemode;
    private List<Team> teams;

    public int getGrid_size() {
        return grid_size;
    }

    public String getGamemode() {
        return gamemode;
    }

    public List<Team> getTeams() {
        return teams;
    }
}
