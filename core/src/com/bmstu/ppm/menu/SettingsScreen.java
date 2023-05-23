package com.bmstu.ppm.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.bmstu.ppm.PPMGame;
import com.bmstu.ppm.STAGE;

import java.io.IOException;
import java.util.ArrayList;

public class SettingsScreen implements Screen {
    final PPMGame game;
    protected Stage stage;
    protected STAGE backTo;
    protected final TextButton back;
    protected final Slider sensitivity;
    protected final Slider volume;
    protected final Slider musicVolume;
    protected final Slider FOV;
    protected final Slider graphic;
    protected final SelectBox<String> resolution;
    protected final CheckBox fishEye;
    protected final Label sensitivityLabel;
    protected final Label volumeLabel;
    protected final Label musicVolumeLabel;
    protected final Label FOVLabel;
    protected final Label graphicLabel;
    protected final Label fishEyeLabel;
    public SettingsScreen(final PPMGame game) {
        this.game = game;
        back = new TextButton("Назад", game.skin);
        volume = new Slider(0, 100, 1, false, game.skin);
        volume.setValue(game.settings.volume);
        musicVolume = new Slider(0, 100, 1, false, game.skin);
        musicVolume.setValue(game.settings.musicVolume);
        sensitivity = new Slider(1, 10, 1, false, game.skin);
        sensitivity.setValue(game.settings.sensitivity);
        FOV = new Slider(60, 180, 2, false, game.skin);
        FOV.setValue(game.settings.FOV);
        graphic = new Slider(1, 8, 1, false, game.skin);
        graphic.setValue(game.settings.graphics);
        resolution = new SelectBox<>(game.skin);
        resolution.setItems(new Array<>(new String[]{"1920x1080", "1280x720", "854x480", "640x360"}));
        resolution.setSelected(game.settings.resolution);
        fishEye = new CheckBox("", game.skin);
        fishEye.setChecked(game.settings.fishEye);
        volumeLabel = new Label(String.valueOf(game.settings.volume), game.skin);
        musicVolumeLabel = new Label(String.valueOf(game.settings.musicVolume), game.skin);
        sensitivityLabel = new Label(String.valueOf(game.settings.sensitivity), game.skin);
        FOVLabel = new Label(String.valueOf(game.settings.FOV), game.skin);
        graphicLabel = new Label(String.valueOf(game.settings.graphics), game.skin);
        fishEyeLabel = new Label(game.settings.fishEye ? "Включено" : "Выключено", game.skin);
    }

    @Override
    public void show() {
        backTo = game.currentLevel;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Table rootTable = new Table(game.skin);
        rootTable.background("window");
        rootTable.setDebug(false);
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        rootTable.add(new Label("Чувствительность", game.skin));
        rootTable.add(sensitivity).pad(0, 10, 0, 10);
        rootTable.add(sensitivityLabel);
        rootTable.row();
        rootTable.add(new Label("Громкость звуков", game.skin));
        rootTable.add(volume).pad(0, 10, 0, 10);
        rootTable.add(volumeLabel);
        rootTable.row();
        rootTable.add(new Label("Громкость музыки", game.skin));
        rootTable.add(musicVolume).pad(0, 10, 0, 10);
        rootTable.add(musicVolumeLabel);
        rootTable.row();
        rootTable.add(new Label("Поле зрения", game.skin));
        rootTable.add(FOV).pad(0, 10, 0, 10);
        rootTable.add(FOVLabel);
        rootTable.row();
        rootTable.add(new Label("Разрешение окна", game.skin));
        rootTable.add(resolution).pad(0, 10, 0, 10).colspan(2);
        rootTable.row();
        rootTable.add(new Label("Качество графики", game.skin));
        rootTable.add(graphic).pad(0, 10, 0, 10);
        rootTable.add(graphicLabel);
        rootTable.row();
        rootTable.add(new Label("Эффект рыбьего глаза", game.skin));
        rootTable.add(fishEye).pad(0, 10, 0, 10);
        rootTable.add(fishEyeLabel);
        rootTable.row();
        rootTable.add(new Label("", game.skin));
        rootTable.row();
        rootTable.add(back).colspan(3);
        FOV.addListener(new ChangeListener(){public void changed(ChangeEvent event, Actor actor) {
            FOVLabel.setText(String.valueOf((int) FOV.getValue()));
            game.settings.FOV = (int) FOV.getValue();
            game.settings.save();
        }});
        sensitivity.addListener(new ChangeListener(){public void changed(ChangeEvent event, Actor actor) {
            sensitivityLabel.setText(String.valueOf((int) sensitivity.getValue()));
            game.settings.sensitivity = (int) sensitivity.getValue();
            game.settings.save();
        }});
        volume.addListener(new ChangeListener(){public void changed(ChangeEvent event, Actor actor) {
            volumeLabel.setText(String.valueOf((int) volume.getValue()));
            game.settings.volume = (int) volume.getValue();
            game.settings.save();
        }});
        musicVolume.addListener(new ChangeListener(){public void changed(ChangeEvent event, Actor actor) {
            musicVolumeLabel.setText(String.valueOf((int) musicVolume.getValue()));
            game.settings.musicVolume = (int) musicVolume.getValue();
            game.music.setVolume(musicVolume.getValue() / 100);
            game.settings.save();
        }});
        graphic.addListener(new ChangeListener(){public void changed(ChangeEvent event, Actor actor) {
            switch ((int) graphic.getValue()){
                case 3 -> graphic.setValue(2);
                case 5,6,7 -> graphic.setValue(4);
                default -> graphic.setValue(graphic.getValue());
            }
            graphicLabel.setText(String.valueOf((int) graphic.getValue()));
            game.settings.graphics = (int) graphic.getValue();
            game.settings.save();
        }});
        fishEye.addListener(new ChangeListener(){public void changed(ChangeEvent event, Actor actor) {
            fishEyeLabel.setText(fishEye.isChecked() ? "Включено" : "Выключено");
            game.settings.fishEye = fishEye.isChecked();
            game.settings.save();
        }});
        resolution.addListener(new ChangeListener(){public void changed(ChangeEvent event, Actor actor) {
            game.settings.resolution = resolution.getSelected();
            Gdx.graphics.setWindowedMode(
                    Integer.parseInt(resolution.getSelected().split("x")[0]),
                    Integer.parseInt(resolution.getSelected().split("x")[1])
            );
            game.settings.save();
        }});
        back.addListener( new ClickListener(){public void clicked(InputEvent event, float x, float y){
            game.setScreen(game.screens.get(backTo));
        }});
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
