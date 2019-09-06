package kr.co.apexsoft.jpaboot._support.mail;

public interface MailService {

    void sendAsync(MailDto mailDto);

    void send(MailDto mailDto);

}
