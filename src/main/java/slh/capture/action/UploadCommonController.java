package slh.capture.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadCommonController extends HttpServlet {

  @Override  
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)  
          throws ServletException, IOException {  
      // TODO Auto-generated method stub  
//    super.doGet(req, resp);  
    System.out.println("1111111");
  }  
    
  @Override  
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)  
          throws ServletException, IOException {  
      // TODO Auto-generated method stub  
//    super.doPost(req, resp);  
    System.out.println("22222222222");
  }  
}
