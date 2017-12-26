package pojo;

public class Option {
	public long start;
	public long end;
	public boolean allday;
	public long date;
	public long startDate;
	public long endDate;
	public String text;
	public String available;
	
	@Override
	public String toString() {
		return "Option [start=" + start + ", end=" + end + ", allday=" + allday + ", date=" + date + ", startDate="
				+ startDate + ", endDate=" + endDate + ", text=" + text + ", available=" + available + "]";
	}
}
