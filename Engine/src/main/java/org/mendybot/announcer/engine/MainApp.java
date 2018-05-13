package org.mendybot.announcer.engine;

import org.mendybot.announcer.engine.bean.Engine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MainApp
{
  public static void main(String[] args)
  {
    ApplicationContext ctx = SpringApplication.run(MainApp.class, args);

    Engine bean = ctx.getBean(Engine.class);
    bean.init();
    bean.start();
  }
}
