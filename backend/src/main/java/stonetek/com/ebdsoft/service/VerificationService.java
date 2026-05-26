package stonetek.com.ebdsoft.service;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import stonetek.com.ebdsoft.model.CodigoVerificacao;
import stonetek.com.ebdsoft.repository.CodigoVerificacaoRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;


@Service
//@RequiredArgsConstructor
public class VerificationService {

    private final JavaMailSender mailSender;

    private final CodigoVerificacaoRepository codigoVerificacaoRepository;

    public VerificationService(JavaMailSender mailSender, CodigoVerificacaoRepository codigoVerificacaoRepository) {
        this.mailSender = mailSender;
        this.codigoVerificacaoRepository = codigoVerificacaoRepository;
    }

    public void sendVerificationCode(String toEmail) {
        String code = String.format("%06d", new Random().nextInt(999999));

        CodigoVerificacao codigoVerificacao = codigoVerificacaoRepository.findByEmail(toEmail)
                .orElse(new CodigoVerificacao());

        codigoVerificacao.setEmail(toEmail);
        codigoVerificacao.setCodigo(code); // ← CORRIGIDO
        codigoVerificacao.setExpiracao(LocalDateTime.now().plusMinutes(10));

        codigoVerificacaoRepository.save(codigoVerificacao);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Seu Código de Verificação");
        message.setText("Seu código de verificação é: " + code);
        message.setFrom("SEU_EMAIL@gmail.com");

        mailSender.send(message);
        System.out.println("E-mail enviado com sucesso: " + code);
    }


    public boolean verifyCode(String email, String code) {
        Optional<CodigoVerificacao> codigoOpt = codigoVerificacaoRepository.findByEmail(email);
        return codigoOpt.isPresent() &&
                codigoOpt.get().getCodigo().equals(code) &&
                codigoOpt.get().getExpiracao().isAfter(LocalDateTime.now());
    }

}


