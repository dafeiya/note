import java.net.URL;
import java.net.URLClassLoader;
import java.lang.reflect.Method;

public class LoadClassFromJar {
    public String doJar() {
        String result=null;
        try {
            URL url = new URL("file:/Users/workspace/HelloWorld.jar"); // 这里需要重点看URLClassLoader用法，
            URLClassLoader loader = new URLClassLoader(new URL[] { url }); // URL跟我们日常见到的格式用法不太一样
            Class c = loader.loadClass("com.qunar.helloworld.HelloWorld"); // 在上面找到并加载jar包后，可以通过此
            Object myObject = c.newInstance(); // 函数加载某个类，并使用 newInstance生成一个新的对象
            Method m = c.getDeclaredMethod("sayHello"); // getDeclaredMethod（）方法可以获得指定名字的方法
            m.setAccessible(true); // setAccessible方法使上面得到的方法可用
            result= (String) m.invoke(myObject);// 调用sayHello方法
            System.out.println(result);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return result;
    }
}