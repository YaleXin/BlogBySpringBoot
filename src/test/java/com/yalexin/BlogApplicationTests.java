package com.yalexin;

import com.yalexin.util.MarkdownUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogApplicationTests {


//    @Value("${friends}")
//    private Object maps;
    @Value("${blogger_nickname}")
    private String blogger_nickname;

    MarkdownUtils markdownUtils;

    @Test
    public void contextLoads() {
//        System.out.println(maps);
//		System.out.println(blogger_nickname);
    }

    @Test
    public void mdTest(){
        String str= MarkdownUtils.markdownToHtmlExtensions("`hello`");
        System.out.println(str);

        str = MarkdownUtils.markdownToHtmlExtensions("```java\n" +
                "121212121\n" +
                "```\n");
        System.out.println(str);
    }
    @Value("#{'${pictrues.ps}'.split(',')}")
    private List<String>ptrs;

    @Test
    public void listTest(){
        System.out.println(ptrs);
    }
}
