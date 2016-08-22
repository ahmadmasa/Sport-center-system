/**
 * 
 */
package sport.center.terminal.jpa.entities;

import java.time.LocalDate;
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
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Entity
@Table(name="notes")
public class NoteEntity {
	
	/**
	 * 
	 */
	@Id
	@Column(name="_id")
    private long id;
	
	/**
	 * 
	 */
	@Column(name="date")
    private LocalDate date;
	
	/**
	 * 
	 */
	@Column(name="comment")
    private String comment;
	
	/**
	 * 
	 */
	@Column(name="source")
    private String source;
	
	/**
	 * 
	 */
	@Column(name="client_id")
    private long clientId;

	
}
