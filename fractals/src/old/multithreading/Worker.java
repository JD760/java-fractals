package old.multithreading;

public class Worker extends Thread {
  public int startIndex;
  public int endIndex;
  public int[] nums;
  SumContainer sumc;

  public Worker(int startIndex, int endIndex, int[] nums, SumContainer sumc) {
    this.startIndex = startIndex;
    this.endIndex = endIndex;
    this.nums = nums;
    this.sumc = sumc;
  }

  @Override
  public void run() {
    int sum = 0;
    for (int i = startIndex; i < endIndex; i++) {
      sum += nums[i];
    }
    sumc.add(sum);
  }


}
