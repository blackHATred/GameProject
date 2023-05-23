package com.bmstu.ppm.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.bmstu.ppm.SuperDuperMath;

public class Wall extends GameObject {
    public Texture texture;
    private final Pixmap pixmap;
    private final double width;

    public double getWidth() {
        return width;
    }

    public Pixmap getPixmap() {
        return pixmap;
    }

    public Wall(float posX1, float posY1, float posX2, float posY2, String texturePath){
        super(true, true, posX1, posY1, posX2, posY2);
        texture = new Texture(Gdx.files.internal(texturePath));
        width = SuperDuperMath.dist(posX1, posY1, posX2, posY2);
        texture.getTextureData().prepare();
        pixmap = texture.getTextureData().consumePixmap();
    }
}
