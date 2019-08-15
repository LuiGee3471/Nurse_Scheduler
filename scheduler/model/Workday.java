package scheduler.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import scheduler.DateUtils;

/**
 * 업무일의 정보를 담고 있는 클래스
 */
public class Workday {
  private LocalDate workdate; // 날짜
  private String weekday; // 요일
  private Map<String, GroupName> todayShift; // 오늘의 근무시간대
  private List<String> offDayNurses; // 오늘 휴무 간호사 이름 목록

  private Nurse headOnWeekend; // 주말 당일 수간호사
  private List<Nurse> generalsOnWeekend; // 주말 일반간호사
  private List<Nurse> aidesOnWeekend; // 주말 간호조무사

  public Workday(LocalDate workdate) {
    this.todayShift = new LinkedHashMap<>();
    this.offDayNurses = new ArrayList<>();
    this.generalsOnWeekend = new ArrayList<>();
    this.aidesOnWeekend = new ArrayList<>();
    this.workdate = workdate;
    this.weekday = workdate.getDayOfWeek().name();
  }

  public LocalDate getWorkdate() {
    return this.workdate;
  }

  public void setWorkdate(LocalDate workdate) {
    this.workdate = workdate;
  }

  public String getWeekday() {
    return this.weekday;
  }

  public void setWeekday(String weekday) {
    this.weekday = weekday;
  }

  public List<String> getOffDayNurses() {
    return this.offDayNurses;
  }

  public void setOffDayNurses(List<String> offDayNurses) {
    this.offDayNurses = offDayNurses;
  }

  public Nurse getHeadOnWeekend() {
    return this.headOnWeekend;
  }

  public void setHeadOnWeekend(Nurse headOnWeekend) {
    this.headOnWeekend = headOnWeekend;
  }

  public List<Nurse> getGeneralsOnWeekend() {
    return this.generalsOnWeekend;
  }

  public void setGeneralsOnWeekend(List<Nurse> generalsOnWeekend) {
    this.generalsOnWeekend = generalsOnWeekend;
  }

  public List<Nurse> getAidesOnWeekend() {
    return this.aidesOnWeekend;
  }

  public void setAidesOnWeekend(List<Nurse> aidesOnWeekend) {
    this.aidesOnWeekend = aidesOnWeekend;
  }

  public Map<String, GroupName> getTodayShift() {
    return this.todayShift;
  }

  public void setTodayShift(Map<String, GroupName> todayShift) {
    this.todayShift = todayShift;
  }

  @Override
  public String toString() {
    String result = "";
    boolean isHoliday = false;
    for (LocalDate holiday : DateUtils.holidays) {
      Period diff = Period.between(holiday, workdate);
      if (diff.getMonths() == 0 && diff.getDays() == 0) {
        isHoliday = true;
        break;
      }
    }
    if (Arrays.asList(DateUtils.weekends).contains(weekday) || isHoliday) {
      result += "휴무일 당직자\n수간호사: " + headOnWeekend.getName() + "\n새벽: ";
      result += generalsOnWeekend.get(0).getName() + " " + aidesOnWeekend.get(0).getName();
      result += "\n주간: " + generalsOnWeekend.get(1).getName() + " " + aidesOnWeekend.get(1).getName();
      result += "\n야간: " + generalsOnWeekend.get(2).getName() + " " + aidesOnWeekend.get(2).getName();
    } else {
      result += "근무표:\n";
      result += "새벽: " + todayShift.get("새벽");
      result += "\n주간: " + todayShift.get("주간");
      result += "\n야간: " + todayShift.get("야간");
      result += "\n오늘 휴무자: " + offDayNurses;
    }

    result += "\n";
    return result;
  }
}