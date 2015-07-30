package com.example.demoandengine.entity;

import java.util.Observable;
import java.util.Observer;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;

import com.example.demoandengine.DataManager;
import com.example.demoandengine.ResourceManager;
import com.example.demoandengine.entity.Princess.State;
import com.example.demoandengine.scence.GameScene.PlayerDirection;
import com.example.demoandengine.utils.Constants;

public class Stone extends Obstacle implements Observer {
	private boolean falling = false;

	public Stone(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		DataManager.getInstance().addObserver(this);
	}

	@Override
	public boolean isAblePush() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(Observable observable, Object data) {
		final int height = getHeight(getRow(), getCol());
		if (height > 0 && !falling) {
			fall(height);
		}
	}

	private void fall(final int height) {
		final IEntityModifier.IEntityModifierListener modifier = new IEntityModifier.IEntityModifierListener() {

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
				falling = true;
				ResourceManager.getInstance().updateData(getRow() + height,
						getCol(), Constants.STONE);
				ResourceManager.getInstance().updateData(getRow(), getCol(),
						Constants.STEPED_FIELD);
			}

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				falling = false;
				if (Princess.getInstance().getState() == State.WILL_DIE) {
					Princess.getInstance().setState(State.DIED);
				}
			}
		};

		registerEntityModifier(new MoveYModifier(1, getY(), getY()
				+ Constants.HEIGHT_CELL * height, modifier));
	}

	private int getHeight(int row, int col) {
		int height = 0;
		int frontStone = 0;
		while (true) {
			row = row + 1;
			if (DataManager.getInstance().getTileId(row, col) == Constants.STEPED_FIELD
					&& (Princess.getInstance().getRow() != row || Princess
							.getInstance().getCol() != col)) {
				height = height + 1;

				if (DataManager.getInstance().getTileId(row, col) == Constants.STONE) {
					frontStone = frontStone + 1;
				}

			} else if (Princess.getInstance().getRow() == row
					&& Princess.getInstance().getCol() == col && height != 0) {
				Princess.getInstance().setState(State.WILL_DIE);
				break;
			} else {
				break;
			}
		}

		height = height - frontStone;

		return height;
	}

}
