package com.example.demoandengine.factory;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.example.demoandengine.ResourceManager;
import com.example.demoandengine.entity.Glass;

public class GlassFactory {
	private static GlassFactory INSTANCE = new GlassFactory();
	private VertexBufferObjectManager vbom;

	private GlassFactory() {
	}

	public static GlassFactory getInstance() {
		return INSTANCE;
	}

	public Glass createGlass(float x, float y) {
		Glass glass = new Glass(x, y, ResourceManager.getInstance().mGlass,
				vbom);

		return glass;
	}
}
