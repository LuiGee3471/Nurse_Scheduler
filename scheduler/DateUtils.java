package scheduler;

import java.time.LocalDate;

public class DateUtils {
  public static final String[] weekends = { "SATURDAY", "SUNDAY" }; // 주말인지 판단하기 위해
  public static final LocalDate[] holidays = { LocalDate.parse("2019-01-01"), LocalDate.parse("2019-03-01"),
      LocalDate.parse("2019-05-05"), LocalDate.parse("2019-06-06"), LocalDate.parse("2019-08-15"),
      LocalDate.parse("2019-10-03"), LocalDate.parse("2019-12-25") }; // 공휴일 임의로 생성
}