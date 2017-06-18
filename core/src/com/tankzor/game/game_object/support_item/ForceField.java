package com.tankzor.game.game_object.support_item;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.tankzor.game.common_value.GameSounds;
import com.tankzor.game.game_object.DamagedEntity;
import com.tankzor.game.game_object.GameEntity;
import com.tankzor.game.game_object.decorations.Ring;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.PlayerWarMachine;
import com.tankzor.game.gamehud.Pane;
import com.tankzor.game.gamehud.PaneTable;

/**
 * Created by Admin on 1/3/2017.
 */

public class ForceField {
    public static final float FASTEST_RECOVER_TIME = 0.7f;
    public static final int MAX_FORCE_FIELD_PANE_PER_ROW = 10;

    private int maximumNumber;
    private int forceField = 0;
    private float baseRecoverTime;
    private float trueRecoverTime;
    private float currentRecoverTime = 0;
    private WarMachine owner;
    private PaneTable forceFieldPaneTable;
    private Ring ring;

    public ForceField(WarMachine warMachine, float baseRecoverTime, int maximumNumber){
        this.owner = warMachine;
        this.baseRecoverTime = baseRecoverTime;
        this.maximumNumber = maximumNumber;
        this.forceFieldPaneTable = new PaneTable(Pane.BLUE_PANE,
                                                maximumNumber,
                                                Pane.PANE_LOST,
                                                MAX_FORCE_FIELD_PANE_PER_ROW,
                                                warMachine.getAssetLoader());
        reCalculateCurrentRecoverTime();

        if(warMachine instanceof PlayerWarMachine) {
            ring = new Ring(Ring.PLAYER_RING, warMachine.getAssetLoader());
        }else if(warMachine.getTeamID() == DamagedEntity.ENEMIES_TEAM){
            ring = new Ring(Ring.ENEMY_RING, warMachine.getAssetLoader());
        }else {
            ring = new Ring(Ring.ALLY_RING, warMachine.getAssetLoader());
        }
    }

    public PaneTable getForceFieldPaneTable() {
        return forceFieldPaneTable;
    }

    public void reset(){
        maximumNumber = 0;
        forceField = 0;
        forceFieldPaneTable.reset();
        ring.setNormalMode();
    }

    public void addMaximumNumber(int maximumNumber) {
        this.maximumNumber += maximumNumber;
        forceFieldPaneTable.addPane(maximumNumber);
        if(maximumNumber > 0){
            ring.setForceFieldMode();
        }
    }

    public void setBaseRecoverTime(float baseRecoverTime) {
        this.baseRecoverTime = baseRecoverTime;
        reCalculateCurrentRecoverTime();
    }

    public int getMaximumNumber() {
        return maximumNumber;
    }

    public int getCurrentForceField() {
        return forceField;
    }

    /**
     * deal damage for this force field.
     * @param damage the taken damage
     * @return the damage left after dealing
     */
    public int takeDamage(int damage){
        int resultDamage = -forceFieldPaneTable.setPane(-damage);
        if(resultDamage < damage) {
            this.forceField -= damage - resultDamage;
            this.currentRecoverTime = 0;
            reCalculateCurrentRecoverTime();
            ring.changeRingImage();
            GameSounds.getInstance().playSFX(GameSounds.FORCE_FIELD_HIT_SFX_ID);
        }
        return resultDamage;
    }

    public void act(float delta){
        ring.act(delta);
        if(forceField == maximumNumber){
            return;
        }
        if(currentRecoverTime < trueRecoverTime){
            currentRecoverTime += delta;
        }else {
            forceField++;
            forceFieldPaneTable.setPane(1);
            currentRecoverTime = 0;
            reCalculateCurrentRecoverTime();
        }
    }

    public void draw(Batch batch) {
        ring.draw(batch, owner.getX(), owner.getY());
    }

    private void reCalculateCurrentRecoverTime() {
        trueRecoverTime = baseRecoverTime - forceField * 0.1f + (1 - owner.getHitPoint() / (1.0f * owner.getMaxHitPoint()));
        if(trueRecoverTime < FASTEST_RECOVER_TIME){
            trueRecoverTime = FASTEST_RECOVER_TIME;
        }
    }

    public Ring getRing() {
        return ring;
    }
}
