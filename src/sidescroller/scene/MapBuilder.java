package sidescroller.scene;

import javafx.scene.canvas.Canvas;
import sidescroller.entity.GenericEntity;
import sidescroller.entity.property.Entity;
import sidescroller.entity.property.HitBox;
import sidescroller.entity.sprite.*;
import sidescroller.entity.sprite.tile.Tile;
import utility.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class MapBuilder implements MapBuilderInterface {

    private Tuple rowColCount;
    private Tuple dimension;
    private double scale;
    private Canvas canvas;
    private Entity background;
    private List<Entity> landMass;
    private List<Entity> other;


    protected MapBuilder(){
        this.landMass = new ArrayList<>();
        this.other = new ArrayList<>();
    }

    public static MapBuilder createBuilder(){
        return new MapBuilder();
    }

    @Override
    public MapBuilderInterface setCanvas(Canvas canvas) {
        this.canvas = canvas;
        return this;
    }

    @Override
    public MapBuilderInterface setGrid(Tuple rowColCount, Tuple dimension) {
        this.rowColCount = Tuple.pair(rowColCount.x(), rowColCount.y());
        this.dimension = Tuple.pair(dimension.x(), dimension.y());
        return this;
    }

    @Override
    public MapBuilderInterface setGridScale(double scale) {
        this.scale = scale;
        return this;
    }

    @Override
    public MapBuilderInterface buildBackground(BiFunction<Integer, Integer, Tile> callback) {
        BackgroundSprite bs = SpriteFactory.get("Background");
        bs.init(scale, dimension, Tuple.pair(0,0));
        bs.createSnapshot(canvas,rowColCount, callback);
        HitBox hitBox = HitBox.build(0, 0, scale * dimension.x() * rowColCount.y(), scale * dimension.y() * rowColCount.x());
        background = new GenericEntity(bs, hitBox);
        return this;
    }

    @Override
    public MapBuilderInterface buildLandMass(int rowPos, int colPos, int rowConut, int colCount) {
        LandSprite ls = SpriteFactory.get("Land");
        ls.init(scale, dimension, Tuple.pair(colPos,rowPos));
        ls.createSnapshot(canvas,rowConut, colCount);
        HitBox hitBox = HitBox.build(colPos * dimension.x() * scale, rowPos * dimension.y() * scale, scale * dimension.x() * colCount, scale * dimension.y() * rowConut);
        landMass.add( new GenericEntity(ls, hitBox));
        return this;
    }

    @Override
    public MapBuilderInterface buildTree(int rowPos, int colPos, Tile tile) {
        TreeSprite ts = SpriteFactory.get("Tree");
        ts.init(scale, dimension, Tuple.pair(colPos,rowPos));
        ts.createSnapshot(canvas,tile);
        other.add(new GenericEntity(ts, null));
        return this;
    }

    @Override
    public MapBuilderInterface buildPlatform(int rowPos, int colPos, int length, Tile tile) {
        PlatformSprite ps = SpriteFactory.get("Platform");
        ps.init(scale,dimension, Tuple.pair(colPos, rowPos));
        ps.createSnapshot(canvas, tile,length);
        HitBox hitBox = HitBox.build((colPos + .5) * dimension.x() * scale, rowPos * dimension.y() * scale, scale * dimension.x() * (length - 1), scale * dimension.y() / 2);
        other.add(new GenericEntity(ps, hitBox));
        return this;
    }

    @Override
    public Entity getBackground() {
        return this.background;
    }

    @Override
    public List<Entity> getEntities(List<Entity> list) throws NullPointerException {
        list.addAll(landMass);
        list.addAll(other);
        return list;
    }
}
