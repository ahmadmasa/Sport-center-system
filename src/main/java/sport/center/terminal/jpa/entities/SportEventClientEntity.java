package sport.center.terminal.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Asendar
 *
 */
@Data
@Entity
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Table(name="sport_event_client")
public class SportEventClientEntity {

	/**
	 * 
	 */
	@Id
	@Column(name="_id")
	private long id;
	
	/**
	 * 
	 */
	@Column(name="event_id")
	private long eventId;
	
	/**
	 * 
	 */
	@Column(name="client_id")
	private long clientId;
	
	/**
	 * 
	 */
	@Column(name="comment")
	private String comment;
}
