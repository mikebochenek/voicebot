package util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class CreationDateHelper {
	public enum predefined {TODAY, THIS_MONTH, THIS_YEAR};
	
	public static long convertPresetToTime(String preset) {
		if ("today".equals(preset)) { return getTimeToday() * 1000; }
		else if ("this_month".equals(preset)) { return getTimeLastMonth() * 1000; }
		else if ("this_year".equals(preset)) { return getTimeLastYear() * 1000; }
		else return 0;
	}
	
	public static long getTimeToday() {
		return LocalDateTime.now().minusDays(1).toEpochSecond(ZoneOffset.UTC); //FIXME this is probably off-by-one since ZRH is not UTC
	}
	public static long getTimeLastMonth() {
		return LocalDateTime.now().minusMonths(1).toEpochSecond(ZoneOffset.UTC); //FIXME this is probably off-by-one since ZRH is not UTC
	}
	public static long getTimeLastYear() {
		return LocalDateTime.now().minusYears(1).toEpochSecond(ZoneOffset.UTC); //FIXME this is probably off-by-one since ZRH is not UTC
	}
	
}
