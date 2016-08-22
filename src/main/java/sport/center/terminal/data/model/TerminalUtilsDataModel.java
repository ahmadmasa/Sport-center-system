/**
 * 
 */
package sport.center.terminal.data.model;

import java.util.Date;

import org.apache.log4j.Logger;

import lombok.Getter;
import lombok.Setter;
import sport.center.terminal.database.AddToDatabase;
import sport.center.terminal.database.GetFromDatabase;
import sport.center.terminal.database.UpdateDatabase;
import sport.center.terminal.jpa.entities.AccountEntity;
import sport.center.terminal.jpa.entities.TerminalUtilsEntity;

/**
 * @author Asendar
 *
 */
public class TerminalUtilsDataModel {

	/**
	 * 
	 */
	public static final TerminalUtilsDataModel instance = new TerminalUtilsDataModel();
	/**
	 * 
	 */
	TerminalUtilsEntity terminalUtils;

	/**
	 * 
	 */
	@Setter @Getter private AccountEntity loggedInUser;
	
	
	/**
	 * 
	 */
	final static Logger logger = Logger.getLogger(TerminalUtilsDataModel.class);

	/**
	 * 
	 */
	private TerminalUtilsDataModel() {
		terminalUtils = GetFromDatabase.instance.getTerminalutilsObject();
	}


	/**
	 * @return
	 */
	public String getTheme() {
		return terminalUtils.getTheme();
	}
	
	/**
	 * @return
	 */
	public AccountEntity getLastLogin() {
		
		if(terminalUtils.getLastLogin() == null)
			return null;
		
		return AccountDataModel.instance.getAccountById(terminalUtils.getLastLogin());
	}
	
	/**
	 * @param id
	 */
	public void setLastLogin(long id) {

		terminalUtils.setLastLogin(id);
		UpdateDatabase.instance.updateLastLogin(id);
	}

	/**
	 * @return
	 */
	public java.sql.Date getTimeStamp() {
		return terminalUtils.getTimeStamp();
	}

	/**
	 * @param theme
	 */
	public void updateTheme(String theme) {
		try {
			terminalUtils.setTheme(theme);
			UpdateDatabase.instance.updateTheme(theme);

		} catch (Exception exception) {
			logger.error("Exception : ", exception);
		}
	}

	/**
	 * 
	 */
	public void addTimeStamp() {
		try {
			java.sql.Date timeStamp = new java.sql.Date(new Date().getTime());
			terminalUtils.setTimeStamp(timeStamp);
			AddToDatabase.instance.addTimeStamp(timeStamp);
		} catch (Exception exception) {

			logger.error("Exception : ", exception);
		}
	}

}
