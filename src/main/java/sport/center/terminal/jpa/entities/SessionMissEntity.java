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
 *
 * @author Asendar
 */
@Data
@Entity
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Table(name = "session_misss")
public class SessionMissEntity {

	/**
	 * 
	 */
	@Id
	@Column(name = "_id")
	private long id;

	/**
	 * 
	 */
	@Column(name = "client_id")
	private long clientId;
	
	/**
	 * 
	 */
	@Column(name = "session_miss_date")
	private Date sessionMissDate;
	
	/**
	 * 
	 */
	@Column(name = "comment")
	private String comment;
	
	/**
	 * 
	 */
	@Column(name = "notify")
	private boolean notify;
	
	/**
	 * 
	 */
	@Column(name = "sport_id")
	private long sportId;
}
