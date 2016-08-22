package sport.center.terminal.jpa.repositories;

import java.util.List;
import javax.persistence.EntityManager;

/**
 * @author Asendar
 *
 * @param <T>
 * @param <V>
 */
public interface DatabaseRepository<T, V> {
	/**
	 * @return
	 */
	public List<T> findAll();

	/**
	 * @param key
	 * @return
	 */
	public List<T> find(V key);

	/**
	 * @param entry
	 * @return
	 */
	public T saveAndFlush(T entry);
	
	/**
	 * @param entry
	 * @return
	 */
	public T delete(T entry);

	/**
	 * @param entry
	 * @return
	 */
	public T update(T entry);
	
	/**
	 * @return
	 * @throws Exception
	 */
	public EntityManager connect() throws Exception;
}
