package com.zys;

import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSpanElement;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


public class reptile {
    /**
     * 此方法实验了，jsopu解析脚本路径并且成功转换为绝对路径，
     * 取js生成的数据，未有思路
     */
    public void getHtmlResource(){
        Document html =  null;
        HttpClient httpClient = new HttpClient();
        HttpMethod httpMethod = new GetMethod("http://localhost:54336");//http://localhost:54336/
        try {
            int cont = httpClient.executeMethod(httpMethod);
            String body = httpMethod.getResponseBodyAsString();
            html = Jsoup.parse(body);
            //构建base URI
            URI uri = new URI("http://localhost:54336");


            Elements elements = html.select("[src]");
            System.out.println(httpMethod.getURI());
            for (Element element: elements) {
                if (element.tagName().equals("script"))
                {
                    String relative = element.attr("src");
                    uri = uri.resolve(relative);//将相对uri根据URI base中的参数转换为绝对url

                    System.out.println(uri.toURL());

                }
            }


//            FileOutputStream fs = new FileOutputStream("d:1.txt");
//            fs.write(body.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
//        String html = "<html><head><title>First parse</title></head>"
//                + "<body><p>Parsed HTML into a doc.</p></body></html>";
//        Document doc = Jsoup.parse(html);
    }

    /**
     * 实验 htmlunit + jsoup
     */
    public void testHtmlUnit()
    {
        //构造一个无界面浏览器
//        WebClient webClient = new WebClient(BrowserVersion.CHROME);//模拟谷歌浏览器
        WebClient webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);//禁用css
        webClient.getOptions().setThrowExceptionOnScriptError(false);//当脚本出现异常不抛出
        //webClient.getOptions().setJavaScriptEnabled(false);
        try {
            //获得页面
            HtmlPage page = webClient.getPage("Address");//地址
            webClient.waitForBackgroundJavaScript(3000);//js后台等待时间
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());//支持AJAX
            HtmlSpan span = (HtmlSpan) page.getElementById("displaySpan");
//            HtmlElement elements = page.getBody();
            String content = page.querySelector("#price9").getTextContent();
            System.out.println(content);


        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(webClient!=null)webClient.close();
        }

    }
    public static void main(String[] args) {
        reptile rp = new reptile();
        rp.testHtmlUnit();
    }
}
