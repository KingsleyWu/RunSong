package kingsley.www.runsong.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/6/5 19:28
 * file change date : on 2017/6/5 19:28
 * version: 1.0
 */

public class StreamUtil {
    public static String createStr(InputStream is){
        String jsonStr = "";
        BufferedReader reader = null;
        try {
            //包装输入流
            reader =new BufferedReader(new InputStreamReader(is,"utf-8"));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine())!=null){
                //读取输入流的数据
                builder.append(line);
            }
            //转化成字符串
            jsonStr = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonStr;
    }

    public static byte[] createBytes(InputStream is){
        //定义一个字节数组输出流
        ByteArrayOutputStream os = null;
        byte[] datas;
        try {
            //流的目标是内存数组
            os = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024*2];
            int len;
            while ((len = is.read(buffer)) != -1){
                os.write(buffer,0,len);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (os != null) {
                try {
                    is.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //把输出流转换成byte数组
        datas = os.toByteArray();
        return datas;
    }
}
