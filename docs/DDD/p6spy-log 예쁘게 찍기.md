---
title: "p6spy - log 예쁘게 찍기"
tags: ["p6spy"]
---

p6spy를 달면 기본적으로 쿼리는 한줄로만 찍힌다. 
개발하면서 확인하기에는 가독성이 떨어져서 쿼리가 줄바껴서 예쁘게 찍히도록 설정한다.

### spy.properties
```properties
logMessageFormat=kr.co.apexsoft.gradnet2.user_api._config.P6SpyLogger  
  
excludecategories=info,debug,result,batch,resultset  
  
dateformat=yyyy-MM-dd HH:mm:ss  
driverlist=com.mysql.cj.jdbc.Driver  
deregisterdrivers=true
```

### P6SpyLogger
```java 
public class P6SpyLogger implements MessageFormattingStrategy {  
    @Override  
  public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared,  
  String sql, String url) {  
        sql = formatSql(category,sql);  
 return now + "|" + elapsed + "ms|" + category + "|connection " + connectionId + "|" + P6Util.singleLine(prepared) + sql;  
  }  
  
    private String formatSql(String category,String sql) {  
        if(sql ==null || sql.trim().equals("")) return sql;  
  
  // Only format Statement, distinguish DDL And DML  
  if(Category.STATEMENT.getName().equals(category)) {  
            String tmpsql = sql.trim().toLowerCase(Locale.ROOT);  
 if(tmpsql.startsWith("create") || tmpsql.startsWith("alter") || tmpsql.startsWith("comment")) {  
                sql = FormatStyle.DDL.getFormatter().format(sql);  
  }else {  
                sql = FormatStyle.BASIC.getFormatter().format(sql);  
  }  
            sql = "|\nHeFormatSql(P6Spy sql,Hibernate format):"+ sql;  
  }  
  
        return sql;  
  }
}
```

### Console 
```sql
HeFormatSql(P6Spy sql,Hibernate format):
    select
        role0_.ROLE_ID as ROLE_ID1_24_0_,
        role0_.ROLE_TYPE as ROLE_TYP2_24_0_ 
    from
        ROLE role0_ 
    where
        role0_.ROLE_ID=10000
```

<!--stackedit_data:
eyJoaXN0b3J5IjpbLTY4Mjc0NjUwN119
-->