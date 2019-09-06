/**
 *
 */
package kr.co.apexsoft.jpaboot._support.mail;

import kr.co.apexsoft.jpaboot._support.MessageUtils;
import kr.co.apexsoft.jpaboot._support.mail.exception.MailTemplateException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;


class MailTemplateUtil {

    String getMailContent(String templateId, Map<String, String> replacementParams, ResourceLoader resourceLoader) {
        String template = loadTemplate(templateId, resourceLoader);
        return createHtmlMailContent(template, replacementParams);
    }


    /**
     * F
     * templates/email-templates/ 폴더 의 html 템플릿 파일 데이터 추출
     *
     * @param templateId
     * @param resourceLoader
     * @return
     * @throws Exception
     */
    private String loadTemplate(String templateId, ResourceLoader resourceLoader) {
        String content;
        BufferedReader bufferedReader;
        StringBuffer buffer = new StringBuffer();
        InputStream inputStream = null;
        Resource designFile = resourceLoader.getResource("classpath:/templates/email-templates/" + templateId);
        try {
            inputStream = designFile.getInputStream();

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

            while ((content = bufferedReader.readLine()) != null) {
                content = content.trim();
                if (!StringUtils.isEmpty(content)) {
                    buffer.append(content).append("\n");
                }
            }
        } catch (Exception e) {
            throw new MailTemplateException(MessageUtils.getMessage("MAIL_SEND_FAIL"));
        }
        content = buffer.toString();

        return content;
    }

    /**
     * html 템플릿의 {{key}} 해당 value넣기
     *
     * @param replacements
     * @return
     */
    private String createHtmlMailContent(String template, Map<String, String> replacements) {
        String cTemplate = template;
        if (StringUtils.hasText(cTemplate)) {
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                cTemplate = cTemplate.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
        }
        return cTemplate;
    }
}
