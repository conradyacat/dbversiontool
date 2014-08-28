package com.cyacat;

import com.cyacat.engine.runner.SqlServerScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Conrad Yacat on 8/4/2014.
 */
public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException {

//        if (args.length == 0) {
//            LOG.error("Please provide arguments!");
//        }

        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("classpath:application-context.xml");
        PropertiesFactoryBean propFact = appContext.getBean(PropertiesFactoryBean.class);
        Properties props = propFact.getObject();

        //String dbType = props.getProperty("db.type");

        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)appContext.getBeanFactory();
        beanFactory.registerBeanDefinition("ScriptRunner", BeanDefinitionBuilder.rootBeanDefinition(SqlServerScriptRunner.class.getName()).getBeanDefinition());
        appContext.refresh();

        // TODO: what to do here?
        for (int i = 0; i < args.length; i++) {
            String[] parts = args[i].split("=");
        }
    }
}