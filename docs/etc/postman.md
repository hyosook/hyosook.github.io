# postman

## 사용하기 

1. `New`  >> `Collection`
2. ![image-20210419143010043](C:\Users\apexsoft\AppData\Roaming\Typora\typora-user-images\image-20210419143010043.png)



```bash
pm.test("token set",  ()=> {
    pm.expect(pm.response.text()).to.include("accessToken")    
    var jsonData =pm.response.json();
    pm.environment.set("token", jsonData.accessToken);
});

```

