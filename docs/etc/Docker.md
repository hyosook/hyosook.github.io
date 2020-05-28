# Docker

참고 링크

https://blog.nacyot.com/articles/2014-01-27-easy-deploy-with-docker/



## Docker 

* 가상화 어플리케이션 

* 단순한 가상 머신을 넘어서 어느 플랫폼에서나 재현가능한 어플리케이션 컨테이너를 만들어 주는것을 목표로 한다.

* 격리된 환경을 만들어 주는 도구 

* ##### 컨테이너의 특정 상태를 항상 보존해두고 , 필요할때 언제 어디서나 (도커가 설치된) 실행할수 있도록 도와 주는 도구 

### 설치 하기

> 우분투 환경에서

* 도커 설치 

```bash
curl -s https://get.docker.io/ubuntu/ | sudo sh
```

> `curl`이 없다면 `sudo apt-get curl`로 설치하시면 됩니다

* 설치 확인

``` bash
$ docker -v
```

* 현재 유저를 docker 그룹에 포함

  > 기본적으로 docker의 대부분의 명령어를 실행 시 root권한이 필요 하다
  >
  > sudo를 사용해야하는데, 이를위해서 

```bash
$ sudo groupadd docker
$ sudo gpasswd -a ${USER} docker
$ sudo service docker restart
```

> 재 로그인시 sudo 명령어를 붙이지 않아도, docker 명령어 사용가능
>
> docker group은 root와 같은 권한을 가지게 된다



### 이미지

> 도커에서 실제로 실행되는건 이미지가 아니다
>
> 실행 되느건, 이미지를 기반으로 생성된 컨테이너

* 현재 시스템에서 사용 가능한 이미지 

```bash
$ docker images
```

* 이미지 추가 방법
  * `docker pull 이미지이름`
    * docker.io의 공식 저장소에서 이미지를 다운로드 받아온다
    * npm에서 install대신 pull 명령어 사용한다  생각하면 비슷함
  * 커밋 
  * dockerfile을 통해서 기술된 과정을 거쳐 도커 이미지를 생성



### 컨테이너

* 현재 실행중인 컨테이너 출력

```bash
docker ps
```

* 죽은거 포함 전체 

```bash
docker ps -a
```

* 이미지 되살리기

``` bas
 docker restart 컨테이너id
```

* 컨테이너 속으로 들어가기

```bash
docker attach 컨테이너id
```



* 실제로 실행되는 가상머신은 항상 컨테이너
* 

### 도커 컴포즈

도커 컴포즈를 사용하면 컨테이너 실행에 필요한 옵션을 `docker-compose.yml`이라는 파일에 적어둘 수 있고, 컨테이너 간 실행 순서나 의존성도 관리할 수 있습니다.