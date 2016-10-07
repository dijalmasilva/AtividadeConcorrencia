package pod.polling.sample;

public class PollingController {
  private final MessageService service;
  
  public PollingController(MessageService service) {
    this.service = service;
  }

  public String[] responses(){
    int count = 0;
    while (count < 10) {
      String[] msgs = service.messages();
      if (msgs != null && msgs.length > 0){
        return msgs;
      }
      try {
        Thread.sleep(3000);
        count++;
      } 
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
  
}
