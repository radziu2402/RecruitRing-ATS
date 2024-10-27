package pl.pwr.recruitringcore.service;

public interface AuthService {
    void sendVerificationCode(String email);

    boolean verifyCode(String email, String code);
}
