package com.baselet.control.basics.geom;

public class Rectangle {

	public int x;
	public int y;
	public int width;
	public int height;

	public Rectangle() {
		super();
	}

	/**
	 * TODO as with DimensionDouble and PointDouble, Rectangle should also contain only double values in future!
	 */
	public Rectangle(Double x, Double y, Double width, Double height) {
		this((int) Math.round(x), (int) Math.round(y), (int) Math.round(width), (int) Math.round(height));
	}

	public Rectangle(int x, int y, int width, int height) {
		this();
		setBounds(x, y, width, height);
	}

	public void setBounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public int getX2() {
		return x + width;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public int getY2() {
		return y + height;
	}

	public PointDouble getUpperLeftCorner() {
		return new PointDouble(x, y);
	}

	public PointDouble getCenter() {
		return new PointDouble(x + width / 2, y + height / 2);
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public Dimension getSize() {
		return new Dimension(width, height);
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void move(int diffX, int diffY) {
		x += diffX;
		y += diffY;
	}

	public void addBorder(int border) {
		x -= border;
		y -= border;
		width += border * 2;
		height += border * 2;
	}

	public boolean contains(Point p) {
		return contains(new Rectangle(p.getX(), p.getY(), 0, 0));
	}

	public boolean contains(Rectangle other) {
		return getX() <= other.getX() && getX2() >= other.getX2() && getY() <= other.getY() && getY2() >= other.getY2();
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public boolean intersects(Rectangle other) {
		if (getY2() < other.getY()) {
			return false;
		}
		if (getY() > other.getY2()) {
			return false;
		}
		if (getX2() < other.getX()) {
			return false;
		}
		if (getX() > other.getX2()) {
			return false;
		}
		return true;
	}

	/**
	 * move the bounds of this rectangle to the lowest upper/left and highest lower/right bounds
	 * eg: Rect(x=-1,y=2,x2=3,y2=5).merge(Rect(x=2,y=1,x2=5,y2=3))=Rect(x=-1,y=1,x2=5,y2=5)
	 */
	public void merge(Rectangle other) {
		// must store X2 and Y2 before changing this X and Y, otherwise information can be lost
		// eg: this(y=100,h=10) and other(y=50,h=10) -> this.y2 is 110 but would be changed to 60)
		int oldX2 = getX2();
		int oldY2 = getY2();
		setX(Math.min(getX(), other.getX()));
		setY(Math.min(getY(), other.getY()));
		setWidth(Math.max(oldX2, other.getX2()) - getX());
		setHeight(Math.max(oldY2, other.getY2()) - getY());
	}

	public static Rectangle mergeToLeft(Rectangle left, Rectangle right) {
		if (left == null) {
			left = right;
		}
		else {
			left.merge(right);
		}
		return left;
	}

	public Rectangle copy() {
		return new Rectangle(x, y, width, height);
	}

	public Rectangle copyInverted() {
		return new Rectangle(-x, -y, -width, -height);
	}

	public Rectangle subtract(Rectangle other) {
		return new Rectangle(x - other.x, y - other.y, width - other.width, height - other.height);
	}

	public Rectangle add(Rectangle other) {
		return new Rectangle(x + other.x, y + other.y, width + other.width, height + other.height);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + width;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Rectangle other = (Rectangle) obj;
		if (height != other.height) {
			return false;
		}
		if (width != other.width) {
			return false;
		}
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Rectangle [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
	}

}
