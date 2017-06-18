package com.tankzor.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.dialog.DialogManager;
import com.tankzor.game.dialog.LoadingDialog;
import com.tankzor.game.dialog.MessageDialog;
import com.tankzor.game.dialog.RetryDialog;
import com.tankzor.game.main.Tankzor;
import com.tankzor.game.network.NotifyAdapter;
import com.tankzor.game.network.RoomRequestAdapter;
import com.tankzor.game.network.message.MessageCreator;
import com.tankzor.game.network.message.MessageResolver;
import com.tankzor.game.util.FloatPoint;

import org.json.JSONObject;

/**
 * Created by Admin on 6/9/2017.
 */

public class RoomScreen extends BaseScreen {
    public static final int MAX_MESSAGE = 20;
    private Stage screenStage;
    private RoomData roomData;
    private ScreenTitle screenTitle;
    private Image background;
    private LoadingDialog loadingDialog;
    private PlayerGroup playerGroup;
    private ChatPanel chatPanel;
    private TextButton.TextButtonStyle playerItemButtonStyle;
    private String playerName;
    private TextButton actionButton;
    private boolean isReady = false;

    public RoomScreen(Tankzor parent, Viewport viewport, SpriteBatch batch, InputMultiplexer gameMultiplexer) {
        super(parent, viewport, batch, gameMultiplexer);
    }

    @Override
    protected void initViews() {
        float widthScreen = Gdx.graphics.getWidth();
        float heightScreen = Gdx.graphics.getHeight();
        GameImages gameImages = GameImages.getInstance();
        screenTitle = new ScreenTitle(0, heightScreen - Dimension.buttonHeight, widthScreen, Dimension.buttonHeight);
        background = new Image(gameImages.getUiSkin().getDrawable(GameImages.KEY_BACKGROUND));
        background.setBounds(0, 0, widthScreen, heightScreen);

        loadingDialog = new LoadingDialog("");

        TextButton.TextButtonStyle actionButtonStyle = new TextButton.TextButtonStyle();
        float widthButton = Dimension.buttonWidth / 1.5f;
        float heightButton = Dimension.buttonHeight * 1.5f;
        actionButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(GameImages.ORANGE_BACKGROUND_IMAGE_NAME)));
        actionButtonStyle.up.setMinWidth(widthButton);
        actionButtonStyle.up.setMinHeight(heightButton);
        actionButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(GameImages.BROWN_BACKGROUND_IMAGE_NAME)));
        actionButtonStyle.down.setMinWidth(widthButton);
        actionButtonStyle.down.setMinHeight(heightButton);
        actionButtonStyle.font = GameImages.getInstance().getGameFont();
        actionButtonStyle.fontColor = Color.WHITE;
        actionButtonStyle.disabledFontColor = Color.GRAY;


        actionButton = new TextButton("", actionButtonStyle);
        actionButton.getLabel().setFontScale(Dimension.normalFontScale);
        actionButton.setPosition(widthScreen - widthButton - 20, 20);
        actionButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                String message = null;
                if (roomData.getRoomOwner().equals(playerName)) {
                    if (playerGroup.readyPlayer == playerGroup.memberNames.size) {
                        message = MessageCreator.createGameStateMessage(true);
                    } else {
                        MessageDialog messageDialog = new MessageDialog("Message");
                        messageDialog.setMessage("All player must be ready to start game");
                        messageDialog.showOn(screenStage);
                    }
                } else {
                    if (isReady) {
                        message = MessageCreator.createReadyMessage(playerName, false);
                    } else {
                        message = MessageCreator.createReadyMessage(playerName, true);
                    }
                    actionButton.setDisabled(true);
                }
                if (message != null) {
                    try {
                        WarpClient.getInstance().sendUpdatePeers(message.getBytes());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        playerGroup = new PlayerGroup(widthScreen / 2 - 10, heightScreen - heightButton - screenTitle.getHeight() - 40);
        chatPanel = new ChatPanel(widthScreen / 2 - 10, heightScreen - heightButton - screenTitle.getHeight() - 40);

        playerItemButtonStyle = new TextButton.TextButtonStyle();
        playerItemButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(GameImages.BUTTON_BACKGROUND_IMAGE_NAME)));
        float widthRoomButton = playerGroup.getWidth() - 20;
        float heightRoomButton = Dimension.buttonHeight;
        playerItemButtonStyle.up.setMinWidth(widthRoomButton);
        playerItemButtonStyle.up.setMinHeight(heightRoomButton);
        playerItemButtonStyle.font = GameImages.getInstance().getGameFont();
        playerItemButtonStyle.fontColor = Color.WHITE;
        playerItemButtonStyle.overFontColor = Color.LIGHT_GRAY;
    }

    @Override
    protected void addViews() {
        screenStage = new Stage();
        screenStage.addActor(background);
        screenStage.addActor(screenTitle);
        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.setBounds(0, Gdx.graphics.getHeight() - playerGroup.getHeight() - screenTitle.getHeight(), Gdx.graphics.getWidth(), playerGroup.getHeight());
        horizontalGroup.space(20);
        horizontalGroup.addActor(playerGroup);
        horizontalGroup.addActor(chatPanel);

        screenStage.addActor(horizontalGroup);
        screenStage.addActor(actionButton);
    }

    public boolean isRoomOwner() {
        return playerName.equals(roomData.getRoomOwner());
    }

    private MessageResolver messageResolver = new MessageResolver() {
        @Override
        protected void onMessageReceived(int messageId, JSONObject jsonObject) {
            switch (messageId) {
                case MessageCreator.READY_MESSAGE: {
                    boolean isReady = jsonObject.getBoolean(MessageCreator.IS_READY);
                    playerGroup.setReady(jsonObject.getString(MessageCreator.NAME), isReady);
                    if (isRoomOwner()) {
                        try {
                            WarpClient.getInstance()
                                    .sendUpdatePeers(MessageCreator.createConfirmReadyMessage(isReady).getBytes());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;

                case MessageCreator.READY_CONFIRMED_MESSAGE: {
                    if(isRoomOwner()){
                        return;
                    }
                    isReady = jsonObject.getBoolean(MessageCreator.IS_READY);
                    actionButton.setDisabled(false);
                    if (isReady) {
                        actionButton.setText("UNREADY");
                    } else {
                        actionButton.setText("READY");
                    }
                }
                break;

                case MessageCreator.GAME_STATE_MESSAGE: {
                    if (jsonObject.getBoolean(MessageCreator.IS_START)) {
//                        getParent().getGameScreen().initAndStartOnlineMap("map_online",
//                                                                            playerGroup.memberNames.indexOf(playerName, false));
                        MessageDialog messageDialog = new MessageDialog("Message");
                        messageDialog.setMessage("Battle online is in development");
                        messageDialog.showOn(screenStage);
                    }
                }
                break;

                default: {
                    break;
                }
            }
        }
    };

    private NotifyAdapter notifyAdapter = new NotifyAdapter() {

        @Override
        public void onUpdatePeersReceived(UpdateEvent updateEvent) {
            messageResolver.resolveMessage(new String(updateEvent.getUpdate()));
        }

        @Override
        public void onRoomDestroyed(RoomData roomData) {
            if (RoomScreen.this.roomData.getId().equals(roomData.getId())) {
                if (playerName.equals(roomData.getRoomOwner())) {
                    return;
                }
                try {
                    getParent().setScreen(getPreviousScreen());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onUserLeftRoom(RoomData roomData, String s) {
            playerGroup.removePlayerByName(s);
            if (roomData.getRoomOwner().equals(s)) {
                try {
                    WarpClient.getInstance().deleteRoom(roomData.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onUserJoinedRoom(RoomData roomData, String s) {
            playerGroup.addPlayer(s, false);
        }

        @Override
        public void onChatReceived(ChatEvent chatEvent) {
            String sender = chatEvent.getSender();
            chatPanel.addMessageToChatBoard(new ChatMessage(sender, chatEvent.getMessage(), GameImages.getInstance().getLabelStyle()), sender.equals(playerName) ? Align.right : Align.left);
        }
    };

    private RoomRequestAdapter roomRequestAdapter = new RoomRequestAdapter() {

        @Override
        public void onSubscribeRoomDone(final RoomEvent roomEvent) {
            loadingDialog.dismiss();
            switch (roomEvent.getResult()) {
                case WarpResponseResultCode.SUCCESS: {
                    try {
                        WarpClient.getInstance().getLiveRoomInfo(roomData.getId());
                        loadingDialog.setMessage("Getting Information...");
                        loadingDialog.showOn(screenStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

                default: {
                    RetryDialog retryDialog = new RetryDialog("Subscribe Room Error") {
                        @Override
                        protected void onActionButtonPress() {
                            try {
                                WarpClient.getInstance().subscribeRoom(roomData.getId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    retryDialog.setMessage("Error code: " + roomEvent.getResult());
                    retryDialog.showOn(screenStage);
                    break;
                }
            }
        }

        @Override
        public void onUnSubscribeRoomDone(final RoomEvent roomEvent) {
            loadingDialog.dismiss();
            switch (roomEvent.getResult()) {
                case WarpResponseResultCode.SUCCESS: {
                    try {
                        WarpClient warpClient = WarpClient.getInstance();
                        warpClient.removeRoomRequestListener(roomRequestAdapter);
                        warpClient.removeNotificationListener(notifyAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    getParent().setScreen(getPreviousScreen());
                }
                break;

                default: {
                    RetryDialog retryDialog = new RetryDialog("Unsubscribe Room Error") {
                        @Override
                        protected void onActionButtonPress() {
                            try {
                                WarpClient.getInstance().unsubscribeRoom(roomData.getId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    retryDialog.setMessage("Error code: " + roomEvent.getResult());
                    retryDialog.showOn(screenStage);
                    break;
                }
            }
        }

        @Override
        public void onLeaveRoomDone(RoomEvent roomEvent) {
            loadingDialog.dismiss();
            switch (roomEvent.getResult()) {
                case WarpResponseResultCode.SUCCESS: {
                    try {
                        WarpClient warpClient = WarpClient.getInstance();
                        warpClient.unsubscribeRoom(roomData.getId());
                        loadingDialog.setMessage("Unsubscribeing Room");
                        loadingDialog.showOn(screenStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

                default: {
                    RetryDialog retryDialog = new RetryDialog("Leave Room Error") {
                        @Override
                        protected void onActionButtonPress() {
                            try {
                                exitRoom();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    retryDialog.setMessage("Error code: " + roomEvent.getResult());
                    retryDialog.showOn(screenStage);
                    break;
                }
            }
        }

        @Override
        public void onGetLiveRoomInfoDone(LiveRoomInfoEvent liveRoomInfoEvent) {
            loadingDialog.dismiss();
            switch (liveRoomInfoEvent.getResult()) {
                case WarpResponseResultCode.SUCCESS: {
                    String joinedUsers[] = liveRoomInfoEvent.getJoinedUsers();
                    screenTitle.playerNumber.setText(joinedUsers.length + "");
                    for (int i = 0; i < joinedUsers.length; i++) {
                        playerGroup.addPlayer(joinedUsers[i], joinedUsers[i].equals(roomData.getRoomOwner()));
                    }
                    if (playerName.equals(roomData.getRoomOwner())) {
                        actionButton.setText("START GAME");
                    } else {
                        actionButton.setText("READY");
                    }
                    try {
                        WarpClient.getInstance().addNotificationListener(notifyAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

                default: {
                    RetryDialog retryDialog = new RetryDialog("Get Room Information Error") {
                        @Override
                        protected void onActionButtonPress() {
                            try {
                                WarpClient.getInstance().getLiveRoomInfo(roomData.getId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    retryDialog.setMessage("Error code: " + liveRoomInfoEvent.getResult());
                    retryDialog.showOn(screenStage);
                    break;
                }
            }
        }
    };

    private void clearRoom() {
        playerGroup.clearListPlayers();
        chatPanel.clearChatBoard();
    }

    public void initRoom(RoomData roomData, String playerName) throws Exception {
        this.roomData = roomData;
        this.playerName = playerName;
        screenTitle.roomName.setText("[" + roomData.getId() + "] " + roomData.getName());
        screenTitle.maxPlayer.setText("/" + roomData.getMaxUsers());

        WarpClient warpClient = WarpClient.getInstance();
        warpClient.addRoomRequestListener(roomRequestAdapter);
        warpClient.subscribeRoom(roomData.getId());
        loadingDialog.setMessage("Subscribing Room...");
        loadingDialog.showOn(screenStage);
    }

    public void exitRoom() throws Exception {
        WarpClient.getInstance().leaveRoom(roomData.getId());
        loadingDialog.setMessage("Leaving Room...");
        loadingDialog.showOn(screenStage);
        clearRoom();
    }

    @Override
    public void show() {
        super.show();
        gameMultiplexer.addProcessor(screenStage);
    }

    @Override
    public void hide() {
        super.hide();
        gameMultiplexer.removeProcessor(screenStage);
    }

    @Override
    public void render(float delta) {
        screenStage.act(delta);
        screenStage.draw();
    }

    @Override
    public void dispose() {
        screenStage.dispose();
    }

    @Override
    public void onBackPress() {
        if (!DialogManager.getInstance().dismissTopDialog() && getPreviousScreen() != null) {
            try {
                exitRoom();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ScreenTitle extends Group {
        Label playerLabel;
        Label roomName, playerNumber, maxPlayer;
        Image background;

        ScreenTitle(float x, float y, float width, float height) {
            setBounds(x, y, width, height);
            Skin skin = GameImages.getInstance().getUiSkin();
            background = new Image(skin.getDrawable(GameImages.KEY_LABEL_BACKGROUND));
            background.setBounds(0, 0, width, height);
            addActor(background);

            Table iconGroup = new Table();
            iconGroup.padLeft(Dimension.buttonSpace * 2);
            iconGroup.setBounds(0, 0, width, height);

            TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
            buttonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(GameImages.BUTTON_BACKGROUND_IMAGE_NAME)));
            float buttonWidth = Dimension.buttonWidth / 3;
            buttonStyle.down.setMinWidth(Dimension.buttonWidth / 3);
            buttonStyle.down.setMinHeight(48);
            buttonStyle.font = GameImages.getInstance().getGameFont();
            buttonStyle.fontColor = Color.WHITE;
            buttonStyle.overFontColor = Color.LIGHT_GRAY;
            TextButton backButton = new TextButton("< Exit Room", buttonStyle);
            backButton.getLabel().setFontScale(Dimension.normalFontScale);
            backButton.setBounds(0, 0, buttonWidth, Dimension.buttonHeight);
            backButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    onBackPress();
                }
            });

            Label.LabelStyle labelStyle = GameImages.getInstance().getLabelStyle();
            roomName = new Label("", labelStyle);
            roomName.setFontScale(Dimension.normalFontScale);

            playerLabel = new Label("Player: ", labelStyle);
            playerLabel.setFontScale(Dimension.normalFontScale);

            playerNumber = new Label("0", labelStyle);
            playerNumber.setFontScale(Dimension.normalFontScale);

            maxPlayer = new Label("", labelStyle);
            maxPlayer.setFontScale(Dimension.normalFontScale);

            float withLabelSpaceUnit = (Gdx.graphics.getWidth() - backButton.getWidth() - 15) / 8;
            iconGroup.align(Align.left);
            iconGroup.add(backButton).padRight(15);
            iconGroup.add(roomName).width(withLabelSpaceUnit * 6);

            HorizontalGroup playerInformationGroup = new HorizontalGroup();
            playerInformationGroup.addActor(playerLabel);
            playerInformationGroup.addActor(playerNumber);
            playerInformationGroup.addActor(maxPlayer);
            iconGroup.add(playerInformationGroup).width(withLabelSpaceUnit * 2);
            ;

            addActor(iconGroup);
        }
    }

    private class PlayerGroup extends Group {
        VerticalGroup listPlayer;
        Array<String> memberNames;
        Image background;
        ScrollPane scrollPane;
        int readyPlayer = 0;

        PlayerGroup(float width, float height) {
            setSize(width, height);

            Label label = new Label("PLAYER LIST", GameImages.getInstance().getLabelStyle());
            label.setFontScale(Dimension.largeFontScale);
            label.setPosition((width - label.getWidth()) / 2, (height - label.getHeight()) / 2);

            background = new Image(new Texture(Gdx.files.internal(GameImages.GRAY_BACKGROUND_IMAGE_NAME)));
            background.setBounds(0, 0, width, height);

            listPlayer = new VerticalGroup();
            listPlayer.setSize(width, height);

            scrollPane = new ScrollPane(listPlayer);
            scrollPane.setBounds(0, 0, width, height);
            addActor(label);
            addActor(background);
            addActor(scrollPane);

            memberNames = new Array<String>();
        }

        public void clearListPlayers() {
            listPlayer.clearChildren();
        }

        void addPlayer(String playerName, boolean isOwner) {
            if (memberNames.contains(playerName, false)) {
                return;
            }
            memberNames.add(playerName);
            screenTitle.playerNumber.setText(memberNames.size + "");
            PlayerItem playerItem = new PlayerItem(playerName, playerItemButtonStyle);
            String type;
            if (isOwner) {
                type = "Room Owner";
                readyPlayer++;
            } else {
                type = "Unready";
            }
            playerItem.setType(type);
            listPlayer.addActor(playerItem);
        }

        public void setReady(String playerName, boolean isReady) {
            int index = memberNames.indexOf(playerName, false);
            if (index == -1) {
                return;
            }
            String type;
            if (isReady) {
                type = "Ready";
                readyPlayer++;
            } else {
                type = "Unready";
                readyPlayer--;
            }
            ((PlayerItem) listPlayer.getChildren().get(index)).setType(type);
        }

        void removePlayerByName(String playerName) {
            int index;
            if ((index = memberNames.indexOf(playerName, false)) != -1) {
                playerGroup.getChildren().removeIndex(index);
            }
        }
    }

    private class PlayerItem extends TextButton {
        private Label typeLabel;

        PlayerItem(String playerName, TextButtonStyle style) {
            super(playerName, style);
            Label label = getLabel();
            getLabelCell().padLeft(15);
            label.setFontScale(Dimension.normalFontScale);
            label.setAlignment(Align.left);

            setText(playerName);
            typeLabel = new Label("", GameImages.getInstance().getLabelStyle());
            add(typeLabel).align(Align.right);
        }

        public void setType(String type) {
            typeLabel.setText(type);
        }
    }

    private class ChatPanel extends Group {
        ScrollPane scrollPane;
        Table chatBoard;
        TextField chatBox;
        TextButton sendButton;
        Image background;

        ChatPanel(float width, float height) {
            setSize(width, height);
            Label label = new Label("CHAT BOARD", GameImages.getInstance().getLabelStyle());
            label.setFontScale(Dimension.largeFontScale);
            label.setPosition((width - label.getWidth()) / 2, (height - label.getHeight()) / 2);

            background = new Image(new Texture(Gdx.files.internal(GameImages.GRAY_BACKGROUND_IMAGE_NAME)));
            background.setBounds(0, 0, width, height);

            float widthButton = Dimension.buttonWidth / 2;
            float heightButton = Dimension.buttonHeight;

            GameImages gameImages = GameImages.getInstance();
            TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
            textFieldStyle.background = gameImages.getUiSkin().getDrawable(GameImages.KEY_TEXT_FIELD_BACKGROUND);
            textFieldStyle.background.setMinWidth(width - widthButton);
            textFieldStyle.background.setMinHeight(Dimension.buttonHeight);
            textFieldStyle.font = gameImages.getGameFont();
            textFieldStyle.fontColor = Color.WHITE;
            textFieldStyle.disabledFontColor = Color.DARK_GRAY;
            chatBox = new TextField("", textFieldStyle);
            chatBox.setAlignment(Align.left);
            chatBox.setBounds(0, 0, width - widthButton, Dimension.buttonHeight);

            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(GameImages.ORANGE_BACKGROUND_IMAGE_NAME)));
            textButtonStyle.up.setMinWidth(widthButton);
            textButtonStyle.up.setMinHeight(heightButton);
            textButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(GameImages.BROWN_BACKGROUND_IMAGE_NAME)));
            textButtonStyle.down.setMinWidth(widthButton);
            textButtonStyle.down.setMinHeight(heightButton);
            textButtonStyle.font = GameImages.getInstance().getGameFont();
            textButtonStyle.fontColor = Color.WHITE;
            textButtonStyle.overFontColor = Color.LIGHT_GRAY;

            sendButton = new TextButton("SEND", textButtonStyle);
            sendButton.setBounds(chatBox.getX() + chatBox.getWidth(),
                    chatBox.getY(),
                    widthButton,
                    heightButton);
            sendButton.getLabel().setFontScale(Dimension.normalFontScale);
            sendButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    String message = chatBox.getText();
                    if (!message.equals("")) {
                        try {
                            WarpClient.getInstance().sendChat(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        chatBox.setText("");
                    }
                }
            });

            chatBoard = new Table();
            chatBoard.setFillParent(true);
            chatBoard.setBounds(chatBox.getX(),
                    chatBox.getY() + chatBox.getHeight(),
                    width,
                    height - chatBox.getHeight());
            chatBoard.align(Align.bottom);

            scrollPane = new ScrollPane(chatBoard);
            scrollPane.setBounds(chatBoard.getX(), chatBoard.getY(), chatBoard.getWidth(), chatBoard.getHeight());

            addActor(label);
            addActor(background);
            addActor(chatBox);
            addActor(sendButton);
            addActor(scrollPane);
        }

        public void clearChatBoard() {
            chatBoard.clearChildren();
        }

        public void addMessageToChatBoard(ChatMessage chatMessage, int align) {
            Array<Cell> cells = chatBoard.getCells();
            if (cells.size == MAX_MESSAGE) {
                cells.removeIndex(0);
            }
            chatBoard.add(chatMessage).align(align).expandX().row();
            scrollPane.scrollTo(0, 0, 0, 0);
        }
    }

    private class ChatMessage extends Label {
        public ChatMessage(String name, String message, LabelStyle style) {
            super("[" + name + "] " + message, style);
            setFontScale(Dimension.smallFontScale);
        }
    }
}
