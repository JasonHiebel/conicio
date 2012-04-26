package conicio.util;

/**
 * A three component, immutable RGB color representation.
 *
 * @author Jason Hiebel
 **/
public interface Color<C> {

	/**
	 *
	 **/
	public C add(C c);

	/**
	 *
	 **/
	public C mask(C c);

	/**
	 *
	 **/
	public C scale(double s);
	
	/**
	 *
	 **/
	public double power();
	
	/**
	 *
	 **/
	public int asRGB();
}