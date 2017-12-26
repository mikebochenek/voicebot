package pojo;

import java.util.List;

public class Poll {
	/** generally public members are evil, but for POJOs you could argue that its only boilerplate code */
	public long uuid;
	public String id;
	public String adminKey;
	public long latestChange; // we assume it's number of seconds that have elapsed since epoch
	public long initiated;
	public int participantsCount;
	public int inviteesCount;
	public String type;
	public boolean hidden;
	public String preferencesType;
	public String state;
	public String locale;
	public String title;
	public Location location;
	public String description;
	public long initiator_id; //TODO?
	public Initiator initiator;
	
	public List<Option> options;
	
	public String optionsHash;
	public List<Participant> participants;
	public List<String> invitees;

	public boolean multiDate;
	public String device;
	public String levels;
	
	public String jsonFullPretty;
	
	@Override
	public String toString() {
		return "Poll [uuid=" + uuid + ", id=" + id + ", adminKey=" + adminKey + ", latestChange=" + latestChange
				+ ", initiated=" + initiated + ", participantsCount=" + participantsCount + ", inviteesCount="
				+ inviteesCount + ", type=" + type + ", hidden=" + hidden + ", preferencesType=" + preferencesType
				+ ", state=" + state + ", locale=" + locale + ", title=" + title + ", location=" + location
				+ ", description=" + description + ", initiator_id=" + initiator_id + ", initiator=" + initiator
				+ ", options=" + options + ", optionsHash=" + optionsHash + ", participants=" + participants
				+ ", invitees=" + invitees + ", multiDate=" + multiDate + ", device=" + device + ", levels=" + levels
				+ "]";
	}
}
