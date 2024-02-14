package com.three.ott_suggestion.user.service;

import com.three.ott_suggestion.image.entity.UserImage;
import com.three.ott_suggestion.image.repository.UserImageRepository;
import com.three.ott_suggestion.user.dto.SignupRequestDto;
import com.three.ott_suggestion.user.entity.RefreshToken;
import com.three.ott_suggestion.user.entity.User;
import com.three.ott_suggestion.user.repository.RefreshTokenRepository;
import com.three.ott_suggestion.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${defaultImage.path}")
    private String localPath;

    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JavaMailSender mailSender;

    @Transactional
    public void signup(SignupRequestDto signupRequestDto, String siteURL)
            throws UnsupportedEncodingException, MessagingException {

        String email = signupRequestDto.getEmail();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        String nickname = signupRequestDto.getNickname();
        String introduction = signupRequestDto.getIntroduction();
        String randomCode = RandomStringUtils.random(64, true, true);

        // 회원 중복 확인
        Optional<User> checkUserEmail = userRepository.findByEmail(email);
        if (checkUserEmail.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복된 email 입니다.");
        }
        String fileName = "1.jpg";
        String filePath = localPath + fileName;
        UserImage image = UserImage.builder()
                .fileName(fileName)
                .saveFileName("default")
                .contentType("image/png")
                .filePath(filePath)
                .build();
        userImageRepository.save(image);
        User user = new User(email, password, nickname, introduction, image, randomCode, false);
        userRepository.save(user);

        sendVerificationEmail(user, siteURL);
    }

    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.verify(null, true);
            userRepository.save(user);

            return true;
        }

    }

    @Transactional
    public void logout(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() ->
                new IllegalArgumentException("User가 존재하지 않습니다.")
        );
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId());
        refreshTokenRepository.delete(refreshToken);
    }

    private void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "test!";
        String senderName = "three";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getNickname());
        String verifyURL = siteURL + "/auth/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }
}
