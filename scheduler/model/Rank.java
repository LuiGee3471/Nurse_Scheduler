package scheduler.model;

public enum Rank {
  HEAD("수간호사", 0), GENERAL("일반간호사", 1), AIDE("간호조무사", 2);

  private String rankName;
  private int index;

  Rank(String rankName, int index) {
    this.rankName = rankName;
    this.index = index;
  }

  public int index() {
    return index;
  }

  @Override
  public String toString() {
    return rankName;
  }
}