package com.kartaca.slcm.admin.Service;

import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("file:./src/main/resources/application.properties")
public class UrlService implements IUrlService{
    @Value("${server.port:-1}")
    private  int port;
    @Value("${server.host:localhost}")
    private  String host;
    @Value("${server.ssl.enabled:false}")
    private  boolean isSSL;
    @Value("${api.endpoint.baseurl}")
    private  String apiBaseUrl;
    @Value("${api.endpoint.path}")
    private  String apiPath;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    public  String getUrl(String path){
        URL url;
        String protocol="http";
        if (isSSL==true){
            protocol="https";
        }
        try{      
            if (port!=-1){
                url = new URL(protocol,host,port,path);
            }
            else{
                url = new URL(protocol,host,path);
            }
            
            return url.toString();
        }
        catch(Exception e){
            logger.error("Error occured while creating url : "+e.toString());
            return "";
        }
    }
    public  String getUrl(){
        URL url;
        String protocol="http";
        if (isSSL==true){
            protocol="https";
        }
        try{
            url = new URL(protocol,host,port,"");
            return url.toString();
        }
        catch(Exception e){
            logger.error("Error occured while creating url : "+e.toString());
            return "";
        }
    }
    public  String getApiUrl(String path){
        try{
            URL baseUrl = new URL(apiBaseUrl);
            System.out.println(baseUrl.toString());
            URL apiPathUrl =new URL(baseUrl,apiPath);
            System.out.println(apiPathUrl.toString());
            URL relative =new URL(apiPathUrl,path);
            System.out.println(relative.toString());
            return relative.toString();
        }
        catch(Exception e){
            logger.error("Error occured while creating url : "+e.toString());
            return "";
        }
    }
}
