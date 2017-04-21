package tp1_sd.mqttapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mTemperatura = mSensorManager.getDefaultSensor(Sensor.TYPE_ALL);

        //publish();
    }

    //Variaveis do sensor
    protected SensorManager mSensorManager;
    protected Sensor mTemperatura;

    //Publicar esta variavel retorno do sensor
    float [] axis;
    String topic = "bacon";
    String content = "PUBLICACHESUS";
    int qos = 0;

    //troca o IP
    String broker = "tcp://192.168.25.5:1883";
    String clientId = "JavaSample";
    MemoryPersistence persistence = new MemoryPersistence();

    //public void printo(){
      //  Toast.makeText(this, "valor: " + Float.toString(axis1), Toast.LENGTH_LONG).show();
    //}

    //publish está dentro do onSensorChanged
   public void publish() {
       //Dado do Sensor que será publicado
       content = "Sensor data: " + Float.toString(axis[0]);
        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message published");
            sampleClient.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        //printo();
        axis = event.values;
        publish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mTemperatura, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
