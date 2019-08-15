package scheduler.model;

public enum Shift {
  MIDNIGHT("새벽", 0), DAY("주간", 1), NIGHT("야간", 2);

  private String shiftName;
  private int index;

  Shift(String shiftName, int index) {
    this.shiftName = shiftName;
    this.index = index;
  }

  public int index() {
    return index;
  }

  @Override
  public String toString() {
    return shiftName;
  }
}