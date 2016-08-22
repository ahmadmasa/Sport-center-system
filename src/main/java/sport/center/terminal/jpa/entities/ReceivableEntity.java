
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
@Table(name="receivables")
public class ReceivableEntity {
	
	/**
	 * 
	 */
	@Id
	@Column(name="_id")
    private long id;
	
	/**
	 * 
	 */
	@Column(name="receivable_date")
    private Date receivableDate;
	
	/**
	 * 
	 */
	@Column(name="client_id")
    private long clientId;
	
	/**
	 * 
	 */
	@Column(name="receivable_ammount")
    private double receivableAmmount;
	
    /**
     * 
     */
    @Column(name="payed_ammount")
    private double payedAmmount;
    
    /**
     * 
     */
    @Column(name="comment")
    private String comment;
    
    /**
     * 
     */
    @Column(name="payed")
    private boolean payed;
    
	/**
	 * 
	 */
	@Column(name="sport_id")
    private long sportId;
}
