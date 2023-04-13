package com.app.feenix.viewmodel;

public interface OtpReceivedInterface {

    void onOtpReceived(String otp);

    void onOtpTimeout();
}
