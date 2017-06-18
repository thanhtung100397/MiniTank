package com.tankzor.game.network;

import com.badlogic.gdx.utils.ObjectMap;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.tankzor.game.game_object.manager.WarMachineManager;
import com.tankzor.game.game_object.movable_item.war_machine.WarMachine;
import com.tankzor.game.game_object.movable_item.war_machine.bot.Turret;
import com.tankzor.game.game_object.movable_item.war_machine.movable_machine.MovableWarMachine;
import com.tankzor.game.network.message.MessageCreator;

/**
 * Created by Admin on 6/16/2017.
 */

public class Network {
    private static Network instance;
    public GameStateMessenger messenger;
    private ObjectMap<String, WarMachine> mapWarMachines;
    private WarMachineManager warMachineManager;
    private WarpClient warpClient;

    public static Network getInstance() {
        return instance;
    }

    public static void init(boolean isOnline, WarMachineManager warMachineManager) {
        Network.instance = new Network(isOnline, warMachineManager);
    }

    public void newWarMachineCreated(WarMachine warMachine){
        mapWarMachines.put(warMachine.getId(), warMachine);
        if (warMachine instanceof MovableWarMachine){
            warMachineManager.addMovableMachine((MovableWarMachine) warMachine);
        }else {
            warMachineManager.addTurret((Turret) warMachine);
        }
    }

    public void onWarMachineDestroyed(String key){
        WarMachine warMachine = mapWarMachines.remove(key);
        warMachine.destroy();
    }

    public WarMachine getWarMachineByID(String id){
        return mapWarMachines.get(id);
    }

    public void rotateWarMachine(String name, int orient){
        WarMachine warMachine = getWarMachineByID(name);
        warMachine.setNextOrient(orient);
    }

    private Network(boolean isOnline, WarMachineManager warMachineManager) {
        this.mapWarMachines = new ObjectMap<String, WarMachine>(4);
        this.warMachineManager = warMachineManager;
        if(isOnline){
            try {
                warpClient = WarpClient.getInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            messenger = new GameStateMessenger() {
                @Override
                public void onWarMachineRotate(String name, int orient) {
                    warpClient.sendUpdatePeers(MessageCreator.createWarMachineRotateMessage(name, orient).getBytes());
                }
            };
        }else {
            messenger = new GameStateMessenger() {
                @Override
                public void onWarMachineRotate(String name, int orient) {

                }
            };
        }
    }
}
