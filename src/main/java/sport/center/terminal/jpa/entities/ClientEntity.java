package sport.center.terminal.jpa.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sport.center.terminal.data.model.SessionDataModel;
import sport.center.terminal.data.model.SportDataModel;
import sport.center.terminal.data.model.support.ClientIdGenerator;
import sport.center.terminal.support.Messages;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name="clients")
public class ClientEntity {
	
	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="_id")
	private long id;

	/**
	 * 
	 */
	@Column(name="client_name")
	private String name;
	
	/**
	 * 
	 */
	@Column(name="birthdate")
	private Date birthdate;

	/**
	 * 
	 */
	@Column(name="gender")
	private Gender gender;
	
	/**
	 * 
	 */
	@Column(name="phone1")
	private String phone1;
	
	/**
	 * 
	 */
	@Column(name="phone2")
	private String phone2;
	
	/**
	 * 
	 */
	@Column(name="emergency_phone")
	private String emergencyPhone;
	
	/**
	 * 
	 */
	@Column(name="helth")
	private String helth;
	
	/**
	 * 
	 */
	@Column(name="hight")
	private double hight;
	
	/**
	 * 
	 */
	@Column(name="wieght")
	private double wieght;
	
	/**
	 * 
	 */
	@Column(name="collage")
	private String collage;
	
	/**
	 * 
	 */
	@Column(name="study_level")
	private String studyLevel;
	
	/**
	 * 
	 */
	@Column(name="father_work_info")
	private String fatherWorkInfo;
	
	/**
	 * 
	 */
	@Column(name="mother_work_info")
	private String motherWorkInfo;
	
	/**
	 * 
	 */
	@Column(name ="email")
	private String email;
	
	/**
	 * 
	 */
	@Column(name ="fb_name")
	private String fbName;
	
	/**
	 * 
	 */
	@Column(name ="transportation_participated")
	private boolean transportationParticipated;
	
	/**
	 * 
	 */
	@Column(name ="address")
	private String address;
	
	/**
	 * 
	 */
	@Column(name ="belt")
	private String belt;

	/**
	 * 
	 */
	@Column(name ="monthly_payment")
	private double monthlyPayment;
	
	/**
	 * 
	 */
	@Column(name="sign_date")
	private Date signDate;
	
	/**
	 * 
	 */
	@Column(name="monthly_payment_date")
	private Date monthlyPaymentDate;
	
	/**
	 * 
	 */
	@Column(name ="session")
	private long session;
	
	/**
	 * 
	 */
	@Column(name ="active")
	private boolean active;
	
	/**
	 * 
	 */
	@Column(name ="sport_id")
	private long sportId;	

	/**
	 * @author Asendar
	 *
	 */
	public enum Gender {
		MALE, FEMALE;
		

		public String toString() {
			switch (this) {
			
			case MALE:
				return "ذكر";
				
			case FEMALE:
				return "انثى";

				
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	/**
	 * @return
	 */
	public long getHumanReadableId(){
		return ClientIdGenerator.generateId(this);
	}
	
	
	/**
	 * @param filter
	 * @return
	 */
	public boolean contains(String filter) {
		try {
			if ((this.getHumanReadableId()+ "").contains(filter) || this.getName().contains(filter)) {
				return true;

			} else if (this.getMonthlyPaymentDate() != null
					&& this.getMonthlyPaymentDate().toString().contains(filter)) {
				return true;
			} else if (this.getBirthdate() != null && this.getBirthdate().toString().contains(filter)) {
				return true;
			} else if (this.getAddress() != null && this.getAddress().contains(filter)) {
				return true;
			} else if (this.getFatherWorkInfo() != null && this.getFatherWorkInfo().contains(filter)) {
				return true;
			} else if (this.getMotherWorkInfo() != null && this.getMotherWorkInfo().contains(filter)) {
				return true;
			} else if ((this.getGender() == Gender.MALE ? Messages.getString("MALE") : Messages.getString("FEMALE"))
					.contains(filter)) {
				return true;
			} else if (this.getCollage() != null && this.getCollage().contains(filter)) {
				return true;
			} else if (this.getStudyLevel() != null && this.getStudyLevel().contains(filter)) {
				return true;
			} else if (SessionDataModel.instance.getSessionById(this.getSession()).getName().contains(filter)) {
				return true;
			}

			return false;
		} catch (NullPointerException exception) {
			return false;

		}
	}
	
	/**
	 * @return
	 */
	public List<String> getXLSrow() {

		List<String> args = new ArrayList<>();
		args.add(getHumanReadableId() + "");
		args.add(getName());
		args.add(getBirthdate().toString());
		args.add(getSignDate().toString());
		args.add(getMonthlyPaymentDate().toString());
		args.add(getGender().toString());
		args.add(getPhone1());
		args.add(getPhone2());
		args.add(getEmergencyPhone());
		args.add(getHelth());
		args.add(getHight() + "");
		args.add(getWieght() + "");
		args.add(getCollage());
		args.add(getStudyLevel());
		args.add(getFatherWorkInfo());
		args.add(getMotherWorkInfo());
		args.add(getEmail());
		args.add(getFbName());
		args.add(isTransportationParticipated() ? "نعم" : "لا");
		args.add(getAddress());
		args.add(getBelt());
		args.add(getMonthlyPayment() + "");
		SessionEntity session = SessionDataModel.instance.getSessionById(getSession());
		args.add(session != null ? session.getName() : "-");
		SportEntity sport = SportDataModel.instance.getSport(getSportId());
		args.add(sport != null ? sport.getSportName() : "-");
		args.add(!isActive() ? "نعم" : "لا");

		return args;

	}
	
	/**
	 * @return
	 */
	public static List<String> getXLSHeaders() {
		return Arrays.asList("الرقم", "الاسم", "تاريخ الميلاد", "تاريخ التسجيل", "تاريخ الدفعه الشهريه", "الجنس",
				"خلوي 1", "خلوي 2", "هاتف الطوارئ", "الصحه", "الطول", "الوزن", "مكان الدراسه", "المستوى الدراسي",
				"معلومات عمل الاب", "معلومات عمل الام", "Email", "Facebook", "مشترك بالمواصلات", "العنوان", "الحزام",
				"الدفعه الشهريه", "الحصه", "الرياضه", "منقطع");
	}
}
