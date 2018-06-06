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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;


public class reptile implements PageProcessor {
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

    /**
     *  模拟用户在浏览器操作，测试工具
     */
    public void testSelenium(){
        //由于火狐安装路径不在默认的c盘，所以需要设置路径
        //System.setProperty("webdriver.firefox.bin", "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
        //System.setProperty("webdriver.gecko.driver", "E:\\st\\reptile\\geckodriver-v0.20.1-win32.zip");
        WebDriver webDriver = new FirefoxDriver();
        //最大化
        webDriver.manage().window().maximize();
        //访问百度
        webDriver.get("https://www.baidu.com");
        //根据页面元素 xpath ，右键元素可获取//*[@id="kw"],这是百度的输入框
        WebElement account = webDriver.findElement(By.xpath("//*[@id=\"kw\"]"));
//        actions.sendKeys("selenium java怎么实现方法调用");
        //根据id获取元素 su ，百度一下的按钮
        account = webDriver.findElement(By.id("trainPay"));
        //点击
        account.click();
        //获取页面加载的所有文本内容
        String text = webDriver.findElement(By.tagName("body")).getText();
        //关闭浏览器（这个包括驱动完全退出，会清除内存），close 是只关闭浏览器
        webDriver.quit();


    }

    public static void main(String[] args) {
        reptile rp = new reptile();
        rp.testSelenium();

//        Spider.create(new reptile())
//                //开始抓
//                .addUrl("http://localhost:54336/")
//                //开启1个线程抓取
//                .thread(1)
//                //启动爬虫
//                .run();
    }
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    // 实现webMagic的PageProcessor接口
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    @Override
    public void process(Page page) {
        page.putField("key1",page.getUrl());
        page.putField("name", page.getHtml());//.xpath("//h1[@class='entry-title public']/strong/a/text()").toString()
    }

    @Override
    public Site getSite() {
        return site;
    }


}
