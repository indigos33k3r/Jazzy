package com.ktar5.mapeditor.tilemaps.composite;

import com.ktar5.mapeditor.Main;
import com.ktar5.mapeditor.gui.PixelatedImageView;
import com.ktar5.mapeditor.tilemaps.BaseTilemap;
import com.ktar5.mapeditor.tilemaps.whole.WholeTile;
import com.ktar5.mapeditor.tileset.TilesetManager;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Paths;

public class CompositeTilemap extends BaseTilemap<CompositeTileset> {
    protected CompositeTilemap(File saveFile, JSONObject json) {
        super(saveFile, json);
    }

    protected CompositeTilemap(File saveFile, int width, int height, int tileSize) {
        super(saveFile, width, height, tileSize);
    }

    @Override
    public void deserializeBlock(String block, int x, int y) {
        this.grid[x][y] = new CompositeTile(getTileset(), block);
    }

    @Override
    protected void loadTilesetIfExists(JSONObject json) {
        if (json.has("tileset")) {
            File tileset = Paths.get(getSaveFile().getPath()).resolve(json.getString("tileset")).toFile();
            CompositeTileset tileset1 = TilesetManager.get().loadTileset(tileset, CompositeTileset.class);
            this.setTileset(tileset1);
        }
    }

    @Override
    public void onClick(MouseEvent event) {
        int x = (int) (event.getX() / this.getTileSize());
        int y = (int) (event.getY() / this.getTileSize());

        double xRemainder = (event.getX() / (double) this.getTileSize()) - x;
        double yRemainder = (event.getY() / (double) this.getTileSize()) - y;
        CompositeTile.Corner corner = CompositeTile.Corner.fromRemainders(xRemainder, yRemainder);

        if (event.getButton().equals(MouseButton.PRIMARY)) {
            Node node = event.getPickResult().getIntersectedNode();
            if (node == null || !(node instanceof PixelatedImageView)) {
                set(x, y, corner, 1, 1);
            } else {
                WholeTile wholeTile = (WholeTile) this.grid[x][y];
                wholeTile.setBlockId((wholeTile.getBlockId() + 1) % 45);
                wholeTile.updateImageView();
            }
        } else if (event.getButton().equals(MouseButton.SECONDARY)) {
            remove(x, y);
        } else if (event.getButton().equals(MouseButton.MIDDLE)) {
            Node node = event.getPickResult().getIntersectedNode();
            if (node != null && node instanceof PixelatedImageView) {
                WholeTile wholeTile = (WholeTile) this.grid[x][y];
                wholeTile.setDirection((wholeTile.getDirection() + 1) % 4);
                wholeTile.updateImageView();
            }
        }
    }

    int previousX = -1, previousY = -1;
    CompositeTile.Corner previousCorner = CompositeTile.Corner.DOWN_LEFT;

    @Override
    public void onDrag(MouseEvent event) {
        int x = (int) (event.getX() / this.getTileSize());
        int y = (int) (event.getY() / this.getTileSize());
        if (x >= getWidth() || y >= getHeight() || x < 0 || y < 0) {
            return;
        }

        double xRemainder = (event.getX() / (double) this.getTileSize()) - x;
        double yRemainder = (event.getY() / (double) this.getTileSize()) - y;
        CompositeTile.Corner corner = CompositeTile.Corner.fromRemainders(xRemainder, yRemainder);

        if (x != previousX || y != previousY && corner != previousCorner) {
            previousX = x;
            previousY = y;
            previousCorner = corner;

            if (event.getButton().equals(MouseButton.PRIMARY)) {
                Node node = event.getPickResult().getIntersectedNode();
                if (node == null || !(node instanceof PixelatedImageView)) {
                    set(x, y, corner, 1, 1);
                } else {
                    WholeTile wholeTile = (WholeTile) this.grid[x][y];
                    wholeTile.setBlockId(wholeTile.getBlockId());
                    wholeTile.updateImageView();
                }
            } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                remove(x, y);
            }
        }
    }

    public void draw() {
        Pane pane = Main.root.getCenterView().getEditorViewPane().getTabDrawingPane(getId());
        for (int y = getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x <= getWidth() - 1; x++) {
                if (grid[x][y] == null) {
                    continue;
                }
                int blockId = ((WholeTile) grid[x][y]).getBlockId();
                if (blockId == 0) {
                    continue;
                }
                grid[x][y].draw(pane, x * getTileSize(), y * getTileSize());
            }
        }
    }

    public void set(int x, int y, CompositeTile.Corner corner, int id, int data) {
        CompositeTile tile;
        if (grid[x][y] != null && grid[x][y] instanceof CompositeTile) {
            tile = (CompositeTile) grid[x][y];
            tile.set(corner, id, data);
            tile.updateImageView();
        } else {
            remove(x, y);
            tile = new CompositeTile(getTileset());
            this.grid[x][y] = tile;
            refreshTile(x, y);
        }
    }

    public void set(int x, int y, WholeTile tile) {
        remove(x, y);
        this.grid[x][y] = tile;
        refreshTile(x, y);
    }

    private void refreshTile(int x, int y) {
        this.grid[x][y].updateImageView();
        Pane pane = Main.root.getCenterView().getEditorViewPane().getTabDrawingPane(getId());
        this.grid[x][y].draw(pane, x * getTileSize(), y * getTileSize());
        setChanged(true);
    }

    public void remove(int x, int y) {
        if (grid[x][y] == null) {
            return;
        }
        this.grid[x][y].remove(Main.root.getCenterView().getEditorViewPane().getTabDrawingPane(getId()));
        this.grid[x][y] = null;
        setChanged(true);
    }

}
