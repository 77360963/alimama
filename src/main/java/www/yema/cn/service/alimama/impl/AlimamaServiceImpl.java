package www.yema.cn.service.alimama.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkCouponGetRequest;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.request.TbkItemInfoGetRequest;
import com.taobao.api.request.TbkTpwdCreateRequest;
import com.taobao.api.response.TbkCouponGetResponse;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import com.taobao.api.response.TbkItemInfoGetResponse;
import com.taobao.api.response.TbkTpwdCreateResponse;

import www.yema.cn.pojo.conpon.ConponBean;
import www.yema.cn.pojo.conpon.Map_data;
import www.yema.cn.pojo.conpon.Result_list;
import www.yema.cn.pojo.conpon.Tbk_dg_material_optional_response;
import www.yema.cn.pojo.conponDetail.ConponDetailBean;
import www.yema.cn.pojo.conponDetail.Tbk_coupon_get_response;
import www.yema.cn.pojo.generateShare.Data;
import www.yema.cn.pojo.generateShare.GenerateShare;
import www.yema.cn.pojo.product.N_tbk_item;
import www.yema.cn.pojo.product.ProductBean;
import www.yema.cn.pojo.product.Results;
import www.yema.cn.request.ConponRequest;
import www.yema.cn.response.ConponDetailResponse;
import www.yema.cn.response.ConponResponse;
import www.yema.cn.response.ProductResponse;
import www.yema.cn.service.alimama.IAlimamaService;
import www.yema.cn.service.parse.IProductIdParse;
import www.yema.cn.utils.HttpClientUtil;

@Service
public class AlimamaServiceImpl implements IAlimamaService{
	
    private static final Logger logger = LoggerFactory.getLogger(AlimamaServiceImpl.class);
    
	@Resource(name="mmfadProductIdParse")
	private IProductIdParse productIdParse;
	
	public static String url="http://gw.api.taobao.com/router/rest";
	public static String appkey="25245534";
	public static String secret="391965a3c847eec1018cfe941f0d8221";
	public static final String ALIMAMA_CACHE_NAME = "alimama";

	@Override
	public String getProductIdByOutShareUrl(String outShareContext) {
	    logger.info("getProductIdByOutShareUrl请求参数={}",outShareContext);
		String productId= productIdParse.getProductId(outShareContext);
		return productId;
	}

	@Override	
	public ProductResponse getProductInfo(String productId) {
	    logger.info("getProductInfo请求参数={}",productId);
		ProductResponse productVo=null;
		try {
			TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);		
			TbkItemInfoGetRequest req = new TbkItemInfoGetRequest();
			req.setNumIids(productId);
			TbkItemInfoGetResponse response = client.execute(req);
			String body=response.getBody();	
			logger.info("商品详情信息={}",body);	
			ProductBean infoDo = JSON.parseObject(body, ProductBean.class);		
			Results results=infoDo.getTbk_item_info_get_response().getResults();
			N_tbk_item item=results.getN_tbk_item().get(0);
			productVo=new ProductResponse();
			productVo.setTitle(item.getTitle());
			productVo.setPictUrl(item.getPict_url());
			productVo.setReservePrice(item.getReserve_price());
			productVo.setZkFinalPrice(item.getZk_final_price());
			productVo.setItemUrl(item.getItem_url());
		} catch (ApiException e) {
			
		}
		return productVo;
	}

	@Override
	public List<ConponResponse> getConpon(ConponRequest conponRequest) {	 
	    logger.info("getConpon请求参数={}", ToStringBuilder.reflectionToString(conponRequest, ToStringStyle.MULTI_LINE_STYLE));
	    List<ConponResponse> conponList=new ArrayList<ConponResponse>();
		try {
			TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
			TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
			req.setQ(conponRequest.getQuery());
			req.setHasCoupon(conponRequest.isHasCoupon());		
			req.setAdzoneId(60129500330L);
			TbkDgMaterialOptionalResponse response = client.execute(req);
			String body=response.getBody();		
			logger.info("商品优惠券信息={}",body);	
			ConponBean conponBean=JSON.parseObject(body, ConponBean.class);	
			Tbk_dg_material_optional_response conponresponse=conponBean.getTbk_dg_material_optional_response();
			if(conponresponse==null){
			  throw new RuntimeException("查询无结果"); 
			}
			Result_list result_list=conponresponse.getResult_list();
			List<Map_data> list=result_list.getMap_data();			
			for(Map_data data:list) {
			    ConponResponse conponVo=new ConponResponse();
			    conponVo.setProductId(String.valueOf(data.getNum_iid()));
			   if (StringUtils.isNotBlank(data.getCoupon_share_url())){
			       conponVo.setCouponShareUrl("https:"+data.getCoupon_share_url());
			    }
			   if (StringUtils.isNotBlank(data.getUrl())){
			       conponVo.setShareUrl("https:"+data.getUrl());
                }				
				conponVo.setCouponId(data.getCoupon_id());
				conponVo.setCouponInfo(data.getCoupon_info());	
				conponVo.setCommissionRate(new BigDecimal(data.getCommission_rate()).divide(new BigDecimal("10000")));
				conponList.add(conponVo);
			}			
		} catch (Exception e) {
		    throw new RuntimeException("查询无结果"); 
		}
		return conponList;
	}
	
	@Override
	public ConponDetailResponse getConponDetail(String productId, String couponId) {
	    logger.info("getConponDetail请求参数,productId={},couponId={}",productId,couponId);
		ConponDetailResponse conponDetailResponse=new ConponDetailResponse();		
		try {
			TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
			TbkCouponGetRequest req = new TbkCouponGetRequest();
			req.setItemId(Long.valueOf(productId));
			req.setActivityId(couponId);
			TbkCouponGetResponse response = client.execute(req);
			String body=response.getBody();	
			logger.info("商品优惠券详情信息={}",body);	
			ConponDetailBean conponDetailBean=JSON.parseObject(body, ConponDetailBean.class);
			Tbk_coupon_get_response tbk_coupon_get_response=conponDetailBean.getTbk_coupon_get_response();
			www.yema.cn.pojo.conponDetail.Data data=tbk_coupon_get_response.getData();
			conponDetailResponse.setCouponAmount(new BigDecimal(data.getCoupon_amount()));
		} catch (Exception e) {
			
		} 

		return conponDetailResponse;
	}
	
	

	@Override
	public String generateShare(String text,String logo,String productHref) {
	    logger.info("generateShare请求参数,productHref={}",productHref);
		String generateShareHref=null;
		try {
			TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
			TbkTpwdCreateRequest req = new TbkTpwdCreateRequest();
			req.setText(text);
			req.setUrl(productHref);
			req.setLogo(logo);
			TbkTpwdCreateResponse response = client.execute(req);
			String body=response.getBody();		
			logger.info("生成自己淘口令信息={}",body);	
			GenerateShare generateShare=JSON.parseObject(body, GenerateShare.class);
			Data data=generateShare.getTbk_tpwd_create_response().getData();
			generateShareHref = data.getModel();
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return generateShareHref;
	}

	
	public  void queryAimamaOrder(String startTime,String session) {
		String httpUrl="http://gateway.kouss.com/tbpub/orderGet";
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("fields", "tb_trade_parent_id,tk_status,tb_trade_id,num_iid,item_title,item_num,price,pay_price,seller_nick,seller_shop_title,commission,commission_rate,unid,create_time,earning_time,tk3rd_pub_id,tk3rd_site_id,tk3rd_adzone_id,relation_id");
		jsonObject.put("start_time", startTime);
		jsonObject.put("span", 1200);
		jsonObject.put("page_size", 100);
		jsonObject.put("tk_status", 1);
		jsonObject.put("order_query_type", "create_time");
		jsonObject.put("session", session);		
		String requestBody=jsonObject.toJSONString();		
		String body=HttpClientUtil.getInstance().sendHttpPost(httpUrl, new HashMap(), requestBody, "application/json");
		System.out.println(body);
	}
	

}
