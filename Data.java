/**
 * Student: David Naughton
 * Student no. 320479
 * Email: dna@student.unimelb.edu.au 
 */

import java.util.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;
/**
 * Testing class
 */
public class Data {
    ArrayList<IOps> iops = new ArrayList<IOps>(3);
    String[] opNames = new String[]{"Dynamic Array","Binary Heap","Quake Heap"};
    final int[] e1_times = {100000,200000,500000,800000,1000000};
    final int[] e2_pct = {1,5,10,50,100};
    private Random random = new Random(50);
    
    public static void main(String[] args) {
        Data data = new Data();
        data.e1();
        data.e2();
        data.e3();
        data.e4();
    }

    /**
     * all operations, 5% delete mins and decrease keys, 90% inserts
     */
    private void e4() {
        for (int i=0;i<e1_times.length;i++) {
            iops.clear();
            // new data structures each round
            iops.add(new DynArray());
            iops.add(new BinaryHeap());
            iops.add(new QuakeHeapFast(0.6f));

            for (int k=0;k<iops.size();k++) {
                
                // all ops list
                ArrayList<Runnable> operations = 
                new ArrayList<Runnable>(e1_times[i]);

                int runningTotal=0;
                // generate ops
                for (int j=0;j<e1_times[i];j++) {
                    if (runningTotal==0) {
                        operations.add(iops.get(k).gen_insert());
                        runningTotal++;
                    } else {
                        // randomly sample
                        int r = random.nextInt(1000);
                        if(r<50) {
                            operations.add(iops.get(k).gen_decrease_key());
                        } else if(r<100) {
                            operations.add(iops.get(k).gen_delete_min());
                            runningTotal--;
                        } else {
                            operations.add(iops.get(k).gen_insert());
                            runningTotal++;
                        }
                    }      
                }
                // run timer
                long startTime = System.nanoTime();
                
                // run ops
                for (int j=0;j<e1_times[i];j++) {
                    operations.get(j).run();
                }
                long endTime = System.nanoTime();

                // report results
                long duration = (endTime - startTime); 
                System.out.println(opNames[k] + " iterations = "+ e1_times[i] + " time is " + duration/1000000 + " millisecs");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * inserts and decrease keys, decrease keys vary in pct
     */
    private void e3() {
        for (int i=0;i<e2_pct.length;i++) {
            iops.clear();
            // new data structures each round
            iops.add(new DynArray());
            iops.add(new BinaryHeap());
            iops.add(new QuakeHeapFast(0.6f));

            for (int k=0;k<iops.size();k++) {
                
                // insert and decrease key ops list
                ArrayList<Runnable> operations = 
                new ArrayList<Runnable>();

                int runningTotal=0;
                // generate ops
                for (int j=0;j<e1_times[4];j++) {
                    if (runningTotal==0) {
                        operations.add(iops.get(k).gen_insert());
                        runningTotal++;
                    } else {
                        // randomly sample
                        if (random.nextInt(1000)<e2_pct[i]) {
                            operations.add(iops.get(k).gen_decrease_key());
                        } else {
                            operations.add(iops.get(k).gen_insert());
                        }
                    }      
                }
                // run timer
                long startTime = System.nanoTime();
                
                // run ops
                for (int j=0;j<e1_times[4];j++) {
                    operations.get(j).run();
                }
                long endTime = System.nanoTime();

                // report results
                long duration = (endTime - startTime); 
                System.out.println(opNames[k] + " % decrease keys = "+ (double)e2_pct[i]/1000 + " time is " + duration/1000000 + " millisecs");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 1M inserts and deletes, deletes varying in pct
     */
    private void e2() {
        for (int i=0;i<e2_pct.length;i++) {
            iops.clear();
            // new data structures each round
            iops.add(new DynArray());
            iops.add(new BinaryHeap());
            iops.add(new QuakeHeapFast(0.6f));

            for (int k=0;k<iops.size();k++) {
                
                // insert and delete ops list
                ArrayList<Runnable> operations = 
                new ArrayList<Runnable>();

                int runningTotal=0;
                // generate ops
                for (int j=0;j<e1_times[4];j++) {
                    if (runningTotal==0) {
                        operations.add(iops.get(k).gen_insert());
                        runningTotal++;
                    } else {
                        // randomly sample
                        if (random.nextInt(1000)<e2_pct[i]) {
                            operations.add(iops.get(k).gen_delete_min());
                            runningTotal--;
                        } else {
                            operations.add(iops.get(k).gen_insert());
                            runningTotal++;
                        }
                    }      
                }
                // run timer
                long startTime = System.nanoTime();
                
                // run ops
                for (int j=0;j<e1_times[4];j++) {
                    operations.get(j).run();
                }
                long endTime = System.nanoTime();

                // report results
                long duration = (endTime - startTime); 
                System.out.println(opNames[k] + " % deletes = "+ (double)e2_pct[i]/1000 + " time is " + duration/1000000 + " millisecs");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * insertion only experiment
     */ 
    private void e1() {
        for (int i=0;i<e1_times.length;i++) {
            iops.clear();
            // new data structures each round
            iops.add(new DynArray());
            iops.add(new BinaryHeap());
            iops.add(new QuakeHeapFast(0.6f));

            for (int k=0;k<iops.size();k++) {

                ArrayList<Runnable> c = 
                new ArrayList<Runnable>(e1_times[i]);

                for (int j=0;j<e1_times[i];j++) {
                    c.add(iops.get(k).gen_insert()); 
                }
                // run timer
                long startTime = System.nanoTime();
                
                for (int j=0;j<e1_times[i];j++) {
                    c.get(j).run(); 
                }
                long endTime = System.nanoTime();

                long duration = (endTime - startTime); 
                System.out.println(opNames[k] + " iterations = "+ e1_times[i] + " time is " + duration/1000000 + " millisecs");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
            }

        }
            
    } 


}


