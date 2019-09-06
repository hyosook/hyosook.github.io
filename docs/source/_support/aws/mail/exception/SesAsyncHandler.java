package kr.co.apexsoft.jpaboot._support.aws.mail.exception;

import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SesAsyncHandler implements AsyncHandler<SendEmailRequest, SendEmailResult> {


    Logger LOGGER = LoggerFactory.getLogger(SesAsyncHandler.class);

    @Override
    public void onError(Exception exception) {
        LOGGER.error("SES send Error:"+ exception.getMessage());
    } //TODO : 에러 발생시 알림 OR DB 처리 논의 필요

    @Override
    public void onSuccess(SendEmailRequest request, SendEmailResult result) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Email sent to: {}", request.getDestination().getToAddresses());
            LOGGER.debug("SES SendEmailResult messageId: [{}]", result.getMessageId());
        }
    }
}