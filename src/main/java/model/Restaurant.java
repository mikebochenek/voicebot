package model;

import java.util.Date;

public class Restaurant {
	public int id;
	public String name;
	public String city;
	public String address;
	public String schedulecron;
	public Date createdate;
	public int status;
	public int seats;
	public String email;
	public String phone;
	public String website;
	public String googleplaces;
	
	@Override
	public String toString() {
		return "Restaurant [id=" + id + ", name=" + name + ", city=" + city + ", address=" + address
				+ ", schedulecron=" + schedulecron + ", createdate=" + createdate + ", status=" + status + ", seats="
				+ seats + ", email=" + email + ", phone=" + phone + ", website=" + website + ", googleplaces="
				+ googleplaces + "]";
	}
}
