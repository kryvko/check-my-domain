package ua.kh.kryvko.listener;

import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

public class Log4jInit implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String homeDir=servletContextEvent.getServletContext().getRealPath("/");
        File configFile=new File(homeDir, "WEB-INF/classes/log4j.lcf");
        PropertyConfigurator.configure(configFile.toString());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}