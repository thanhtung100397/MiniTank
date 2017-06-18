package com.tankzor.game.game_object.manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.WarMachineStateListener;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Dynamite;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Mine;
import com.tankzor.game.util.QuadRectangle;
import com.tankzor.game.util.QuadTree;

/**
 * Created by Admin on 2/7/2017.
 */

public class GroundWeaponManager extends Stage {
    private QuadTree<Mine> minesQuadTree;
//    private ShapeRenderer shapeRenderer;

    public GroundWeaponManager(Viewport viewport, SpriteBatch batch) {
        super(viewport, batch);
//        getRoot().setCullingArea(((PlayerCamera) viewport.getCamera()).getCullingArea());
//        shapeRenderer = new ShapeRenderer();
//        shapeRenderer.setAutoShapeType(true);
    }

    public void setMapSize(float widthMap, float heightMap) {
        minesQuadTree = new QuadTree<Mine>(new QuadRectangle(0, 0, widthMap, heightMap), 0);
    }

    @Override
    public void draw() {
//        Array<QuadTree<Mine>> listNode = new Array<QuadTree<Mine>>();
//        minesQuadTree.getAllTrees(listNode);
//        shapeRenderer.setProjectionMatrix(getCamera().combined);
//        shapeRenderer.begin();
//        for (QuadTree<Mine> z : listNode) {
//            QuadRectangle rectangle = z.getZone();
//            shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
//            SpriteBatch spriteBatch = (SpriteBatch) getBatch();
//            spriteBatch.begin();
//            GameImages.getInstance().getGameFont().draw(spriteBatch, z.nodes.size+"", rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
//            spriteBatch.end();
//        }
//        shapeRenderer.end();
        super.draw();
    }

    public void addDynamite(Dynamite dynamite) {
        addActor(dynamite);
    }

    public void addMine(Mine mine) {
        if (!mine.isVisible()) {
            minesQuadTree.insert(mine.getQuadBound(), mine);
        }
        addActor(mine);
    }

    public void scanMines(QuadRectangle scanBound) {
        Array<Mine> listMinesDetected = new Array<Mine>();
        minesQuadTree.findAllElementOverlapThenRemove(listMinesDetected, scanBound);
        for (int i = 0; i < listMinesDetected.size; i++) {
            listMinesDetected.get(i).setVisible(true);
        }
    }
}
