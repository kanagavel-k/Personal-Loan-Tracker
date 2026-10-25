package application.utility;

import java.time.LocalDate;

public class DateUtil {
	
	public static boolean isValid(String date) {
        try {
            LocalDate.parse(date);   
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
