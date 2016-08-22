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
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Entity
@Table(name="payment")
public class PaymentEntity {
	
	/**
	 * 
	 */
	@Id
	@Column(name="_id")
    private long id;
	
	/**
	 * 
	 */
	@Column(name="payment_date")
    private Date paymentDate;
	
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
	
	/**
	 * 
	 */
	@Column(name="notify")
    private boolean notify;
	
	/**
	 * 
	 */
	@Column(name="ammount")
    private Double ammount;
	
	/**
	 * 
	 */
	@Column(name="payment_type")
    private PaymentType paymentType;
	
	/**
	 * 
	 */
	@Column(name = "sport_id")
	private long sportId;
	
	/**
	 * @author Asendar
	 *
	 */
	public static enum PaymentType {REGULAR,MONTHLY}
}
