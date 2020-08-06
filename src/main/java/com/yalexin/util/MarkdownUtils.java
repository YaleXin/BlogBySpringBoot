package com.yalexin.util;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.*;

/**
 * Author：Yalexin
 * Email： 181303209@yzu.edu.cn
 */
public class MarkdownUtils {
    /**
     * markdown格式转换成HTML格式
     * @param markdown
     * @return
     */
    public static String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    /**
     * 增加扩展[标题锚点，表格生成]
     * Markdown转换成HTML
     * @param markdown
     * @return
     */
    public static String markdownToHtmlExtensions(String markdown) {
        //h标题生成id
        Set<Extension> headingAnchorExtensions = Collections.singleton(HeadingAnchorExtension.create());
        //转换table的HTML
        List<Extension> tableExtension = Arrays.asList(TablesExtension.create());
        Parser parser = Parser.builder()
                .extensions(tableExtension)
                .build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(headingAnchorExtensions)
                .extensions(tableExtension)
                .attributeProviderFactory(new AttributeProviderFactory() {
                    public AttributeProvider create(AttributeProviderContext context) {
                        return new CustomAttributeProvider();
                    }
                })
                .build();
        return renderer.render(document);
    }

    /**
     * 处理标签的属性
     */
    static class CustomAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            //改变a标签的target属性为_blank
            if (node instanceof Link) {
                attributes.put("target", "_blank");
            }
            String name = node.getClass().getName();
//            if (node instanceof TableBlock) {
//                attributes.put("class", "ui celled table");
//            }
            // 代码块
            if (node instanceof FencedCodeBlock) {
                String aClass = attributes.get("class");
                if (aClass != null && !aClass.equals("")) {
                    aClass = aClass + " line-numbers";
                } else {
                    aClass = "line-numbers";
                }
                attributes.put("class", aClass);
            }
            // 行内代码
            if (node instanceof Code && node.getParent() instanceof Paragraph) {
                attributes.put("class", " language-markup");
            }
            if (node instanceof Image) {
                String aClass = attributes.get("class");
                if (aClass != null && !aClass.equals("")) {
                    aClass = aClass + " ui rounded image fancy-box-img";
                } else {
                    aClass = "ui rounded image fancy-box-img";
                }
                attributes.put("class", aClass);
            }
            // 斜体
            if (node instanceof Emphasis){
                String aClass = attributes.get("class");
                if (aClass != null && !aClass.equals("")) {
                    aClass = aClass + " m-em";
                } else {
                    aClass = "m-em";
                }
                attributes.put("class", aClass);
            }
            // 粗体
            if (node instanceof StrongEmphasis){
                String aClass = attributes.get("class");
                if (aClass != null && !aClass.equals("")) {
                    aClass = aClass + " strong";
                } else {
                    aClass = "strong";
                }
                attributes.put("class", aClass);
            }
        }
    }


    public static void main(String[] args) {
//        System.out.println(markdownToHtmlExtensions("![](https://q.yalexin.top/%E7%94%9F%E6%88%90%E5%AF%86%E9%92%A5.png)"));

//        System.out.println("========== \n" + "代码块\n");
//        System.out.println(MarkdownUtils.markdownToHtmlExtensions("```java\n" +
//                "121212121\n" +
//                "```\n"));
//        System.out.println("========== \n" + "行内代码\n");
//        System.out.println(MarkdownUtils.markdownToHtmlExtensions("`hello`"));
        System.out.println(MarkdownUtils.markdownToHtmlExtensions("*斜体*"));
    }
}
