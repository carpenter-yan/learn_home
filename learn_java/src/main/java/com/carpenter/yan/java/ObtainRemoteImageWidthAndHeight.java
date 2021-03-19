package com.carpenter.yan.java;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ObtainRemoteImageWidthAndHeight {

    /**
     * 获取远程图片的宽和高
     */
    public Map<String, Integer> obtainRemoteImageWAH(String imageURL) {
        boolean isClose = false;
        Map<String, Integer> result = new HashMap<String, Integer>();
        try {
            //实例化图片URL
            URL url = new URL(imageURL);
            //载入远程图片到输入流
            InputStream bis = new BufferedInputStream(url.openStream());
            //实例化存储字节数组
            byte[] buffer = new byte[500];
            //设置写入路径以及图片名称
            OutputStream bos = new FileOutputStream(new File("D:\\thetempimg.png"));
            int len;
            while ((len = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
            bis.close();
            bos.flush();
            bos.close();
            //关闭输出流
            isClose = true;
        } catch (Exception e) {
            //如果图片没有找到
            isClose = false;
        }
        if (isClose) {
            File file = new File("D:\\thetempimg.png");
            BufferedImage bi = null;
            try {
                //读取图片
                bi = ImageIO.read(file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            result.put("width", bi.getWidth());
            result.put("height", bi.getHeight());
            //删除临时图片
            file.delete();
        }

        return result;
    }

    public static void main(String[] args) {
        ObtainRemoteImageWidthAndHeight oriwh = new ObtainRemoteImageWidthAndHeight();
        String imageURL = "http://www.baidu.com/img/baidu_logo.gif";
        imageURL = "http://storage.360buyimg.com/smart-open/upload/84711323657825043.png?Expires=3741246689&AccessKey=uqmkb988E8U78xO5&Signature=GTJ0PWbPsClxBk2ZqPqMlWsCG64%3D";
        Map<String, Integer> result = oriwh.obtainRemoteImageWAH(imageURL);
        if (result == null) {
            System.err.println("图片没有找到...");
        } else {
            System.out.println(result);
        }
    }
}
