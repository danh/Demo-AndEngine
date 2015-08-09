package com.example.demoandengine;

import java.util.Observable;

import org.andengine.extension.tmx.TMXTile;

import com.example.demoandengine.model.Data;

public class DataManager extends Observable {
	private volatile static DataManager instance;
	private int[][] mTileIds;
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
		this.setCols(cols);
		this.setRows(rows);

		mTileIds = new int[getRows()][getRows()];

		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				mTileIds[i][j] = tile[i][j].getGlobalTileID();
			}
		}
	}

	public int getTileId(int row, int col) {
		if (row >= getRows() || row < 0 || col >= getCols() || col < 0) {
			return -1;
		}
		return mTileIds[row][col];
	}

	public void updateTileId(int row, int col, int value) {
		mTileIds[row][col] = value;
		setChanged();
		notifyObservers(new Data(row, col, value));
	}

	public int[][]getTileIds(){
		return  mTileIds;
	}

	public int getRows() {
		return mRows;
	}

	public void setRows(int mRows) {
		this.mRows = mRows;
	}

	public int getCols() {
		return mCols;
	}

	public void setCols(int mCols) {
		this.mCols = mCols;
	}
}
