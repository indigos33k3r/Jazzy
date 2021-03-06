package com.ktar5.jazzy.editor.gui.utils;

import com.ktar5.jazzy.editor.tileset.BaseTileset;
import lombok.Getter;

@Getter
public class TilesetImageView extends PixelatedImageView {
    private BaseTileset tileset;
    private int tileId;
    
    public TilesetImageView(BaseTileset tileset, int tileId) {
        super(tileset.getTileImages().get(tileId));
        this.tileId = tileId;
        this.tileset = tileset;
    }
    
}
