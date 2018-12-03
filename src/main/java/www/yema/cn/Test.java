package www.yema.cn;

import java.util.HashMap;
import java.util.Map;

import www.yema.cn.utils.HttpClientUtil;

public class Test {
	
	public static void main(String[] args) {
		String httpUrl = "http://quan.mmfad.com/";;                                             
		Map<String, String> maps = new HashMap<String, String>();                                            
		maps.put("b2c", "0");                                             
		maps.put("channel", "1");        
		maps.put("coupon", "1");
		maps.put("p", "1");   
		maps.put("sort", "0");   
		maps.put("k", "￥gvTyblx9az2￥");        
		String body=HttpClientUtil.getInstance().sendHttpPost(httpUrl, maps);     
		System.out.println(body);

	}

}
