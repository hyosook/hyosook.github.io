# gradnet2 -RDS

gradnet2

gradnet2739



![1583809166902](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1583809166902.png)

![1583809184579](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1583809184579.png)

![1583809202585](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1583809202585.png)

![1583809223300](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1583809223300.png)

![1583809242032](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1583809242032.png)

![1583809265885](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1583809265885.png)

![1583809278918](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1583809278918.png)

![1583809298004](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1583809298004.png)





## 파라미터 그룹 만들기 

1. 파라미터 그룹 생성

   ![1584067431201](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\1584067431201.png)



aws rds modify-db-instance --db-instance-identifier gradnet2 --cloudwatch-logs-export-configuration EnableLogTypes=slowquery --profile gradnet2.0_admin



http://egloos.zum.com/genes1s/v/3093801



로그 처리 

https://hyunki1019.tistory.com/151







## 로그 

https://grip.news/archives/1428

https://grip.news/archives/1435



Performance Insight



https://woowabros.github.io/r-d/2018/12/01/performance_insight.html





https://osc131.tistory.com/107





https://osc131.tistory.com/105?category=736354





## Elastic search

> Elastic에서 제공하는 확장성이 뛰어난 오픈 소스 텍스트 검색 및 분석 엔진입니다. 대량의 데이터를 신속하고 거의 실시간으로 저장, 검색 및 분석 할 수 있습니다. 일반적으로 복잡한 검색 기능과 요구 사항이 있는 응용 프로그램을 구동하는 기본 엔진 / 기술로 사용됩니다.