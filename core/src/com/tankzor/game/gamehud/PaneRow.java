package com.tankzor.game.gamehud;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.utils.Array;
import com.tankzor.game.common_value.AssetLoader;

/**
 * Created by Admin on 1/24/2017.
 */

public class PaneRow extends HorizontalGroup {
    private Array<Pane> listPane;
    private int currentPointer;
    private int type;
    private int state;
    private int maxPane;
    private AssetLoader assetLoader;

    public PaneRow(int type, int state, int maxPane, AssetLoader assetLoader) {
        this.type = type;
        this.state = state;
        this.maxPane = maxPane;
        this.assetLoader = assetLoader;
        listPane = new Array<Pane>(15);
        currentPointer = -1;
    }

    /**
     * Add a number of panes to this row.
     * @param number the number of panes will be added
     * @return number of panes left which can not be added because this row is full
     */
    public int addPane(int number) {
        while (!isFullOfPane() && number > 0) {
            addPane(new Pane(type, state, assetLoader));
            number--;
        }
        return number;
    }

    public boolean isFullOfPane(){
        return listPane.size == maxPane;
    }

    public void reset() {
        clear();
        listPane.clear();
        currentPointer = -1;
    }

    private void addPane(Pane pane) {
        if (state == Pane.PANE_NORMAL) {
            addActorAt(0, pane);
            listPane.insert(0, pane);
            currentPointer++;
        } else {
            addActor(pane);
            listPane.add(pane);
        }
    }

    /**
     * Enable/Disable a number of panes of this row.
     * @param delta the number of panes will be enabled (delta > 0) or disabled (delta < 0)
     * @return number of panes left which can not be enabled/disabled because all panes of this row is enabled/disabled
     */
    public int setPane(int delta) {
        if (delta == 0) {
            return 0;
        }
        if (delta > 0) {
            while (delta != 0 && currentPointer < listPane.size - 1) {
                listPane.get(++currentPointer).setState(Pane.PANE_NORMAL);
                delta--;
            }
        } else {
            while (delta != 0 && currentPointer >= 0) {
                listPane.get(currentPointer--).setState(Pane.PANE_LOST);
                delta++;
            }
        }
        return delta;
    }
}
