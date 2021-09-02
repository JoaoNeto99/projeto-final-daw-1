package com.leiloes.sistemadeleiloes.email;

import com.leiloes.sistemadeleiloes.model.Leilao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public String sendMail(Leilao leilao) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Parabéns " + leilao.getLance().getUsuario().getNome() + " você foi o ganhador!");
        message.setText("Parabéns por ter arrematado o item " + leilao.getItem() + " por " + leilao.getLance().getValor());
        message.setTo(leilao.getLance().getUsuario().getEmail());
        message.setFrom("leilao7499@outlook.com");

        try {
            mailSender.send(message);
            return "Email enviado com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao enviar email.";
        }
    }
}
