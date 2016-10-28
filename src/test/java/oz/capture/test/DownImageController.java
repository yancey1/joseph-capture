package oz.capture.test;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;


public class DownImageController {
  private static String imgUrl="d://image";   //图片地址
  private static String sqlUrl="d://cap_image_res.txt";   //sql生成地址
  private static String localUrl="/usr/local/nginx/html/appstore/img/";  //数据库本地地址
  private static String saveUrl="http://dws.mobiappservice.net:8080/appstore/img/";  //数据库相对地址
  private static SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  public static void main(String[] args) {
    readText("d://image.txt");
    //removeImage();
  }

  public static void readText(String path) {
    try {
      String encoding = "GBK";
      File file = new File(path);
      if (file.isFile() && file.exists()) { // 判断文件是否存在
        InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;
        while ((lineTxt = bufferedReader.readLine()) != null) {
          String imageName = lineTxt.substring(0, lineTxt.indexOf("http://"));
          String imageAdress = lineTxt.substring(lineTxt.indexOf("http://"));
          System.out.println(imageName + "---" + imageAdress);
          getImageByUrl(imageAdress.trim(),imageName.trim(), imgUrl);
        }
        read.close();
      } else {
        System.out.println("找不到指定的文件");
      }
    } catch (Exception e) {
      System.out.println("读取文件内容出错");
      e.printStackTrace();
    }
  }

  public static void getImageByUrl(String urlstr,String imageName, String savepath) {
    int num = urlstr.indexOf('/', 8);
    int extnum = urlstr.lastIndexOf('.');
    String u = urlstr.substring(0, num);
    String ext = urlstr.substring(extnum, urlstr.length());
    try {
      String fileName =imageName + ext;
      // 图片的路径
      URL url = new URL(urlstr);
      URLConnection connection = url.openConnection();
      connection.setDoOutput(true);
      connection.setRequestProperty("referer", u); // 通过这个http头的伪装来反盗链
      BufferedImage image = ImageIO.read(connection.getInputStream());
      FileOutputStream fout = new FileOutputStream(savepath+"//"+fileName);
      ImageIO.write(image, "jpg", fout);
      fout.flush();
      fout.close();
    } catch (Exception e) {
      System.out.print(e.getMessage().toString());
    }
  }
  
  public static void removeImage(){
    String sqlTitle="insert into cap_image_res (img_name,img_local_path,img_url"
        + ",img_width,img_height,create_time,modify_time) values";
    File fold = new File(imgUrl);
    File sqlFile=new File(sqlUrl);
    File []file=fold.listFiles();
    String sql="";
    for (File ff : file) {
     String imgName=ff.getName().substring(0,ff.getName().lastIndexOf("."));
      String relativePath = getUUID() + getFileExtName(ff.getName());
      String imgLocalPath = localUrl + relativePath;
      String imgUu = saveUrl + relativePath;
      int width=getImgWidth(ff);
      int height=getImgHeight(ff);
      Date date=new Date();
      String dd=convert(sim.format(date));
      ff.renameTo(new File(imgUrl+"//"+relativePath));
      sql+=sqlTitle+"("+convert(imgName)+","+convert(imgLocalPath)+","+convert(imgUu)+","+width+","+height+","+dd+","+dd+");\r\n";
    }
    try {
      if(!sqlFile.exists()){
        sqlFile.getParentFile().mkdirs();
      }
      FileWriter fw = new FileWriter(sqlFile, true);
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(sql);
      bw.flush();
      bw.close();
      fw.close();
     } catch (IOException e) {
      e.printStackTrace();
     }
  }
  
  public static String getUUID() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }
  
  public static String getFileExtName(String imageName) {
    String extName = ".jpg";
    if (StringUtils.isNotBlank(imageName)) {
      extName = imageName.substring(imageName.lastIndexOf("."), imageName.length());
    }
    return extName;
  }
  
  public static int getImgWidth(File file){
    BufferedImage buff;
    int width=0;
    try {
      buff = ImageIO.read(file);
      width=  buff.getWidth(); //得到图片的宽度
    } catch (IOException e) {
      e.printStackTrace();
    }
    return width;
  }
  
  public static int getImgHeight(File file){
    BufferedImage buff;
    int height=0;
    try {
      buff = ImageIO.read(file);
      height= buff.getHeight();  //得到图片的高度
    } catch (IOException e) {
      e.printStackTrace();
    }
    return height;
  }
  
  public static String convert(String str){
    return str="\'"+str+"\'";
  }
}
