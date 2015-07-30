package com.example.demoandengine.factory;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.example.demoandengine.ResourceManager;
import com.example.demoandengine.entity.Princess;

public class PrincessFactory {
	private static PrincessFactory INSTANCE = new PrincessFactory();
	private VertexBufferObjectManager vbom;
	
	private PrincessFactory() {	}
	
	public static PrincessFactory getInstance() {
		return INSTANCE;
	}
	
	// public Princess createPlayer(float x, float y) {
	// Princess player = new Princess(x, y,
	// ResourceManager.getInstance().mPrincessTextureRegion, vbom);
	// player.setZIndex(2);
	//
	// return player;
	// }
}
