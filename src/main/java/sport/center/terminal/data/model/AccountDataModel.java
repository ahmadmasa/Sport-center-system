/**
 * 
 */
package sport.center.terminal.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import sport.center.terminal.data.model.support.CommonIDGenerator;
import sport.center.terminal.database.AddToDatabase;
import sport.center.terminal.database.DeleteFromDatabase;
import sport.center.terminal.database.GetFromDatabase;
import sport.center.terminal.database.UpdateDatabase;
import sport.center.terminal.jpa.entities.AccountEntity;

/**
 * @author Asendar
 *
 */
public class AccountDataModel extends DataModelChangeSupport implements AbstractDataModel<AccountEntity> {

	/**
	 * 
	 */
	public static final AccountDataModel instance = new AccountDataModel();

	/**
	 * 
	 */
	final static Logger logger = Logger.getLogger(AccountDataModel.class);

	/**
	 * 
	 */
	Map<Long, AccountEntity> allAccounts = new HashMap<>();

	/**
	 * 
	 */
	private AccountDataModel() {
		
		for (AccountEntity account : GetFromDatabase.instance.getAccounts()) {
			allAccounts.put(account.getId(), account);
		}
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#add(java.lang.Object)
	 */
	@Override
	public boolean add(AccountEntity data) {
		try {
			data.setId(CommonIDGenerator.newID());
			this.allAccounts.put(data.getId(), data);
			AddToDatabase.instance.addAccount(data);
			this.sync(data);
		} catch (Exception exception) {

			logger.error("Exception : ", exception);
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#delete(java.lang.Object)
	 */
	@Override
	public boolean delete(AccountEntity data) {
		try {

			this.allAccounts.remove(data.getId());
			DeleteFromDatabase.instance.deleteAccount(data);
			this.sync(data);
		} catch (Exception exception) {

			logger.error("Exception : ", exception);
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#update(java.lang.Object)
	 */
	@Override
	public boolean update(AccountEntity data) {
		try {

			this.allAccounts.put(data.getId(), data);
			UpdateDatabase.instance.updateAccount(data);
			this.sync(data);
		} catch (Exception exception) {

			logger.error("Exception : ", exception);
			return false;
		}
		return true;
	}
	
	/**
	 * @return
	 */
	public List<AccountEntity> getAccounts() {
		List<AccountEntity> accounts = new ArrayList<>();
		for (AccountEntity account :allAccounts.values()) {
			if (account.isActive()) {
				accounts.add(account);
			}
		}
		return accounts;
	}

	/**
	 * @return
	 */
	public List<AccountEntity> getAllAccounts() {
		return new ArrayList<>(allAccounts.values());
	}

	/**
	 * @return
	 */
	public int getAccountsCount() {
		return getAllAccounts().size();
	}

	/**
	 * @param email
	 * @return
	 */
	public AccountEntity getAccountByEmail(String email) {
		for (AccountEntity account : allAccounts.values()) {
			if (account.getEmail().equals(email)) {
				return account;
			}
		}
		return null;
	}
	
	/**
	 * @param id
	 * @return
	 */
	public AccountEntity getAccountById(long id) {

		return allAccounts.get(id);
	}
	

	
	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#sync(java.lang.Object)
	 */
	@Override
	public boolean sync(AccountEntity data) {
		this.fireDataModelChanged();
		return true;
	}

}
