package kr.co.apexsoft.jpaboot._support.aws.mail;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.model.*;
import kr.co.apexsoft.jpaboot._support.MessageUtils;
import kr.co.apexsoft.jpaboot._support.aws.mail.exception.SESMailException;
import kr.co.apexsoft.jpaboot._support.aws.mail.exception.SesAsyncHandler;
import kr.co.apexsoft.jpaboot._support.mail.MailDto;
import kr.co.apexsoft.jpaboot._support.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SESMailServiceImpl implements MailService {
    private static final Logger logger = LoggerFactory.getLogger(SESMailServiceImpl.class);

    @Autowired
    private AmazonSimpleEmailServiceAsync sesClient;

    @Value("${aws.ses.sendMail}")
    private String sendMail;

    @Override
    public void sendAsync(MailDto mailDto) {

        sendMail(mailDto, true);
    }

    @Override
    public void send(MailDto mailDto) {
        try {
            sendMail(mailDto, false);
        } catch (Exception e) {
            throw new SESMailException(mailDto.getTo() + MessageUtils.getMessage("MAIL_SEND_FAIL"));
        }

    }

    private void sendMail(MailDto mailDto, boolean isAsync) {
        SendEmailRequest emailRequest = toSendRequestDto(mailDto); //ses전송 공통객체 만들기
        List<String> toList = mailDto.getTo();
        int listSize = toList.size();
        int MAIL_MAX_SIZE = 50; //ses는 한번에 50개씩 메일 전송가능하다
        int firstIndex = 0;
        int lastIndex = 0;
        int max = listSize < MAIL_MAX_SIZE ? listSize : MAIL_MAX_SIZE;
        while (lastIndex < listSize) {
            lastIndex = firstIndex + max;
            List<String> addr = new ArrayList<>(toList.subList(firstIndex, lastIndex));
            Destination destination = new Destination();
            destination.setBccAddresses(addr);// bcc로 보내는 주소 넣기
            emailRequest.withDestination(destination);

            if (isAsync) { //TODO : boolean 이 아니라 , 메소드 자체를 넘겨받는 형태로 변경( 확장고려)
                sesClient.sendEmailAsync(emailRequest, new SesAsyncHandler());
            } else {
                sesClient.sendEmail(emailRequest);
            }
            max = listSize - lastIndex < MAIL_MAX_SIZE ? listSize - lastIndex : MAIL_MAX_SIZE;
            firstIndex = lastIndex;
        }
    }

    private SendEmailRequest toSendRequestDto(MailDto mailDto) {

        return new SendEmailRequest()
                .withSource(sendMail)
                .withMessage(newMessage(mailDto));
    }

    private Message newMessage(MailDto mailDto) {
        return new Message()
                .withSubject(createContent(mailDto.getSubject()))
                .withBody(new Body()
                        .withHtml(createContent(mailDto.getContent()))); // content body는 HTML 형식으로 보내기 때문에 withHtml을 사용합니다.

    }

    private Content createContent(String text) {
        return new Content()
                .withCharset("UTF-8")
                .withData(text);
    }

}
