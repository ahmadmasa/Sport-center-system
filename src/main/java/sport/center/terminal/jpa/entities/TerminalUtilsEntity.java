/**
 * 
 */
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
@Table(name="terminal_utils")
public class TerminalUtilsEntity {
	
	/**
	 * 
	 */
	@Id
	@Column(name="_id")
	private long id;
	
	/**
	 * 
	 */
	@Column(name="theme")
	private String theme;
	
	/**
	 * 
	 */
	@Column(name="time_stamp")
	private java.sql.Date timeStamp;
	
	/**
	 * 
	 */
	@Column(name="last_login")
	private Long lastLogin;

}
