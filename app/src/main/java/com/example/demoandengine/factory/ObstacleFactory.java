package com.example.demoandengine.factory;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.example.demoandengine.ResourceManager;
import com.example.demoandengine.entity.Brick;
import com.example.demoandengine.entity.Glass;
import com.example.demoandengine.entity.Orange;
import com.example.demoandengine.entity.StarveFlower;
import com.example.demoandengine.entity.StarveFlower.State;
import com.example.demoandengine.entity.Stone;
import com.example.demoandengine.entity.WaterMelon;

public class ObstacleFactory {
	private static ObstacleFactory INSTANCE = new ObstacleFactory();
	private VertexBufferObjectManager vbom;

	private ObstacleFactory() {
	}

	public static ObstacleFactory getInstance() {
		return INSTANCE;
	}

	public Brick createBrick(float x, float y) {
		Brick brick = new Brick(x, y, ResourceManager.getInstance().mBrick,
				vbom);

		return brick;
	}

	public Orange createOrange(float x, float y) {
		Orange orange = new Orange(x, y, ResourceManager.getInstance().mOrange,
				vbom);

		return orange;
	}

	public Stone createStone(float x, float y) {
		Stone Stone = new Stone(x, y, ResourceManager.getInstance().mStone,
				vbom);

		return Stone;
	}

	public Glass createGlass(float x, float y) {
		Glass glass = new Glass(x, y, ResourceManager.getInstance().mGlass,
				vbom);

		return glass;
	}

	public WaterMelon createWaterMelon(float x, float y) {
		WaterMelon waterMelon = new WaterMelon(x, y,
				ResourceManager.getInstance().mWaterMelon, vbom);

		return waterMelon;
	}
	
	public StarveFlower createStarveFlower(float x, float y, State state) {
		StarveFlower starveFlower = new StarveFlower(x, y,
				ResourceManager.getInstance().mStarveFlower, vbom, state);

		return starveFlower;
	}
}
