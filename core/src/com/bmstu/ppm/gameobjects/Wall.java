package com.bmstu.ppm.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Wall extends GameObject {
    Texture texture;

    public Wall(double posX1, double posY1, double posX2, double posY2, String texturePath){
        super(true, true, posX1, posY1, posX2, posY2);
        texture = new Texture(Gdx.files.internal(texturePath));
    }
}
