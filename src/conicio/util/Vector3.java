package conicio.util;

/**
 * A three componenet, immutable 3D vector representation.
 *
 * @author Jason Hiebel
 **/
public class Vector3 {
	/** The zero vector. **/
	public static final Vector3 ZERO = new Vector3(0.0, 0.0, 0.0);

	/** The X component. **/
	public final double x;

	/** The Y component. **/
	public final double y;

	/** The Z component. **/
	public final double z;

	/**
	 * Creates a 3D vector with the given x, y, and z coordinates.
	 *
	 * @param x the x coordinate of the vector
	 * @param y the y coordinate of the vector
	 * @param z the z coordinate of the vector
	 **/
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * @param v the vector with which to add
	 * @return the given vector added to this vector
	 **/
	public Vector3 add(Vector3 v) {
		return new Vector3(x + v.x, y + v.y, z + v.z);
	}
	
	/**
	 * @param vs the vectors with which to add
	 * @return the given vectors added to this vector
	 **/
	public Vector3 add(Vector3... vs) {
		double x = this.x, y = this.y, z = this.z;
		for(Vector3 v : vs) { x += v.x; y += v.y; z += v.z; }
		
		return new Vector3(x, y, z);
	}

	/**
	 * @param v the vector with which to subtract
	 * @return the given vector subtracted from this vector
	 **/
	public Vector3 sub(Vector3 v) {
		return new Vector3(x - v.x, y - v.y, z - v.z);
	}
	
	/**
	 * @param vs the vectors with which to subtract
	 * @return the given vectors subtracted from this vector
	 **/
	public Vector3 sub(Vector3... vs) {
		double x = this.x, y = this.y, z = this.z;
		for(Vector3 v : vs) { x -= v.x; y -= v.y; z -= v.z; }
		
		return new Vector3(x, y, z);
	}

	/**
	 * @param v the vector with which to perform the cross product
	 * @return the cross product between this vector and the given vector
	 **/
	public Vector3 mul(Vector3 v) {
		return new Vector3(
			y * v.z - z * v.y, 
			z * v.x - x * v.z, 
			x * v.y - y * v.x
		);
	}

	/**
	 * @param s the scalar with which to perform the product
	 * @return the scalar product between this vector and the given scalar
	 **/
	public Vector3 mul(double s) {
		return new Vector3(x * s, y * s, z * s);
	}

	/**
	 * @param v the vector with which to perform the dot product
	 * @return the dot product between this vector and the given vector
	 **/
	public double dot(Vector3 v) {
		return x * v.x + y * v.y + z * v.z;
	}

	/**
	 * @return this vector negated
	 **/
	public Vector3 neg() {
		return new Vector3(-x, -y, -z);
	}

	/**
	 * @return this vector normalized
	 **/
	public Vector3 normalize() {
		return this.mul(1.0 / this.norm());
	}

	/**
	 * @return the length of this vector
	 **/
	public double norm() {
		return Math.sqrt(this.dot(this));
	}

	/**
	 * @return the square length of this vector
	 **/
	public double normSq() {
		return this.dot(this);
	}

	/**
	 * @param normal the normal with which to reflect
	 * @return the reflected vector
	 **/
	public Vector3 reflect(Vector3 normal) {
		return this.sub(normal.mul(2 * this.dot(normal)));
	}

	/**
	 * @param normal the normal with which to refract
	 * @param index1 the index of refraction of the current medium
	 * @param index2 the index of refraction of the new medium
	 * @return the refracted vector
	 **/
	public Vector3 refract(Vector3 normal, double index1, double index2) {
		// index ratio
		double ratio = index1 / index2;

		// angle calculations
		double cos1 = - normal.dot(this);
		double cos2 = Math.sqrt(1 - ratio * ratio * (1 - cos1 * cos1));
		if(Double.isNaN(cos2)) {
			return null;
		}

		// snell's law
		int mul = (cos1 >= 0 ? +1 : -1);
		return this.mul(ratio).add(normal.mul(ratio * cos1 - mul * cos2));
	}
	
	/**
	 * Compares the specified object for exact vector equality. Returns true
	 * if the given object is an instance of Vector3 and both vectors have
	 * exact equality for all of their coordinates.
	 *
	 * @param o object to be compared for equality with this set
	 * @return if the specified object is equal to this set
	 **/
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Vector3)) { return false; }
		Vector3 v = (Vector3)o;
		return this.x == v.x && 
		       this.y == v.y && 
		       this.z == v.z;
	}

	/**
	 * Returns a string representation of the vector of the form 
	 * [ Vector3 : ;(x) (y) (z) ].
	 *
	 * @return a string representation of this vector.
	 **/
	@Override
	public String toString() {
		return String.format("[ Vector3 : %f, %f, %f ]", x, y, z);
	}
}