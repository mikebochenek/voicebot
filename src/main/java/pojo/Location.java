package pojo;

public class Location {
	public String name;
	public String address;
	public String countryCode;
	public String locationId;
	
	@Override
	public String toString() {
		return "Location [name=" + name + ", address=" + address + ", countryCode=" + countryCode + ", locationId="
				+ locationId + "]";
	}

}
