package ch.javaee.basicMvc.service;

import ch.javaee.basicMvc.bean.MailBean;
import ch.javaee.basicMvc.domain.SecurityCode;
import ch.javaee.basicMvc.domain.User;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailSenderService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private VelocityEngine velocityEngine;
    @Autowired
    private MailBean mailBean;

    private static String CONFIRMATION_TEMPLATE = "confirm_account.vm";

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    @Async
    public void sendAuthorizationMail(final User user, final SecurityCode securityCode) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setSubject(mailBean.getSubject());
                message.setTo(user.getEmail());
                message.setFrom(mailBean.getFrom());
                Map model = new HashMap();
                model.put("websiteName", mailBean.getWebsiteName());
                model.put("host", mailBean.getHost());
                model.put("user", user);
                model.put("securityCode", securityCode);
                String text = VelocityEngineUtils.mergeTemplateIntoString(
                        velocityEngine, CONFIRMATION_TEMPLATE, model);
                message.setText(text, true);
            }
        };
        this.mailSender.send(preparator);
    }
}
