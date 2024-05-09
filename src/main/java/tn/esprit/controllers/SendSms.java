package tn.esprit.controllers;



import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilio.Twilio;
public class SendSms {

    public void sendsms(String msg) {
//        Twilio.init(, );

        Message message = Message.creator(
                        new PhoneNumber("+"),
                        new PhoneNumber("+"),
                        "Hello Sir/Madam The Type of Transaction is  "+ msg+ " Please check it out for more information \uD83D\uDE03 📞")
                .create();
        System.out.println(message.getSid());
    }

    public void sendsmsCodeVerification(String msg) {
//        Twilio.init();

        Message message = Message.creator(
                        new PhoneNumber("+"),
                        new PhoneNumber("+"),
                        "" +
                                "Hello Sir/Madam  " +
                                "The code \uD83D\uDD11 is  = "+ msg+" ||  To Delete The Archive Transaction 📞")
                .create();
        System.out.println(message.getSid());
    }

}
