package br.com.enginer.domain.ui.usercase.schema.field.behavior;

/**
 * 
 */
public class Position {

	private Integer x;
	private Integer y;
	
	/**
	 * 
	 */
	public Position() {
		super();
	}

	/**
	 * @param x
	 * @param y
	 */
	public Position(Integer x, Integer y) {
		super();
		this.x = x;
		this.y = y;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}
}
