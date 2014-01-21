package com.kargames.flumpgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kargames.flumpgdx.gdx.GdxLayer;
import com.kargames.flumpgdx.gdx.GdxLibrary;
import com.kargames.flumpgdx.gdx.GdxMovie;

public class Gui extends Stage{
	public static Skin skin= new Skin(Gdx.files.internal("assets/uiskin.json"));
	SelectBox movieSelector;
	SelectBox layerSelector;
	SelectBox speedSelector;
	CheckBox visible;
	Label timelineLabel;
	Slider timeline;
	
	GdxLibrary library;
	GdxMovie currentMovie;
	
	PanStage panStage;
	
	public Gui(GdxLibrary lib, PanStage stage) {
		library = lib;
		panStage = stage;
		String[] items = {"item1", "item2"};
		String[] speeds = {"0.125x", "0.25x", "0.5x", "1x", "2x"};
		movieSelector = new SelectBox(items, skin);
		layerSelector = new SelectBox(items, skin);
		speedSelector = new SelectBox(speeds, skin);
		visible = new CheckBox("visible", skin);
		timelineLabel = new Label("0.00", skin);
		timeline = new Slider(0, 100, 0.01f, false, skin);
		
		timeline.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				currentMovie.setCurrent(timeline.getValue());
			}
		});
		
		timeline.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				currentMovie.pause();
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent arg0, float arg1, float arg2,
					int arg3, int arg4) {
				currentMovie.resume();
				super.touchUp(arg0, arg1, arg2, arg3, arg4);
			}
			
		});
		
		visible.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GdxLayer.selected.setVisible(visible.isChecked());
			}
		});
		
		movieSelector.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				String s = movieSelector.getSelection();
				
				currentMovie = library.movies.get(s);
				
				String[] layers = new String[currentMovie.layers.values().size()];
				int i = 0;
				
				for (GdxLayer layer : currentMovie.layers.values()) {
					layers[i++] = layer.name;
				}
				
				layerSelector.setItems(layers);
				panStage.setMovie(currentMovie);
				timeline.setRange(0, currentMovie.getDuration());

				currentMovie.setCurrent(0);
			}
		});

		layerSelector.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				String layer = layerSelector.getSelection();
				currentMovie.layers.get(layer).select();
			}
		});

		speedSelector.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				currentMovie.setSpeed((float)Math.pow(2, speedSelector.getSelectionIndex()));
			}
		});
		
		speedSelector.setSelection("1x");
		
		Table table = new Table();
		table.setFillParent(true);
		table.defaults().pad(10);
		table.bottom().left();
		table.add(movieSelector);
		table.add(layerSelector);
		table.add(visible);
		table.add(speedSelector);
		table.add(timelineLabel);
		table.add(timeline).fillX().expandX();
		addActor(table);
	}
	
	public void setMovies(Object[] movies){
		movieSelector.setItems(movies);
	}
	

	@Override
	public void act() {
		timeline.setValue(currentMovie.getCurrent());
		timelineLabel.setText(String.format("%.2f / %.2f", 
				currentMovie.getCurrent(), currentMovie.getDuration()));
		layerSelector.setSelection(GdxLayer.selected.name);
		super.act();
	}
	
	
}
