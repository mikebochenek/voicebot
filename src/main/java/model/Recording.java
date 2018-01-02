package model;

import java.util.Date;

public class Recording {
	public Recording() {
		super();
	}
	
	public Recording(String f, String u, String p, String t, String a) {
		super();
		filename = f;
		url = u;
		phone = p;
		phonecalled = t;
		urlcalled = a;
	}
	
	public int id;
	public String filename;
	public String url;
	public Date createdate;
	public int status;
	public int conversation;
	public String phone;
	public String phonecalled;
	public String urlcalled;
	public String parsedtext;
	public String misc;
	
	@Override
	public String toString() {
		return "Recording [id=" + id + ", filename=" + filename + ", url=" + url + ", createdate=" + createdate
				+ ", status=" + status + ", conversation=" + conversation + ", phone=" + phone + ", phonecalled="
				+ phonecalled + ", urlcalled=" + urlcalled + ", parsedtext=" + parsedtext + ", misc=" + misc + "]";
	}
}
