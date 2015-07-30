package com.example.demoandengine;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class CustomDigitalOnScreenControl extends DigitalOnScreenControl {

	public CustomDigitalOnScreenControl(float pX, float pY, Camera pCamera,
			ITextureRegion pControlBaseTextureRegion,
			ITextureRegion pControlKnobTextureRegion,
			float pTimeBetweenUpdates,
			VertexBufferObjectManager pVertexBufferObjectManager,
			IOnScreenControlListener pOnScreenControlListener) {
		super(pX, pY, pCamera, pControlBaseTextureRegion,
				pControlKnobTextureRegion, pTimeBetweenUpdates,
				pVertexBufferObjectManager, pOnScreenControlListener);
	}

}
