package model;

import java.util.Date;

public class Prompt {
	public int id;
	public Date createdate;
	public int status;
	public String phone;
	public String ptext;
	public String actionurl;
	public String url;
	
	@Override
	public String toString() {
		return "Prompt [id=" + id + ", createdate=" + createdate + ", status=" + status + ", phone=" + phone
				+ ", ptext=" + ptext + ", actionurl=" + actionurl + ", url=" + url + "]";
	}
}
