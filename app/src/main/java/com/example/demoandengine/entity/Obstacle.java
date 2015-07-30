package com.example.demoandengine.entity;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;

import com.example.demoandengine.ResourceManager;
import com.example.demoandengine.scence.GameScene.PlayerDirection;
import com.example.demoandengine.utils.Constants;

public abstract class Obstacle extends AnimatedSprite {

	public Obstacle(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
	}

	public int getCol() {
		return getX() % 24 > 0 ? (int) (getX() / 24) + 1 : (int) getX() / 24;
	}

	public int getRow() {
		return getY() % 24 > 0 ? (int) (getY() / 24) + 1 : (int) getY() / 24;
	}

	public void move(PlayerDirection direction, final int globalId) {
		final IEntityModifier.IEntityModifierListener modifier = new IEntityModifier.IEntityModifierListener() {

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
				ResourceManager.getInstance()
						.updateData(getRow(), getCol(), -1);
			}

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				ResourceManager.getInstance().updateData(getRow(), getCol(),
						globalId);
			}
		};
		switch (direction) {
		case DOWN:
			registerEntityModifier(new MoveYModifier(1, getY(), getY()
					+ Constants.HEIGHT_CELL, modifier));
			break;
		case UP:
			registerEntityModifier(new MoveYModifier(1, getY(), getY()
					- Constants.HEIGHT_CELL, modifier));
			break;
		case RIGHT:
			registerEntityModifier(new MoveXModifier(1, getX(), getX()
					+ Constants.WIDTH_CELL, modifier));
			break;
		case LEFT:
			registerEntityModifier(new MoveXModifier(1, getX(), getX()
					- Constants.WIDTH_CELL, modifier));
			break;
		default:
			break;
		}
	}

	public abstract boolean isAblePush();
}
