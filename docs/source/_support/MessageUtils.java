package kr.co.apexsoft.jpaboot._support;

import org.springframework.context.support.MessageSourceAccessor;
import java.util.Locale;

/**
 * Class Description
 *
 * @author 김혜연
 * @since 2019-05-16
 */
public class MessageUtils {
    static MessageSourceAccessor messageSourceAccessor;

    public static void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
        MessageUtils.messageSourceAccessor = messageSourceAccessor;
    }

    public static MessageSourceAccessor getMessageSourceAccessor() {
        return messageSourceAccessor;
    }

    public static String getMessage(String key) {
        return messageSourceAccessor.getMessage(key);
    }

    public static String getMessage(String key, Object[] objs) {
        return messageSourceAccessor.getMessage(key, objs);
    }
}
