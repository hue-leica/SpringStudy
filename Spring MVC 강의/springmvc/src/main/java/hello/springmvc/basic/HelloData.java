package hello.springmvc.basic;

import lombok.Data;

@Data // lombok의 어노테이션
public class HelloData {
    private String username;
    private int age;
}
