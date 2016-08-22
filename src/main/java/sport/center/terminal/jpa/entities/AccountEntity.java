package sport.center.terminal.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import javafx.scene.image.Image;
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
@Table(name="accounts")
public class AccountEntity {
	
	/**
	 * 
	 */
	@Id
	@Column(name="_id")
    private long id;
	
	/**
	 * 
	 */
	@Column(name="username")
    private String username;
	
	/**
	 * 
	 */
	@Column(name="email")
    private String email;
	
	/**
	 * 
	 */
	@Column(name="password")
    private String password;
    
	/**
	 * 
	 */
	@Column(name="avatar")
    private String avatar;
	
	/**
	 * 
	 */
	@Column(name="active")
    private boolean active;
	
	
	/**
	 * @return
	 */
	public Image getAvatar() {
		try {
			return new Image("/images/avatars/" + this.avatar + ".jpg");
		} catch (Exception e) {

			return new Image("/images/avatars/char10.jpg");
		}
	}
	
}
