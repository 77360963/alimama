package www.yema.cn.controller;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.taobao.api.ApiException;

import www.yema.cn.manager.AlimamaManager;
import www.yema.cn.utils.RequestStreamUtil;
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
    
    
    /**
        *  调用图灵API(用于个人机器人)
     * @param request
     * @return
     * @throws IOException 
     */
    @RequestMapping(value = "/openapi/api",method = RequestMethod.POST)
    public String api(HttpServletRequest request,HttpServletResponse res) throws IOException{    	
    	String share="";    
    	String requestContex=RequestStreamUtil.toString(request);    	
    	if(StringUtils.isNotBlank(requestContex)) {
    		int firstIndex=requestContex.indexOf("&info=")+6;
        	int lastIndex=requestContex.lastIndexOf("&");
        	String context=requestContex.substring(firstIndex, lastIndex);
        	String decodeContext=UnicodeUtil.unescape(context);
            System.out.println(decodeContext);       
           
            String taokouling=alimamaManager.getTaoKouLing(decodeContext);
            if(StringUtils.isNotBlank(taokouling)) {
            	share=alimamaManager.getProductShareUrl(taokouling);   
            }          
    	}    	
        return "{\"code\":100000,\"text\":\""+share+"\"}";
    }
    
    /**
       * 微信机器人群聊
     * @param content
     * @return
     * @throws ApiException
     */
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public String alimama(String content) throws ApiException{    	
    	String context=UnicodeUtil.unicodetoString(content);
    	String share="";
    	String taokouling=alimamaManager.getTaoKouLing(context);
    	if(StringUtils.isNotBlank(taokouling)) { 
    		share=alimamaManager.getProductShareUrl(context); 
    	}    	    	
        return "{\"rs\":1,\"tip\":\""+share+"\",\"end\":0}";
    	
    }
   
    
    
    
    @RequestMapping(value = "/tk",method = RequestMethod.POST)
    public String alimamatk(String q) throws ApiException{  
        logger.info("进入,q={}",q);        
        String share=alimamaManager.getProductShareUrl(q);        
        return "{\"rs\":1,\"tip\":\""+share+"\",\"end\":0}";        
    }
    
    
   
    
   
    
    
    


}
