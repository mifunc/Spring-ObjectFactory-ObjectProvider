## Spring延迟依赖注入ObjectFactory/ObjectProvider

> ObjectProvider 继承自 ObjectFactory


![](https://imgkr.cn-bj.ufileos.com/337c37a2-1e7f-46b9-8c1c-2018e64b1e88.png)

- ObjectFactory延迟注入 (单一类型注入/集合类型注入)
- ObjectProvider延迟注入 (单一类型注入/集合类型注入) 推荐

**实体类Rumenz/SuperRumenz**

```java
package com.rumenz;

public class Rumenz{

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Rumenz{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}


package com.rumenz;

public class SuperRumenz extends Rumenz {
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SuperRumenz{" +
                "type='" + type + '\'' +
                "} " + super.toString();
    }
}

```

**配置文件Beans.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:util="http://www.springframework.org/schema/util"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/util https://www.springframework.org/schema/util/spring-util.xsd">
    <bean id="rumenz" class="com.rumenz.Rumenz">
        <property name="id" value="123"/>
        <property name="name" value="入门小站"/>
    </bean>
    <bean id="superRumenz" class="com.rumenz.SuperRumenz" parent="rumenz" primary="true">
        <property name="id" value="456"/>
        <property name="name" value="入门小站-子类"/>
        <property name="type" value="1"/>
    </bean>
</beans>
```


**调用**

```
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
```

**输出**

```
入门小站-子类
入门小站
[Rumenz{id=123, name='入门小站'}, SuperRumenz{type='1'} Rumenz{id=456, name='入门小站-子类'}]
[Rumenz{id=123, name='入门小站'}, SuperRumenz{type='1'} Rumenz{id=456, name='入门小站-子类'}]
```

源码:https://github.com/mifunc/Spring-ObjectFactory-ObjectProvider


