package www.yema.cn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taobao.api.ApiException;

import www.yema.cn.manager.AlimamaManager;
import www.yema.cn.utils.UnicodeUtil;

@RestController
public class Taobao {
    
    @Autowired
    private AlimamaManager alimamaManager;
    
    @RequestMapping(value = "/tk/{id}/api",method = RequestMethod.GET)
    public String login(@PathVariable(name = "id") String id, @RequestParam(name = "name") String name){
        System.out.println("id="+id);
        System.out.println("name="+name);
        return "login登录界面000";
    }
    
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public String alimama(String content) throws ApiException{
    	
    	String context=UnicodeUtil.unicodetoString(content);
    	
    	System.out.println("======================");
    	System.out.println(content);
    	
    	String share=alimamaManager.getProductShareUrl(context);
    	
        return "{\"rs\":1,\"tip\":\""+share+"\",\"end\":0}";
    	
    	//return "{\"rs\":1,\"tip\":\"这里是返回的内容，utf-8格式中文，不需要转码[结束][img]http://www.dijiu.com/upload/2009/2/24/2009022479639361.gif[/img][结束][img]http://www.dijiu.com/upload/2009/2/24/2009022479639361.gif[/img]\",\"wxuin\":\"扩展对象\",\"wxuin_tip\":\"扩展回复内容\",\"end\":0}";
    }
    
    
    


}
