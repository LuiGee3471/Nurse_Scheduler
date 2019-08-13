package scheduler;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import scheduler.model.Nurse;
import scheduler.model.Workday;

/**
 * main 클래스에서 사용되는 함수를 별도로 가지고 있는 클래스
 */
public class NurseCalculator {
  private Random random = new Random();

  /**
   * 월요일에 근무시간대 새로 배정
   */
  public void assignNewShift(List<List<Nurse>> nurses) {
    List<Nurse> generals = nurses.get(Nurse.GENERAL_NURSE);
    List<Nurse> aides = nurses.get(Nurse.AIDE_NURSE);

    for (Nurse general : generals) {
      int lastShift = general.getCurrentShift();
      int newShift = lastShift == 0 ? 2 : lastShift - 1;
      general.setLastShift(lastShift);
      general.setCurrentShift(newShift);
    }

    for (Nurse aide : aides) {
      int lastShift = aide.getCurrentShift();
      int newShift = lastShift == 0 ? 2 : lastShift - 1;
      aide.setLastShift(lastShift);
      aide.setCurrentShift(newShift);
    }
  }

  /**
   * 오늘 근무 시간대 출력을 위해
   */
  public void setTodayShift(List<Nurse> nurses, Workday workday) {
    Map<String, String> todayShift = workday.getTodayShift();
    for (int i = 0; i < 3; i++) {
      Nurse nurse = nurses.get(i);
      switch (nurse.getCurrentShift()) {
      case Nurse.MIDNIGHT:
        todayShift.put("새벽", nurse.getGroupName());
        break;
      case Nurse.DAY:
        todayShift.put("주간", nurse.getGroupName());
        break;
      case Nurse.AIDE_NURSE:
        todayShift.put("야간", nurse.getGroupName());
        break;
      }
    }

  }

  /**
   * 초기 간호사 정보 생성
   */
  public List<List<Nurse>> makeNurses(int headNurse, int generalNurse, int aideNurse) {
    List<List<Nurse>> nurses = new ArrayList<>();
    List<Nurse> headNurses = new ArrayList<>();
    List<Nurse> generalNurses = new ArrayList<>();
    List<Nurse> aideNurses = new ArrayList<>();

    for (int i = 0; i < headNurse; i++) {
      Nurse head = new Nurse();
      head.setName("수" + (i + 1));
      head.setRank(Nurse.HEAD_NURSE);
      headNurses.add(head);
    }

    for (int i = 0; i < generalNurse; i++) {
      Nurse general = new Nurse();
      general.setName("일반" + (i + 1));
      general.setRank(Nurse.GENERAL_NURSE);
      if (i % 3 == 0) {
        general.setCurrentShift(Nurse.MIDNIGHT);
        general.setGroupName(Nurse.groupNames[0]);
      } else if (i % 3 == 1) {
        general.setGroupName(Nurse.groupNames[1]);
      } else {
        general.setCurrentShift(Nurse.NIGHT);
        general.setGroupName(Nurse.groupNames[2]);
      }
      generalNurses.add(general);
    }

    for (int i = 0; i < aideNurse; i++) {
      Nurse aide = new Nurse();
      aide.setName("조무" + (i + 1));
      aide.setRank(Nurse.AIDE_NURSE);
      if (i % 3 == 0) {
        aide.setCurrentShift(Nurse.MIDNIGHT);
        aide.setGroupName(Nurse.groupNames[0]);
      } else if (i % 3 == 1) {
        aide.setGroupName(Nurse.groupNames[1]);
      } else {
        aide.setCurrentShift(Nurse.NIGHT);
        aide.setGroupName(Nurse.groupNames[2]);
      }
      aideNurses.add(aide);
    }

    nurses.add(headNurses);
    nurses.add(generalNurses);
    nurses.add(aideNurses);

    return nurses;
  }

  /**
   * 평일 근무 배정
   */
  public void assignNurses(List<List<Nurse>> nurses, Workday workday) {
    setTodayShift(nurses.get(Nurse.GENERAL_NURSE), workday);
    for (List<Nurse> list : nurses) {
      assignNursesOnWeekday(list, workday);
    }
  }

  /**
   * 평일 간호사, 간호조무사 배정
   */
  public void assignNursesOnWeekday(List<Nurse> nurses, Workday workday) {
    for (Nurse nurse : nurses) {
      if (isBirthday(nurse.getBirthday(), workday.getWorkdate())) {
        nurse.setOffDays(nurse.getOffDays() + 1);
        workday.getOffDayNurses().add(nurse.getName());
      }
    }
  }

  /**
   * 주말 근무 배정
   */
  public void assignWeekends(Workday workday, List<List<Nurse>> nurses) {
    List<List<Nurse>> filteredNurses = filterWithOffDays(nurses);
    List<Nurse> heads = filteredNurses.get(Nurse.HEAD_NURSE);
    List<Nurse> generals = filteredNurses.get(Nurse.GENERAL_NURSE);
    List<Nurse> aides = filteredNurses.get(Nurse.AIDE_NURSE);

    workday.setHeadOnWeekend(heads.get(random.nextInt(heads.size())));
    workday.setGeneralsOnWeekend(assignNurseOnWeekend(workday, generals));
    workday.setAidesOnWeekend(assignNurseOnWeekend(workday, aides));

    for (Nurse head : nurses.get(Nurse.HEAD_NURSE)) {
      if (head != workday.getHeadOnWeekend()) {
        head.setOffDays(head.getOffDays() + 1);
      }
    }

    for (Nurse general : nurses.get(Nurse.GENERAL_NURSE)) {
      if (!workday.getGeneralsOnWeekend().contains(general)) {
        general.setOffDays(general.getOffDays() + 1);
        general.setWorkToday(false);
      }
    }

    for (Nurse aide : nurses.get(Nurse.AIDE_NURSE)) {
      if (!workday.getAidesOnWeekend().contains(aide)) {
        aide.setOffDays(aide.getOffDays() + 1);
        aide.setWorkToday(false);
      }
    }
  }

  /**
   * 주말 일반간호사, 간호조무사 배정
   */
  public List<Nurse> assignNurseOnWeekend(Workday workday, List<Nurse> nurses) {
    List<Nurse> nursesToWork = new ArrayList<>();

    while (nursesToWork.size() < 3) {
      Nurse nurse = nurses.get(random.nextInt(nurses.size()));
      if (nurse.getCurrentShift() == nursesToWork.size()) {
        nurse.setWorkToday(true);
        nursesToWork.add(nurse);
      }
    }

    return nursesToWork;
  }

  /**
   * 평균 휴무일 계산
   */
  public int averageOffDays(List<Nurse> nurses) {
    int totalOffDays = 0;
    int averageOffDays = 0;

    for (Nurse nurse : nurses) {
      totalOffDays += nurse.getOffDays();
    }

    averageOffDays = (int) Math.round((double) totalOffDays / nurses.size());
    return averageOffDays;
  }

  /**
   * 평균 휴무일보다 많이 또는 똑같이 쉰 간호사만 불러오기
   */
  public List<List<Nurse>> filterWithOffDays(List<List<Nurse>> nurses) {
    List<List<Nurse>> filteredNurses = new ArrayList<>();
    List<Nurse> lessWorkedHeads = new ArrayList<>();
    List<Nurse> lessWorkedGenerals = new ArrayList<>();
    List<Nurse> lessWorkedAides = new ArrayList<>();

    for (List<Nurse> nursesList : nurses) {
      for (Nurse nurse : nursesList) {
        if (nurse.getOffDays() >= averageOffDays(nursesList) && !nurse.getWorkToday()) {
          switch (nurse.getRank()) {
          case Nurse.HEAD_NURSE:
            lessWorkedHeads.add(nurse);
            break;
          case Nurse.GENERAL_NURSE:
            lessWorkedGenerals.add(nurse);
            break;
          case Nurse.AIDE_NURSE:
            lessWorkedAides.add(nurse);
            break;
          default:
            System.out.println("올바르지 않은 간호사 정보");
          }
        }
      }
    }

    // 조건에 맞는 간호사가 충분하지 않다면 전체 대상으로 변경
    filteredNurses.add(isOkToUseList(lessWorkedHeads) ? lessWorkedHeads : nurses.get(Nurse.HEAD_NURSE));
    filteredNurses.add(isOkToUseList(lessWorkedGenerals) ? lessWorkedGenerals : nurses.get(Nurse.GENERAL_NURSE));
    filteredNurses.add(isOkToUseList(lessWorkedAides) ? lessWorkedAides : nurses.get(Nurse.AIDE_NURSE));

    return filteredNurses;
  }

  /**
   * 간호사 리스트를 그대로 사용해도 문제가 없는지 체크
   */
  public boolean isOkToUseList(List<Nurse> nurses) {
    boolean midnight = false;
    boolean day = false;
    boolean night = false;

    for (Nurse nurse : nurses) {
      switch (nurse.getCurrentShift()) {
      case Nurse.MIDNIGHT:
        midnight = true;
        break;
      case Nurse.DAY:
        day = true;
        break;
      case Nurse.NIGHT:
        night = true;
        break;
      }
    }

    return midnight && day && night && nurses.size() > 0;
  }

  /**
   * 생일인지 검사
   */
  public boolean isBirthday(LocalDate birthday, LocalDate today) {
    Period period = Period.between(birthday, today);
    boolean isBirthday = period.getMonths() == 0 && period.getDays() == 0;
    return isBirthday;
  }
}