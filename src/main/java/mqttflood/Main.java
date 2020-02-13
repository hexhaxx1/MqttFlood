package mqttflood;

import org.eclipse.paho.client.mqttv3.MqttException;

public class Main {

	public static void main(String[] args) throws MqttException, InterruptedException {
		
		int startSize = 1024;
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
		for (int size = startSize; size < endSize; size *= stepFactor) {
			byte[] data = new byte[size];
			
			System.out.println("sending " + size + " bytes ");
			new Flooder().wave("/hello", data, frequency, durationInSeconds);
		}
	}
}
