package sport.center.terminal.data.model.support;

import java.io.Serializable;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentityGenerator;

import sport.center.terminal.jpa.entities.ClientEntity;

/**
 *
 * @author Asendar
 */
public class ClientIdGenerator extends IdentityGenerator {

	/**
	 * @param client
	 * @return
	 */
	public static long generateId(ClientEntity client) {
		String idFormatter = client.getBirthdate().toLocalDate().getYear()
				+ (client.getGender() == ClientEntity.Gender.MALE ? "10"
						: "20") /* + client.getSportId() */;

		long auto = getAutoIncrementNumber(client.getId());
		
		idFormatter = idFormatter + String.format("%06d", auto);

		return Long.parseLong(idFormatter);
	}

	/**
	 * @param num
	 * @return
	 */
	private static long getAutoIncrementNumber(long num) {
		return (num % 1000000);
	}

	/* (non-Javadoc)
	 * @see org.hibernate.id.AbstractPostInsertGenerator#generate(org.hibernate.engine.spi.SessionImplementor, java.lang.Object)
	 */
	@Override
	public Serializable generate(SessionImplementor session, Object obj) {
		return ClientIdGenerator.generateId((ClientEntity) obj);
	}

}
