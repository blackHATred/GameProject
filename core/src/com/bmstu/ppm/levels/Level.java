package com.bmstu.ppm.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bmstu.ppm.PPMGame;
import com.bmstu.ppm.STAGE;
import com.bmstu.ppm.SuperDuperMath;
import com.bmstu.ppm.gameobjects.*;

import java.util.ArrayList;
import java.util.Collections;

public class Level implements Screen {
    protected final PPMGame game;
    protected final Stage stage;
    protected Player player;
    protected Texture skybox;
    protected Texture floor;
    protected SpriteBatch batch;
    protected ShapeRenderer shapeRenderer;
    protected float fogLevel = 200;
    protected final ArrayList<GameObject> objects;
    protected final int tileSize = 100;
    private float reloadTimer = 0;
    /** UI */
    private final Label FPSCounter;
    private final Label Bullets;
    private final Texture UI;
    private final Texture red_piece;
    private final Texture green_piece;
    private final Texture FaceGood;
    private final Texture FaceBad;
    private final Texture FaceDead;

    public Level(final PPMGame game, int[][] map, Texture skybox, Texture floor, ArrayList<GameObject> objects) {
        this.game = game;
        this.objects = objects;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                switch (map[i][j]){
                    case 1 -> {
                        // Спавним игрока
                        player = new Player(j*tileSize+tileSize/2f, i*tileSize+tileSize/2f);
                    }
                    case 2 -> {
                        // рисуем кирпичную стенку 1
                        this.objects.add(new Wall(i*tileSize, j*tileSize, i*tileSize, (j+1)*tileSize, "Textures/256x256/256_Brick 01 Mud.png"));
                        this.objects.add(new Wall(i*tileSize, j*tileSize, (i+1)*tileSize, j*tileSize, "Textures/256x256/256_Brick 01 Mud.png"));
                        this.objects.add(new Wall((i+1)*tileSize, (j+1)*tileSize, (i+1)*tileSize, j*tileSize, "Textures/256x256/256_Brick 01 Mud.png"));
                        this.objects.add(new Wall(i*tileSize, (j+1)*tileSize, (i+1)*tileSize, (j+1)*tileSize, "Textures/256x256/256_Brick 01 Mud.png"));
                    }
                }
            }
        }
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        this.skybox = skybox;
        this.floor = floor;
        this.FPSCounter = new Label("0", game.skin);
        this.Bullets = new Label("0", game.skin);
        this.UI = new Texture(Gdx.files.internal("Textures/UI/ui.png"));
        this.red_piece = new Texture(Gdx.files.internal("Textures/UI/redpiece.png"));
        this.green_piece = new Texture(Gdx.files.internal("Textures/UI/greenpiece.png"));
        this.FaceGood = new Texture(Gdx.files.internal("Textures/UI/Face_good.png"));
        this.FaceBad = new Texture(Gdx.files.internal("Textures/UI/Face_pain.png"));
        this.FaceDead = new Texture(Gdx.files.internal("Textures/UI/Face_dead.png"));
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Table rootTable = new Table(game.skin);
        rootTable.setFillParent(true);
        stage.addActor(rootTable);
        rootTable.add(FPSCounter).align(Align.topRight).expandX().expandY();
        rootTable.row();
        rootTable.add(Bullets).align(Align.bottomRight).expandX().expandY();
        rootTable.row();
    }

    @Override
    public void render(float dt) {
        // System.out.println(dt);
        // Очищаем экран
        Gdx.gl.glClearColor(66f/255, 56f/255, 55f/255,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //renderPixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);

        // Обрабатываем ввод с клавиатуры
        float x = 0, y = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {x+=Math.cos(player.getRotate())*dt; y+=Math.sin(player.getRotate())*dt;}
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {x+=Math.sin(player.getRotate())*dt; y+=-Math.cos(player.getRotate())*dt;}
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {x+=-Math.cos(player.getRotate())*dt;  y+=-Math.sin(player.getRotate())*dt;}
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {x+=-Math.sin(player.getRotate())*dt;  y+=Math.cos(player.getRotate())*dt;}
        if (x != 0 & y != 0) player.move(x, y, objects, dt);
        float shakeCoefficient = (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.D)) ? Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 4 : 2 : .5f;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) player.rotate(-dt, game.settings.sensitivity);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) player.rotate(dt, game.settings.sensitivity);
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) player.setRunSpeed(); else player.setWalkSpeed();
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) game.setScreen(game.screens.get(STAGE.PAUSE));
        if (Gdx.input.isKeyPressed(Input.Keys.R)) player.weaponReload();
        if (Gdx.input.isKeyPressed(Input.Keys.E)) player.weaponShoot();
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) player.gunSwap(WEAPONS.RIFLE);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) player.gunSwap(WEAPONS.SHOTGUN);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) player.gunSwap(WEAPONS.GMEOWK18);
        // Делаем тик для обработки кадра оружия
        player.weaponTick(dt);

        // Уровень рендера с учётом тряски экрана
        int renderLevel = (int) (Gdx.graphics.getHeight() / 2f + 10 * Math.sin(Math.PI* shakeCoefficient * System.currentTimeMillis()/1000d));

        // Рисуем скайбокс уровня
        batch.begin();
        batch.setColor(Colors.get("WHITE"));
        batch.draw(skybox, 0, Gdx.graphics.getHeight() - renderLevel, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Рисуем землю
        // ...

        // Рисуем стены и энтити
        int pixelCounter = -8/game.settings.graphics; // Какой пиксель по горизонтали обрабатываем
        for (float angle = player.getRotate() - game.settings.FOV/2f * SuperDuperMath.rad;
             angle <= player.getRotate() + game.settings.FOV/2f * SuperDuperMath.rad;
             angle += game.settings.FOV * 8f / Gdx.graphics.getWidth() * SuperDuperMath.rad /game.settings.graphics
        ){
            ArrayList<GameObject> objsToRender = new ArrayList<>(10);
            objsToRender.add(null);
            // Обновлённый угол с учётом тряски экрана
            float ang = angle - .01f*(float)Math.sin(Math.PI* shakeCoefficient /2*System.currentTimeMillis()/1000d);
            pixelCounter+=8/game.settings.graphics;
            // Ищем ближайшую для рендера стену
            SuperDuperMath.Point intersect;
            float dist = 1000000000;
            for (GameObject obj: objects){
                if (obj.getClass() == Wall.class){
                    intersect = SuperDuperMath.lineIntersect(player, obj, ang);
                    if (intersect != null && SuperDuperMath.dist(player, intersect) < dist){
                        dist = SuperDuperMath.dist(player, intersect);
                        objsToRender.set(0, obj);
                    }
                }
            }
            // Нашли стену, теперь ищем все объекты перед ней
            for (GameObject obj: objects){
                if (obj.getClass() == Pickup.class || obj.getClass() == Enemy.class){
                    intersect = SuperDuperMath.lineIntersect(player, obj, ang);
                    if (intersect != null && SuperDuperMath.dist(player, intersect) < dist){
                        objsToRender.add(obj);
                    }
                }
            }
            // Сортируем полученный массив по мере отдаления
            objsToRender.sort((obj1, obj2) -> {
                float dist1, dist2;
                SuperDuperMath.Point inter1 = SuperDuperMath.lineIntersect(player, obj1, ang);
                dist1 = SuperDuperMath.dist(player, inter1);
                SuperDuperMath.Point inter2 = SuperDuperMath.lineIntersect(player, obj2, ang);
                dist2 = SuperDuperMath.dist(player, inter2);
                return Float.compare(dist1, dist2);
            });
            // Рендерим объекты по мере отдаления
            Collections.reverse(objsToRender);
            for (GameObject obj: objsToRender) {
                intersect = SuperDuperMath.lineIntersect(player, obj, ang);
                dist = SuperDuperMath.dist(player, intersect);
                // Устраняем артефакты изображения при необходимости
                if(!game.settings.fishEye) dist *= Math.cos(player.getRotate()-ang);
                int h = (int) (player.getVFOV_const()*Gdx.graphics.getHeight()/dist);
                // эффект тумана
                batch.setColor(dist > fogLevel ? fogLevel / dist : 1f, dist > fogLevel ? fogLevel / dist : 1f, dist > fogLevel ? fogLevel / dist : 1f, 1f);
                if (obj.getClass() == Wall.class)
                    batch.draw(
                            new TextureRegion(
                                    ((Wall)obj).texture,
                                    (int) (((Wall)obj).texture.getWidth() * SuperDuperMath.dist(((Wall)obj).getPosX1(), ((Wall)obj).getPosY1(), intersect.getX(), intersect.getY()) / ((Wall)obj).getWidth()),
                                    0,
                                    1,
                                    ((Wall)obj).texture.getHeight()
                            ),
                            pixelCounter,
                            Gdx.graphics.getHeight()-renderLevel-h,
                            8f/game.settings.graphics,
                            2*h
                    );
                else if (obj.getClass() == Enemy.class)
                    batch.draw(
                            new TextureRegion(
                                    ((Enemy)obj).getCurrentTexture(),
                                    (int) (((Enemy)obj).getCurrentTexture().getRegionWidth() * SuperDuperMath.dist(((Enemy)obj).getPosX1(), ((Enemy)obj).getPosY1(), intersect.getX(), intersect.getY()) / ((Enemy)obj).getWidth()),
                                    0,
                                    1,
                                    ((Enemy)obj).getCurrentTexture().getRegionHeight()
                            ),
                            pixelCounter,
                            Gdx.graphics.getHeight()-renderLevel-h,
                            8f/game.settings.graphics,
                            2*h
                    );
                else if (obj.getClass() == Pickup.class) {
                    Pickup pickup = (Pickup) obj;
                    batch.draw(
                            new TextureRegion(
                                    pickup.getSprite(),
                                    (int) (pickup.getSprite().getWidth() * SuperDuperMath.dist(pickup.getPosX() + pickup.getRadius() * (float) Math.cos(angle+Math.PI/2), pickup.getPosY() + pickup.getRadius() * (float) Math.sin(angle+Math.PI/2), intersect.getX(), intersect.getY()) / pickup.getSprite().getWidth()),
                                    0,
                                    1,
                                    pickup.getSprite().getHeight()
                            ),
                            pixelCounter,
                            Gdx.graphics.getHeight() - renderLevel - h,
                            8f / game.settings.graphics,
                            2 * h * pickup.getHProportion()
                    );
                }
            }
        }
        batch.setColor(1f, 1f, 1f, 1f);
        //оружие
        if (player.isReloading()){
            // рисуем оружие со сдвигом
            batch.draw(
                    player.getCurrentWeaponFrame(),
                    (Gdx.graphics.getWidth() - Gdx.graphics.getHeight()*player.getCurrentGun().getScale()) / 2f - player.getCurrentGun().getPixelScreenOffsetX(),
                    renderLevel-Gdx.graphics.getHeight()/2f - player.getCurrentGun().getPixelScreenOffsetY() - (player.getCurrentGun().getReloadTime() - reloadTimer) * 50,
                    Gdx.graphics.getHeight()*player.getCurrentGun().getScale(),
                    Gdx.graphics.getHeight()*player.getCurrentGun().getScale() * player.getCurrentWeaponFrame().getRegionHeight() /player.getCurrentWeaponFrame().getRegionWidth()
            );
        } else {
            // отрисовываем обычный кадр
            reloadTimer = 0;
            batch.draw(
                    player.getCurrentWeaponFrame(),
                    (Gdx.graphics.getWidth() - Gdx.graphics.getHeight()*player.getCurrentGun().getScale()) / 2f - player.getCurrentGun().getPixelScreenOffsetX(),
                    renderLevel-Gdx.graphics.getHeight()/2f - player.getCurrentGun().getPixelScreenOffsetY(),
                    Gdx.graphics.getHeight()*player.getCurrentGun().getScale(),
                    Gdx.graphics.getHeight()*player.getCurrentGun().getScale() * player.getCurrentWeaponFrame().getRegionHeight() /player.getCurrentWeaponFrame().getRegionWidth()
            );
        }
        // прицел
        batch.setColor(0f, 1f, 0f, 0.7f);
        if (player.getCrosshair() != null)
            batch.draw(player.getCrosshair(),
                    (Gdx.graphics.getWidth() - Gdx.graphics.getHeight()*.05f) / 2f,
                    (Gdx.graphics.getHeight() - Gdx.graphics.getHeight()*.05f) / 2f,
                    Gdx.graphics.getHeight()*.05f, Gdx.graphics.getHeight()*.05f);
        // интерфейс
        batch.setColor(1f, 1f, 1f, 1f);
        if (player.getHP() > 33) {
            if (player.getHP() > 66) {
                batch.draw(FaceGood, 33, Gdx.graphics.getHeight() - 85, 50, 50);
            } else {
                batch.draw(FaceBad, 33, Gdx.graphics.getHeight() - 85, 50, 50);
            }
        } else {
            batch.draw(FaceDead, 33, Gdx.graphics.getHeight() - 85, 50, 50);
        }
        batch.draw(UI, 10, Gdx.graphics.getHeight() - 110, 200, 100);
        if (player.getCurrentGun().getMagBullets()*1f / player.getCurrentGun().getMagSize() > 1f / 4)
            batch.draw(green_piece, 108, Gdx.graphics.getHeight() - 100, 15, 20);
        if (player.getCurrentGun().getMagBullets()*1f / player.getCurrentGun().getMagSize() > 2f / 4)
            batch.draw(green_piece, 135, Gdx.graphics.getHeight() - 103, 13, 25);
        if (player.getCurrentGun().getMagBullets()*1f / player.getCurrentGun().getMagSize() > 3f / 4)
            batch.draw(green_piece, 150, Gdx.graphics.getHeight() - 103, 13, 25);
        if (player.getCurrentGun().getMagBullets()*1f == player.getCurrentGun().getMagSize())
            batch.draw(green_piece, 170, Gdx.graphics.getHeight() - 100, 15, 20);

        if (player.getHP() > 0)
            batch.draw(red_piece, 108, Gdx.graphics.getHeight() - 100 + 60, 15, 20);
        if (player.getHP() > 25)
            batch.draw(red_piece, 135, Gdx.graphics.getHeight() - 103 + 60, 13, 25);
        if (player.getHP() > 50)
            batch.draw(red_piece, 150, Gdx.graphics.getHeight() - 103 + 60, 13, 25);
        if (player.getHP() > 75)
            batch.draw(red_piece, 170, Gdx.graphics.getHeight() - 100 + 60, 15, 20);

        batch.end();
        Bullets.setText(player.getCurrentGun().getMagBullets() +"/"+ player.getCurrentGun().getTotalBullets());
        FPSCounter.setText(String.valueOf(Math.floor(1/dt)));
        stage.act(dt);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("resize");
        stage.getViewport().update(width, height, true);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
