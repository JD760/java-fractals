package old.multithreading;

public class Test {
  public static void main(String[] args) {
    int[] nums = {1, 2, 3, 4, 5, 6};
    SumContainer sumc = new SumContainer();

    Worker t1 = new Worker(0, 2, nums, sumc);
    Worker t2 = new Worker(2, nums.length, nums, sumc);
    //Worker t3 = new Worker(3, 5, nums);

    t1.start();
    t2.start();
    //t3.start();
    try {
      t1.join();
      t2.join();
      //t3.join();
    } catch (InterruptedException e) {
      return;
    }

    System.out.println(sumc.getSum());

  }
}
