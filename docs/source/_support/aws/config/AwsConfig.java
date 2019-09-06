package kr.co.apexsoft.jpaboot._support.aws.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.*;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// TODO AWS SDK for Java 2.0 버전업
@Configuration
public class AwsConfig {

    @Value("${aws.credential.profile}")
    private String awsCredentialProfileName;

    @Value("${aws.ses.region}")
    private String mailRegion;

    @Value("${aws.s3.signingRegion}")
    private String signingRegion;

    @Bean("awsCredentialsProvider")


    public AWSCredentialsProvider awsCredentialsProvider() {
        return new AWSCredentialsProviderChain(
                new EnvironmentVariableCredentialsProvider(),
                new SystemPropertiesCredentialsProvider(),
                new ProfileCredentialsProvider(awsCredentialProfileName),
                InstanceProfileCredentialsProvider.getInstance()
        );
    }

    @Bean
    public AmazonSimpleEmailServiceAsync sesClient() {
        return AmazonSimpleEmailServiceAsyncClient.asyncBuilder()
                .withCredentials(awsCredentialsProvider())
                .withRegion(mailRegion)
                .build();
    }

    @Bean
    public AmazonS3 amazonS3Client() {
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);
        clientConfig.setMaxErrorRetry(10);
        clientConfig.setConnectionTimeout(9 * 1000);
        clientConfig.setSocketTimeout(9 * 1000);
        clientConfig.setMaxConnections(500);

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(awsCredentialsProvider())
                .withClientConfiguration(clientConfig)
                .withRegion(signingRegion)
                .build();
    }

}
