package com.mathquest.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Const {
    static int gWidth, gHeight;
    static String skinPath, buttonType;
    static final int ANIM_TIME = 20;

    public Const() {
        gWidth = 1024;
        gHeight = 872;
        skinPath = "shadeui/uiskin.json";
        buttonType = "round";

    }
}
