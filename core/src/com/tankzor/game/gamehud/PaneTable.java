package com.tankzor.game.gamehud;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.SnapshotArray;
import com.tankzor.game.common_value.AssetLoader;

/**
 * Created by Admin on 5/8/2017.
 */

public class PaneTable extends VerticalGroup {
    private AssetLoader assetLoader;
    private int type;
    private int state;
    private int currentRowIndex;
    private int maxPanePerRow;

    public PaneTable(int type, int number, int state, int maxPanePerRow, AssetLoader assetLoader) {
        this.type = type;
        this.state = state;
        this.maxPanePerRow = maxPanePerRow;
        this.assetLoader = assetLoader;
        this.currentRowIndex = 0;
        addActor(new PaneRow(type, state, maxPanePerRow, assetLoader));
        addPane(number);
        fill();
    }

    public void addPane(int number) {
        PaneRow addedRow = getLastRow();
        while (true) {
            number = addedRow.addPane(number);
            if (number > 0) {
                addedRow = new PaneRow(type, state, maxPanePerRow, assetLoader);
                addActor(addedRow);
                if(state == Pane.PANE_NORMAL) {
                    currentRowIndex++;
                }
            } else {
                break;
            }
        }
    }

    /**
     * Enable/Disable a number of panes of this table.
     * @param delta the number of panes will be enabled (delta > 0) or disabled (delta < 0)
     * @return number of panes left which can not be enabled/disabled because all panes of this table is enabled/disabled
     */
    public int setPane(int delta) {
        PaneRow currentRow = getRow(currentRowIndex);
        while (true) {
            delta = currentRow.setPane(delta);
            if(delta == 0){
                break;
            }else if(delta > 0){
                if(currentRowIndex < getChildren().size - 1){
                    currentRow = getRow(++currentRowIndex);
                }else {
                    break;
                }
            } else {
                if(currentRowIndex > 0){
                    currentRow = getRow(--currentRowIndex);
                }else {
                    break;
                }
            }
        }
        return delta;
    }

    public void reset(){
        clear();
        currentRowIndex = 0;
        addActor(new PaneRow(type, state, maxPanePerRow, assetLoader));
    }

    private PaneRow getLastRow() {
        SnapshotArray<Actor> children = getChildren();
        return (PaneRow) children.get(children.size - 1);
    }

    private PaneRow getRow(int index) {
        return (PaneRow) getChildren().get(index);
    }


}
