package pod.polling.sample;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class PollingServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    MessageService service = MessageService.instance();
    PollingController controller = new PollingController(service);
    String[] msgs = controller.responses();
    if (msgs != null){
      resp.setContentType("text/plain");
      resp.getWriter().append(Arrays.toString(msgs));
      resp.getWriter().append("\r\n");
      resp.getWriter().flush();
    }
    else {
      resp.setContentType("text/plain");
      resp.getWriter().append("ERRO");
    }
  }
  
}
