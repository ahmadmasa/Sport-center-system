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
@Table(name="sports")
@EqualsAndHashCode(of="id")
public class SportEntity {
	
	/**
	 * 
	 */
	@Id
	@Column(name = "_id")
	private long id;
	
	/**
	 * 
	 */
	@Column(name = "sport_name")
	private String sportName;
	
	/**
	 * 
	 */
	@Column(name = "sport_note")
	private String sportNote;
	
	/**
	 * 
	 */
	@Column(name = "active")
	private boolean active;

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return sportName;
	}

}
