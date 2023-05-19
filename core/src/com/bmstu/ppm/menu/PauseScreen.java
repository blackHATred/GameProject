package com.bmstu.ppm.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.bmstu.ppm.PPMGame;
import com.bmstu.ppm.STAGE;

public class PauseScreen implements Screen {
    final PPMGame game;
    private Stage stage;

    public PauseScreen(final PPMGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table rootTable = new Table(game.skin);
        rootTable.background("window");
        rootTable.setFillParent(true);
        stage.addActor(rootTable);
        TextButton continueButton = new TextButton("Продолжить", game.skin);
        TextButton settingsButton = new TextButton("Настройки", game.skin);
        TextButton exitButton = new TextButton("Выйти в меню", game.skin);
        rootTable.add(continueButton).expandX();
        rootTable.row();
        rootTable.add(settingsButton).expandX();
        rootTable.row();
        rootTable.add(exitButton).expandX();

        continueButton.addListener( new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(game.screens.get(STAGE.MAIN_MENU_LOAD));
            }
        });
        settingsButton.addListener( new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(game.screens.get(STAGE.MAIN_MENU_SETTINGS));
            }
        });
        exitButton.addListener( new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
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
        stage.dispose();
    }
}
