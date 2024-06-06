package javiergs;

import org.eclipse.paho.client.mqttv3.*;

public class PongPublisher implements MqttCallback {
    MqttClient client;
    String broker = "tcp://test.mosquitto.org:1883";
    String sendTopic;
    String receiveTopic;
    String clientId;
    public PongPublisher(String sendTopic, String receiveTopic, String clientId){
        this.clientId = clientId;
        this.sendTopic = sendTopic;
        this.receiveTopic = receiveTopic;
        try {
            client = new MqttClient(broker, clientId);
            client.connect();
            System.out.println("Connected to broker: " + broker);
            client.subscribe(receiveTopic);
            System.out.println("Subscribed to topic: " + receiveTopic);
            client.setCallback(this);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection lost: " + throwable.getMessage());
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) {
        System.out.println("Message arrived. Topic: " + s +
                " Message: " + new String(mqttMessage.getPayload()));

        try {
            PongData pongData = (PongData) SerializationUtils.deserialize(mqttMessage.getPayload());
            if(clientId.equals("client")){
                System.out.println("Setting server player to " + pongData.getServerPlayerY());
                PongBrain.getInstance().setServerPlayerY(pongData.getServerPlayerY());
            }
            else{
                System.out.println("Setting client player to " + pongData.getClientPlayerY());
                PongBrain.getInstance().setClientPlayerY(pongData.getClientPlayerY());
            }


        }catch(Exception ex){
            ex.printStackTrace();
        }


    }

    public void sendData(PongData pongData){
        try {
            MqttMessage message = new MqttMessage(SerializationUtils.serialize(pongData));
            message.setQos(2);
            if (client.isConnected())  client.publish(sendTopic, message);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }

    public boolean isConnected(){
        return client.isConnected();
    }
}
