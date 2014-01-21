package com.kargames.flumpgdx.gdx;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.kargames.flumpgdx.FlumpGdx;
import com.kargames.flumpgdx.flump.Atlas;
import com.kargames.flumpgdx.flump.FlumpLibrary;
import com.kargames.flumpgdx.flump.FlumpTexture;
import com.kargames.flumpgdx.flump.Movie;
import com.kargames.flumpgdx.flump.TextureGroup;

public class GdxLibrary {
	public String md5;
	public int frameRate;
	public Map<String, GdxMovie> movies;
	
	public GdxLibrary(FlumpLibrary lib) {
		md5 = lib.md5;
		frameRate = lib.frameRate;
		Map<String, GdxTexture> textures = getTextures(lib.textureGroups);
		
		movies = new HashMap<String, GdxMovie>();
		for (Movie movie : lib.movies) {
			movies.put(movie.id, new GdxMovie(textures, movie));
		}
	}
	
	
	public Map<String, GdxTexture> getTextures(Array<TextureGroup> textureGroups) {
		Map<String, GdxTexture> textures = new HashMap<String, GdxTexture>();
		for (TextureGroup group : textureGroups) {
			for (Atlas atlas : group.atlases) {
				Texture texture = new Texture(Gdx.files.internal(FlumpGdx.path + atlas.file));
				for (FlumpTexture flumpTexture : atlas.textures) {
					GdxTexture s = new GdxTexture(texture, flumpTexture, group.scaleFactor);
					System.out.println("putting null symbol");
					textures.put(flumpTexture.symbol, s);
				}
			}
		}
		return textures;
	}
	
	
}
