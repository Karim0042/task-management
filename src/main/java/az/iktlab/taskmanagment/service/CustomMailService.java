package az.iktlab.taskmanagment.service;

public interface CustomMailService {
    void sendOTPMail(String to, String otp);
    void sendMail(String to);
}
