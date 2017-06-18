package com.tankzor.game.gamehud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.tankzor.game.common_value.AssetLoader;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.common_value.OnMoneyStarLifeChangedListener;
import com.tankzor.game.common_value.PlayerProfile;

/**
 * Created by Admin on 11/29/2016.
 */
public class InformationPanel extends Group implements OnMoneyStarLifeChangedListener{
    private Label lifeLabel;
    private Label moneyLabel;
    private Label starLabel;
    private Label enemyLabel;

    public InformationPanel(AssetLoader assetLoader){
        float widthScreen = Gdx.graphics.getWidth();
        float heightScreen = Gdx.graphics.getHeight();

        Image background = new Image(assetLoader.getInformationPanelImage());
        background.setBounds(0, 0, Dimension.informationPanelWidth, Dimension.informationPanelHeight);
        setBounds(widthScreen - background.getWidth(),
                    heightScreen - background.getHeight(),
                    background.getWidth(),
                    background.getHeight());
        addActor(background);

        Label.LabelStyle labelStyle = GameImages.getInstance().getLabelStyle();
        float fontHeight = labelStyle.font.getCapHeight() * Dimension.normalFontScale;

        enemyLabel = new Label("", labelStyle);
        enemyLabel.setAlignment(Align.center);
        enemyLabel.setFontScale(Dimension.normalFontScale);
        enemyLabel.setBounds(0, Dimension.informationPanelLabelSpace, getWidth() / 2, fontHeight);
        addActor(enemyLabel);

        lifeLabel = new Label("", labelStyle);
        lifeLabel.setAlignment(Align.center);
        lifeLabel.setFontScale(Dimension.normalFontScale);
        lifeLabel.setBounds(0, enemyLabel.getHeight() + enemyLabel.getY() + Dimension.informationPanelLabelSpace, getWidth() / 2, fontHeight);
        addActor(lifeLabel);

        starLabel = new Label("", labelStyle);
        starLabel.setAlignment(Align.center);
        starLabel.setFontScale(Dimension.normalFontScale);
        starLabel.setBounds(0, lifeLabel.getHeight() + lifeLabel.getY() + Dimension.informationPanelLabelSpace, getWidth() / 2, fontHeight);
        addActor(starLabel);

        moneyLabel = new Label("", labelStyle);
        moneyLabel.setAlignment(Align.center);
        moneyLabel.setFontScale(Dimension.normalFontScale);
        moneyLabel.setBounds(0, starLabel.getHeight() + starLabel.getY() + Dimension.informationPanelLabelSpace, getWidth() / 2, fontHeight);
        addActor(moneyLabel);

        PlayerProfile.getInstance().setMoneyStarLifeChangedListener(this);
    }

    public void setEnemiesNumber(int value){
        enemyLabel.setText(value+"");
    }

    @Override
    public void onMoneyChanged(int newMoney) {
        moneyLabel.setText(newMoney+"");
    }

    @Override
    public void onStarChanged(int newStar) {
        starLabel.setText(newStar+"");
    }

    @Override
    public void onLifeChanged(int newLife) {
        lifeLabel.setText(newLife+"");
    }
}
