package com.itfvck.wechatframework.api.third;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itfvck.wechatframework.api.core.req.model.WexinReqException;
import com.itfvck.wechatframework.api.third.model.ApiAuthorizerToken;
import com.itfvck.wechatframework.api.third.model.ApiAuthorizerTokenRet;
import com.itfvck.wechatframework.api.third.model.ApiComponentToken;
import com.itfvck.wechatframework.api.third.model.ApiGetAuthorizer;
import com.itfvck.wechatframework.api.third.model.ApiGetAuthorizerRet;
import com.itfvck.wechatframework.api.third.model.GetPreAuthCodeParam;
import com.itfvck.wechatframework.api.third.model.ReOpenAccessToken;
import com.itfvck.wechatframework.core.util.http.HttpUtils;

/**
 * 微信--token信息
 * 
 * @author lizr
 * 
 */
public class WxThirdAPI {

	private static Logger logger = LoggerFactory.getLogger(WxThirdAPI.class);
	// 获取预授权码
	private static String api_create_preauthcode_url = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=COMPONENT_ACCESS_TOKEN";
	private static String api_component_token_url = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
	private static String get_access_token_bycode_url = "https://api.weixin.qq.com/sns/oauth2/component/access_token?appid=APPID&code=CODE&grant_type=authorization_code&component_appid=COMPONENT_APPID&component_access_token=COMPONENT_ACCESS_TOKEN";
	private static String api_query_auth_url = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=xxxx";
	// 客服接口地址
	public static String send_message_url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
	// 4、获取（刷新）授权公众号的令牌
	private static String api_authorizer_token_url = "https:// api.weixin.qq.com /cgi-bin/component/api_authorizer_token?component_access_token=COMPONENT_ACCESS_TOKEN";
	// 5、获取授权方的账户信息
	private static String api_get_authorizer_info_url = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=COMPONENT_ACCESS_TOKEN";
	// 6、获取授权方的选项设置信息
	private static String api_get_authorizer_option_url = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_option?component_access_token=COMPONENT_ACCESS_TOKEN";
	// 7、设置授权方的选项信息
	private static String api_set_authorizer_option_url = "https://api.weixin.qq.com/cgi-bin/component/api_set_authorizer_option?component_access_token=COMPONENT_ACCESS_TOKEN";

	/**
	 * 1、获取第三方平台access_token
	 * 
	 * @param appid
	 * @param appscret
	 * @return kY9Y9rfdcr8AEtYZ9gPaRUjIAuJBvXO5ZOnbv2PYFxox__uSUQcqOnaGYN1xc4N1rI7NDCaPm_0ysFYjRVnPwCJHE7v7uF_l1hI6qi6QBsA
	 * @throws WexinReqException
	 */
	public static String getAccessToken(ApiComponentToken apiComponentToken) throws WexinReqException {
		String component_access_token = "";
		String requestUrl = api_component_token_url;
		String json = JSONObject.toJSONString(apiComponentToken);
		JSONObject result = JSONObject.parseObject(HttpUtils.post(requestUrl, json));
		if (result.containsKey("errcode")) {
			logger.error("获取第三方平台access_token！errcode=" + result.getString("errcode") + ",errmsg = " + result.getString("errmsg"));
			throw new WexinReqException("获取第三方平台access_token！errcode=" + result.getString("errcode") + ",errmsg = " + result.getString("errmsg"));
		} else {
			component_access_token = result.getString("component_access_token");
		}
		return component_access_token;
	}

	/**
	 * 2、获取预授权码
	 * 
	 * @param appid
	 * @param appscret
	 * @return kY9Y9rfdcr8AEtYZ9gPaRUjIAuJBvXO5ZOnbv2PYFxox__uSUQcqOnaGYN1xc4N1rI7NDCaPm_0ysFYjRVnPwCJHE7v7uF_l1hI6qi6QBsA
	 * @throws WexinReqException
	 */
	public static String getPreAuthCode(String component_appid, String component_access_token) throws WexinReqException {
		String pre_auth_code = "";
		String requestUrl = api_create_preauthcode_url.replace("COMPONENT_ACCESS_TOKEN", component_access_token);
		GetPreAuthCodeParam getPreAuthCodeParam = new GetPreAuthCodeParam();
		getPreAuthCodeParam.setComponent_appid(component_appid);
		String json = JSONObject.toJSONString(getPreAuthCodeParam);
		JSONObject result = JSONObject.parseObject(HttpUtils.post(requestUrl, json));
		if (result.containsKey("errcode")) {
			logger.error("获取权限令牌信息！errcode=" + result.getString("errcode") + ",errmsg = " + result.getString("errmsg"));
			throw new WexinReqException("获取权限令牌信息！errcode=" + result.getString("errcode") + ",errmsg = " + result.getString("errmsg"));
		} else {
			pre_auth_code = result.getString("pre_auth_code");
		}
		return pre_auth_code;
	}

	/**
	 * 3、使用授权码换取公众号的授权信息
	 * 
	 * @param appid
	 * @param appscret
	 * @return kY9Y9rfdcr8AEtYZ9gPaRUjIAuJBvXO5ZOnbv2PYFxox__uSUQcqOnaGYN1xc4N1rI7NDCaPm_0ysFYjRVnPwCJHE7v7uF_l1hI6qi6QBsA
	 * @throws WexinReqException
	 */
	public static JSONObject getApiQueryAuthInfo(String component_appid, String authorization_code, String component_access_token) throws WexinReqException {
		String requestUrl = api_query_auth_url.replace("xxxx", component_access_token);
		Map<String, String> mp = new HashMap<String, String>();
		mp.put("component_appid", component_appid);
		mp.put("authorization_code", authorization_code);
		System.out.println("-------------------3、使用授权码换取公众号的授权信息---requestUrl------------------------" + requestUrl);
		String json = JSONObject.toJSONString(mp);
		JSONObject result = JSONObject.parseObject(HttpUtils.post(requestUrl, json));
		if (result.containsKey("errcode")) {
			logger.error("获取第三方平台access_token！errcode=" + result.getString("errcode") + ",errmsg = " + result.getString("errmsg"));
			throw new WexinReqException("获取第三方平台access_token！errcode=" + result.getString("errcode") + ",errmsg = " + result.getString("errmsg"));
		}
		return result;
	}

	/**
	 * 4、获取（刷新）授权公众号的令牌
	 * 
	 * @param apiAuthorizerToken
	 * @param component_access_token
	 */
	public static ApiAuthorizerTokenRet apiAuthorizerToken(ApiAuthorizerToken apiAuthorizerToken, String component_access_token) throws WexinReqException {
		String requestUrl = api_authorizer_token_url.replace("COMPONENT_ACCESS_TOKEN", component_access_token);
		String json = JSONObject.toJSONString(apiAuthorizerToken);
		String result = HttpUtils.post(requestUrl, json);
		ApiAuthorizerTokenRet apiAuthorizerTokenRet = JSON.parseObject(result, ApiAuthorizerTokenRet.class);
		return apiAuthorizerTokenRet;
	}

	/**
	 * 5、获取授权方的账户信息
	 */
	public static ApiGetAuthorizerRet apiGetAuthorizerInfo(ApiGetAuthorizer apiGetAuthorizer, String component_access_token) throws WexinReqException {
		String requestUrl = api_get_authorizer_info_url.replace("COMPONENT_ACCESS_TOKEN", component_access_token);
		String json = JSONObject.toJSONString(apiGetAuthorizer);
		String result = HttpUtils.post(requestUrl, json);
		ApiGetAuthorizerRet apiGetAuthorizerRet = JSON.parseObject(result, ApiGetAuthorizerRet.class);
		return apiGetAuthorizerRet;
	}

	/**
	 * 6、获取授权方的选项设置信息
	 */
	public static AuthorizerOptionRet apiGetAuthorizerOption(AuthorizerOption authorizerOption, String component_access_token) throws WexinReqException {
		String requestUrl = api_get_authorizer_option_url.replace("COMPONENT_ACCESS_TOKEN", component_access_token);
		String json = JSONObject.toJSONString(authorizerOption);
		String result = HttpUtils.post(requestUrl, json);
		AuthorizerOptionRet authorizerOptionRet = JSON.parseObject(result, AuthorizerOptionRet.class);
		return authorizerOptionRet;
	}

	/**
	 * 7、设置授权方的选项信息
	 */
	public static AuthorizerSetOptionRet apiSetAuthorizerOption(AuthorizerSetOption authorizerSetOption, String component_access_token) throws WexinReqException {
		String requestUrl = api_set_authorizer_option_url.replace("COMPONENT_ACCESS_TOKEN", component_access_token);
		String json = JSONObject.toJSONString(authorizerSetOption);
		String result = HttpUtils.post(requestUrl, json);
		AuthorizerSetOptionRet authorizerSetOptionRet = JSON.parseObject(result, AuthorizerSetOptionRet.class);
		return authorizerSetOptionRet;
	}
	/**
	 * 微信主动推送给第三方服务器 8、推送component_verify_ticket协议 9、推送取消授权通知
	 */

	/**
	 * 获取第三方平台access_token
	 * 
	 * @param appid
	 * @param appscret
	 * @return kY9Y9rfdcr8AEtYZ9gPaRUjIAuJBvXO5ZOnbv2PYFxox__uSUQcqOnaGYN1xc4N1rI7NDCaPm_0ysFYjRVnPwCJHE7v7uF_l1hI6qi6QBsA
	 * @throws WexinReqException
	 */
	public static ReOpenAccessToken getAccessTokenByCode(String appid, String code, String grant_type, String component_appid, String component_access_token)
			throws WexinReqException {
		String requestUrl = get_access_token_bycode_url.replace("COMPONENT_APPID", component_appid).replace("COMPONENT_ACCESS_TOKEN", component_access_token)
				.replace("authorization_code", grant_type).replace("CODE", code).replace("APPID", appid);
		String result = HttpUtils.get(requestUrl);
		ReOpenAccessToken reOpenAccessToken = JSON.parseObject(result, ReOpenAccessToken.class);
		if (reOpenAccessToken.getErrcode() != 0) {
			logger.error("获取第三方平台access_token！errcode=" + reOpenAccessToken.getErrcode() + ",errmsg = " + reOpenAccessToken.getErrmsg());
			throw new WexinReqException("获取第三方平台access_token！errcode=" + reOpenAccessToken.getErrcode() + ",errmsg = " + reOpenAccessToken.getErrmsg());
		}
		return reOpenAccessToken;
	}

	/**
	 * 发送客服消息
	 * 
	 * @param obj
	 * @param ACCESS_TOKEN
	 * @return
	 */
	public static String sendMessage(Map<String, Object> obj, String ACCESS_TOKEN) {
		// 调用接口获取access_token
		String url = send_message_url.replace("ACCESS_TOKEN", ACCESS_TOKEN);
		String json = JSONObject.toJSONString(obj);
		String result = HttpUtils.post(url, json);
		return result;
	}
	
}