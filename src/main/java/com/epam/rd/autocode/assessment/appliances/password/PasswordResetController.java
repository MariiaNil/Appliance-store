package com.epam.rd.autocode.assessment.appliances.password;

import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import com.epam.rd.autocode.assessment.appliances.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/password")
public class PasswordResetController {

    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "/password/forgot-password";
    }

    @PostMapping("/forgot-password")
    @Transactional
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        Optional<Client> clientOps = clientRepository.findByEmail(email);
        if(clientOps.isEmpty()){
            model.addAttribute("error", "Пользователь с таким email не найден.");
            return "/password/forgot-password";
        }
        Client client = clientOps.get();
        tokenRepository.deleteByClient(client);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setClient(client);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        tokenRepository.save(resetToken);


        String resetLink = "http://localhost:8080/password/reset-password?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(client.getEmail());
        message.setSubject("Сброс пароля");
        message.setText("Перейдите по следующей ссылке для сброса пароля: " + resetLink);
        mailSender.send(message);

        model.addAttribute("message", "Письмо с инструкцией отправлено на ваш email.");
        return "/password/forgot-password";
    }

    @GetMapping("/reset-password")
    public String displayResetPasswordPage(@RequestParam("token") String token, Model model) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        if (tokenOpt.isEmpty() || tokenOpt.get().isExpired()) {
            model.addAttribute("error", "Токен недействителен или истёк.");
            return "/password/reset-password-error";
        }
        model.addAttribute("token", token);
        return "/password/reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("password") String password,
                                       Model model) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        if (tokenOpt.isEmpty() || tokenOpt.get().isExpired()) {
            model.addAttribute("error", "Токен недействителен или истёк.");
            return "/password/reset-password-error";
        }

        Client client = tokenOpt.get().getClient();
        client.setPassword(passwordEncoder.encode(password));
        clientRepository.save(client);

        tokenRepository.delete(tokenOpt.get());

        model.addAttribute("message", "Пароль успешно изменен.");
        return "/password/reset-password-success";
    }



}
