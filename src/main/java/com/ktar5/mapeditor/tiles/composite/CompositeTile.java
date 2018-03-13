package com.ktar5.mapeditor.tiles.composite;

import com.ktar5.mapeditor.tiles.Tile;
import javafx.scene.canvas.Canvas;

public class CompositeTile extends Tile {
    CompositeTilePart[] tileparts = new CompositeTilePart[4];

    public CompositeTile() {
        for (int i = 0; i < tileparts.length; i++) {
            tileparts[i] = new CompositeTilePart(0, 0);
        }
    }

    public CompositeTile(String block) {
        this();
        block = block.substring(1, block.length());
        String[] split = block.split("/");
        for (int i = 0; i < split.length; i++) {
            tileparts[i] = new CompositeTilePart(split[i]);
        }
    }

    @Override
    public boolean isFoursquare() {
        return true;
    }

    @Override
    public void draw(Canvas canvas, int x, int y) {

    }

    @Override
    public String serialize() {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < tileparts.length; i++) {
            builder.append(tileparts[i].serialize());
            builder.append("/");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public String toString() {
        return serialize();
    }
}
