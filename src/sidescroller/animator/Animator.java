package sidescroller.animator;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sidescroller.entity.player.Player;
import sidescroller.entity.property.Entity;
import sidescroller.entity.property.HitBox;
import java.util.Iterator;
import java.util.function.Consumer;

public class Animator extends AbstractAnimator {

    private Color background = Color.ANTIQUEWHITE;

    @Override
    public void drawEntities(GraphicsContext gc) {
        Consumer<Entity> draw = e -> {
            if(e!=null && e.isDrawable()){
                e.getDrawable().draw(gc);
                if(map.getDrawBounds() && e.hasHitbox())
                    e.getHitBox().getDrawable().draw(gc);
            }
        };
        draw.accept(map.getBackground());
        map.staticShapes().forEach( entity -> draw.accept(entity));
        map.players().forEach( player -> draw.accept(player));
    }

    @Override
    public void updateEntities() {
        map.players().forEach(entity -> entity.update());
        map.staticShapes().forEach(entity -> entity.update());
        if(map.getDrawBounds())
            map.players().forEach(entity -> entity.getHitBox().getDrawable().setStroke(Color.RED));
        map.staticShapes().forEach(entity -> proccessEntityList(map.players().iterator(), entity.getHitBox()));
    }

    @Override
    public void proccessEntityList(Iterator<Entity> iterator, HitBox shapeHitBox) {
        while(iterator.hasNext()){
            Entity entity = iterator.next();
            HitBox bounds = entity.getHitBox();
            if(!map.inMap(bounds))
                updateEntity(entity, iterator);
            else if(shapeHitBox != null && bounds.intersectBounds(shapeHitBox)){
                if(map.getDrawBounds()){
                    bounds.getDrawable().setStroke(Color.BLUEVIOLET);
                }
                updateEntity(entity, iterator);
            }
        }
    }

    @Override
    public void updateEntity(Entity entity, Iterator<Entity> iterator) {
        if(entity instanceof Player)
            ((Player) entity).stepBack();
    }

    @Override
    public void handle(GraphicsContext gc, long now){
        updateEntities();
        clearAndFill(gc, background);
        drawEntities(gc);
    }
}
