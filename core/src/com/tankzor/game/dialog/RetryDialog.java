package com.tankzor.game.dialog;

/**
 * Created by Admin on 6/9/2017.
 */

public abstract class RetryDialog extends NotificationDialog {

    public RetryDialog(String title) {
        super(title);
        setActionButtonLabel("Retry");
    }
}
