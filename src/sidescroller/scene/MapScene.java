package sidescroller.scene;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.Canvas;
import sidescroller.animator.AnimatorInterface;
import sidescroller.entity.property.Entity;
import sidescroller.entity.property.HitBox;
import sidescroller.entity.sprite.TreeSprite;
import sidescroller.entity.sprite.tile.*;
import utility.Tuple;

import java.util.ArrayList;
import java.util.List;

public class MapScene implements MapSceneInterface {

    private Tuple count;
    private Tuple size;
    private double scale;
    private AnimatorInterface animator;
    private List<Entity> players;
    private List<Entity> staticShapes;
    private BooleanProperty drawBounds;
    private BooleanProperty drawFPS;
    private BooleanProperty drawGrid;
    private Entity background;

    public MapScene(){
        this.players = new ArrayList<>();
        this.staticShapes = new ArrayList<>();
        this.drawBounds = new SimpleBooleanProperty();
        this.drawFPS = new SimpleBooleanProperty();
        this.drawGrid = new SimpleBooleanProperty();
    }

    @Override
    public BooleanProperty drawFPSProperty() {
        return this.drawFPS;
    }

    @Override
    public boolean getDrawFPS() {
        return drawFPSProperty().get();
    }

    @Override
    public BooleanProperty drawBoundsProperty() {
        return this.drawBounds;
    }

    @Override
    public boolean getDrawBounds() {
        return drawBoundsProperty().get();
    }

    @Override
    public BooleanProperty drawGridProperty() {
        return this.drawGrid;
    }

    @Override
    public boolean getDrawGrid() {
        return drawGridProperty().get();
    }

    @Override
    public MapSceneInterface setRowAndCol(Tuple count, Tuple size, double scale) {
        this.count = count;
        this.size= size;
        this.scale = scale;
        return this;
    }

    @Override
    public Tuple getGridCount() {
        return this.count;
    }

    @Override
    public Tuple getGridSize() {
        return this.size;
    }

    @Override
    public double getScale() {
        return this.scale;
    }

    @Override
    public MapSceneInterface setAnimator(AnimatorInterface newAnimator) {
        if(this.animator!=null)
            animator.stop();
        this.animator = newAnimator;
        return this;
    }

    @Override
    public Entity getBackground() {
        return this.background;
    }

    @Override
    public void start() {
        if(this.animator!=null)
            animator.start();
    }

    @Override
    public void stop() {
        if(this.animator!=null)
            animator.stop();
    }

    @Override
    public List<Entity> staticShapes() {
        return staticShapes;
    }

    @Override
    public List<Entity> players() {
        return this.players;
    }

    @Override
    public MapSceneInterface createScene(Canvas canvas) {
        MapBuilder mb = MapBuilder.createBuilder();
        mb.setCanvas(canvas).setGrid(getGridCount(), getGridSize()).setGridScale(getScale());
        mb.buildBackground((x,y)-> {
            if(x<4 && (y+ (int)(Math.random() * 3)) % 5 == 0)
                return BackgroundTile.MORNING_CLOUD;
            if(x>13 && y>14)
                return BackgroundTile.WATER_SURFACE;
            return BackgroundTile.MORNING;
        });

        //landmasses
        mb.buildLandMass(13,5,3,10);
        mb.buildLandMass(9,0,7,5);
        mb.buildLandMass(10,30,5,7);

        //boxes
        mb.buildTree(12,5, ItemTile.BOX);
        mb.buildTree(12,6, ItemTile.BOX);
        mb.buildTree(12,7, ItemTile.BOX);
        mb.buildTree(11,5, ItemTile.BOX);
        mb.buildTree(11,6, ItemTile.BOX);

        //sunflowers
        mb.buildTree(7,0, FloraTile.SUNFLOWER_LONG);
        mb.buildTree(7,1, FloraTile.SUNFLOWER_SHORT);
        mb.buildTree(7,2, FloraTile.SUNFLOWER_LONG);
        mb.buildTree(7,3, FloraTile.SUNFLOWER_SHORT);

        //flowers
        mb.buildTree(12,8, FloraTile.FLOWER_PINK);
        mb.buildTree(12,9, FloraTile.FLOWER_PINK);
        mb.buildTree(12,12, FloraTile.FLOWER_PINK);
        mb.buildTree(12,13, FloraTile.FLOWER_PINK);
        mb.buildTree(12,14, FloraTile.FLOWER_PINK);
        mb.buildTree(9,30, FloraTile.FLOWER_YELLOW);
        mb.buildTree(9,31, FloraTile.FLOWER_YELLOW);
        mb.buildTree(9,32, FloraTile.FLOWER_YELLOW);
        mb.buildTree(9,33, FloraTile.FLOWER_YELLOW);

        //tree
        mb.buildTree(6, 8, FloraTile.TREE);
        mb.buildTree(3, 32, FloraTile.TREE);

        //sign
        mb.buildTree(10, 20, ItemTile.SIGN);

        //grass
        mb.buildPlatform(11,17,5, LandTile.GRASS);
        mb.buildPlatform(8,24,4, LandTile.GRASS);

        //dirt
        mb.buildPlatform(12,17,5, LandTile.DIRT_BOTTOM);
        mb.buildPlatform(9,24,4, LandTile.DIRT_BOTTOM);

        //other grass
        mb.buildTree(7,24,FloraTile.GRASS_FULL);
        mb.buildTree(7,25,FloraTile.GRASS_FULL);
        mb.buildTree(7,26,FloraTile.GRASS_FULL);
        mb.buildTree(7,27,FloraTile.GRASS_FULL);
        mb.buildTree(10,17,FloraTile.GRASS_FULL);
        mb.buildTree(10,18,FloraTile.GRASS_FULL);
        mb.buildTree(10,19,FloraTile.GRASS_FULL);
        mb.buildTree(10,21,FloraTile.GRASS_FULL);

        background = mb.getBackground();
        mb.getEntities(staticShapes);
        return this;
    }

    @Override
    public boolean inMap(HitBox hitbox) {
        return getBackground().getHitBox().containsBounds(hitbox);
    }
}
