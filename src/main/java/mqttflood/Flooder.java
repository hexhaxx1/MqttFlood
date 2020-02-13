package mqttflood;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Flooder {

	public void wave(String topic, byte[] data, long frequency, int durationInSeconds) throws MqttException, InterruptedException {
		MqttClient mc = new MqttClient("tcp://localhost:1883", "client_" + Thread.currentThread().getName());
		mc.connect();
		int qos = 0;
		boolean retained = false;
		
		new FrequencyCaller().callLoop(frequency, frequency * durationInSeconds, () -> {
			try {
				mc.publish(topic, data, qos, retained);
			} catch (MqttException e) {
				throw new RuntimeException("MQTT send failed", e);
			}
		});
		
		mc.disconnect();
		mc.close();
	}
	
}
