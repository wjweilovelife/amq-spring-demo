package com.pactera.controller;

import com.pactera.service.ConsumerService;
import com.pactera.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class HomeController {
	@Autowired
   Destination destination;
   @Autowired
   private ProducerService producerService;
   @Autowired
   private ConsumerService consumerService;

   @RequestMapping(value="/welcome",method= RequestMethod.GET)
   public ModelAndView welcome(){
      System.out.println("------------welcome");
      ModelAndView mv = new ModelAndView();
      mv.setViewName("welcome");
      return mv;
   }
   @RequestMapping(value="/producer",method=RequestMethod.GET)
   public ModelAndView producer(){
      System.out.println("------------go producer");

      Date now = new Date();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String time = dateFormat.format( now );
      System.out.println(time);

      ModelAndView mv = new ModelAndView();
      mv.addObject("time", time);
      mv.setViewName("producer");
      return mv;
   }

   @RequestMapping(value="/onsend",method=RequestMethod.POST)
   public ModelAndView producer(@RequestParam("message") String message) {
      System.out.println("------------send to jms");
      ModelAndView mv = new ModelAndView();
      producerService.sendMessage(destination, message);
      mv.setViewName("welcome");
      return mv;
   }

   @RequestMapping(value="/receive",method=RequestMethod.GET)
   public ModelAndView queue_receive() throws JMSException {
      System.out.println("------------receive message");
      ModelAndView mv = new ModelAndView();

      TextMessage tm = consumerService.receive(destination);
      mv.addObject("textMessage", tm.getText());

      mv.setViewName("receive");
      return mv;
   }

   /*
    * ActiveMQ Manager Test
    */
   @RequestMapping(value="/jms",method=RequestMethod.GET)
   public ModelAndView jmsManager() throws IOException {
      System.out.println("------------jms manager");
      ModelAndView mv = new ModelAndView();
      mv.setViewName("welcome");

      JMXServiceURL url = new JMXServiceURL("");
      JMXConnector connector = JMXConnectorFactory.connect(url);
      connector.connect();
      MBeanServerConnection connection = connector.getMBeanServerConnection();
      return mv;
   }
}
