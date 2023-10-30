package com.timeco.application.Service.otpservice;

import com.timeco.application.Repository.OtpRepository;
import com.timeco.application.Repository.UserRepository;
import com.timeco.application.model.user.Otp;
import com.timeco.application.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private UserRepository userRepository;


    @Autowired

    private EmailSender emailSender;

    @Autowired
    private OtpRepository otpRepository;

    private String errorMessage;

    private String successMessage;

    public void sendRegistrationOtp(String email) {


        // Create a random number generator
        Random random = new Random();
        int min = 100000; // Minimum 6-digit number
        int max = 999999; // Maximum 6-digit number
        int randomNumber = random.nextInt(max - min + 1) + min;

        // Set the expiration time for the OTP
        LocalDateTime expirationTime = LocalDateTime.now().plus(2, ChronoUnit.MINUTES);

        String emailBody = "Your OTP is: " + randomNumber;
        try {
            emailSender.sendEmail(email, "OTP Verification", emailBody);
        } catch (MessagingException e) {
            System.out.println("Failed to send email"+ e);
        }


        Otp newOtp = otpRepository.findByEmail(email);
        if(newOtp==null) {
            newOtp = new Otp(email, randomNumber, expirationTime);
        }
        newOtp.setOtp(randomNumber);
        newOtp.setExpirationTime(expirationTime);
        otpRepository.save(newOtp);
        errorMessage = null;

    }
    public boolean validateRegistrationOtp(String email, int otp) {
        Otp validatedOtp = otpRepository.findByEmail(email);

        if (validatedOtp != null) {
            LocalDateTime expirationTime = validatedOtp.getExpirationTime();
            LocalDateTime currentTime = LocalDateTime.now();

            if (currentTime.isBefore(expirationTime) && validatedOtp.getOtp().equals(otp)) {
                // The OTP is valid and within the time limit
                errorMessage = null;
                successMessage = "Account Created Successfully";
                otpRepository.delete(validatedOtp);
                return true;
            } else if (currentTime.isAfter(expirationTime)) {
                // Time has expired
                errorMessage = "Time expired....";
                successMessage = null;
                otpRepository.delete(validatedOtp);
            } else {
                // Invalid OTP
                errorMessage = "Invalid OTP";
                successMessage = null;
            }
        }
        return false;
    }

}
