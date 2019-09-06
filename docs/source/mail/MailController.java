package kr.co.apexsoft.jpaboot.mail;

import kr.co.apexsoft.jpaboot._support.MessageUtils;
import kr.co.apexsoft.jpaboot._support.mail.MailDto;
import kr.co.apexsoft.jpaboot._support.mail.MailService;
import kr.co.apexsoft.jpaboot._support.mail.exception.MailValidException;
import kr.co.apexsoft.jpaboot.common.ApexAssert;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MailController {

    @NonNull
    private MailService mailService;

    @Autowired
    ResourceLoader resourceLoader;

    /**
     * 비동기 메일 (에러시 서버 로그만 찍힘)
     */
    @GetMapping("/async")
    public void sendAsyncMail() {

        List<String> mailList = Arrays.asList(("hskim@apexsoft.co.kr,hskim@apexsoft.co.kr,hskim@apexsoft.co.kr,hskim@apexsoft.co.kr,hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.t,hskim@t,hskim@apexsoft.2.2,hskim@apexsoft.co.kr,1@1.1").split(","));

        Map<String, String> replacements = new HashMap<String, String>();

        replacements.put("projectName", "프로젝트 이름");
        replacements.put("content", "내용");

        MailDto dto = MailDto.builder()
                .to(mailList)
                .subject("메일 테스트 제목 ")
                .templateId("sample.html")
                .replacements(replacements)
                .resourceLoader(resourceLoader)
                .build();

        mailService.sendAsync(dto);

    }

    /**
     * 동기 메일 전송
     */
    @GetMapping("/one")
    public ResponseEntity<Boolean> sendMail() {

        //메일 주소 체크가 필요한 경우 사용


        List<String> mailList = Arrays.asList(("hskim@apexsoft.co.kr,hskim@apexsoft.co.kr,hskim@apexsoft.co.kr,hskim@apexsoft.co.kr,hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr," +
                "hskim@apexsoft.co.kr,hskim@apexsoft.co.kr,hskim@apexsoft.co.kr,hskim@apexsoft.co.kr,1@1,1@1.1").split(","));


        for (String mailAddr : mailList) {
            ApexAssert.notMailPattern(mailAddr, MessageUtils.getMessage("MAIL_ADDRESS_CHECK_FAIL"), MailValidException.class);
        }
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("projectName", "프로젝트 이름");
        replacements.put("content", "내용");
        MailDto dto = MailDto.builder()
                //.to(Collections.singletonList(mailAddr))
                .to(mailList)
                .subject("메일 테스트 제목 ")
                .templateId("sample.html")
                .replacements(replacements)
                .resourceLoader(resourceLoader)
                .build();
        mailService.send(dto);

        return ResponseEntity.ok(true);
    }
}