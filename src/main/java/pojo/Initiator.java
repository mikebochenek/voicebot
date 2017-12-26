package pojo;

public class Initiator {
	public int uuid;
	public String name;
	public String email;
	public String notify;
	
	@Override
	public String toString() {
		return "Initiator [name=" + name + ", email=" + email + ", notify=" + notify + "]";
	}
}
