package conicio.util;

/**
 *
 *
 * @author Jason Hiebel
 **/
public class Camera {

	/**  **/
	public final Vector3 eye;

	/**  **/
	public final Vector3 center;

	/**  **/
	public final Vector3 up;

	/**  **/
	public final double  fov;

	/**
	 *
	 *
	 * @param eye
	 * @param center
	 * @param up
	 * @param fov
	 **/
	public Camera(Vector3 eye, Vector3 center, Vector3 up, double fov) {
		this.eye    = eye;
		this.center = center;
		this.up     = up;
		this.fov    = fov;
	}
}
