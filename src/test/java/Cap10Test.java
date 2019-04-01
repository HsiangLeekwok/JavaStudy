import com.java.enjoy.spring.cap10.aop.Calculator;
import com.java.enjoy.spring.cap10.config.Cap10MainConfig;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Author: Hsiang Leekwok
 * Date: 2019/04/01
 * Description:
 */

public class Cap10Test {

    @Test
    public void test01() {
        AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext(Cap10MainConfig.class);
//        String[] names = app.getBeanDefinitionNames();
//        for (String name : names) {
//            System.out.println(name);
//        }

        Calculator calculator = (Calculator) app.getBean("calculator");
        int result = calculator.div(10, 5);
        System.out.println("div --> result: " + result);
        app.close();
    }
}
