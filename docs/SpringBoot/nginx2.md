## nginx

* 동시에 접근했을때, 천명 

* 내가 계속 유지 시켜주는거, 스케일 아웃의 개념으로 

* aws를 구성하는게 좋다

  * 프로세스1개
  * 커넥션 1024
  * 예약 시스템 

* worker_connection

  * 보통 1024 이지만, 2000까지 가능하다

    * but 이게 올라가면, 메모리도 올려야한다 
    * 1024면 1G
    * 한개당 1MB를 

  * Keppalive_timeout 

    * 연결되자 않는 상태에서, 아무것도 안한 동안이 얼마나 유지되면 끊기는지

    * 국내는 5초 만 해도 상관이 없다.

      * 네트워크 상태가 안 좋으면, 길게하는게 좋다
        * 커넥션 맺는 과정이 어느정도 네트웍의 영향이 있을수 있다.

    * keepalive_off

      * 다운받으면, 일방적으로 끊어버린다.

      * 계속 웬만하면, 부하로 생각된다

      * idle time과 같다 

      * ssl일때는 부담된다 

        * http가 https에 비해서 속도가 좋다,

        * http는암복화가 없으니까.

        * https로 갔을때는, 커넥션이 좀 유지가 되는게 좋다 

        * https는 매번인증을 해야한다, 암복호화

          

* 프로세스 하나의 움직이는 주체 , core와 같게 놓는게 좋다 

* pc의 cpu는 순번으로 간다 ,

* 코어수가 여러개면 병렬로 간다 .

* 경합이 일어나지 않게 하려면, 프로세스 수를 줄여주어야한다. 

* 웹서버의 개념은 , 커넥션으로 간다 

* 총 프로세스당 몇개의 커넥션 

* 대용량 파일 처리 

  * 어디서 처리하느냐 ?
    * 웹 
    * 보통 톰캣 , WAS 에서 처리한다 
    * URL 풀경로 > 웹 다운로드 . 웹
    * WAS 다운로드 경로 , 면은 WAS 

* 파일 다운로드 

  * 2GB 이상은 다운로드 받지 못하게 하는것도 있다

* proxy buffer 

  * 포크레인 , 
  * 이 사이즈가 되었을때, 처리하겠다 
  * 한번에 가져와서, 하는 양 이다 
  * 일일히 작은 사이즈 하는거랑
  * 큰 덩어리로 한번에읽는거 >> 이게 더 좋지만, 메모리를 사용한다 , 성능을 높히는 방법은 좀더 빠르게 다운로드 가능하다

* express 1M 설정 

  * 쿠기 같은거 제약 확인 

* 캐싱의 의미는 , 클라이언트에서 가지고있겠다 .

  ​	확인해보기

* pc는 멀티 쓰레딩 

* 안드로이드/ ios 는 멀티쓰레딩이 된지 얼마 안됬다.

  * PC와 안드로드이드 같이 가면 안된다

* 엑세스 로그 

  * 날짜 단위로 받을수 있게 설정하기 

* nginx 에서 정확히 몇명의 커넥션이 붙었나 확인 가능 ?

  ```bash
  netstat -na |grep 'EST'|wc -l
  이게 keepalive 유지 되고 있는거다 .
  붙어있는 갯수를 확인 할수 있다
  https://brunch.co.kr/@springboot/98
  
  
  
  ```

  





# tomcat

* 한서버에 인스턴스 두개일때 , 당연히 포트가 충돌 되면 안된다
* AJP는 뒷쪽이 노출이 되지않는다. 보안상으로 
  * 효율성 측면으로도 NGINX 역활이 더 크다 
* 프록시는 바이패스 넘겨주기 
* AJP는 톰캣과 웹을 연결고리 선을 만들어놓는다. 
* 실제 일하는 포트는 , 서비스의 connector
  * http  다이렉트로 http 통신을 받는다 . http 통신으로 부터 날려온 8080
    * 프록시 패스로 설정하면 여기서 받는다 
  * AJP : 커넥터  서로 연동시켜주는 연동 모듈 , 연동 모듈 에서 쓰는 포트 
  * WEB에서 어느정도 통제가 가능하다. 연결에 대한 통제권을 가지고있기 때문에 

* 커넥터 
  * 커넥션을 붙여주는 설정에 대한 내용
  * http 만 쓰는데, AJP 설정 이런거 하면, 불필요한 쓰레드를 설정하게 된다 
    * 주석으로 닫아도 된다.
  * 커넥션 타임아웃
    * 커넥션 해주고, 일이없을때 끊어주는거 
* 메모리 설정으 
  * jvm에서 한다



* 쓰레드

  * maxThreads = 200

    * 100-150개 정도로 설정하는게 좋다
    * 이 시스템이 어느정도 max 갔을때 , 버틸수있나.
    * 그럼 죽지는 않는다. 
    * 시스템을 죽이지 않는 개념으로 

  * minSpaire

    *  최소 몇개 설정할것인가 

    * min-spare-threads =10 

      * 구지 낮게 갈 일이 없다. 
      * 준비과정을 가질 필요가 없다. 
      * 애는 아낄 이유가 없다.
      * 그러면 몰렸을때, 튕기는 현상이 있다. 
      * 너무 낮으면 안된다 .오버헤드가 안걸리게 해야한다.

      

      순간 리퀘스를 봐야한다, web말고 was 로

      web에서 was 넘어가는거, 를 봐야한다 

      

      0.5초에 처리되는게, 

      

      * 쓰레드 하나당 cpu를 엄청 먹는다 
        * 그러면 몇개이상 돌면 안되니까, 
        * 안정적인 개념을 둬야한다 

  * **cpu의 임계는 60%이상이 간다 하면 위험한 상황으로 본다** 

  * cpu가 일을 하고 있다. 
    * 일하는 놈이 동작 할때,
    * 어플리케이션 로직이 있다. 연산 비즈니스 , 오브젝트 객체 메모리에 세팅하고, for문 
      * 이 행위를 할때가 cpu를 먹는다 
  * 남에게 던지고, 통신을 할때
    * 놀고 있는거 , cpu는 쉬고있다 
    * cpu는 계산하는거
  * 잘 짠 프로그램은, 자원 활용을 잘하는것이다.
    
    * 

* gc로그 설정

  * jvm 설정에서

  * -verbosegc

    * -xx: +pringgtGCDetils

    * -xx: +pringgtGCTime

    * heap 정보 전후도 

    * 그리고 꼭 파일로 남겨라

    * outofMemoryError 했을때, 남겨라 파일로 

    * 전체 OS에서 메모리 쓰는게 4G인데, 우리가

    * WEB-INF에서 합찻ㄴ 50

    * maxMetaSpaceSize와 , min 도 설정을 해야한다 

    * 전체 사이즈의 1/3을 준다 

    * 2gb면 전체 사이즈의 1/3인 것을 준다 

    * old 영역으로 넘어가는것은, 64k 이하는 new에서 처리한다 

    * 64k 이상은 old로 넘어간다 사이즈가 크다면

      

    * 로그를 남기지 않았는데, gc로그를보고  싶을때

    * jstat   

      * heap os가아니라 이안에꺼 볼때

      java bin에 들어가면된다

      jstat -gc -t -h 30 pid 3000

      3초마다 찍는다 

      oc  => old 영역 이게 차고있는지 , 

      ou  => 실제로 old에 사용하고 있는거  ===> 이걸 봐야한다.

      이걸 보면, 아 fullgc가 있다 일어나겠구나.

      얼마나 걸리나 차기 까지 .이걸 봐야한다 

      FGC  => fullgc의 카운드, 몇번이 발생했나

      FGC-T  =>  토탈 시간

       FGC-T / FGC 해서, 1에 몇 시간이 걸리나를 봐야한다.

      만약에 1초 대면 문제가 없다.

      근데만약에 2-4초면 문제가 있다.

      그 프로그램을 이용하면 우리가남긴 로그를 , 자동으로 분석해 준다 .

    * jstack 

      * 얘는 자꾸 떨어트려도, was에 부담을 주지 않는다 

      `>` 는 엎어치기

      `>>`는 계속 

      * ````bash
        jstack -l <pid> >> jstack_0304.log
        ````

      * 스택이 떨어진다 

    * 쓰레드 생명주기 . java >> 어떤 상태 종류 

      * running 일때, cpu 먹는거

      * blocked, 순번대기 ,일은 하기 위한 / 잘못짜면 blocked이 일어나서, 이슈가된다

        멀티쓰레드 붙어도, 순번을 잘짜면, blocked 상태가 적다

      * waiting, 떤지고 기다리기 ,db 

  

  

  메모리는, 힙 덤프

  리눅스에서

  was에 무리를준다. 

  멈추고 찍어야한다, 메모리를 다 읽어야 하기때문에, 멈추는 시간이 길다 

  무엇을 담아두고 있었느냐 

  힙이 대체 뭘 담아 두고 있길래, 메모리가 이렇게 차냐 ?

  그럴때, 이걸 열어보는 것을 써야하고,

  거기서는, 우리가 확인할수있는 코드, 솔루션...등이 실제로 보인다

  이걸보고 , 이 부분이 문제구나를 확인 할 수 있다.

  * static 파일을잘못 선언하면, 누적하고 가지고있다. 이런 문제가있음. 고정형으로만 쓴다

  * 상속의 관계를 잘못 만들었을 경우 . 종결의 기회를 주지 않아서, 붙들고 있게 된다.

    메모리 클리어의 기회를 주지않는다 

  여러군데서 잘못 쓰기 떄문에, 이슈가 된다.

  * 엑셀 100만건 내리기 이런거 

  한정된 메모리를 쓰기때문에, static 이 너무 크게있으면 안된다 .

  




1. jstat를 먼저 보고 

   여기서 메모리 문제가 없으면

2. jstack 쓰레드 보고

3. 힙 덤프 

   이거는, 최후의 보루



## 툴

쓰레드를 모는것

힘 덤프

apm 툴 , was 를 모니터링 하는 툴이 있다.

이걸로 이용해서 하면 된다.

스카우터 

3가지 

1. heap

   ibm heap / 이클립스

   자바만 설치 되어있으면 된다

2. gc

   ibm /  gsview

3. thread 

   ibm

    

   apm 툴

   <https://github.com/scouter-project/scouter/blob/master/scouter.document/main/Quick-Start.md>





### 아파치

결과적으로 맥스 

멀티 프로세스 모델

포크 = 1프로세스당 1개씩의 쓰레드의 방식이다 , 프로세스의 개수가 많다. 프로세스를  포크 가진다 

max 클라이언트 

워커

쓰레드 방식으로 간다 npm 워ㅓ

이벤트

방식이 3가지 이다 

설정법이 조금 다를뿐이지 

톰캣은 한프로세드당, 쓰레드가 작다

max로 봐야한다 

limt 