package com.bachoco.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FechasUtil {

	private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	 
	public static Timestamp toTimesTamp(String fecha) {
		LocalDate localDate = LocalDate.parse(fecha, formatter);
		Timestamp sqlTimestamp = Timestamp.valueOf(localDate.atStartOfDay());
		return sqlTimestamp;
	}
}
