package com.example.demoandengine.model;

public class Data {
	private int row;
	private int col;
	private int value;
	public Data(int row, int col, int value){
		this.setRow(row);
		this.setCol(col);
		this.setValue(value);
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
}
