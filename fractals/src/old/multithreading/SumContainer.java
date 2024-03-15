package old.multithreading;

public class SumContainer {
  public int sum;

  public SumContainer() {
    sum = 0;
  }

  public void add(int sum) {
    this.sum += sum;
  }

  public int getSum() {
    return sum;
  }
}
