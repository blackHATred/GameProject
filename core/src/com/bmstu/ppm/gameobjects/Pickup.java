package com.bmstu.ppm.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Pickup extends GameObject {
    private Texture sprite;
    private PICKUPS type;
    private int amount = 30;
    private float hProportion;
    public Pickup(String texturePath, PICKUPS type, int amount, float hProportion, float x, float y){
        super(false, true, x, y, 20);
        this.sprite = new Texture(Gdx.files.internal(texturePath));
        this.type = type;
        this.amount = amount;
        this.hProportion = hProportion;
    }

    public Texture getSprite() {
        return sprite;
    }

    public PICKUPS getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public float getHProportion() {
        return hProportion;
    }
}
