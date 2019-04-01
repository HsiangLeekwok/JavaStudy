import com.java.enjoy.spring.cap9.bean.Light;
import com.java.enjoy.spring.cap9.config.Cap9MainConfig;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/04/01
 * Description:
 */

public class Cap9Test {

    @Test
    public void test01() {
        AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext(Cap9MainConfig.class);
//        String[] names = app.getBeanDefinitionNames();
//        for (String name : names) {
//            System.out.println(name);
//        }

        Light light = (Light) app.getBean("light");
        System.out.println(light);
        System.out.println("IOC initialize complete......");

        app.close();
    }
}
