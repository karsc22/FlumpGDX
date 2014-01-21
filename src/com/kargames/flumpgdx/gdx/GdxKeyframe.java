package com.kargames.flumpgdx.gdx;

import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.kargames.flumpgdx.flump.FlumpKeyframe;

public class GdxKeyframe {
	public boolean tweened;
	public String ref;
	public float index;
	public float duration;
	public float alpha;

	public Vector2 pivot;
	public Vector2 scale;
	public Vector2 skew;
	public Vector2 loc;
	
	public GdxTexture texture;
	
	public GdxKeyframe(Map<String, GdxTexture> textures, FlumpKeyframe fkf) {
		tweened = fkf.tweened;
		ref = fkf.ref;
		index = fkf.index;
		duration = fkf.duration;
		alpha = fkf.alpha;

		pivot = getVal(fkf.pivot); 
		scale = getVal(fkf.scale); 
		skew = getVal(fkf.skew); 
		loc = getVal(fkf.loc); 
		
		texture = textures.get(ref);
	}
	
	private Vector2 getVal(float[] val) {
		if (val == null || val.length < 2) return new Vector2(1, 1);
		return new Vector2(val[0], val[1]);
	}

}
