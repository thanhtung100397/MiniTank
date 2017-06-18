package com.tankzor.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.AllRoomsEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LobbyEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.dialog.CreateNewRoomDialog;
import com.tankzor.game.dialog.DialogManager;
import com.tankzor.game.dialog.EnterNameDialog;
import com.tankzor.game.dialog.LoadingDialog;
import com.tankzor.game.dialog.MessageDialog;
import com.tankzor.game.dialog.RetryDialog;
import com.tankzor.game.main.Tankzor;
import com.tankzor.game.network.ConnectionRequestAdapter;
import com.tankzor.game.network.LobbyRequestAdapter;
import com.tankzor.game.network.NotifyAdapter;
import com.tankzor.game.network.RoomRequestAdapter;
import com.tankzor.game.network.ZoneRequestAdapter;

import java.util.Arrays;

/**
 * Created by Admin on 6/7/2017.
 */

public class ListRoomScreen extends BaseScreen {
    public static final String APP_WARP_API_KEY = "e2195f9fb5fe0bae7e96d4be4a308dc10b8ef2b4b6022226d22c45c1bb28294d";
    public static final String APP_WARP_PRIVATE_KEY = "b78c64e6293d8ff847ec8adbc16581ae574941d116d83a543c234b39313a539e";

    private Stage screenStage;
    private Group rootGroup;
    private TextButton createRoomButton;
    private Label playerNameLabel;
    private VerticalGroup listRoom;
    private Image background;
    private Image menuBackground;
    private TextButton.TextButtonStyle roomItemStyle;
    private boolean isConnectionSuccessful;
    private LoadingDialog loadingDialog;

    public ListRoomScreen(Tankzor parent,
                          Viewport viewport,
                          SpriteBatch batch,
                          InputMultiplexer gameMultiplexer) {
        super(parent, viewport, batch, gameMultiplexer);
    }

    @Override
    protected void initViews() {
        GameImages gameImages = GameImages.getInstance();
        Skin skin = gameImages.getUiSkin();
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(GameImages.BUTTON_BACKGROUND_IMAGE_NAME))));
        buttonStyle.down.setMinWidth(Dimension.buttonWidth / 1.5f);
        buttonStyle.down.setMinHeight(Dimension.buttonHeight);
        buttonStyle.font = gameImages.getGameFont();
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.overFontColor = Color.LIGHT_GRAY;

        createRoomButton = new TextButton("Create Room", buttonStyle);
        createRoomButton.getLabel().setFontScale(Dimension.smallFontScale);
        createRoomButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isConnectionSuccessful) {
                    new CreateNewRoomDialog(playerNameLabel.getText().toString()) {
                        @Override
                        protected void onStartCreateRoom() {
                            loadingDialog.showOn(screenStage);
                            loadingDialog.setMessage("Creating room...");
                        }
                    }.showOn(screenStage);
                }
            }
        });

        listRoom = new VerticalGroup();
        listRoom.space(Dimension.buttonSpace / 2);
        listRoom.padTop(20);

        background = new Image(skin.getDrawable(GameImages.KEY_BACKGROUND));
        menuBackground = new Image(skin.getDrawable(GameImages.KEY_LABEL_BACKGROUND));

        playerNameLabel = new Label("", GameImages.getInstance().getLabelStyle());
        playerNameLabel.setFontScale(Dimension.normalFontScale);

        loadingDialog = new LoadingDialog("");
    }

    @Override
    protected void addViews() {
        float widthScreen = Gdx.graphics.getWidth();
        float heightScreen = Gdx.graphics.getHeight();

        screenStage = new Stage();
        rootGroup = new Group();

        background.setBounds(0, 0, widthScreen, heightScreen);
        screenStage.addActor(background);

        VerticalGroup buttonGroup = new VerticalGroup();
        buttonGroup.top();
        buttonGroup.padTop(100);
        buttonGroup.space(Dimension.buttonSpace);
        HorizontalGroup playerNameGroup = new HorizontalGroup();
        Label playerLabel = new Label("Player: ", GameImages.getInstance().getLabelStyle());
        playerLabel.setFontScale(Dimension.normalFontScale);
        playerNameGroup.addActor(playerLabel);
        playerNameGroup.addActor(playerNameLabel);
        buttonGroup.addActor(playerNameGroup);
        buttonGroup.addActor(createRoomButton);
        float widthButtonGroup = createRoomButton.getWidth() + 40;
        buttonGroup.setBounds(0, 0, widthButtonGroup, heightScreen);
        menuBackground.setBounds(0, 0, widthButtonGroup, heightScreen);
        rootGroup.addActor(menuBackground);
        rootGroup.addActor(buttonGroup);

        ScrollPane scrollPane = new ScrollPane(listRoom);
        scrollPane.setBounds(widthButtonGroup, 0, widthScreen - widthButtonGroup, heightScreen);
        rootGroup.addActor(scrollPane);

        Skin skin = GameImages.getInstance().getUiSkin();
        roomItemStyle = new TextButton.TextButtonStyle();
        roomItemStyle.up = skin.getDrawable(GameImages.KEY_ROOM_BUTTON_NORMAL);
        float widthRoomButton = scrollPane.getWidth() - 40;
        float heightRoomButton = Dimension.buttonHeight * 1.5f;
        roomItemStyle.up.setMinWidth(widthRoomButton);
        roomItemStyle.up.setMinHeight(heightRoomButton);
        roomItemStyle.down = skin.getDrawable(GameImages.KEY_ROOM_BUTTON_PRESS);
        roomItemStyle.down.setMinWidth(widthRoomButton);
        roomItemStyle.down.setMinHeight(heightRoomButton);
        roomItemStyle.font = GameImages.getInstance().getGameFont();
        roomItemStyle.fontColor = Color.WHITE;
        roomItemStyle.overFontColor = Color.LIGHT_GRAY;

        rootGroup.setVisible(false);
        screenStage.addActor(rootGroup);
    }

    @Override
    public void show() {
        super.show();
        gameMultiplexer.addProcessor(screenStage);
    }

    public void showEnterNameDialog() {
        EnterNameDialog enterNameDialog = new EnterNameDialog() {
            @Override
            protected void onNameConfirmed(String name) {
                playerNameLabel.setText(name);
                try {
                    connectToServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        enterNameDialog.showOn(screenStage);
    }

    public void initAppWarp() throws Exception {
        WarpClient.initialize(APP_WARP_API_KEY, APP_WARP_PRIVATE_KEY);
        WarpClient warpClient = WarpClient.getInstance();
        warpClient.addConnectionRequestListener(connectionRequestAdapter);
        warpClient.addRoomRequestListener(roomRequestAdapter);
        warpClient.addNotificationListener(notifyAdapter);
        warpClient.addZoneRequestListener(zoneRequestAdapter);
        warpClient.addLobbyRequestListener(lobbyRequestAdapter);
    }

    public void connectToServer() throws Exception {
        WarpClient.getInstance().connectWithUserName(playerNameLabel.getText().toString());
        loadingDialog.setMessage("Connecting...");
        loadingDialog.showOn(screenStage);
    }

    public void disconnect() throws Exception {
        rootGroup.setVisible(false);
        WarpClient.getInstance().disconnect();
        loadingDialog.setMessage("Disconnecting...");
        loadingDialog.showOn(screenStage);
    }

    @Override
    public void onBackPress() {
        if (!DialogManager.getInstance().dismissTopDialog() && getPreviousScreen() != null) {
            try {
                disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void hide() {
        gameMultiplexer.removeProcessor(screenStage);
    }

    @Override
    public void render(float delta) {
        screenStage.act();
        screenStage.draw();
    }

    @Override
    public void dispose() {
        screenStage.dispose();
    }

    private ConnectionRequestAdapter connectionRequestAdapter = new ConnectionRequestAdapter() {
        @Override
        public void onConnectDone(ConnectEvent connectEvent) {
            loadingDialog.dismiss();
            switch (connectEvent.getResult()) {
                case WarpResponseResultCode.SUCCESS: {
                    isConnectionSuccessful = true;
                    try {
                        rootGroup.setVisible(true);
                        WarpClient warpClient = WarpClient.getInstance();
                        warpClient.initUDP();
                        warpClient.subscribeLobby();
                        loadingDialog.setMessage("Subscribing Lobby...");
                        loadingDialog.showOn(screenStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

                case WarpResponseResultCode.CONNECTION_ERROR: {
                    isConnectionSuccessful = false;
                    MessageDialog messageDialog = new MessageDialog("Connection Error") {
                        @Override
                        protected void onActionButtonPress() {
                            try {
                                rootGroup.setVisible(false);
                                WarpClient warpClient = WarpClient.getInstance();
                                warpClient.removeConnectionRequestListener(connectionRequestAdapter);
                                warpClient.removeRoomRequestListener(roomRequestAdapter);
                                warpClient.removeNotificationListener(notifyAdapter);
                                warpClient.removeZoneRequestListener(zoneRequestAdapter);
                                warpClient.removeLobbyRequestListener(lobbyRequestAdapter);
                                getParent().setScreen(getPreviousScreen());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            super.onActionButtonPress();
                        }
                    };
                    messageDialog.setMessage("Connection error, auto disconnect");
                    messageDialog.showOn(screenStage);
                }
                break;

                case WarpResponseResultCode.AUTH_ERROR: {
                    isConnectionSuccessful = false;
                    RetryDialog retryDialog = new RetryDialog("Authorized Error") {
                        @Override
                        protected void onActionButtonPress() {
                            try {
                                showEnterNameDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dismiss();
                        }
                    };
                    retryDialog.setMessage("Username is exist");
                    retryDialog.showOn(screenStage);
                }
                break;

                default: {
                    isConnectionSuccessful = false;
                    RetryDialog retryDialog = new RetryDialog("Connection Request Error") {
                        @Override
                        protected void onActionButtonPress() {
                            try {
                                connectToServer();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dismiss();
                        }
                    };
                    retryDialog.setMessage("Error code: " + connectEvent.getResult());
                    retryDialog.showOn(screenStage);
                    break;
                }
            }
        }

        @Override
        public void onDisconnectDone(ConnectEvent connectEvent) {
            loadingDialog.dismiss();
            switch (connectEvent.getResult()) {
                case WarpResponseResultCode.SUCCESS: {
                    try {
                        WarpClient warpClient = WarpClient.getInstance();
                        warpClient.removeConnectionRequestListener(connectionRequestAdapter);
                        warpClient.removeRoomRequestListener(roomRequestAdapter);
                        warpClient.removeNotificationListener(notifyAdapter);
                        warpClient.removeZoneRequestListener(zoneRequestAdapter);
                        warpClient.removeLobbyRequestListener(lobbyRequestAdapter);
                        warpClient.unsubscribeLobby();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    getParent().setScreen(getPreviousScreen());
                }
                break;

                default: {
                    RetryDialog retryDialog = new RetryDialog("Disconnect Error") {
                        @Override
                        protected void onActionButtonPress() {
                            try {
                                disconnect();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dismiss();
                        }
                    };
                    retryDialog.setMessage("Error code: " + connectEvent.getResult());
                    retryDialog.showOn(screenStage);
                    break;
                }
            }
        }

        @Override
        public void onInitUDPDone(byte b) {

        }
    };

    private LobbyRequestAdapter lobbyRequestAdapter = new LobbyRequestAdapter() {
        @Override
        public void onSubscribeLobbyDone(LobbyEvent lobbyEvent) {
            loadingDialog.dismiss();
            switch (lobbyEvent.getResult()) {
                case WarpResponseResultCode.SUCCESS: {
//                    try {
//                        WarpClient.getInstance().getAllRooms();
//                        loadingDialog.setMessage("Loading Rooms...");
//                        loadingDialog.showOn(screenStage);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
                break;

                default: {
                    RetryDialog retryDialog = new RetryDialog("Subscribe Lobby Error") {
                        @Override
                        protected void onActionButtonPress() {
                            try {
                                WarpClient.getInstance().subscribeLobby();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dismiss();
                        }
                    };
                    retryDialog.setMessage("Error code: " + lobbyEvent.getResult());
                    retryDialog.showOn(screenStage);
                    break;
                }
            }
        }
    };

    private ZoneRequestAdapter zoneRequestAdapter = new ZoneRequestAdapter() {

//        @Override
//        public void onGetAllRoomsDone(AllRoomsEvent allRoomsEvent) {
//            loadingDialog.dismiss();
//            switch (allRoomsEvent.getResult()){
//                case WarpResponseResultCode.SUCCESS:{
//
//                }
//                break;
//
//                default:{
//                    RetryDialog retryDialog = new RetryDialog("Load Rooms Error") {
//                        @Override
//                        protected void onActionButtonPress() {
//                            try {
//                                WarpClient.getInstance().getAllRooms();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            dismiss();
//                        }
//                    };
//                    retryDialog.setMessage("Error code: " + allRoomsEvent.getResult());
//                    retryDialog.showOn(screenStage);
//                    break;
//                }
//            }
//        }

        @Override
        public void onCreateRoomDone(RoomEvent roomEvent) {
            loadingDialog.dismiss();
            switch (roomEvent.getResult()) {
                case WarpResponseResultCode.SUCCESS: {
                    try {
                        WarpClient.getInstance().joinRoom(roomEvent.getData().getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

                default: {
                    MessageDialog messageDialog = new MessageDialog("Create Room Error");
                    messageDialog.setMessage("Error code: " + roomEvent.getResult());
                    messageDialog.showOn(screenStage);
                    break;
                }
            }
        }
    };

    private RoomRequestAdapter roomRequestAdapter = new RoomRequestAdapter() {

        @Override
        public void onJoinRoomDone(RoomEvent roomEvent) {
            loadingDialog.dismiss();
            switch (roomEvent.getResult()) {
                case WarpResponseResultCode.SUCCESS: {
                    Tankzor parent = getParent();
                    RoomScreen roomScreen = parent.getRoomScreen();
                    try {
                        roomScreen.initRoom(roomEvent.getData(), playerNameLabel.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    parent.setScreen(roomScreen);
                }
                break;

                default: {
                    MessageDialog messageDialog = new MessageDialog("Join Room Error");
                    messageDialog.setMessage("This room is full");
                    messageDialog.showOn(screenStage);
                    break;
                }
            }
        }
    };

    private NotifyAdapter notifyAdapter = new NotifyAdapter() {

        @Override
        public void onRoomCreated(RoomData roomData) {
            listRoom.addActor(new RoomItem(roomData, roomItemStyle));
        }

        @Override
        public void onRoomDestroyed(RoomData roomData) {
            removeRoomItemByID(roomData.getId());
        }
    };

    private void removeRoomItemByID(String roomID) {
        SnapshotArray<Actor> children = listRoom.getChildren();
        for (int i = children.size - 1; i >= 0; i--) {
            if (((RoomItem) children.get(i)).roomID.equals(roomID)) {
                children.removeIndex(i);
                return;
            }
        }
    }

    private class RoomItem extends TextButton {
        private String roomID;
        private Label playerNumberLabel;
        private Label ownerLabel;

        RoomItem(final RoomData roomData, TextButtonStyle style) {
            super("[" + roomData.getId() + "] " + roomData.getName(), style);
            this.roomID = roomData.getId();
            pad(10);
            Label buttonLabel = getLabel();
            buttonLabel.setFontScale(Dimension.normalFontScale);
            buttonLabel.setAlignment(Align.topLeft);

            playerNumberLabel = new Label("Max Player: " + roomData.getMaxUsers(), GameImages.getInstance().getLabelStyle());
            playerNumberLabel.setFontScale(Dimension.smallFontScale);
            add(playerNumberLabel).align(Align.bottomRight);

            ownerLabel = new Label("Owner: " + roomData.getRoomOwner(), GameImages.getInstance().getLabelStyle());
            ownerLabel.setFontScale(Dimension.smallFontScale);
            add(playerNumberLabel).align(Align.topRight);

            addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    try {
                        WarpClient.getInstance().joinRoom(roomID);
                        loadingDialog.setMessage("Joining...");
                        loadingDialog.showOn(screenStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
