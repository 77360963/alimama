package www.yema.cn.service.parse.impl;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import www.yema.cn.service.parse.IProductIdParse;
import www.yema.cn.utils.HttpClientUtil;

@Service(value="chaozhiProductIdParse")
public class ChaozhiProductIdParse implements IProductIdParse {

    @Override
    public String getProductId(String outShare) {
        HashMap maps=new HashMap(); 
        String body="{\"password_content\": \""+outShare+"\"}";
        String contentType="application/json";
       String str= HttpClientUtil.getInstance().sendHttpPost("http://gateway.kouss.com/tbpub/tpwdConvert", maps,body,contentType);    
       System.out.println(str); 
       JSONObject jObj = JSONObject.parseObject(str); 
       JSONObject obj=jObj.getJSONObject("data");  
        return obj.get("num_iid").toString();
    }
    
    public static void main(String[] args) {
        ChaozhiProductIdParse a=new ChaozhiProductIdParse();
        a.getProductId("￥MXWeblB3xS0￥");
    }

}
