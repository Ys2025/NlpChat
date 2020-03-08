package cn.yanghuisen.nlpchat;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Version 1.0
 * @Author
 * @Date 2020/3/8 10:44
 * @Desc
 */
public class NlpChat {
    private String ret;
    private String msg;
    private String session;
    private String answer;
    private Map<String,String> map;
    private String app_key;
    private String app_ID;
    private String surl;

    public NlpChat() {

    }

    public NlpChat(String app_key, String app_ID,String surl) {
        this.app_key = app_key;
        this.app_ID = app_ID;
        this.surl = surl;
    }

    public Map<String, String> getAnswer(String msg){
        map = new TreeMap<>();
        map.put("app_id",this.app_ID);      // 应用ID
        map.put("time_stamp",new Date().getTime()/1000+"");     // 获取当前时间戳(秒级别)
        map.put("nonce_str",RandomStringUtils.randomAlphabetic(16));    // 随机生成16为字符
        map.put("session", RandomStringUtils.randomNumeric(6));     // 随机生成6为数字
        map.put("question",encode(msg));                // 要发送的信息
        map.put("sign",getReqSign(map,this.app_key));       // 计算sign并写入容器中
        URL url = null;
        try {
            url = new URL(surl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(mapToString(map));
            bw.flush();
            bw.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine())!=null){
                sb.append(line);
            }
            br.close();
            return json(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 计算Sign
    private String getReqSign(Map<String,String> map, String app_key){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String,String> e:map.entrySet()){
            sb.append(e.getKey()+"="+e.getValue()+"&");
        }
        sb.append("app_key="+app_key);
        return DigestUtils.md5Hex(sb.toString()).toUpperCase();
    }

    private String encode(String str){
        try {
            str = URLEncoder.encode(str,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str.toUpperCase();
    }

    // 把集合转换为URL请求参数格式
    private String mapToString(Map<String,String> map){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String,String> e:map.entrySet()){
            sb.append(e.getKey()+"="+e.getValue()+"&");
        }
        return sb.deleteCharAt(sb.length()-1).toString();   // 删除最后一个多余&符号
    }

    private Map<String,String> json(String json){
        JSONObject jsonObject = JSONObject.parseObject(json);
        Map<String,String> response =  new HashMap<>();
        response.put("ret",jsonObject.getString("ret"));
        response.put("msg",jsonObject.getString("msg"));
        response.put("session",jsonObject.getJSONObject("data").getString("session"));
        response.put("answer",jsonObject.getJSONObject("data").getString("answer"));
        return response;
    }
}
