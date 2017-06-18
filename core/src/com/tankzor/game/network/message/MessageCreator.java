package com.tankzor.game.network.message;

import org.json.JSONObject;

/**
 * Created by Admin on 6/10/2017.
 */

public class MessageCreator {
    public static final String ID = "id";
    public static final String IS_READY = "ready";
    public static final String NAME = "name";
    public static final String IS_START = "start";
    public static final String ORIENT = "orient";

    public static final byte READY_MESSAGE = 0;
    public static final byte GAME_STATE_MESSAGE = 1;
    public static final byte READY_CONFIRMED_MESSAGE = 2;

    public static final byte WAR_MACHINE_ROTATE_MESSAGE = 3;

    public static String createReadyMessage(String name, boolean isReady) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ID, READY_MESSAGE);
        jsonObject.put(NAME, name);
        jsonObject.put(IS_READY, isReady);
        return jsonObject.toString();
    }

    public static String createConfirmReadyMessage(boolean isReady){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ID, READY_CONFIRMED_MESSAGE);
        jsonObject.put(IS_READY, isReady);
        return jsonObject.toString();
    }

    public static String createGameStateMessage(boolean isStart){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ID, GAME_STATE_MESSAGE);
        jsonObject.put(IS_START, isStart);
        return jsonObject.toString();
    }

    public static String createWarMachineRotateMessage(String name, int orient){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ID, WAR_MACHINE_ROTATE_MESSAGE);
        jsonObject.put(NAME, name);
        jsonObject.put(ORIENT, orient);
        return jsonObject.toString();
    }
}
