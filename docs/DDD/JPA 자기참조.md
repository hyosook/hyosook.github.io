# JPA 자기참조

```JAVA
@Table(name = "POST")
public class Post {  

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name="POST_NO",referencedColumnName = "PARENT_POST_NO")
private Post parentPost;


@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "parentPost")
private List<Post> subPost;

}
```







https://medium.com/@jason.moon.kr/selfjoin-relation-in-jpa-58942284d72



https://siyoon210.tistory.com/82