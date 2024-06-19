package com.example.a1_gavinpan_fit2081;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {

    // used as a channel to broadcast the message
    //Any application aware of this channel can listen to the broadcasts
    public static final String CATEGORY_SMS_FILTER = "CATEGORY_SMS_FILTER";

    public static final String EVENT_SMS_FILTER = "EVENT_SMS_FILTER";


    //Within the broadcast, we would like to send information
    // and this will be the key to identifying that information, in this case, the SMS message
    public static final String SMS_MSG_KEY = "SMS_MSG_KEY";

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (int i = 0; i < messages.length; i++) {
            SmsMessage currentMessage = messages[i];
            String message = currentMessage.getDisplayMessageBody();
                if (message.startsWith("category:")) {
                    /*
                     * For each new message, send a broadcast that contains the new message to the desired acitvity
                     * The activity has to tokenize the new message and update the UI
                     * */
                    Intent msgCategoryIntent = new Intent();
                    msgCategoryIntent.setAction(CATEGORY_SMS_FILTER);
                    msgCategoryIntent.putExtra(SMS_MSG_KEY, message);
                    context.sendBroadcast(msgCategoryIntent);

                }
                if (message.startsWith("event:")) {
                    /*
                     * For each new message, send a broadcast that contains the new message to the desired acitvity
                     * The activity has to tokenize the new message and update the UI
                     * */
                    Intent msgEventIntent = new Intent();
                    msgEventIntent.setAction(EVENT_SMS_FILTER);
                    msgEventIntent.putExtra(SMS_MSG_KEY, message);
                    context.sendBroadcast(msgEventIntent);

                }


        }

    }
}