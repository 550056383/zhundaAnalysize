package com.test;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class DynamicBean {
    private Object object = null; // 动态生成的类

    private BeanMap beanMap = null; // 存放属性名称以及属性的类型

    public DynamicBean() {
        super();
    }

    public DynamicBean(Map propertyMap) {
        this.object = generateBean(propertyMap);
        this.beanMap = BeanMap.create(this.object);
    }

    public static void main(String[] args) throws ClassNotFoundException {

        // 设置类成员属性
        Map propertyMap = new LinkedHashMap();

        String[] strings={"id","name","age","phone","address"};
        for (String s:strings){
            propertyMap.put(s,Class.forName("java.lang.String"));
        }

       /* propertyMap.put("id", Class.forName("java.lang.Integer"));

        propertyMap.put("name", Class.forName("java.lang.String"));

        propertyMap.put("address", Class.forName("java.lang.String"));*/

        // 生成动态 Bean
        DynamicBean bean = new DynamicBean(propertyMap);

        // 给 Bean 设置值
       /* bean.setValue("id", new Integer(123));

        bean.setValue("name", "454");

        bean.setValue("address", "789");*/

        // 从 Bean 中获取值，当然了获得值的类型是 Object

        /*System.out.println("  >> id      = " + bean.getValue("id"));

        System.out.println("  >> name    = " + bean.getValue("name"));

        System.out.println("  >> address = " + bean.getValue("address"));*/

        // 获得bean的实体
        Object object = bean.getObject();
        // 通过反射查看所有方法名
        Class clazz = object.getClass();
        //Method[] methods = clazz.getDeclaredMethods();
      /*  for (int i = 0; i < methods.length; i++) {
            System.out.println(methods[i].getName());
        }*/
        // 获取实体类的所有属性信息，返回Field数组
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            String name;
            name = field.getName();
            System.out.println(name);
            System.out.println(field);
        }
    }

    /**
     * @param propertyMap
     * @return
     */
    private Object generateBean(Map propertyMap) {
        BeanGenerator generator = new BeanGenerator();
        Set keySet = propertyMap.keySet();
        for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
            String key = (String) i.next();
            generator.addProperty(key, (Class) propertyMap.get(key));
        }
        return generator.create();
    }

    /**
     * 给bean属性赋值
     * @param property 属性名
     * @return 值ֵ
     */
    public void setValue(Object property, Object value) {
        beanMap.put(property, value);
    }

    /**
     * 通过属性名得到属性值
     * @param property 属性名
     * @return ֵ
     */
    public Object getValue(String property) {
        return beanMap.get(property);
    }

    /**
     * 返回新生成的对象
     * @return
     */
    public Object getObject() {
        return this.object;
    }
}