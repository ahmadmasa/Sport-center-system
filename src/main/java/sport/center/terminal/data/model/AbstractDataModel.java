/**
 * 
 */
package sport.center.terminal.data.model;

/**
 * @author Asendar
 *
 */
public interface AbstractDataModel<E> {

	/**
	 * @param data
	 * @return
	 */
	public boolean add(E data);
	/**
	 * @param data
	 * @return
	 */
	public boolean delete(E data);
	/**
	 * @param data
	 * @return
	 */
	public boolean update(E data);
	/**
	 * @param data
	 * @return
	 */
	public boolean sync(E data);

}
