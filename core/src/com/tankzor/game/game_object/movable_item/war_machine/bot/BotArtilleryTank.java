package com.tankzor.game.game_object.movable_item.war_machine.bot;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.common_value.WeaponModel;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.OnEntityDestroyedListener;
import com.tankzor.game.game_object.RoundSet;
import com.tankzor.game.game_object.SpawnLocation;
import com.tankzor.game.game_object.animation.OnExplosionFinishListener;
import com.tankzor.game.game_object.manager.AirManager;
import com.tankzor.game.game_object.manager.LightingManager;
import com.tankzor.game.game_object.manager.MapInformationProvider;
import com.tankzor.game.game_object.manager.PathFindingProvider;
import com.tankzor.game.game_object.manager.WarMachineManager;
import com.tankzor.game.game_object.movable_item.OnDynamicDamagedEntityMovingListener;
import com.tankzor.game.game_object.movable_item.war_machine.OnWarMachineFiringListener;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.WarMachineStateListener;
import com.tankzor.game.game_object.movable_item.weapon.AreaWeapon.Artillery;

/**
 * Created by Admin on 12/21/2016.
 */

public class BotArtilleryTank extends BotTank implements OnExplosionFinishListener{
    private boolean canFireArtillery = false;
    private WeaponModel artilleryModel;
    private int artilleryRange;
    private float artillerySpeed;
    private float artilleryAreaSize;
    private float artilleryAreaDx;
    private float artilleryAreaDy;

    public BotArtilleryTank(SpawnLocation spawnLocation,
                            int teamID,
                            int hitPoint,
                            float speed,
                            OnDynamicDamagedEntityMovingListener entityMovingListener,
                            OnEntityDestroyedListener entityDestroyedListener,
                            MapInformationProvider mapInformationProvider,
                            AirManager airManager,
                            LightingManager lightingManager,
                            AssetLoader assetLoader,
                            WarMachineStateListener trackListener,
                            Array<GridPoint2> controlAreas,
                            int range,
                            PathFindingProvider pathFindingProvider,
                            OnWarMachineFiringListener firingListener) {
        super(spawnLocation,
                WarMachine.ARTILLERY_TANK_TYPE,
                teamID,
                hitPoint,
                speed,
                entityMovingListener,
                entityDestroyedListener,
                mapInformationProvider,
                airManager,
                lightingManager,
                assetLoader,
                trackListener,
                controlAreas,
                range,
                pathFindingProvider,
                firingListener);
    }

    public void setArtilleryModel(WeaponModel artilleryModel, int range, float speed) {
        this.artilleryModel = artilleryModel;
        this.artilleryRange = range;
        this.artillerySpeed = speed;
        this.artilleryAreaSize = ITEM_SIZE * ((artilleryModel.explosion == 4)? 5 : 3);
        this.artilleryAreaDx = ITEM_SIZE + ((artilleryModel.explosion == 4)? ITEM_SIZE : 0);
        this.artilleryAreaDy = ITEM_SIZE * 2;
    }

    @Override
    protected void drawDebugBounds(ShapeRenderer shapes) {
        super.drawDebugBounds(shapes);
        Rectangle rectangle = getArtilleryRangeBound(currentImageIndex);
        shapes.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    @Override
    public void act(float delta) {
        if(canFireArtillery){
            if(currentRotateAction == null){
                Artillery artillery = new Artillery(this,
                                                    artilleryModel.damage,
                                                    artilleryModel.explosion,
                                                    artillerySpeed,
                                                    artilleryRange);
                artillery.setOnExplosionFinishListener(this);
                firingListener.onFiredAreaWeapon(artillery);
                canFireArtillery = false;
            }
        }else {
            super.act(delta);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    protected void onMovingActionStop() {
        super.onMovingActionStop();

        if(artilleryModel == null){
            return;
        }

        int fireArtilleryOrient = findOpenFireArtilleryOrient();
        if(fireArtilleryOrient != -1){
            addAction(new WarMachineRotateAction(fireArtilleryOrient));
            if(WarMachineManager.isEnemiesFrenzy && teamID == ENEMIES_TEAM){
                canFireArtillery = true;
            }
        }else {
            int fireOrient = findOpenFireOrient();
            if (fireOrient != -1) {
                addAction(new WarMachineRotateAction(fireOrient));
                canFireBullet = true;
                isMoving = true;
            }
        }
    }

    @Override
    public void onExplosionFinish() {
        onMovingActionStop();
    }

    private Rectangle getArtilleryRangeBound(int orient){
        switch (orient){
            case UP_ORIENT:{
                return new Rectangle(getX() - artilleryAreaDx,
                                    getY() + artilleryAreaDy + ITEM_SIZE,
                                    artilleryAreaSize,
                                    artilleryAreaSize);
            }

            case DOWN_ORIENT:{
                return new Rectangle(getX() - artilleryAreaDx,
                                    getY() - artilleryAreaDy - artilleryAreaSize,
                                    artilleryAreaSize,
                                    artilleryAreaSize);
            }

            case LEFT_ORIENT:{
                return new Rectangle(getX() - artilleryAreaDy - artilleryAreaSize,
                                    getY() - artilleryAreaDx,
                                    artilleryAreaSize,
                                    artilleryAreaSize);
            }

            case RIGHT_ORIENT:{
                return new Rectangle(getX() + artilleryAreaDy + ITEM_SIZE,
                                    getY() - artilleryAreaDx,
                                    artilleryAreaSize,
                                    artilleryAreaSize);
            }

            default:{
                return null;
            }
        }
    }

    private int findOpenFireArtilleryOrient(){
        if(currentAttackTarget == null){
            return -1;
        }
        float dXTarget = currentAttackTarget.getX() - getX();
        float dYTarget = currentAttackTarget.getY() - getY();
        if ((-GameEntity.ITEM_SIZE < dXTarget && dXTarget < GameEntity.ITEM_SIZE && dYTarget > 0) &&
                isTargetWithinArtilleryRange(UP_ORIENT)) {
            return UP_ORIENT;
        } else if ((-GameEntity.ITEM_SIZE < dXTarget && dXTarget < GameEntity.ITEM_SIZE && dYTarget < 0) &&
                isTargetWithinArtilleryRange(DOWN_ORIENT)) {
            return DOWN_ORIENT;
        } else if (dXTarget > 0 && -GameEntity.ITEM_SIZE < dYTarget && dYTarget < GameEntity.ITEM_SIZE &&
                isTargetWithinArtilleryRange(RIGHT_ORIENT)) {
            return RIGHT_ORIENT;
        } else if (dXTarget < 0 && -GameEntity.ITEM_SIZE < dYTarget && dYTarget < GameEntity.ITEM_SIZE &&
                isTargetWithinArtilleryRange(LEFT_ORIENT)) {
            return LEFT_ORIENT;
        }
        return -1;
    }

    private boolean isTargetWithinArtilleryRange(int orient){
        return currentAttackTarget != null && getArtilleryRangeBound(orient).overlaps(currentAttackTarget.getBound());
    }
}
