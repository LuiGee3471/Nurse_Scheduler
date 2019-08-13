package scheduler;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import scheduler.model.Nurse;
import scheduler.model.Workday;

public class Scheduler {

  public static void main(String[] args) {
    NurseCalculator cal = new NurseCalculator();
    int headNurse = 0; // 수간호사 수
    int generalNurse = 0; // 일반간호사 수
    int aideNurse = 0; // 조무사 수
    LocalDate startDate = LocalDate.now(); // 시작일자
    LocalDate endDate = LocalDate.now(); // 마지막 일자
    List<List<Nurse>> nurses; // 간호사 목록
    Map<LocalDate, Workday> schedule = new LinkedHashMap<>(); // 일정표

    Scanner sc = new Scanner(System.in);
    try {
      System.out.println("수간호사의 수를 입력해주세요");
      headNurse = Integer.parseInt(sc.nextLine());
      System.out.println("일반간호사의 수를 입력해주세요");
      generalNurse = Integer.parseInt(sc.nextLine());
      System.out.println("간호조무사의 수를 입력해주세요");
      aideNurse = Integer.parseInt(sc.nextLine());
    } catch (NumberFormatException e) {
      System.out.println("숫자로 올바르게 입력해주세요");
      System.exit(0);
    }
    nurses = cal.makeNurses(headNurse, generalNurse, aideNurse);

    try {
      System.out.println("시작 날짜 입력(ex. 2019-03-01)");
      startDate = LocalDate.parse(sc.nextLine());
      System.out.println("마지막 날짜 입력(ex. 2019-04-02)");
      endDate = LocalDate.parse(sc.nextLine());
    } catch (DateTimeParseException e) {
      System.out.println("올바른 형태로 날짜를 입력해주세요.");
      System.exit(0);
    }

    if (ChronoUnit.DAYS.between(startDate, endDate) <= 0) {
      System.out.println("기간 설정이 잘못되었습니다.");
      System.exit(0);
    }

    for (int i = 0; i <= ChronoUnit.DAYS.between(startDate, endDate); i++) {
      LocalDate dateToSchedule = startDate.plusDays(i);
      Workday workday = new Workday(dateToSchedule);

      // 한 주의 시작인 월요일에는 근무시간대 변경
      if (workday.getWeekday().equals("MONDAY")) {
        cal.assignNewShift(nurses);
      }

      if (Arrays.asList(DateUtils.weekends).contains(workday.getWeekday())) { // 주말이라면
        cal.assignWeekends(workday, nurses);
      } else {
        boolean isHoliday = false;
        for (LocalDate holiday : DateUtils.holidays) {
          Period diff = Period.between(holiday, dateToSchedule);
          if (diff.getMonths() == 0 && diff.getDays() == 0) {
            isHoliday = true;
            break;
          }
        }

        // 공휴일인지 판단 후 공휴일이면 주말과 같이, 아니면 평일 방식으로 배정
        if (isHoliday) {
          cal.assignWeekends(workday, nurses);
        } else {
          cal.assignNurses(nurses, workday);
        }
      }

      // Map 방식으로 날짜별 업무 정보 저장
      schedule.put(dateToSchedule, workday);
    }

    System.out.println("\n근무조 안내");
    StringBuffer groupA = new StringBuffer("A조: ");
    StringBuffer groupB = new StringBuffer("B조: ");
    StringBuffer groupC = new StringBuffer("C조: ");
    for (Nurse nurse : nurses.get(Nurse.GENERAL_NURSE)) {
      switch (nurse.getGroupName()) {
      case "A조":
        groupA.append(nurse.getName() + " ");
        break;
      case "B조":
        groupB.append(nurse.getName() + " ");
        break;
      case "C조":
        groupC.append(nurse.getName() + " ");
        break;
      }
    }

    System.out.println(groupA);
    System.out.println(groupB);
    System.out.println(groupC + "\n");

    for (Map.Entry<LocalDate, Workday> sch : schedule.entrySet()) {
      System.out.println(sch.getKey().toString() + " " + sch.getKey().getDayOfWeek().toString().substring(0, 3));
      System.out.println(sch.getValue());
    }

    sc.close();
  }
}