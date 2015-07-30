package com.example.demoandengine.factory;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.example.demoandengine.ResourceManager;
import com.example.demoandengine.entity.Stone;

public class StoneFactory {
	private static StoneFactory INSTANCE = new StoneFactory();
	private VertexBufferObjectManager vbom;

	private StoneFactory() {
	}

	public static StoneFactory getInstance() {
		return INSTANCE;
	}

	public Stone createStone(float x, float y) {
		Stone stone = new Stone(x, y, ResourceManager.getInstance().mStone,
				vbom);

		return stone;
	}
}
