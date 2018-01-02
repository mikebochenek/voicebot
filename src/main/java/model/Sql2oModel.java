package model;

import java.math.BigInteger;
import java.util.List;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

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
    
    public List<Recording> getRecordings() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select * from recordings ")
            		.executeAndFetch(Recording.class);
        }
    }

    /**
     * get recordings from the last five minutes
     */
    public List<Recording> getRecordings(String phoneFrom, String phoneCalled) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select * from recordings where createdate > (NOW() - INTERVAL 5 MINUTE) and phone = :phone and phonecalled = :phoneCalled ")
                    .addParameter("phone", phoneFrom)
                    .addParameter("phonecalled", phoneCalled)
            		.executeAndFetch(Recording.class);
        }
    }

    public int getMaximumConversationFromRecordings() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select max(conversation) from recordings ")
            		.executeScalar(Integer.class);
        }
    }
    
    public long createRecording(Recording r) {
        try (Connection con = sql2o.open()) {
        	Connection executeUpdate = con.createQuery("insert into recordings(filename, url, status, conversation, phone, phonecalled, urlcalled, parsedtext, misc) "
            		+ "values (:filename, :url, :status, :conversation, :phone, :phonecalled, :urlcalled, :parsedtext, :misc)")
                    .addParameter("filename", r.filename)
                    .addParameter("url", r.url)
                    .addParameter("status", r.status)
                    .addParameter("conversation", r.conversation)
                    .addParameter("phone", r.phone)
                    .addParameter("phonecalled", r.phonecalled)
                    .addParameter("urlcalled", r.urlcalled)
                    .addParameter("parsedtext", r.parsedtext)
                    .addParameter("misc", r.misc)
                    .executeUpdate();
        	Object key = executeUpdate.getKey();
        	return (key instanceof BigInteger ? ((BigInteger) key).longValue() : (Long) key);
        }
    }

    public void updateRecording(Recording r) {
        try (Connection con = sql2o.open()) {
        	con.createQuery("update recordings set filename = :filename, url = :url, status = :status, "
        				+ "conversation = :conversation, phone = :phone, phonecalled = :phonecalled, urlcalled = :urlcalled, "
        				+ "parsedtext = :parsedtext, misc = :misc where id = :id")
                    .addParameter("filename", r.filename)
                    .addParameter("url", r.url)
                    .addParameter("status", r.status)
                    .addParameter("conversation", r.conversation)
                    .addParameter("phone", r.phone)
                    .addParameter("phonecalled", r.phonecalled)
                    .addParameter("urlcalled", r.urlcalled)
                    .addParameter("parsedtext", r.parsedtext)
                    .addParameter("misc", r.misc)
                    .addParameter("id", r.id)
                    .executeUpdate();
        }
    }
    
    public List<User> getUsers() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select * from users ")
            		.executeAndFetch(User.class);
        }
    }
    
    public long createUser(User u) {
        try (Connection con = sql2o.open()) {
        	Connection executeUpdate = con.createQuery("insert into users(authprovider, email, password, usertype, username, phone, fullname, misc) "
            		+ "values (:authprovider, :email, :password, :password, :username, :phone, :fullname, :misc)")
                    .addParameter("authprovider", u.authprovider)
                    .addParameter("email", u.email)
                    .addParameter("password", u.password)
                    .addParameter("username", u.username)
                    .addParameter("phone", u.phone)
                    .addParameter("fullname", u.fullname)
                    .addParameter("misc", u.misc)
                    .executeUpdate();
        	Object key = executeUpdate.getKey();
        	return (key instanceof BigInteger ? ((BigInteger) key).longValue() : (Long) key);
        }
    }
    

    public List<Prompt> getPrompts() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select * from prompts ")
            		.executeAndFetch(Prompt.class);
        }
    }

    public Prompt getPrompt(String phone, String actionurl) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select * from prompts where phone = :phone and actionurl = :actionurl")
                    .addParameter("phone", phone)
                    .addParameter("actionurl", actionurl)
            		.executeAndFetchFirst(Prompt.class);
        }
    }
}
