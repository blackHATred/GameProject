package com.bmstu.ppm.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class Weapon {
    private WEAPONS weapon;
    private final int magSize;
    private final float scale;
    private Texture allFrames;
    private float animSpeed;
    private float reloadTime = 2.0f;
    private ArrayList<TextureRegion> frames = new ArrayList<>();
    private float fireRate;
    private Sound shootSound;
    private Sound reloadSound;
    private Sound swapSound;
    private boolean inInventory = false;
    private int totalBullets = 0;
    private int magBullets = 0;
    private int pixelScreenOffsetX = 0;
    private int pixelScreenOffsetY = 0;

    public Weapon(WEAPONS weapon, int magSize, Texture allFrames, float animSpeed, float fireRate,
                  Sound shootSound, Sound reloadSound, Sound swapSound, float scale){
        this.weapon = weapon;
        this.magSize = magSize;
        this.allFrames = allFrames;
        this.animSpeed = animSpeed;
        this.fireRate = fireRate;
        this.shootSound = shootSound;
        this.reloadSound = reloadSound;
        this.swapSound = swapSound;
        this.scale = scale;
        switch (weapon){
            case RIFLE -> {
                pixelScreenOffsetX = (int) (Gdx.graphics.getWidth()*0.18f);
                pixelScreenOffsetY = (int) (Gdx.graphics.getHeight()*0.05f);
                frames.add(new TextureRegion(allFrames, 0, 0, 237, 126));
                frames.add(new TextureRegion(allFrames, 237, 0, 237, 126));
                frames.add(new TextureRegion(allFrames, 0, 126, 251, 154));
                frames.add(new TextureRegion(allFrames, 251, 126, 251, 154));
            }
            case SHOTGUN -> {
                pixelScreenOffsetX = 0;
                pixelScreenOffsetY = (int) (Gdx.graphics.getHeight()*0.05f);
                for (int i = 0; i < 15; i++) {
                    frames.add(new TextureRegion(allFrames, 0, i*150, 200, 150));
                }
            }
            case GMEOWK18 -> {
                pixelScreenOffsetX = -(int) (Gdx.graphics.getWidth()*0.1f);;
                pixelScreenOffsetY = (int) (Gdx.graphics.getHeight()*0.1f);
                frames.add(new TextureRegion(allFrames, 0, 0, 100, 122));
                frames.add(new TextureRegion(allFrames, 106, 0, 93, 123));
                frames.add(new TextureRegion(allFrames, 0, 126, 117, 169));
                frames.add(new TextureRegion(allFrames, 117, 126, 172, 168));
            }
        }
    }

    public void setInInventory(boolean f){
        inInventory = f;
    }

    public boolean isInInventory() {
        return inInventory;
    }

    public int getTotalBullets() {
        return totalBullets;
    }

    public void setTotalBullets(int totalBullets) {
        this.totalBullets = totalBullets;
    }

    public int getMagBullets() {
        return magBullets;
    }

    public void setMagBullets(int magBullets) {
        this.magBullets = magBullets;
    }

    public int getMagSize() {
        return magSize;
    }

    public float getReloadTime() {
        return reloadTime;
    }

    public Sound getShootSound() {
        return shootSound;
    }

    public Sound getReloadSound() {
        return reloadSound;
    }

    public Sound getSwapSound() {
        return swapSound;
    }

    public float getFireRate() {
        return fireRate;
    }

    public ArrayList<TextureRegion> getFrames() {
        return frames;
    }

    public float getAnimSpeed() {
        return animSpeed;
    }

    public int getPixelScreenOffsetX() {
        return pixelScreenOffsetX;
    }
    public int getPixelScreenOffsetY() {
        return pixelScreenOffsetY;
    }

    public float getScale() {
        return scale;
    }
}
