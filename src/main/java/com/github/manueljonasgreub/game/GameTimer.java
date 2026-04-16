package com.github.manueljonasgreub.game;

import com.github.manueljonasgreub.BingoMain;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GameTimer {

    private int time = 0;
    private int startTime = 3600;
    private boolean countdown = false;
    private boolean showTimer = true;


    private final Runnable onTimeUp;

    public GameTimer(Runnable onTimeUp) {
        this.onTimeUp = onTimeUp;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(
                BingoMain.getInstance(),
                this::tick,
                0L, 20L
        );
    }


    private void tick() {
        if (showTimer) broadcastActionBar();

        if (!running) return;

        if (countdown) {
            time--;
            if (time <= 0) {
                running = false;
                onTimeUp.run();
            }
        } else {
            time++;
        }
    }

    private boolean running = false;

    public void start() {
        time = countdown ? startTime : 0;
        running = true;
    }

    public void pause() { running = false; }
    public void resume() { running = true; }
    public void set(int seconds) { this.time = seconds; }

    public boolean isRunning() { return running; }
    public int getTime() { return time; }
    public int getStartTime() { return startTime; }
    public void setStartTime(int startTime) { this.startTime = startTime; }
    public boolean isCountdown() { return countdown; }
    public void toggleCountdown() { countdown = !countdown; }
    public void setShowTimer(boolean show) { this.showTimer = show; }
    public boolean isShowingTimer() { return showTimer; }

    private void broadcastActionBar() {
        Component message = running ? buildTimerComponent() : buildPausedComponent();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendActionBar(message);
        }
    }
    private Component buildTimerComponent() {
        int h = time / 3600;
        int m = (time % 3600) / 60;
        int s = time % 60;
        String formatted = String.format("%02d:%02d:%02d", h, m, s);
        return Component.text(formatted)
                .color(NamedTextColor.WHITE)
                .decorate(TextDecoration.BOLD);
    }

    private Component buildPausedComponent() {

        long gameTime = Bukkit.getWorlds().get(0).getGameTime();
        int phase = (int) (gameTime % 80);
        String dots = phase <= 20 ? "" : phase <= 40 ? "·" : phase <= 60 ? "· ·" : "· · ·";
        return Component.text(dots + " Timer paused " + dots)
                .color(NamedTextColor.YELLOW)
                .decorate(TextDecoration.ITALIC);
    }

}
