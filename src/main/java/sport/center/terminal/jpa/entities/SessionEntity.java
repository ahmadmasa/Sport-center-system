package sport.center.terminal.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author Asendar
 */
@Data
@Entity
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@EqualsAndHashCode(of="id")
@Table(name="sessions")
public class SessionEntity {
	
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
	@Column(name="instructor_name")
    private String instructorName;
	
	/**
	 * 
	 */
	@Column(name="session_max_capacity")
    private int sessionMaxCapacity;
	
	/**
	 * 
	 */
	@Column(name="sport_id")
    private long sportId;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
	
	
}
