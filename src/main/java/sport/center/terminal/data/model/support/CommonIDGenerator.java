/**
 * 
 */
package sport.center.terminal.data.model.support;

import java.io.Serializable;
import java.security.SecureRandom;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentityGenerator;

/**
 * @author Asendar
 *
 */
public class CommonIDGenerator extends IdentityGenerator {

	/**
	 * 
	 */
	private static volatile SecureRandom numberGenerator = new SecureRandom();
	/**
	 * 
	 */
	private static volatile short sequence = (short) numberGenerator.nextInt();

	/**
	 * @return
	 */
	public static synchronized long newID() {
		return (System.currentTimeMillis() << 20) | ((sequence++ & 0xFFFF) << 4) | (numberGenerator.nextInt() & 0xF);
	}

	/**
	 * @param id
	 * @return
	 */
	public static String getHumanReadableId(long id) {
		int hash = (int) (id ^ (id >>> 32));
		hash = hash < 0 ? ~hash : hash;
		return StringUtils.upperCase(Integer.toString(hash, 36));
	}

	@Override
	public Serializable generate(SessionImplementor session, Object obj) {
		return CommonIDGenerator.newID();
	}
}