package com.tankzor.game.game_object.movable_item.weapon.AreaWeapon;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tankzor.game.game_object.light.ItemPointLight;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;

/**
 * Created by Admin on 11/29/2016.
 */

public class Artillery extends AreaWeapon {
    public static final float BASE_TIME_TO_PASS_A_TERRAIN_BOX = 0.8f;
    public static final float BASE_NODE_DELAY_TIME = 0.15f;
    public static final float LIGHT_RADIUS = 150.0f;

    private float timeToPassATerrainBox;
    private float nodeDelayTime;
    private int range;
    private Node[] nodeLocations;
    private ItemPointLight light;

    public Artillery(WarMachine warMachine,
                     int damage,
                     int level,
                     float timeToPassATerrainBox,
                     int range) {
        super(warMachine, damage, level);
        setTimeToPassATerrainBox(timeToPassATerrainBox);
        this.range = range;
        images = warMachine.getAssetLoader().getArtilleryImages();

        createAction();

        initBody();
        if(lightingManager.getDayMode() != LightingManager.DAY_MODE) {
            light = lightingManager.createItemPointLight(getX(), getY(), ITEM_SIZE / 2, ITEM_SIZE / 2, LIGHT_RADIUS, null, true);
        }
    }

    public void setTimeToPassATerrainBox(float timeToPassATerrainBox) {
        this.timeToPassATerrainBox = timeToPassATerrainBox;
        this.nodeDelayTime = timeToPassATerrainBox / BASE_TIME_TO_PASS_A_TERRAIN_BOX * BASE_NODE_DELAY_TIME;
    }

    @Override
    protected void createAction() {
        addAction(new ArtilleryMovingAction(warMachine));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(light != null) {
            light.updatePosition(nodeLocations[3].x, nodeLocations[3].y);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (int i = 0; i < 5; i++) {
            batch.draw(images[i], nodeLocations[i].x, nodeLocations[i].y, ITEM_SIZE, ITEM_SIZE);
        }
    }

    private class ArtilleryMovingAction extends Action {
        public static final int VERTICAL_DIRECTION = 0;
        public static final int HORIZONTAL_DIRECTION = 1;
        private float s;
        private float h;
        private float direction;
        private float v;
        private float xStart, yStart;
        private float totalTime;

        private ArtilleryMovingAction(WarMachine warMachine) {
            switch (warMachine.getCurrentOrient()) {
                case WarMachine.UP_ORIENT: {
                    s = ITEM_SIZE * range;
                    h = -(ITEM_SIZE - 30) * range / 4;
                    totalTime = timeToPassATerrainBox * 4;
                    direction = VERTICAL_DIRECTION;
                }
                break;

                case WarMachine.DOWN_ORIENT: {
                    s = -ITEM_SIZE * range;
                    h = -(ITEM_SIZE - 30) * range / 4;
                    totalTime = timeToPassATerrainBox * 4;
                    direction = VERTICAL_DIRECTION;
                }
                break;

                case WarMachine.LEFT_ORIENT: {
                    s = -ITEM_SIZE * range;
                    h = ITEM_SIZE * range / 2;
                    totalTime = timeToPassATerrainBox * 4;
                    direction = HORIZONTAL_DIRECTION;
                }
                break;

                case WarMachine.RIGHT_ORIENT: {
                    s = ITEM_SIZE * range;
                    h = ITEM_SIZE * range / 2;
                    totalTime = timeToPassATerrainBox * 4;
                    direction = HORIZONTAL_DIRECTION;
                }
                break;

                default: {
                    break;
                }
            }
            v = s / totalTime;

            nodeLocations = new Node[5];
            nodeLocations[0] = new Node(totalTime, 3 * nodeDelayTime);
            nodeLocations[1] = new Node(totalTime, 2 * nodeDelayTime);
            nodeLocations[2] = new Node(totalTime, nodeDelayTime);
            nodeLocations[3] = new Node(totalTime, 0);
            nodeLocations[4] = new Node(totalTime, 0);
        }

        @Override
        public void setTarget(Actor target) {
            super.setTarget(target);
            xStart = target.getX();
            yStart = target.getY();
        }

        @Override
        public boolean act(float delta) {
            if(nodeLocations[0].currentTime <= nodeLocations[0].endTime) {
                if (direction == HORIZONTAL_DIRECTION) {
                    for (int i = 0; i < 4; i++) {
                        calculateNodeLocationByHorizontalDirection(nodeLocations[i], delta);
                    }
                    nodeLocations[4].x = nodeLocations[3].x;
                    nodeLocations[4].y = yStart;
                } else {
                    for (int i = 0; i < 4; i++) {
                        calculateNodeLocationByVerticalDirection(nodeLocations[i], delta);
                    }
                    nodeLocations[4].x = xStart;
                    nodeLocations[4].y = nodeLocations[3].y;
                }
                return false;
            }
            destroy();
            return true;
        }

        private void calculateNodeLocationByHorizontalDirection(Node node, float deltaTime){
            if(node.currentTime >= node.endTime){
                return;
            }
            float x = calculateHorizontal(node.currentTime - node.delayTime);
            node.x = xStart + x;
            node.y = yStart + calculateVertical(x);
            node.currentTime += deltaTime;
        }

        private void calculateNodeLocationByVerticalDirection(Node node, float deltaTime){
            if(node.currentTime >= node.endTime){
                return;
            }
            float y = calculateHorizontal(node.currentTime - node.delayTime);
            node.y = yStart + y;
            node.x = xStart + calculateVertical(y);
            node.currentTime += deltaTime;
        }

        private float calculateHorizontal(float time) {
            if(time <= 0){
                return 0;
            }
            return v * time;
        }

        private float calculateVertical(float horizontalValue) {
            return -((4 * h) / (s * s)) * horizontalValue * horizontalValue + (4 * h / s) * horizontalValue;
        }
    }

    private class Node {
        float x;
        float y;
        float currentTime;
        final float endTime;
        final float delayTime;

        Node(float standardTime, float delayTime){
            this.x = 0.0f;
            this.y = 0.0f;
            this.currentTime = 0;
            this.delayTime = delayTime;
            this.endTime = standardTime + delayTime;
        }
    }

    @Override
    public void destroy() {
        setX(nodeLocations[3].x);
        setY(nodeLocations[3].y);
        if(light != null) {
            light.remove();
        }
        super.destroy();
    }

    @Override
    public int getDamageByLevel(int level) {
        return hitPoint - level;
    }
}
