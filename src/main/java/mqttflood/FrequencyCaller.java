package mqttflood;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FrequencyCaller {

	public void callLoop(long frequency, long times, Runnable method) throws InterruptedException {
		long maxTime = BigDecimal.valueOf(1000000000).divide(BigDecimal.valueOf(frequency)).longValue();
		
		long start = System.nanoTime();
		for(long i=0; i<times; i++) {
			call(method, maxTime, start, i+1);
		}
		long stop = System.nanoTime();
		
		BigDecimal meanCallFreq = BigDecimal.valueOf(1000000000).divide(BigDecimal.valueOf(stop-start).divide(BigDecimal.valueOf(times)), 4, RoundingMode.HALF_UP);
		System.out.println("achieved a mean freqency of " + meanCallFreq);
	}

	private void call(Runnable method, long maxTime, long firstStart, long callCounter) throws InterruptedException {
		method.run();
		long stop = System.nanoTime();
		
		long totalDuration = stop - firstStart;
		long left = (maxTime * callCounter) - totalDuration - 10000;
		
		if (left > 0) {
			Thread.sleep(left / 1000000, (int) (left % 1000000));
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		new FrequencyCaller().callLoop(10, 10, () -> { 
			try {
				Thread.sleep(110);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
}
