package com.rumenz;


import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.Set;


public class DemoApplication  {
    //单一类型注入，同一种类型存在多个，会选择primary="true"注入
    @Autowired
    private ObjectFactory<Rumenz> rumenz;

    //单一类型输入,使用Qualifier选择特定的Bean
    @Autowired
    @Qualifier("rumenz")
    private ObjectProvider<Rumenz> rumenz1;

    //集合类型注入
    @Autowired
    private ObjectProvider<Set<Rumenz>> set1;
    //集合类型注入
    @Autowired
    private ObjectFactory<Set<Rumenz>> set2;

   
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext();
        XmlBeanDefinitionReader xr=new XmlBeanDefinitionReader(ac);
        xr.loadBeanDefinitions("Beans.xml");
        
        ac.register(DemoApplication.class);
        ac.refresh();
        DemoApplication demoApplication = ac.getBean(DemoApplication.class);

        System.out.println(demoApplication.rumenz.getObject().getName());
        System.out.println(demoApplication.rumenz1.getObject().getName());


        Set<Rumenz> lists1 = demoApplication.set1.getIfAvailable();

        System.out.println(Arrays.toString(lists1.toArray()));


        Set<Rumenz> lists2 = demoApplication.set2.getObject();

        System.out.println(Arrays.toString(lists2.toArray()));


        ac.close();
    }

   


}