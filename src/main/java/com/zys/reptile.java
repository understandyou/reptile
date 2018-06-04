package com.zys;

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

    public static void main(String[] args) {
        reptile rp = new reptile();
        rp.getHtmlResource();
    }
}
