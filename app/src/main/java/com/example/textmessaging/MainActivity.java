package com.example.textmessaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final int MY_PERMISSIONS_REQUEST_SEND_SMS =0;
    TextView textView;
    Handler handler;
    int state = 1;
    String message = "";
    String sendMessage = "";
    int count = 1;
    int state3counter = 1;
    //0 is initiating
    int state1count=0;
    int state2count=0;
    int state3count=0;
    int state4count=0;

    ArrayList<String> def = new ArrayList();


    BroadcastReceiver myBroadcast = new ReceiveSMS();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.d("tag", "created!");
        System.out.println("created!!!");

        textView = findViewById(R.id.id_textView);

        def.add("Umm anyways");
        def.add("uh ok then");
        def.add("I'll just ignore that");

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d("tag", ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECEIVE_SMS) + "");

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECEIVE_SMS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);

                Log.d("tag", "asking for permission");
                Log.d("tag", ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECEIVE_SMS) + ""); //shows -1



                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {

        }

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {

                if(count ==1) {

                    Log.d("tag", "handler running state " + state);

                    handler.postDelayed(createRunnable(sendMessage), 5000);
                    count++;
                }

            }
        };



    }

    public Runnable createRunnable(final String send){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                String phoneNumber = "5556"; //change later

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, send, null, null);
            }
        };
        return runnable;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {


        Log.d("tag", "on request permission " + MY_PERMISSIONS_REQUEST_SEND_SMS);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Thank you for permitting", Toast.LENGTH_SHORT).show();
                    Log.d("tag", "permitted");

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(myBroadcast, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myBroadcast);
    }


    public void state1()    //introduction, drops hints
    {

        state1count++;

        textView.setText("1. Warming up to the person");
        ArrayList<String> warmingUp = new ArrayList();
        warmingUp.add("Hey! I'm so glad to talk to you, because I've wanted to tell you something");
        warmingUp.add("Hey! I've been meaning to tell you something");
        warmingUp.add("This quarantine has got me thinking...");

        final Message msg = new Message();

        Log.d("tag", "state 1 received message: " + message);
        if(state1count == 1){
            count = 1;
            sendMessage = warmingUp.get((int) (Math.random() * 3));
            handler.sendMessage(msg); //this sends the intro message
        }
        else{
            if(message.toLowerCase().contains("?") || message.toLowerCase().contains("huh")||message.toLowerCase().contains("what")||message.toLowerCase().contains("ok")) {

                state = 2;
                state2();
            }
            else{
                textView.setText("1. Warming up to the person\nunidentified response");
                count = 1;
                sendMessage = def.get((int) (Math.random() * def.size()));
                handler.sendMessage(msg);
            }
        }

    }

    public void state2()    //pick up line
    {

        state2count++;

        Log.d("tag", "running state 2");
        textView.setText("2. Saying pick up lines to lighten it up");
        ArrayList<String> pickUpLine = new ArrayList<>();
        pickUpLine.add("Are you a fart? Because you blow me away");
        pickUpLine.add("Baby, do you need toilet paper? Because I can be your Prince Charmin");
        pickUpLine.add("Without you my life is as empty as the shelves at Costco");
        pickUpLine.add("You can't spell quarantine without U R A Q T");



        ArrayList<String> reinforcement = new ArrayList<>();
        reinforcement.add("haha I was just testing my pick up lines");
        reinforcement.add("lol sorry for weirding you out, my bad");

        final Message msg = new Message();

        if(state2count == 1){
            sendMessage = pickUpLine.get((int)(Math.random()*pickUpLine.size()));
            count = 1;
            handler.sendMessage(msg);
        }
        else{
            if(message.toLowerCase().contains("thank") || message.toLowerCase().contains("you too") || message.toLowerCase().contains("lol")||message.toLowerCase().contains("lmao")||message.toLowerCase().contains("ok")) {
                state = 3;
                state3();
            }
            else if(message.toLowerCase().contains("um")||message.toLowerCase().contains("uh")) {
                textView.setText("2. Intializing love confession\n*detecting threat of rejection, sending reinforcement*");
                sendMessage = reinforcement.get((int)(Math.random()*reinforcement.size()));
                count = 1;
                handler.sendMessage(msg);
            }
            else{
                textView.setText("2. Intializing pick up lines to lighten it up\nunidentified response");
                count = 1;
                sendMessage = def.get((int) (Math.random() * def.size()));
                handler.sendMessage(msg);
            }
        }


    }



    public void state3()    //love confession
    {

        state3count++;

        Log.d("tag", "running state 2");
        textView.setText("3. Love confession");
        ArrayList<String> confession = new ArrayList<>();
        confession.add("On a more serious note, I am deeply in love with you");
        confession.add("Ok but seriously, I really like you.");
        confession.add("Jokes apart, I really like you");

        ArrayList<String> confusion = new ArrayList<>();
        confusion.add("What I mean to say is, you are an amazing person");
        confusion.add("What I mean to say is, I deeply admire you");

        ArrayList<String> rejectionSave = new ArrayList<>();
        rejectionSave.add("PLEASE DON'T GO");
        rejectionSave.add("wait hear me out");

        final Message msg = new Message();

        if(state3count ==1){
            sendMessage = confession.get((int)(Math.random()*3));
            count = 1;
            handler.sendMessage(msg);
        }

        else{
            if(message.toLowerCase().contains("thank") || message.toLowerCase().contains("you too") || message.toLowerCase().contains("lol")||message.toLowerCase().contains("lmao")||message.toLowerCase().contains("aw")||message.toLowerCase().contains("ok")) {
                state = 4;
                state4();
            }
            else if(message.toLowerCase().contains("what")||message.toLowerCase().contains("where")||message.toLowerCase().contains("how")||message.toLowerCase().contains("when")) {
                textView.setText("3. Intializing love confession\ndetecting confusion");
                sendMessage = confusion.get((int)(Math.random()*confusion.size()));
                count = 1;
                handler.sendMessage(msg);
            }
            else if(message.toLowerCase().contains("ew")||message.toLowerCase().contains("no")||message.toLowerCase().contains("sorry")||message.toLowerCase().contains("uh")) {
                textView.setText("3. Intializing love confession\ndetecting rejection threat *breaks into nervous sweat*");
                sendMessage = rejectionSave.get((int)(Math.random()*rejectionSave.size()));
                count = 1;
                handler.sendMessage(msg);
            }
            else{
                textView.setText("3. Intializing love confession\nunidentified response");
                count = 1;
                sendMessage = def.get((int) (Math.random() * def.size()));
                handler.sendMessage(msg);
            }
        }

    }

    public void state4()    //asking out
    {

        state4count++;

        Log.d("tag", "running state 2");
        textView.setText("4. Proceeding to ask out");
        ArrayList<String> askout = new ArrayList<>();
        askout.add("So do you wanna go out?");
        askout.add("Anyways, can we go out on a date?");
        askout.add("Will you go out with me?");

        ArrayList<String> persuasion = new ArrayList<>();
        persuasion.add("pretty pleaseeeee???");
        persuasion.add("I'm rich and good looking, just putting it out there");

        ArrayList<String> rejection = new ArrayList<>();
        rejection.add("now I'm quarantined AND sad :( ");
        rejection.add("I'd go to the store to buy tissues for my tears but I wouldn't even find any :( ");

        final Message msg = new Message();

        if(state4count == 1){
            sendMessage = askout.get((int)(Math.random()*askout.size()));
            count = 1;
            handler.sendMessage(msg);
        }

        else{
            if(message.toLowerCase().contains("yes") || message.toLowerCase().contains("yea") || message.toLowerCase().contains("of course")||message.toLowerCase().contains("definitely")||message.toLowerCase().contains("sure")) {
                textView.setText("Success!!!");
                sendMessage = "Woohoo! I'm so happy!";
                count = 1;
                handler.sendMessage(msg);
            }
            else if(message.toLowerCase().contains("uh")||message.toLowerCase().contains("um")||message.toLowerCase().contains("sorry")||message.toLowerCase().contains("later")) {
                textView.setText("4. Proceeding to ask out: starting persuasion");
                sendMessage = persuasion.get((int)(Math.random()*rejection.size()));
                count = 1;
                handler.sendMessage(msg);
            }
            else if(message.toLowerCase().contains("no")||message.toLowerCase().contains("sorry")) {
                textView.setText("You got rejected");
                sendMessage = rejection.get((int)(Math.random()*rejection.size()));
                count = 1;
                handler.sendMessage(msg);
            }
            else {
                sendMessage = "I'll take that as a yes!! Yay!";
                textView.setText("Success!!!");
                count = 1;
                handler.sendMessage(msg);
            }
        }

    }




    public class ReceiveSMS extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            //context.registerReceiver(this, new IntentFilter.(android.provider.Telephony.SMS_RECEIVED));
            Bundle bundle = intent.getExtras();
            if(bundle != null) {
                Object[] pdusArray = (Object[]) bundle.get("pdus");
                SmsMessage[] smsMessages = new SmsMessage[pdusArray.length];

                for (int i = 0; i < pdusArray.length; i++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdusArray[i], bundle.getString("format"));
                    }

                    textView.setText(smsMessages[i].getMessageBody());

                    message = smsMessages[i].getMessageBody();

                    Log.d("tag", "state: " + state);

                    switch (state){
                        case 1: state1();
                            break;
                        case 2: state2();
                            break;
                        case 3: state3();
                            break;
                        case 4: state4();
                            break;
                    }
                }

            }
        }


    }

}
