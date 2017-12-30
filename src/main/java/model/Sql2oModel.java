package model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import pojo.Initiator;
import pojo.Option;
import pojo.Participant;
import pojo.Poll;

public class Sql2oModel {

    private static Sql2o sql2o;
    private static Sql2oModel instance;

    
    public Sql2oModel(Sql2o s) {
        sql2o = s;
        instance = this;
    }

    public static Sql2oModel getInstance() {
        return instance;
    }
    
    public List<Recordings> getRecordings() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select * from recordings ")
            		.executeAndFetch(Recordings.class);
        }
    }

    public long createRecording(Recordings r) {
        try (Connection con = sql2o.open()) {
        	Connection executeUpdate = con.createQuery("insert into recordings(filename, url, status, conversation, phone, parsedtext, misc) "
            		+ "values (:filename, :url, :status, :conversation, :phone, :parsedtext, :misc)")
                    .addParameter("filename", r.filename)
                    .addParameter("url", r.url)
                    .addParameter("status", r.status)
                    .addParameter("conversation", r.conversation)
                    .addParameter("phone", r.phone)
                    .addParameter("parsedtext", r.parsedtext)
                    .addParameter("misc", r.misc)
                    .executeUpdate();
        	return ((BigInteger) executeUpdate.getKey()).longValue();
        }
    }
    
    public List<Participant> getParticipant(long id) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select uuid, id, name, preferences from participants where id = :id ")
            		.addParameter("id", id)
            		.executeAndFetch(Participant.class);
        }
    }
    
    public List<Participant> getParticipants(int poll_id) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select uuid, id, name, preferences from participants p "
            		+ " inner join poll_participants pp on p.uuid = pp.participant_uuid "
            		+ " where pp.poll_uuid = :id ")
            		.addParameter("id", poll_id)
            		.executeAndFetch(Participant.class);
        }
    }
    
    public void createParticipant(Participant p, long poll_id) {
        try (Connection con = sql2o.open()) {
        	List<Participant> parts = getParticipant(p.id);
        	long id = 0;
        	if (parts.size() == 0) {
        		Connection executeUpdate = con.createQuery("insert into participants(id, name, preferences) "
                		+ "values (:id, :name, :preferences )")
                        .addParameter("id", p.id)
                        .addParameter("name", p.name)
                        .addParameter("preferences", "") //FIXME it's a string representation of List<Int>!
                        .executeUpdate();
        		id = (Long) executeUpdate.getKey();
        	} else {
        		id = parts.get(0).uuid;
        	}
        	
    		con.createQuery("insert into poll_participants(poll_uuid, participant_uuid) "
            		+ "values (:poll_uuid, :participant_uuid)")
                    .addParameter("poll_uuid", poll_id)
                    .addParameter("participant_uuid", id)
                    .executeUpdate();
        }
    }

    public List<Initiator> getInitiators(int id) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select uuid, name, email, notify from initiators where poll_uuid = :id")
            		.addParameter("id", id)
            		.executeAndFetch(Initiator.class);
        }
    }
    
    public List<Initiator> getInitiators(String email) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select uuid, name, email, notify from initiators where email = :email")
            		.addParameter("email", email)
            		.executeAndFetch(Initiator.class);
        }
    }

	public void createInitiators(Initiator i, long poll_id) {
		try (Connection con = sql2o.open()) {
			List<Initiator> existing = getInitiators(i.email);

			long id = 0;
			if (existing.size() == 0) {
				Connection executeUpdate = con.createQuery("insert into initiators(poll_uuid, name, email, notify) "
							+ "values (:poll_uuid, :name, :email, :notify)")
						.addParameter("poll_uuid", poll_id)
						.addParameter("name", i.name)
						.addParameter("email", i.email)
						.addParameter("notify", i.notify)
						.executeUpdate();
				id = (Long) executeUpdate.getKey();
			} else {
				id = existing.get(0).uuid;
			}
			
			con.createQuery("insert into poll_initiators(poll_uuid, initiator_uuid) "
					+ "values (:poll_uuid, :initiator_uuid)")
				.addParameter("poll_uuid", poll_id)
				.addParameter("initiator_uuid", id)
				.executeUpdate();
		}
	}    
    
    final String selectOptionSQL = " select start, end, allday, date, startDate, endDate, text, available from options";

    public List<Option> getOptions(int id) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(selectOptionSQL + " where poll_id = :id")
            		.addParameter("id", id)
            		.executeAndFetch(Option.class);
        }
    }
    
    public void createOption(Option o, long poll_id) {
        try (Connection con = sql2o.open()) {
            con.createQuery("insert into options(poll_uuid, start, end, allday, date, startDate, endDate, text, available) "
            		+ "values (:poll_uuid, :start, :end, :allday, :date, :startDate, :endDate, :text, :available)")
                    .addParameter("poll_uuid", poll_id)
                    .addParameter("start", o.start)
                    .addParameter("end", o.end)
                    .addParameter("allday", o.allday)
                    .addParameter("date", o.date)
                    .addParameter("startDate", o.startDate)
                    .addParameter("endDate", o.endDate)
                    .addParameter("text", o.text)
                    .addParameter("available", o.available)
                    .executeUpdate();
        }
    }
    
    final String selectPollsSQL = "select id, adminKey, latestChange, initiated, "
    		+ " participantsCount, inviteesCount, ttype as type, " //NB JSON POJO does not match mysql
    		+ " hidden, preferencesType, sstate as state, "
    		+ " locale, title, description, initiator_id, device, levels, jsonfullpretty from polls";

    final String selectJoinHack = " p inner join poll_initiators pi on p.uuid = pi.poll_uuid "
    		+ " inner join initiators i on i.uuid = pi.initiator_uuid ";
    final String selectPollsJoinInitiator = selectPollsSQL + selectJoinHack;
    
    public List<String> getPollsByInitiator(String email) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select jsonfullpretty from polls " 
            		+ selectJoinHack + " where i.email = :email limit 100 ") //TODO needs pagination
            		.addParameter("email", email)
            		.executeAndFetch(String.class);
        }
    }

    public List<Poll> getPollsByInitiatorSearch(String email, long initiated) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(selectPollsJoinInitiator + " where i.email = :email "
            		+ " and p.initiated > :initiated limit 100") //TODO needs pagination
            		.addParameter("email", email)
            		.addParameter("initiated", initiated)
            		.executeAndFetch(Poll.class);
        }
    }
    
    /** somehow I could not get like queries to work with sql2o, so we do keyword search with java */
    public List<String> getPollsByInitiatorSearch(String email, long initiated, String keyword) {
    	List<Poll> polls = getPollsByInitiatorSearch(email, initiated);
    	List<String> filtered = new ArrayList<String>();
    	for (Poll p : polls) {
    		if (keyword == null || keyword.trim().length() == 0) {
    			filtered.add(p.jsonFullPretty);
    		} else if (keyword != null && keyword.length() > 1 
    				&& ((p.title != null && p.title.toLowerCase().contains(keyword.toLowerCase()))
    						|| (p.description != null && p.description.toLowerCase().contains(keyword.toLowerCase()))
    				)) {
    			filtered.add(p.jsonFullPretty);
    		}
    	}
    	return filtered;
    }
    
    public List<Poll> getPolls() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(selectPollsSQL).executeAndFetch(Poll.class);
        }
    }
    
    public long createPoll(Poll p, String jsonPretty) {
        try (Connection con = sql2o.open()) {
        	Connection executeUpdate = con.createQuery("insert into polls(id, adminKey, latestChange, initiated, participantsCount, inviteesCount, "
            		+ " ttype, hidden, preferencesType, sstate, locale, title, description, initiator_id, device, levels, jsonfullpretty) "
            		+ "values (:id, :adminKey, :latestChange, :initiated, :participantsCount, :inviteesCount, "
            		+ " :ttype, :hidden, :preferencesType, :sstate, :locale, :title, :description, :initiator_id, :device, :levels, :jsonfullpretty)")
                    .addParameter("id", p.id)
                    .addParameter("adminKey", p.adminKey)
                    .addParameter("latestChange", p.latestChange)
                    .addParameter("initiated", p.initiated)
                    .addParameter("participantsCount", p.participantsCount)
                    .addParameter("inviteesCount", p.inviteesCount)
                    .addParameter("ttype", p.type)
                    .addParameter("hidden", p.hidden)
                    .addParameter("preferencesType", p.preferencesType)
                    .addParameter("sstate", p.state)
                    .addParameter("locale", p.locale)
                    .addParameter("title", p.title)
                    .addParameter("description", p.description)
                    .addParameter("initiator_id", p.initiator_id)
                    .addParameter("device", p.device)
                    .addParameter("levels", p.levels)
                    .addParameter("jsonfullpretty", jsonPretty)
                    .executeUpdate();
        	return (Long) executeUpdate.getKey();
        }
    }
}
