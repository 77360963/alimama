package www.yema.cn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.taobao.api.ApiException;

import www.yema.cn.manager.AlimamaManager;
import www.yema.cn.utils.UnicodeUtil;

@RestController
public class Taobao {
    
    private static final Logger logger = LoggerFactory.getLogger(AlimamaManager.class);
    
    @Autowired
    private AlimamaManager alimamaManager;
    
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String login(){
        System.out.println("======================");
      
        return "启动正常";
    }
    
    @RequestMapping(value = "/tk",method = RequestMethod.POST)
    public String alimamatk(String q) throws ApiException{  
        logger.info("进入,q={}",q);        
        String share=alimamaManager.getProductShareUrl(q);        
        return "{\"rs\":1,\"tip\":\""+share+"\",\"end\":0}";        
    }
    
    
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public String alimama(String content) throws ApiException{
    	
    	String context=UnicodeUtil.unicodetoString(content);
    	
    	System.out.println("======================");
    	System.out.println(content);
    	
    	String share=alimamaManager.getProductShareUrl(context);    	
        return "{\"rs\":1,\"tip\":\""+share+"\",\"end\":0}";
    	
    }
    
   
    
    
    


}
