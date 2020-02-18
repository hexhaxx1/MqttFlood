package mqttflood;

import org.eclipse.paho.client.mqttv3.MqttException;

public class Main {

	public static void main(String[] args) throws MqttException, InterruptedException {
		
		int startSize = 10;
		int endSize = startSize * 1000;
		long stepFactor = 10;
		int durationInSeconds = 10;
		int parallelThreads = 2;
		long frequency = 1000;
		
		doSend(startSize, endSize, stepFactor, durationInSeconds, parallelThreads, frequency);
	}

	private static void doSend(int startSize, int endSize, long stepFactor, int durationInSeconds,
			int parallelThreads, long frequency) {
		for (int t=0; t<parallelThreads; t++) {
			new Thread(
					() -> {
						try {
							sendLoop(startSize, endSize, stepFactor, durationInSeconds, frequency);
						} catch (MqttException e) {
							throw new RuntimeException("MQTT send failed!", e);
						} catch (InterruptedException e) {
							throw new RuntimeException("We got interrupted!", e);
						}
					},
					"sendLoop" + (1+t)
			).start();
		}
	}

	private static void sendLoop(int startSize, int endSize, long stepFactor, int durationInSeconds, long frequency)
			throws MqttException, InterruptedException {
		String message = "{ \"Temp A\" : 23.45 , \"timestamp\" : 1579774539 , \"Unit\" : \"GradC\" } ";
		for (int size = startSize; size <= endSize; size *= stepFactor) {
			StringBuilder sb = new StringBuilder();
			sb.append("[").append(message);
			for(int s=1; s<size; s++) {
				sb.append(",").append(message);
			}
			sb.append("]");
			byte[] data = sb.toString().getBytes();	
			System.out.println("sending " + data.length + " bytes ");
			new Flooder().wave("Analysis/TestA", data, frequency, durationInSeconds);
		}
	}
}
