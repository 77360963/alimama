package www.yema.cn.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.taobao.api.ApiException;

import www.yema.cn.service.Alipay;
import www.yema.cn.utils.UnicodeUtil;

@RestController
public class Taobao {
	
	
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String login(){
        return "loginµÇÂ¼½çÃæ";
    }
    
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public String alimama(String content) throws ApiException{
    	
    	String context=UnicodeUtil.unicodetoString(content);
    	
    	System.out.println("======================");
    	System.out.println(content);
    	
    	String share=Alipay.getShare(context);
    	
        return "{\"rs\":1,\"tip\":\""+share+"\",\"end\":0}";
    }
    
    
    


}
