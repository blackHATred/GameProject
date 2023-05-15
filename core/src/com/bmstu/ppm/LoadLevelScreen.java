package com.bmstu.ppm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;

public class LoadLevelScreen implements Screen {
    final PPMGame game;
    private Stage stage;
    private Tree tree;

    public LoadLevelScreen(final PPMGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table rootTable = new Table(game.skin);
        rootTable.background("window");
        rootTable.setDebug(true);
        rootTable.setFillParent(true);
        stage.addActor(rootTable);
        rootTable.add(new Label("Выбери файл сохранения", game.skin));
        rootTable.add();
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
