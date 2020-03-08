import cn.yanghuisen.nlpchat.NlpChat;

import java.util.*;

/**
 * @Version 1.0
 * @Author
 * @Date 2020/3/7 21:03
 * @Desc
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        while (true){
            System.out.println("请输入问题：");
            NlpChat chat = new NlpChat("Appkey","AppID","https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat");
            Map<String,String> map = chat.getAnswer(in.nextLine());
            System.out.println(map.get("ret"));
            System.out.println(map.get("msg"));
            System.out.println(map.get("session"));
            System.out.println(map.get("answer"));

        }
    }
}
