package pojo;

import java.util.List;

public class Participant {
	public long uuid;
	public long id;
	public String name;
	public List<Integer> preferences;
	
	@Override
	public String toString() {
		return "Participant [id=" + id + ", name=" + name + ", preferences=" + preferences + "]";
	}
}
