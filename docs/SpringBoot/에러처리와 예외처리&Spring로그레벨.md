

## 예외와 에러 
### 에러
- 잡을 필요 없음 > 미리 예측하여 처리할 수 없기 때문에
- 회복 불가능

### 예외
- 잡을 수 있음 > 예측 가능함
- 회복 가능
#### 실행중 생기는 예외
#### 반드시 잡아야 하는 예외

## 예외 클래스
Checked Exception과 Unchecked Exception 차이는 '꼭 처리해야 하는지'

### Checked Exception
- Exception의 상속받은 하위 클래스 중 Runtime Exception 제외한 모든 예외
- IOException
- SQLException

### Unchecked Exception 
- Runtime Exception 하위 예외 > 예측안되기 때문에 로직으로 처리할 필요 없음. 
- NullPointerException
- IllegalArgumentException
- IndexOutOfBoundException
- SystemException


## 로그 
### 로그레벨 
debug < info < warn < error 순으로 높다.
ex) level = info 
> info 보다 level이 낮은 debug는 로그를 남기지 않겠다는 의미

 - FATAL : 아주 심각한 에러
 - ERROR : 어떠한 요청을 처리하는 중 문제가 발생
 - WARN : 프로그램 실행에는 문제가 없지만, 향후 시스템 에러 원인이 될수 있는 경고성
 - INFO : 어떠한 상태변경과 같은 정보성 메시지 
 - DEBUG : 개발시 디버그 용도로 사용하는 메시지 

