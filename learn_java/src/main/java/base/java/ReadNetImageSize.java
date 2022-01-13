package base.java;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ReadNetImageSize {

    /**
     * 获取远程图片的宽和高
     */
    public Map<String, Integer> obtainRemoteImageWAH(String imageURL) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        BufferedImage bi = null;
        try {
            //读取图片
            URL url = new URL(imageURL);
            bi = ImageIO.read(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        result.put("width", bi.getWidth());
        result.put("height", bi.getHeight());

        return result;
    }

    public static void main(String[] args) {
        ReadNetImageSize oriwh = new ReadNetImageSize();
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
