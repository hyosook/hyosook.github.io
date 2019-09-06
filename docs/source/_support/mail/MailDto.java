package kr.co.apexsoft.jpaboot._support.mail;

import kr.co.apexsoft.jpaboot.common.util.ValidUtil;
import lombok.Builder;
import lombok.Getter;
import org.springframework.core.io.ResourceLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class MailDto {
    private String from;
    private List<String> to = new ArrayList<>(); //받는 주소
    private String subject; // 제목
    private String templateId; //classpath:/templates/email-templates/메일 템플릿 파일명
    private Map<String, String> replacements;
    private String content;

    @Builder
    public MailDto(List<String> to, String subject, String templateId, Map<String, String> replacements, ResourceLoader resourceLoader) {
        this.to = checkMailAddr(to);
        this.subject = subject;
        this.templateId = templateId;
        this.replacements = replacements;

        this.content = new MailTemplateUtil().getMailContent(templateId, replacements, resourceLoader);
    }


    public void addTo(String email) {
        this.to.add(email);
    }

    private List<String> checkMailAddr(List<String> toMailAddr) {
        return toMailAddr.stream()
                .filter(ValidUtil::isMail)
                .collect(Collectors.toList());
    }

}