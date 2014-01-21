package com.kargames.flumpgdx.gdx;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.kargames.flumpgdx.flump.FlumpLayer;
import com.kargames.flumpgdx.flump.Movie;

public class GdxMovie extends WidgetGroup{

	String id;
	public Map<String, GdxLayer> layers;
	float duration;
	float current;
	private boolean isPaused;
	float playSpeed = 1f;
	
	public GdxMovie(Map<String, GdxTexture> textures, Movie movie) {
		id = movie.id;
		layers = new HashMap<String, GdxLayer>();
		duration = 0;
		current = 0;
		for (FlumpLayer layer : movie.layers) {
			GdxLayer gdxLayer = new GdxLayer(textures, layer, this);
			addActor(gdxLayer);
			if (gdxLayer.duration > duration) {
				duration = gdxLayer.duration;
			}
			System.out.println("GdxMovie: movie: " + movie.id + 
					" layer:" + layer.name + " duration:" + duration);
			layers.put(layer.name, gdxLayer);
		}
		isPaused = false;
	}
	public float getDuration() {
		return duration;
	}
	
	public float getCurrent() {
		return current;
	}

	@Override
	public void act(float dt) {
		if (!isPaused) {
			current += dt * playSpeed * 30;
		}
		if (current > duration) {
			current = 0;
		}
		super.act(dt);
	}
	
	public void setCurrent(float c) {
		current = c;
	}
	public void pause() {
		isPaused = true;
	}
	
	public void resume() {
		isPaused = false;
	}
	public void setSpeed(float speed) {
		playSpeed = speed / 8f;
	}
}
