import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.common.collect.Lists;
import com.google.pubsub.v1.ProjectSubscriptionName;

public class Main {

    private static final String PROJECT_ID = "kill-switch-265412";
    public static String message = "";

    public static void main(String[] args) throws RuntimeException, IOException {

        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to KEVIN. KEVIN says:");
        
        String pass = "hallexlol";
        System.out.print("Would you like to change your password? (Y/N): ");
        String input = scan.next();
        if(input.equalsIgnoreCase("Y"))
        {
            System.out.print("Enter a password here (no spaces): ");
            pass = scan.next();
            System.out.println("New password is " + pass);
        }
        else
            System.out.println("Password will remain " + pass);


        
        //code for receiving message
        String subscriptionId = "sub1";
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(
                PROJECT_ID, subscriptionId);
        Subscriber subscriber = null;
        CredentialsProvider credentialsProvider =
        	    FixedCredentialsProvider.create(
        	      ServiceAccountCredentials.fromStream(new FileInputStream("credentials.json")));
        try {
            // create a subscriber bound to the asynchronous message receiver
            subscriber = Subscriber
            		.newBuilder(subscriptionName, new ReceiveMessages(pass))
            		.setCredentialsProvider(credentialsProvider)
            		.build();
            subscriber.startAsync().awaitRunning();
            System.out.println("Subscriber is up and running!");
            // Allow the subscriber to run indefinitely unless an unrecoverable error occurs.
            subscriber.awaitTerminated();
        } catch (IllegalStateException e) {
            System.out.println("Subscriber unexpectedly stopped: " + e);
        }

    }
}
