package cn.yanghuisen.nlpchat;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * @Version 1.0
 * @Author
 * @Date 2020/3/8 10:36
 * @Desc 计算Sign
 */
public class ReqSign {
    /*
    将<key, value>请求参数对按key进行字典升序排序，得到有序的参数对列表N
    将列表N中的参数对按URL键值对的格式拼接成字符串，得到字符串T（如：key1=value1&key2=value2），URL键值拼接过程value部分需要URL编码，URL编码算法用大写字母，例如%E8，而不是小写%e8
    将应用密钥以app_key为键名，组成URL键值拼接到字符串T末尾，得到字符串S（如：key1=value1&key2=value2&app_key=密钥)
    对字符串S进行MD5运算，将得到的MD5值所有字符转换成大写，得到接口请求签名
     */
    static String getReqSign(Map<String,String> map,String app_key){
        Set<Map.Entry<String,String>> set =  map.entrySet();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String,String> e:set){
            sb.append(e.getKey()+"="+e.getValue()+"&");
        }
        sb.append("app_key="+app_key);
        System.out.println(sb);
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
}
