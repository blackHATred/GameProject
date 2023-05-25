package com.bmstu.ppm.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bmstu.ppm.SuperDuperMath;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Player {
    private float hp = 53;
    private float posX;
    private float posY;
    private float rotate = 0.01f;
    private final float VFOV_const = 30;
    private final float walkSpeed = 100f;
    private final float runSpeed = 200f;
    private float speed = walkSpeed;
    /** Прицел */
    private final Texture defaultCrosshair;
    private final Texture cancelCrosshair;
    private Texture currentCrosshair;
    /** Оружие */
    private final HashMap<WEAPONS, Weapon> weapons = new HashMap<>() {{
        put(WEAPONS.RIFLE, new Weapon(
                WEAPONS.RIFLE,
                30,
                new Texture(Gdx.files.internal("Textures/FPGuns/MCGUN1.png")),
                .1f,
                .1f,
                Gdx.audio.newSound(Gdx.files.internal("sounds/RifleShot.wav")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/RifleReload.wav")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/gunswap.wav")),
                1
        ));
        put(WEAPONS.SHOTGUN, new Weapon(
                WEAPONS.SHOTGUN,
                2,
                new Texture(Gdx.files.internal("Textures/FPGuns/Sgun.png")),
                1f,
                1f,
                Gdx.audio.newSound(Gdx.files.internal("sounds/ShotgunSound.mp3")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/ShotgunReload.wav")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/gunswap.wav")),
                0.5f
        ));
        put(WEAPONS.GMEOWK18, new Weapon(
                WEAPONS.GMEOWK18,
                18,
                new Texture(Gdx.files.internal("Textures/FPGuns/PIST1.png")),
                0.3f,
                0.5f,
                Gdx.audio.newSound(Gdx.files.internal("sounds/GMEOWK18Shot.wav")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/GMEOWK18Reload.wav")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/gunswap.wav")),
                0.5f
        ));
    }};
    private WEAPONS currentWeapon;
    private TextureRegion currentWeaponFrame;
    private boolean isReloading = false;
    private boolean isShooting = false;
    private float reloadDtCounter = 0;
    private float shootDtCounter = 0;
    /** Шаги */
    private final Sound stepSound = Gdx.audio.newSound(Gdx.files.internal("sounds/step.ogg"));
    private final float dRun = 0.5f;
    private final float dWalk = 1f;
    private float stepDtCounter = 0;


    public Player(float posX, float posY){
        this.posX = posX;
        this.posY = posY;
        //defaultCrosshair = new Texture(Gdx.files.internal("Textures/Crosshairs/512/11.png"));
        defaultCrosshair = null;
        cancelCrosshair = new Texture(Gdx.files.internal("Textures/Crosshairs/512/63.png"));
        currentCrosshair = defaultCrosshair;
        currentWeapon = WEAPONS.GMEOWK18;
        weapons.get(currentWeapon).setInInventory(true);
        weapons.get(currentWeapon).setTotalBullets(100);
        weapons.get(currentWeapon).setMagBullets(2);
        currentWeaponFrame = weapons.get(currentWeapon).getFrames().get(0);
    }
    public void rotate(float radians, float sensitivity){
        rotate = (rotate + radians * sensitivity) % (2 * (float) Math.PI);
    }
    public void setRunSpeed(){
        speed = runSpeed;
    }
    public void setWalkSpeed(){
        speed = walkSpeed;
    }
    public float getPosX() {
        return posX;
    }
    public float getPosY() {
        return posY;
    }
    public float getVFOV_const() {
        return VFOV_const;
    }
    public float getRotate(){
        return rotate;
    }
    public void move(float x, float y, ArrayList<GameObject> objects, float dt){
        ArrayList<GameObject> objectsToRemove = new ArrayList<>();
        for (GameObject gameObject: objects) {
            if (gameObject.isCollidable()) {
                switch (gameObject.getCollidableType()){
                    case LINE -> {
                        if (SuperDuperMath.lineIntersect(
                                this.posX, this.posY,
                                this.posX + x * speed * 1.5f, this.posY + y * speed * 1.5f,
                                gameObject.getPosX1(), gameObject.getPosY1(),
                                gameObject.getPosX2(), gameObject.getPosY2()
                        ) != null)
                            return;
                    }
                    case RADIUS -> {
                        if (!SuperDuperMath.lineCircleIntersection(
                                this.posX, this.posY,
                                this.posX + x * speed * 1.5f, this.posY + y * speed * 1.5f,
                                gameObject.getPosX(), gameObject.getPosY(), gameObject.getRadius()).isEmpty())
                            return;
                    }
                }
            }
            if (gameObject.getClass() == Pickup.class) {
                Pickup pickup = (Pickup) gameObject;
                if (SuperDuperMath.inCircle(this.posX + x * speed, this.posY + y * speed, pickup.getPosX(), pickup.getPosY(), pickup.getRadius())) {
                    switch (pickup.getType()){
                        case RIFLE -> {
                            weapons.get(WEAPONS.RIFLE).getSwapSound().play();
                            weapons.get(WEAPONS.RIFLE).setInInventory(true);
                            weapons.get(WEAPONS.RIFLE).setTotalBullets(weapons.get(WEAPONS.RIFLE).getTotalBullets() + pickup.getAmount());
                            objectsToRemove.add(pickup);
                        }
                        case SHOTGUN -> {
                            weapons.get(WEAPONS.SHOTGUN).getSwapSound().play();
                            weapons.get(WEAPONS.SHOTGUN).setInInventory(true);
                            weapons.get(WEAPONS.SHOTGUN).setMagBullets(2);
                            weapons.get(WEAPONS.SHOTGUN).setTotalBullets(weapons.get(WEAPONS.SHOTGUN).getTotalBullets() + pickup.getAmount()-2);
                            objectsToRemove.add(pickup);
                        }
                        case GMEOWK18 -> {
                            weapons.get(WEAPONS.GMEOWK18).getSwapSound().play();
                            weapons.get(WEAPONS.GMEOWK18).setInInventory(true);
                            weapons.get(WEAPONS.GMEOWK18).setTotalBullets(weapons.get(WEAPONS.GMEOWK18).getTotalBullets() + pickup.getAmount());
                            objectsToRemove.add(pickup);
                        }
                        case AMMO_RIFLE -> {
                            weapons.get(WEAPONS.RIFLE).getSwapSound().play();
                            weapons.get(WEAPONS.RIFLE).setTotalBullets(weapons.get(WEAPONS.RIFLE).getTotalBullets() + pickup.getAmount());
                            objectsToRemove.add(pickup);
                        }
                        case AMMO_SHOTGUN -> {
                            weapons.get(WEAPONS.SHOTGUN).getSwapSound().play();
                            weapons.get(WEAPONS.SHOTGUN).setTotalBullets(weapons.get(WEAPONS.SHOTGUN).getTotalBullets() + pickup.getAmount());
                            objectsToRemove.add(pickup);
                        }
                        case AMMO_GMEOWK18 -> {
                            weapons.get(WEAPONS.GMEOWK18).getSwapSound().play();
                            weapons.get(WEAPONS.GMEOWK18).setTotalBullets(weapons.get(WEAPONS.GMEOWK18).getTotalBullets() + pickup.getAmount());
                            objectsToRemove.add(pickup);
                        }

                    }
                }
            }
        }
        objects.removeAll(objectsToRemove);
        if (stepDtCounter + dt > (speed == walkSpeed ? dWalk : dRun)){
            stepSound.play();
            stepDtCounter = 0;
        } else {
            stepDtCounter += dt;
        }
        posX += x * speed;
        posY += y * speed;
    }
    public void weaponTick(float dt){
        Weapon gun = weapons.get(currentWeapon);
        currentCrosshair = defaultCrosshair;
        if (isReloading){
            currentCrosshair = cancelCrosshair;
            reloadDtCounter += dt;
            if (reloadDtCounter > gun.getReloadTime()){
                isReloading = false;
                reloadDtCounter = 0;
            }
        } else if (isShooting) {
            shootDtCounter += dt;
            if (shootDtCounter > gun.getFireRate()){
                isShooting = false;
                shootDtCounter = 0;
            } else {
                if (shootDtCounter / gun.getAnimSpeed() > 1)
                    currentWeaponFrame = gun.getFrames().get(0);
                else
                    currentWeaponFrame = gun.getFrames().get(min(gun.getFrames().size()-1,(int) (shootDtCounter / gun.getAnimSpeed() * gun.getFrames().size())));
            }
        } else {
            currentWeaponFrame = gun.getFrames().get(0);
        }
    }
    public void weaponReload(){
        Weapon gun = weapons.get(currentWeapon);
        if (currentWeapon == WEAPONS.SHOTGUN)
            // для дробовика особое поведение
            return;
        if (!isReloading && !isShooting &&
                gun.getMagBullets() < gun.getMagSize() &&
                gun.getTotalBullets() > 0
        ) {
            isReloading = true;
            if (gun.getTotalBullets() < gun.getMagSize()-gun.getMagBullets()){
                gun.setMagBullets(gun.getMagBullets() + gun.getTotalBullets());
                gun.setTotalBullets(0);
            } else {
                gun.setTotalBullets(gun.getTotalBullets()-(gun.getMagSize()-gun.getMagBullets()));
                gun.setMagBullets(gun.getMagSize());
            }
            gun.getReloadSound().play();
        }
    }
    public void weaponShoot(){
        Weapon gun = weapons.get(currentWeapon);
        if (currentWeapon == WEAPONS.SHOTGUN && !isShooting && gun.getMagBullets() > 0){
            isShooting = true;
            gun.getShootSound().play();
            gun.setMagBullets(0);
            if (gun.getTotalBullets() >= 2){
                gun.setMagBullets(2);
                gun.setTotalBullets(gun.getTotalBullets()-2);
            }
        }
        else if (!isReloading && !isShooting && gun.getMagBullets() > 0){
            gun.setMagBullets(gun.getMagBullets()-1);
            isShooting = true;
            gun.getShootSound().play();
        }
    }
    public void gunSwap(WEAPONS weapon){
        if (currentWeapon != weapon && !isShooting && !isReloading && weapons.get(weapon).isInInventory()){
            currentWeapon = weapon;
            weapons.get(currentWeapon).getSwapSound().play();
        }
    }
    public boolean isReloading(){
        return isReloading;
    }
    public boolean isShooting(){
        return isShooting;
    }
    public Texture getCrosshair(){
        return currentCrosshair;
    }
    public TextureRegion getCurrentWeaponFrame() {
        return currentWeaponFrame;
    }
    public Weapon getCurrentGun(){
        return weapons.get(currentWeapon);
    }

    public float getHP() {
        return hp;
    }
}
