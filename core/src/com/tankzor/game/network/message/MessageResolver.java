package com.tankzor.game.network.message;

import org.json.JSONObject;

/**
 * Created by Admin on 6/10/2017.
 */

public abstract class MessageResolver {

    public void resolveMessage(String message){
        JSONObject jsonObject = new JSONObject(message);
        onMessageReceived((Integer) jsonObject.get(MessageCreator.ID), jsonObject);
    }

    protected abstract void onMessageReceived(int messageId, JSONObject jsonObject);
}
