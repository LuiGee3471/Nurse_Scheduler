package scheduler.model;

import java.time.LocalDate;
import java.util.Random;

public class Nurse {
  private static Random random = new Random();

  private String name; // 이름
  private int offDays; // 휴무일
  private Shift lastShift = Shift.DAY; // 지난 주 근무시간
  private Shift currentShift = Shift.DAY; // 이번 주 근무시간
  private Rank rank; // 등급
  private LocalDate birthday; // 생일
  private boolean workToday; // 휴무날 일했는지
  private GroupName groupName; // 근무조 이름

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

  public GroupName getGroupName() {
    return this.groupName;
  }

  public void setGroupName(GroupName groupName) {
    this.groupName = groupName;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public Rank getRank() {
    return this.rank;
  }

  public void setRank(Rank rank) {
    this.rank = rank;
  }

  public void setBirthday(LocalDate birthday) {
    this.birthday = birthday;
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

  public Shift getLastShift() {
    return this.lastShift;
  }

  public void setLastShift(Shift lastShift) {
    this.lastShift = lastShift;
  }

  public Shift getCurrentShift() {
    return this.currentShift;
  }

  public void setCurrentShift(Shift currentShift) {
    this.currentShift = currentShift;
  }
}