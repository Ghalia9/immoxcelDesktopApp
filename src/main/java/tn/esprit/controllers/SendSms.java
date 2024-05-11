package tn.esprit.controllers;



import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilio.Twilio;
public class SendSms {
    public static final String TWILIO_ACCOUNT_SID = "ACeb81a2c6b3b5cf96ccdd22c25a2ec0ec";
    public static final String TWILIO_AUTH_TOKEN = "b8247ae8bb9265b039ba9a64883c2b2b";

    public void sendsms(String msg) {
        Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);

        Message message = Message.creator(
                        new PhoneNumber("+21695206910"),
                        new PhoneNumber("+14234381779"),
                        "Hello Sir/Madam  " +
                                "New transaction type" + msg + " Check it out !  📞")
                .create();
        System.out.println(message.getSid());
    }

    public void sendsmsCodeVerification(String msg) {
        Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);

        Message message = Message.creator(
                        new PhoneNumber("+21695206910"),
                        new PhoneNumber("+14234381779"),
                        "Hello Sir/Madam  " +
                                "The code \uD83D\uDD11  =" + msg + "  To Delete The Archive Transaction 📞")
                .create();
        System.out.println(message.getSid());
    }
}