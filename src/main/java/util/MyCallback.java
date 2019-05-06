package util;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

public class MyCallback implements Callback {

    public void onCompletion(RecordMetadata metadata, Exception exception) {
        if (exception != null){
            exception.printStackTrace();
        }
        else {
            String topic = metadata.topic();
            int partition = metadata.partition();
            long offset = metadata.offset();
            System.out.println("topic:"+topic+",partition"+partition+",offset:"+offset);
        }
    }
}
