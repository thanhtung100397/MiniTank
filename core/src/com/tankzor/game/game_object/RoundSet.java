package com.tankzor.game.game_object;

import com.badlogic.gdx.utils.Array;
import com.tankzor.game.util.LinkedList;

/**
 * Created by Admin on 12/30/2016.
 */

public class RoundSet {
    private LinkedList<Round> listRounds;
    private Array<ConditionRound> listConditionRounds;

    public RoundSet() {
        listRounds = new LinkedList<Round>();
        listConditionRounds = new Array<ConditionRound>();
    }

    public void addRound(Round round) {
        if (round.size() == 0) {
            return;
        }
        round.setParent(this);
        listRounds.add(round);
    }

    public void removeFirstRound() {
        Round round = listRounds.removeFirst();
        round.setParent(null);
        activeFirstRound();
    }

    public void addConditionRound(ConditionRound conditionRound) {
        if (conditionRound.size() == 0) {
            return;
        }
        conditionRound.setParent(this);
        listConditionRounds.add(conditionRound);
    }

    public void removeConditionRound(ConditionRound conditionRound) {
        listConditionRounds.removeValue(conditionRound, true);
        conditionRound.setParent(null);
    }

    public void activeFirstRound() {
        if (listRounds.size() == 0) {
            return;
        }
        listRounds.getFirst().spawnWarMachine();
    }

    public void clear() {
        listRounds.clear();
        listConditionRounds.clear();
    }

    public int getTotalWarMachine() {
        int count = 0;
        LinkedList.Node<Round> node = listRounds.getFirstNode();
        while (node != null){
            count += node.data.getTotalWarMachineCount();
            node = node.getNext();
        }

        for (ConditionRound conditionRound : listConditionRounds) {
            count += conditionRound.getTotalWarMachineCount();
        }
        return count;
    }
}
