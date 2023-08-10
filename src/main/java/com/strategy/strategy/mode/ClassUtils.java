package com.strategy.strategy.mode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClassUtils {

    public static List<Class<?>> getAllClassByInterface(Class<?> c){
        List<Class<?>> returnClassList = new ArrayList<>();
        //判断是不是接口,不是接口不作处理
        if(c.isInterface()){
            String packageName = c.getPackage().getName();	//获得当前包名
            try {
                List<Class<?>> allClass = getClasses(packageName);//获得当前包以及子包下的所有类

                //判断是否是一个接口
                for (Class<?> aClass : allClass) {
                    if (c.isAssignableFrom(aClass)) {
                        if (!c.equals(aClass)) {
                            returnClassList.add(aClass);
                        }
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return returnClassList;
    }

    /**
     *
     * @Description: 根据包名获得该包以及子包下的所有类不查找jar包中的
     * @param packageName 包名
     * @return List<Class>    包下所有类
     */
    private static List<Class<?>> getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace(".", "/");
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String newPath = resource.getFile().replace("%20", " ");
            dirs.add(new File(newPath));
        }
        ArrayList<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClass(directory, packageName));
        }
        return classes;
    }
    private static  List<Class<?>> findClass(File directory, String packageName)
            throws ClassNotFoundException{
        List<Class<?>> classes = new ArrayList<>();
        if(!directory.exists()){
            return classes;
        }
        File[] files = directory.listFiles();
        for(File file:files){
            if(file.isDirectory()){
                assert !file.getName().contains(".");
                classes.addAll(findClass(file, packageName+"."+file.getName()));
            }else if(file.getName().endsWith(".class")){
                classes.add(Class.forName(packageName+"."+file.getName().substring(0,file.getName().length()-6)));
            }
        }
        return classes;
    }
}
