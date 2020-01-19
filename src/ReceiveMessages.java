//GOOGLE_APPLICATION_CREDENTIALS MUST BE SET!!!!!
//interfaces
import java.io.IOException;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;

//classes
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import com.google.protobuf.Timestamp;
import java.util.Date;

public class ReceiveMessages implements MessageReceiver {
    private String PASSWD = "";
    
    public ReceiveMessages(String pass)
    {
        PASSWD = pass;
    }
    

    @Override
    public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
        //Return contents of message to Main class.
        String command = message.getData().toStringUtf8();
        
        
        //Print out the message (for testing)
        System.out.println(
                "Message received. Id: " + message.getMessageId() + " Data: " + command);
        
        // Ack only after all work for the message is complete.
        consumer.ack();
        
            //check for shutdown.
            try {
                checkForShutdown(command);
            } catch (RuntimeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
    
    public void checkForShutdown(String str) throws RuntimeException, IOException
    {
        int ch = str.indexOf(' ');
        String temp = str.substring(0, ch);
        String password = "";
        if(str.substring(ch+1) != "")
            password = str.substring(ch+1);
        //System.out.println(password);
        //System.out.println(str + "done");
        if (temp.equals("Shutdown") && password.equals(PASSWD))
            shutdown();
    }
    
    public static void shutdown() throws RuntimeException, IOException {
        String shutdownCommand;
        String operatingSystem = System.getProperty("os.name");
        System.out.println(operatingSystem);

        if (operatingSystem.contains("Linux") || operatingSystem.contains("Mac OS X")) {
            shutdownCommand = "sudo shutdown -h now";
        }
        else if (operatingSystem.contains("Windows")) {
            shutdownCommand = "shutdown.exe -s -t 0";
        }
        else {
            throw new RuntimeException("Unsupported operating system.");
        }

        Runtime.getRuntime().exec(shutdownCommand);
        System.exit(0);
    }

}
