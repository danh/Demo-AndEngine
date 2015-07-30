package com.example.demoandengine.factory;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.example.demoandengine.ResourceManager;
import com.example.demoandengine.entity.Orange;
import com.example.demoandengine.entity.PurpleMushroom;
import com.example.demoandengine.entity.RedMushroom;

public class ItemFactory {
	private static ItemFactory INSTANCE = new ItemFactory();
	private VertexBufferObjectManager vbom;

	private ItemFactory() {
	}

	public static ItemFactory getInstance() {
		return INSTANCE;
	}

	public Orange createOrange(float x, float y) {
		Orange orange = new Orange(x, y, ResourceManager.getInstance().mOrange,
				vbom);

		return orange;
	}
	
	public RedMushroom createRedMushroom(float x, float y) {
		RedMushroom redMushroom = new RedMushroom(x, y, ResourceManager.getInstance().mRedMushroom,
				vbom);

		return redMushroom;
	}
	
	public PurpleMushroom createPurpleMushroom(float x, float y) {
		PurpleMushroom purpleMushroom = new PurpleMushroom(x, y, ResourceManager.getInstance().mPurpleMushroom,
				vbom);

		return purpleMushroom;
	}

}
