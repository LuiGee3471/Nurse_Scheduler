package scheduler.model;

import java.time.LocalDate;
import java.util.Random;

public class Nurse {
  public static final int MIDNIGHT = 0;
  public static final int DAY = 1;
  public static final int NIGHT = 2;

  public static final int HEAD_NURSE = 0;
  public static final int GENERAL_NURSE = 1;
  public static final int AIDE_NURSE = 2;

  public static final String[] groupNames = { "A조", "B조", "C조" };
  private static Random random = new Random();

  private String name; // 이름
  private int offDays; // 휴무일
  private int lastShift = 1; // 지난 주 근무시간
  private int currentShift = 1; // 이번 주 근무시간
  private int rank; // 등급
  private LocalDate birthday; // 생일
  private boolean workToday; // 휴무날 일했는지
  private String groupName; // 근무조 이름

  public Nurse() {
    // 생일 기능을 위해 생일 임의로 생성
    int daysToSubtract = random.nextInt(365 * 45); // 오늘에서 뺄 날짜 45년 이내에서 구함
    this.birthday = LocalDate.now().minusYears(20).minusDays(daysToSubtract); // 성인으로 간주, 오늘에서 20년 전부터 계산
  }

  public boolean isWorkToday() {
    return this.workToday;
  }

  public boolean getWorkToday() {
    return this.workToday;
  }

  public void setWorkToday(boolean workToday) {
    this.workToday = workToday;
  }

  public String getGroupName() {
    return this.groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public void setBirthday(LocalDate birthday) {
    this.birthday = birthday;
  }

  public int getRank() {
    return this.rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getOffDays() {
    return this.offDays;
  }

  public void setOffDays(int offDays) {
    this.offDays = offDays;
  }

  public int getLastShift() {
    return this.lastShift;
  }

  public void setLastShift(int lastShift) {
    this.lastShift = lastShift;
  }

  public int getCurrentShift() {
    return this.currentShift;
  }

  public void setCurrentShift(int currentShift) {
    this.currentShift = currentShift;
  }
}