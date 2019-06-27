package com.market.ssvip.white.component.interceptor;

import com.licola.llogger.LLogger;
import com.market.ssvip.white.BuildConfig;
import com.market.ssvip.white.utils.CheckUtils;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import okhttp3.HttpUrl;
import okhttp3.HttpUrl.Builder;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.ByteString;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author LiCola
 * @date 2018/10/30
 */
public class StatusInterceptor implements Interceptor {

  private static final String ACCESS_FLAG = "699b9305418757ef9a26e5a32ca9dbfb";
  private static final String SIGN_KEY = "0bca3e8e2baa42218040c5dbf6978f315e104e5c";

  private static final String ACCESS_KEY = "accessKey";
  private static final String REQ_TIME = "reqTime";
  private static final String SIGN = "sign";
  private static final String VERSION = "version";
  private static final String PLATFORM = "platform";
  private static final String DEVICE_ID = "deviceId";
  private static final String APP_KEY = "appKey";
  private static final String APPM_ARKET = "appMarket";

  private static final int SUCCESS_FLAG = 1000000;

  @Override
  public Response intercept(Chain chain) throws IOException {
    LLogger.d("状态处理拦截器");
    Request oldRequest = chain.request();

    HttpUrl oldUrl = oldRequest.url();
    String timestamp = fetchTimestamp();
//    LLogger.d(timestamp);
    //构造公共query
    Map<String, String> baseQuery = makeBaseQuery();

    Set<String> originalQuery = oldUrl.queryParameterNames();
    if (!CheckUtils.isEmpty(originalQuery)) {
      for (String key : originalQuery) {
        String value = oldUrl.queryParameter(key);
        //加入外部注入query
        baseQuery.put(key, value);
      }
    }

    //构造签名
    String signValue = makeSign(baseQuery, timestamp);

    Builder newBuilder = oldUrl.newBuilder();

    for (Entry<String, String> entry : baseQuery.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      newBuilder.setQueryParameter(key, value);
    }

    HttpUrl newUrl = newBuilder
        .build();


    Request newRequest = oldRequest.newBuilder()
        .addHeader(ACCESS_KEY, ACCESS_FLAG)
        .addHeader(REQ_TIME, timestamp)
        .addHeader(SIGN, signValue)
        .url(newUrl)
        .build();

    LLogger.d(newRequest.headers().toString());
    LLogger.d(newUrl.toString());

    Response response = chain.proceed(newRequest);

    ResponseBody responseBody = response.body();
    JSONObject jsonWarp;
    try {
      String bodyStr = responseBody.string();
//      LLogger.d(bodyStr);
      jsonWarp = new JSONObject(bodyStr);
      LLogger.json(jsonWarp);
    } catch (JSONException e) {
      throw new ResponseJsonException("返回网络格式错误", e);
    }

    int code = jsonWarp.optInt("code");
    String msg = jsonWarp.optString("msg");
    if (SUCCESS_FLAG == code) {
      //成功的网络请求
      ResponseBody resultResponseBody;
      String resultData = jsonWarp.optString("data");
      if (resultData == null || resultData.isEmpty() || "null".equals(resultData)) {
        LLogger.d("空缺data的网络返回");
        resultData = "";
      }
      Buffer buffer = new Buffer();
      buffer.write(resultData.getBytes());
      resultResponseBody = ResponseBody
          .create(responseBody.contentType(), responseBody.contentLength(), buffer);
      response = response.newBuilder()
          .body(resultResponseBody)
          .build();
      //成状态码下 取出data（或空缺data） 返回数据结果
      return response;
    } else {
      throw new StateCodeException(msg);
    }
  }

  private String makeSign(Map<String, String> baseQuery, String timestamp) {

    StringBuilder keyInput = new StringBuilder();
    keyInput
        .append(SIGN_KEY)
        .append(timestamp);

    for (Entry<String, String> entry : baseQuery.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      keyInput.append(key).append(value);
    }

    String keys = keyInput.toString();
//    LLogger.d(keys);
    return md5hex(md5hex(keys));
  }

  private Map<String, String> makeBaseQuery() {
    SortedMap<String, String> keyMap = new TreeMap<>();
    keyMap.put(VERSION, BuildConfig.VERSION_NAME);
    keyMap.put(PLATFORM, "Android");
    keyMap.put(DEVICE_ID, "123");
    keyMap.put(APP_KEY,"com.market.ttdk");
    keyMap.put(APPM_ARKET,"_vivo");

    return keyMap;
  }

  private static String fetchTimestamp() {
    return String.valueOf(System.currentTimeMillis() / 1000);
  }

  private static String md5hex(String input) {
    if (CheckUtils.isEmpty(input)) {
      return "";
    }
    return ByteString.encodeUtf8(input).md5().hex();
  }
}
