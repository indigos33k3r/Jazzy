package com.ktar5.mapeditor.tilemaps.whole;

import com.ktar5.mapeditor.tilemaps.BaseTilemap;
import com.ktar5.mapeditor.tileset.Tile;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class WholeTile extends Tile {
    public static final WholeTile AIR = new WholeTile(0, 0);

    private int blockId;
    private int direction;

    public WholeTile(int blockId, int direction) {
        this.blockId = blockId;
        this.direction = direction;
    }

    @Override
    public boolean isFoursquare() {
        return false;
    }

    @Override
    public void draw(BaseTilemap baseTilemap, int x, int y) {

    }

    @Override
    public String serialize() {
        return String.valueOf(blockId);
    }

    @Override
    public String toString() {
        return blockId + "_" + direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WholeTile wholeTile = (WholeTile) o;
        return blockId == wholeTile.blockId &&
                direction == wholeTile.direction;
    }

    @Override
    public int hashCode() {

        return Objects.hash(blockId, direction);
    }
}