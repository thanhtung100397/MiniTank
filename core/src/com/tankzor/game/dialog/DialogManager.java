package com.tankzor.game.dialog;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Admin on 6/9/2017.
 */

public class DialogManager {
    private static DialogManager instance = new DialogManager();
    private Array<BaseDialog> visibleDialogs;
    private Array<BaseDialog> dismissDialogs;

    public static DialogManager getInstance() {
        return instance;
    }

    public DialogManager() {
        visibleDialogs = new Array<BaseDialog>(3);
        dismissDialogs = new Array<BaseDialog>(3);
    }

    public void addCurrentVisibleDialog(BaseDialog currentVisibleDialog) {
        this.visibleDialogs.add(currentVisibleDialog);
    }

    public void removeDismissDialogs() {
        for (int i = dismissDialogs.size - 1; i >= 0; i--) {
            dismissDialogs.get(i).getWindow().remove();
            dismissDialogs.removeIndex(i);
        }
    }

    public void dismissDialog(BaseDialog baseDialog) {
        if (visibleDialogs.removeValue(baseDialog, true)) {
            dismissDialogs.add(baseDialog);
        }
    }

    public boolean dismissTopDialog() {
        if (visibleDialogs.size == 0) {
            return false;
        }
        BaseDialog topVisibleDialog = visibleDialogs.get(visibleDialogs.size - 1);
        if (topVisibleDialog.canDismissWithoutAction) {
            topVisibleDialog.dismiss();
        }
        return true;
    }
}
