package com.example.demoandengine;

import java.util.Observable;

import org.andengine.extension.tmx.TMXTile;

import com.example.demoandengine.model.Data;

public class DataManager extends Observable {
	private volatile static DataManager instance;
	private int[] mTileIds;
	private int mRows;
	private int mCols;

	private DataManager() {

	}

	public static DataManager getInstance() {
		if (instance == null) {
			synchronized (DataManager.class) {
				if (instance == null) {
					instance = new DataManager();
				}
			}
		}

		return instance;
	}

	public void setTile(TMXTile[][] tile, int rows, int cols) {
		this.mCols = cols;
		this.mRows = rows;

		mTileIds = new int[mRows * mCols];

		int position = 0;
		for (int i = 0; i < mRows; i++) {
			for (int j = 0; j < mCols; j++) {
				position = i * mCols + j;
				mTileIds[position] = tile[i][j].getGlobalTileID();
			}
		}
	}

	public int getTileId(int row, int col) {
		if (row >= mRows || row < 0 || col >= mCols || col < 0) {
			return -1;
		}
		int position = row * mCols + col;
		return mTileIds[position];
	}

	public void updateTileId(int row, int col, int value) {
		int position = row * mCols + col;
		mTileIds[position] = value;
		setChanged();
		notifyObservers(new Data(row, col, value));
	}
}
