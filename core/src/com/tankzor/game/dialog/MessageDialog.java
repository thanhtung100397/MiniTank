package com.tankzor.game.dialog;

/**
 * Created by Admin on 6/9/2017.
 */

public class MessageDialog extends NotificationDialog {
    public MessageDialog(String title) {
        super(title);
        canDismissWithoutAction = true;
        setActionButtonLabel("OK");
    }

    @Override
    protected void onActionButtonPress() {
        dismiss();
    }
}
