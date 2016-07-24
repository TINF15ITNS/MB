/**
 * 
 */
package message;

/**
 * Interface to specify minimum requirements to a payload
 * 
 * @author Nikolai Seip, Sebastian Mattheis, Fabian Hinz
 *
 */
public interface Payload {
	/**
	 * 
	 * @return true if the operation was successful
	 */
	public boolean getSuccess();
}
