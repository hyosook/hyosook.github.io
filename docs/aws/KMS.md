# KMS (Key Management Service)

>   데이터를 암호화 할때 사용되는 암호화 Key 를 안전하게 관리하는데 목적을 둔 서비스



### 관리 서비스 3가지 

*  **AWS managed key**

  * AWS 서비스들이 KMS 를 통해 Key를 서비스 받는 것으로, 내부적으로 자동으로 일어나게 되며 사용자가 직접적으로 제어가 불가능하다

  

* **Customer managed key**
  
*   사용자가 직접 key를 생성하고 관리하는 것으로 해당 포스팅에서 주로 다룰 방식이 바로 CMK 이다. CMK 에 대한 제어는 IAM 을 통해 권한을 부여 받아 제어가 가능하다.
  
* **Custom key stores**

  *   AWS 에서 제공하는 또다른 key 관리형 서비스인 CloudHSM 을 활용한 Key 관리 형태를 의미한다.

    



### Keys 종류 

![img](https://k.kakaocdn.net/dn/WwQMo/btqvi4FeWXC/V2gHdAGkerBijP13ROUti1/img.png)

- **customer Master Key**

  - Region별로 생성 &  AWS에 의해 보관 관리되고 키값이 노출되지 않음 

  - 어플리케이션에서는 암복호화를 위해 KEY ID값을 받급 받아 사용합니다.

  - 데이터를 암호화 하기 위해 사용되는 **Data key**를 생성하는데 사용

    

- **평문 Data key**

  - AWS에 의해 발급 & 데이터를 암호화 하는데 사용
  - 데이터를 암호화 한 이후에는 해당 Plaintext data key 는 더이상 사용할 필요가 없으며, 폐기하도록 한다. (메모리에서 제거)

- **암호화된 Data key**
  평문 Data Key를 암호화한 Key값으로 복호화 시 평문 Key를 얻기 위해 사용





### 암호화

> 암호화된 문자열과, 암호화된 DATA KEY 반환 
>
> 암호화된 DATA KEY는 복호화를 위해서 필요
>
> 암호화 과정은 OpenSSL 을 이용하거나 AWS 에서 제공하는 Encryption SDK 를 사용하면 된다. 데이터를 암호화 한 이후에는 해당 Plaintext data key 는 더이상 사용할 필요가 없으며, 폐기하도록 한다. (메모리에서 제거)
>
>   Plaintext data key 를 폐기하면 이제 남은 것은 Encrypted data key 뿐이다. 해당 key는 데이터를 복호화 할때 사용해야 한다. 따라서 암호화된 Data 와 함께 encrypted data key 또한 함께 안전하게 보관하도록 한다. 
>
>  
>
>   이렇게, key를 이용하여 암호화 시킨 데이터와 함께 key 또한 암호화 하고, 이것을 암호화된 데이터와 함께 동봉하여 보관하는 방식을 **Envelope Encryption** 이라고 부른다. 
>
> 

![img](https://k.kakaocdn.net/dn/pbCCE/btqve65NZfq/ywHvUkAcwZXf636akOW5l1/img.png)

### 복호화

>   **암호화된 데이터를 복호화 하려면 먼저 평문화된(plaintext) data key 가 필요**하다. 
>
> 하지만 우리는 위 3번 단계에서 데이터를 암호화 한 이후 plaintext data key 를 폐기한 상태이며, 남은 것은 **encrypted data key** 뿐이다. 
>
> 즉, encrypted data key를 다시 plaintext data key 로 변환 하는 과정이 필요하다. 
>
> 이때 data key를 복호화 하는 과정에 CMK 가 다시 사용되며, plaintext 로 변환된 data key를 통해 다시 암호화된 데이터를 복호화 하는 과정을 거치면 된다.

![img](https://k.kakaocdn.net/dn/T0Rfj/btqviF7S84j/ywlwPRdauRQ6mhKv6c67g1/img.png)





## 암복호화 API 만들기 

### KMS Master Key 생성



![1578537781549](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1578537781549.png)

![1578538116007](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1578538116007.png)

![1578538282929](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1578538282929.png)

![1578538334020](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1578538334020.png)

![1578538373462](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1578538373462.png)

![1578618785861](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1578618785861.png)



![1578618729562](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1578618729562.png)



### java

```java
@Configuration
public class AwsConfig {
    
 @Value("${aws.kms.arn}")
private String awsKmsArn;
@Bean
    public KmsMasterKeyProvider amazonKmsProvider(){

        return KmsMasterKeyProvider.builder()
                .withCredentials(awsCredentialsProvider()) #프로젝트별 확인필수
                .withKeysForEncryption(awsKmsArn) #프로젝트별 확인필수
               .build();

    }
}
```

```java

@Service
public class KmsCryptoServiceImpl implements AES256Service {

    @Autowired
    private KmsMasterKeyProvider amazonKmsProvider;

    @Value("${aws.kms.arn}")
    private String awsKmsArn;

    public String encrypt(String plainText) {

        AwsCrypto crypto = new AwsCrypto();
        String ciphertext;
        try {
            ciphertext = crypto.encryptString(amazonKmsProvider, new String(plainText.getBytes(StandardCharsets.UTF_8))).getResult();
        } catch (AWSKMSException e) {
            throw new KMSKeyException(e.getMessage(),MessageUtil.getMessage("KMS_KEY_FAIL"));
        } catch (Exception e) {
            throw new KMSEncryptException(MessageUtil.getMessage("KMS_FAIL"));
        }


        return ciphertext;
    }

    public String decrypt(String ciphertext) {

        AwsCrypto crypto = new AwsCrypto();
        CryptoResult<String, KmsMasterKey> decryptResult = null;
        String decryptText;
        try {
            decryptResult = crypto.decryptString(amazonKmsProvider, ciphertext);
            decryptText = new String(decryptResult.getResult().getBytes(StandardCharsets.UTF_8));
        } catch (AWSKMSException e) {
            throw new KMSKeyException(e.getMessage(),MessageUtil.getMessage("KMS_KEY_FAIL"));
        } catch (Exception e) {
            throw new KMSDecryptException(MessageUtil.getMessage("KMS_FAIL"));
        }
        return decryptText;
    }
}

```

```yml
aws:
  credential.profile: gradnet2.0_developer
  kms:
    arn: #생성한 kms의 arn넣기
```

```java
compile group: 'com.amazonaws', name: 'aws-encryption-sdk-java', version: '1.6.1'
```

