package com.bmstu.ppm.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Enemy extends GameObject {
    private final Texture texture;
    private TextureRegion currentTexture;
    private final ENEMY_TYPE type;
    private final double width;
    public double getWidth() {
        return width;
    }
    Enemy(float posX, float posY, float radius, String texturePath, ENEMY_TYPE type) {
        super(true, true, posX, posY, radius);
        texture = new Texture(Gdx.files.internal(texturePath));
        texture.getTextureData().prepare();
        width = 2*radius;
        this.type = type;
        switch (type){
            case MUTANT -> {}
            case DOG -> {}
        }
    }

    public TextureRegion getCurrentTexture() {
        return currentTexture;
    }
}
