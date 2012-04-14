package conicio.util;

/**
 *
 **/
public interface ColorFactory<C extends Color<C>> {
	
	/**
	 *
	 **/
	public C black();
	
	/**
	 *
	 **/
	public C white();
}