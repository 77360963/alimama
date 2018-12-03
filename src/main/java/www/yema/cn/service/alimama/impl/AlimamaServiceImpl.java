package www.yema.cn.service.alimama.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.request.TbkItemInfoGetRequest;
import com.taobao.api.request.TbkTpwdCreateRequest;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import com.taobao.api.response.TbkItemInfoGetResponse;
import com.taobao.api.response.TbkTpwdCreateResponse;

import www.yema.cn.pojo.conpon.ConponBean;
import www.yema.cn.pojo.conpon.Map_data;
import www.yema.cn.pojo.conpon.Result_list;
import www.yema.cn.pojo.generateShare.Data;
import www.yema.cn.pojo.generateShare.GenerateShare;
import www.yema.cn.pojo.product.N_tbk_item;
import www.yema.cn.pojo.product.ProductBean;
import www.yema.cn.pojo.product.Results;
import www.yema.cn.service.alimama.IAlimamaService;
import www.yema.cn.service.parse.IProductIdParse;
import www.yema.cn.vo.ConponVo;
import www.yema.cn.vo.ProductVo;

@Service
public class AlimamaServiceImpl implements IAlimamaService{
	
	@Resource(name="mmfadProductIdParse")
	private IProductIdParse productIdParse;
	
	public static String url="http://gw.api.taobao.com/router/rest";
	public static String appkey="25245534";
	public static String secret="391965a3c847eec1018cfe941f0d8221";
	

	@Override
	public String getProductIdByOutShareUrl(String outShareContext) {
		String productId= productIdParse.getProductId(outShareContext);
		return productId;
	}

	@Override
	public ProductVo getProductTitle(String productId) {
		ProductVo productVo=null;
		try {
			TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);		
			TbkItemInfoGetRequest req = new TbkItemInfoGetRequest();
			req.setNumIids(productId);
			TbkItemInfoGetResponse response = client.execute(req);
			String body=response.getBody();	
			//System.out.println(body);		
			ProductBean infoDo = JSON.parseObject(body, ProductBean.class);		
			Results results=infoDo.getTbk_item_info_get_response().getResults();
			N_tbk_item item=results.getN_tbk_item().get(0);
			productVo=new ProductVo();
			productVo.setTitle(item.getTitle());
			productVo.setPictUrl(item.getPict_url());
			productVo.setReservePrice(item.getReserve_price());
		} catch (ApiException e) {
			
		}
		return productVo;
	}

	@Override
	public ConponVo getConpon(String productTitle, String productId) {
		ConponVo conponVo=null;
		try {
			TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
			TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
			req.setQ(productTitle);
			req.setHasCoupon(true);
			req.setAdzoneId(60129500330L);
			TbkDgMaterialOptionalResponse response = client.execute(req);
			String body=response.getBody();		
			System.out.println(body);
			ConponBean conponBean=JSON.parseObject(body, ConponBean.class);
			Result_list result_list=conponBean.getTbk_dg_material_optional_response().getResult_list();
			List<Map_data> list=result_list.getMap_data();
			conponVo=new ConponVo();
			for(Map_data data:list) {
				String num_iid=String.valueOf(data.getNum_iid());
				if(productId.equals(num_iid)) {
					conponVo.setCouponShareUrl("https:"+data.getCoupon_share_url());
					conponVo.setCouponId(data.getCoupon_id());
					conponVo.setCouponInfo(data.getCoupon_info());
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conponVo;
	}

	@Override
	public String generateShare(String text,String logo,String productHref) {
		String generateShareHref=null;
		try {
			TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
			TbkTpwdCreateRequest req = new TbkTpwdCreateRequest();
			req.setText(text);
			req.setUrl(productHref);
			req.setLogo(logo);
			TbkTpwdCreateResponse response = client.execute(req);
			String body=response.getBody();		
			//System.out.println(body);
			GenerateShare generateShare=JSON.parseObject(body, GenerateShare.class);
			Data data=generateShare.getTbk_tpwd_create_response().getData();
			generateShareHref = data.getModel();
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return generateShareHref;
	}

}
