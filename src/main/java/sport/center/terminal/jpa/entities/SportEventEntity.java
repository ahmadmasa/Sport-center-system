/**
 * 
 */
package sport.center.terminal.jpa.entities;

import java.sql.Date;

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
@Table(name="sport_events")
public class SportEventEntity {
	
	/**
	 * 
	 */
	@Id
	@Column(name="_id")
	private long id;
	
	/**
	 * 
	 */
	@Column(name="name")
	private String name;
	
	/**
	 * 
	 */
	@Column(name="start_date")
	private Date startDate;
	
	/**
	 * 
	 */
	@Column(name="end_date")
	private Date endDate;
	
	/**
	 * 
	 */
	@Column(name="comment")
	private String comment;
	
	/**
	 * 
	 */
	@Column(name="event_max_capacity")
    private int eventMaxCapacity;
	
	/**
	 * 
	 */
	@Column(name="sport_id")
	private long sportId;
	
	/**
	 * 
	 */
	@Column(name="active")
	private boolean active;
}
