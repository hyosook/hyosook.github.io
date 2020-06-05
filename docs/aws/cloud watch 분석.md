# cloud watch 분석

##  

## 단순 로그 확인

```bash
fields @timestamp, @message
| filter @message like /(?i)applno/ or  @message like /(?i)applid/ or  @message like /(?i)userid/ or  @message like /(?i)GlobalExceptionHandler/  or  @message like /(?i)Exception/
| sort @timestamp ASC
```



### 전체 카운트 

````bash
fields  @message as ApplSaveStatusBizException

| filter @message like /(?i)ApplSaveStatusBizException/  
| stats count(*) as exceptionCount 
| sort @timestamp ASC
````

```bash
fields  @message as BadCredentialsException

| filter @message like /(?i)BadCredentialsException/  
| stats count(*) as exceptionCount 
| sort @timestamp ASC
```





## 날자별 카운트

```bash
fields  @message as ApplSaveStatusBizException

| filter @message like /(?i)ApplSaveStatusBizException/  
| stats count(*) as exceptionCount by bin(24h)
| sort exceptionCount ASC
```

