# API AWS 구성

![1553676982413](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1553676982413.png)

>`Elastic Beanstalk` 설정을   `java플랫폼`으로 설정한 경우   NginX와 스프링부트의 내장톰캣간 통신포트는 5000번 포트로 통신하도록 설정되어 있다.



### SERVER_PORT 설정

*  기본적으로 Spring Boot 응용 프로그램은 포트 8080에서 수신 대기한다 

  * 따라서 빈스톡 구성 후 한경변수 설정을 통해 spring boot 서버포트를 5000번으로 지정해 줘야 NginX를 통해 톰캣의 서블릿을 호출이 가능하다.

    * Elastic Beanstalk 환경에서 **SERVER_PORT** 환경 변수 를 지정하고 값을 5000으로 설정하는 것

    * `SERVER_PORT = 5000`   

      

    


# `.ebextensions`  사용



## Elastic Beanstalk  설정

> EC2 설정이다 

* ### 위치 

  `.ebextensions`/ 하위 

![1553676595341](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1553676595341.png)

* ### Timezone 설정

linux에서 timezone을 변경하는 방법은 다음과 같다.

```bash
# /etc/localtime 파일을 링크를 통해 Asia/Seoul 파일로 변경
$ sudo rm -rf /etc/localtime
$ sudo ln -f -s /usr/share/zoneinfo/Asia/Seoul /etc/localtime
```



위 내용을 eb config로 적용한 결과는 다음과 같다.

* `.ebextensions`/`timezone.config`

```yaml
commands:
  01_remove_localtime:
    command: sudo rm -rf /etc/localtime
  02_change_clock:
    command: sudo sed -i 's/\"UTC\"/\"Asia\/Seoul\"/g' /etc/sysconfig/clock
  02a_change_clock:
    command: sudo sed -i 's/UTC\=true/UTC\=false/g' /etc/sysconfig/clock
  03_link_singapore_timezone:
    command: sudo ln -f -s /usr/share/zoneinfo/Asia/Seoul /etc/localtime
```



## nginx 설정 

> 웹서버 설정이다 

* ### 위치 :  `.ebextensions`/`nginx`/`conf.d`/ 하위 



![1553673850220](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1553673850220.png)



* ### 적용 구분

  * ![1553674441240](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1553674441240.png)

  * `conf.d`  아래 `.conf `  파일 생성

    *  nginx.conf 전체 속성의 http 단에 들어간다 = 빨간 박스 

  * `elasticbeanstalk `  아래  `.conf `  파일 생성

    *  http 단의 sever 단에 들어간다  = 파란박스

    

* 참고 
  * [nginx config 파일들 기본 설정값](<https://www.nginx.com/resources/wiki/start/topics/examples/full/>)

  * 파일 명명 규칙  : 기본적으로 적용하고 싶은 파일들의 순서는 알파벳 순서로, `00-이름.config` 






* ### context-path

  > 현재 grdanet의 경우  `context-path` 설정이 있다 

  - yml 

    ```yml
    server:
        tomcat.uri-encoding: UTF-8
        context-path: /yonsei
    ```

  

  

* ### `yonsei`  설정 

  #### 1. nginx의 index.html 페이지  설정 

  ![1553845521467](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1553845521467.png)

  

  ![1553845541487](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1553845541487.png)

  * 

  ```bash
  commands:
    01_backup_index_html:
      command: sudo mv /usr/share/nginx/html/index.html /usr/share/nginx/html/index.html.bak
    02_rename_index:
      command: sudo mv /usr/share/nginx/html/new.html /usr/share/nginx/html/index.html
  
  files:
    "/usr/share/nginx/html/new.html":
      mode: "000644"
      owner: root
      group: root
      content: |
        <html>
            <meta http-equiv="refresh" content="0; url=/yonsei" />
        </html>.
  ```

  ### 2. 리버스 프록시 설정

  > Nginx가 현재 실행중인 스프링부트 프로젝트를 바라볼수 있도록 (리버스 프록시)  설정하는것 

  - 위치 : `.ebextensions`/`nginx`/`conf.d`/`elasticbeanstalk`/`00_application.conf`

  ```bash
  location /yonsei {
    proxy_pass          http://127.0.0.1:5000;
    proxy_redirect      / /yonsei;
    proxy_http_version  1.1;
  
    proxy_set_header    Connection          $connection_upgrade;
    proxy_set_header    Upgrade             $http_upgrade;
    proxy_set_header    Host                $host;
    proxy_set_header    X-Real-IP           $remote_addr;
    proxy_set_header    X-Forwarded-For     $proxy_add_x_forwarded_for;
  }
  ```

  - proxy_pass : 요청이 오면 `http://127.0.0.1:5000`로 전달

  - .**proxy_pass**는 **들어온 요청을 어디로 포워딩** 해주는지 지정한다.

  - proxy_set_header XXX : 실제 요청 데이터를 header의 각 항목에 할당

    - ex) `proxy_set_header X-Real-IP $remote_addr`: Request Header의 X-Real-IP에 요청자의 IP를 저장

  -  proxy_redirect    백엔드 서버에 의해 촉발된 리다이렉션에 대해 로케이션 HTTP 헤더에 나타나는 URL을 재작성합니다.

    

* ### 업로드 용량 설정

```
nginx error - 413 Request Entity Too Large
```

> 파일 업로드 시 위와 같은 에러가 발생했다.
>
> 이유는 파일 크기가 nginx 업로드 용량보다 크기 때문이다.
>
> nginx의 업로드 용량은 설정하지 않으면 기본값으로 1MB로 설정된다.
>
> nginx.conf 파일에 아래와 같이 `client_max_body_size` 항목을 설정해주면 된다.



 `nginx/conf.d/proxy.conf` 파일만 작성해주면 된다.

```nginx
client_max_body_size 20M;
```



###  

# 기타



* ## ec2 접속 

  

- putty 를 통해서 ec2에 접속한다 

```bash
# 로그인 id
$ ec2-user
```

- date 확인

```bash
$ date
```

- proxy.conf 설정 확인

```bash
$ cd /etc/nginx/conf.d
$ ls
```

- nginx 재시작

```bash
sudo service nginx restart
```



* ## `.ebextensions` 배포시 경로 문제

`.ebextensions` 폴더는 Elastic beanstalk에 배포 시 환경설정 파일을 위치시키는 폴더이다.

1. [Customizing Server](http://docs.aws.amazon.com/ko_kr/elasticbeanstalk/latest/dg/customize-containers-ec2.html)

> JSON, YAML로 작성된 config 파일은 배포시 실행되어 서버설정을 변경한다.
>
> .ebextensions 폴더가 jar파일의 root에 있지 않아도 잘 실행이 된다.

2. nginx configuration

> nginx의 conf 파일을 위치시키면 nginx 설치 폴더에 생성/복사한다.
>
> 단 ) root에 있어야 nginx conf 파일들이 정상적으로 생성된다.



- 이슈

  - executable jar를 생성하기 위해 `gradle bootJar`를 사용한다.
  - `gradle bootJar`는 jar 내부의 폴더구조를 repackage하고 MANIFEST.MF를 생성하는데 META-INF를 제외한 폴더들은 `BOOT-INF/classes/`로 집어넣는다.
  - `.ebextensions` 폴더도 예외는 아니고 특정 폴더를 exclude 하는 기능은 없어보인다. ([링크](https://docs.spring.io/spring-boot/docs/1.4.2.RELEASE/reference/html/build-tool-plugins-gradle-plugin.html#build-tool-plugins-gradle-repackage-custom-configuration))

- 해결 방법 

  - `bootJar`실행 이후에 .ebextensions를 따로 jar에 삽입한다.

    ```java
    group = 'kr.co.apexsoft'
    version = '0.0.1-SNAPSHOT'
    sourceCompatibility = 1.8
    
    repositories {
        mavenCentral()
    }
    
    task combineJar(type: Exec, dependsOn: bootJar)
    
    bootJar{
        enabled = true
    }
    
    jar {
        baseName = 'social-innovation-town-api'
        exclude(".ebextensions/")
    }
    
    combineJar {
        workingDir "$buildDir"
        if (System.getProperty('os.name').toLowerCase().contains('windows')) {
            commandLine 'cmd', '/c', "jar uf $jar.archivePath -C $processResources.destinationDir .ebextensions"
        } else {
            commandLine 'sh', '-c', "jar uf $jar.archivePath -C $processResources.destinationDir .ebextensions"
        }
        standardOutput = new ByteArrayOutputStream()
    
        doLast() {
            println standardOutput
        }
    }
    ```

    

- 결과

  - `gradlew combineJar` 실행

  - root에 `.ebextensions`가 있다

    ![1550558468105](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1550558468105.png)



* ### 성능 테스트에서 고려 필요

  - **worker_processes**는 CPU 혹은 CPU Core의 총 갯수와 동일하게 맞춘다.
    - `grep processor /proc/cpuinfo | wc -l` CPU 갯수
    - 하지만 보통은 4개 정도가 넘어가면 이미 최대 성능치일 경우가 많다.
  - **worker_connections**는 하나의 `worker_process`가 받을 수 있는 클라이언트 갯수이다.
    - 총 접속 가능 클라이언트 갯수(MaxClients)는 `worker_processes * worker_connections`로 지정된다.
    - Reverse Proxy 상태에서는 `worker_processes * worker_connections / 4` 이 값은 **ulimit -n의 결과값(open files)보다 작아야** 한다. 보통 1024면 충분하다.

