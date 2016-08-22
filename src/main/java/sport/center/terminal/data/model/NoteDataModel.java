/**
 * 
 */
package sport.center.terminal.data.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;

import sport.center.terminal.data.model.support.CommonIDGenerator;
import sport.center.terminal.database.AddToDatabase;
import sport.center.terminal.database.DeleteFromDatabase;
import sport.center.terminal.database.GetFromDatabase;
import sport.center.terminal.database.UpdateDatabase;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.NoteEntity;

/**
 * @author Asendar
 *
 */
public class NoteDataModel extends DataModelChangeSupport implements AbstractDataModel<NoteEntity> {

	/**
	 * 
	 */
	public static final NoteDataModel instance = new NoteDataModel();

	/**
	 * 
	 */
	final static Logger logger = Logger.getLogger(NoteDataModel.class);

	/**
	 * 
	 */
	Map<Long, NoteEntity> allnotes = new HashMap<>();
	
	/**
	 * 
	 */
	private NoteDataModel() {
		for (NoteEntity Client : GetFromDatabase.instance.getNotes()) {
			allnotes.put(Client.getId(), Client);
		}
	}

	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#add(java.lang.Object)
	 */
	@Override
	public boolean add(NoteEntity data) {
		try {
			data.setId(CommonIDGenerator.newID());
			this.allnotes.put(data.getId(), data);
			AddToDatabase.instance.addNote(data);
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
	public boolean delete(NoteEntity data) {
		try {

			this.allnotes.remove(data.getId());
			DeleteFromDatabase.instance.deleteNote(data);
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
	public boolean update(NoteEntity data) {
		try {

			this.allnotes.put(data.getId(), data);
			UpdateDatabase.instance.updateNote(data);
			this.sync(data);
		} catch (Exception exception) {

			logger.error("Exception : ", exception);
			return false;
		}
		return true;
	}
	/**
	 * @param id
	 * @return
	 */
	public NoteEntity getNotebyId(long id) {
		return this.allnotes.get(id);
	}

	/**
	 * @return
	 */
	public List<NoteEntity> getNotes(){
		List<NoteEntity> filteredNotes = new ArrayList<>(this.allnotes.values());
		
		Collections.sort(filteredNotes,new Comparator<NoteEntity>() {

			@Override
			public int compare(NoteEntity o1, NoteEntity o2) {
				return -o1.getDate().compareTo(o2.getDate());
			}
		});
		
		return new ArrayList<>(this.allnotes.values());
	}
	
	/**
	 * @param client
	 * @return
	 */
	public List<NoteEntity> getNotes(ClientEntity client) {

		List<NoteEntity> filteredNotes = new ArrayList<>();
		filteredNotes.addAll(getNotes());

		CollectionUtils.filter(filteredNotes, new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				NoteEntity note = (NoteEntity) object;
				return client.getId() == note.getClientId();
			}
		});

		return filteredNotes;
	}
	
	/* (non-Javadoc)
	 * @see sport.center.terminal.data.model.AbstractDataModel#sync(java.lang.Object)
	 */
	@Override
	public boolean sync(NoteEntity data) {
		this.fireDataModelChanged();
		return true;
	}

}
