package com.github.manueljonasgreub.map;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

public class BingoMapRenderer extends MapRenderer {

    private BufferedImage image;
    private boolean done;

    public BingoMapRenderer() {
        this.done = false;
    }

    public BingoMapRenderer(String url) {
        load(url);
        this.done = false;
    }

    public boolean load(String url){
        BufferedImage image = null;
        try{
            image = ImageIO.read(new URL(url));
            image = MapPalette.resizeImage(image);
        }
        catch (Exception e) {
            return false;
        }
        this.image = image;
        return true;
    }
    @Override
    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
        if (done) {
            return;
        }
        mapCanvas.drawImage(0, 0, image);
        mapView.setTrackingPosition(false);
        done = true;
    }
}
