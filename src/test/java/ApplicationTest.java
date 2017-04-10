import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author clayoverwind
 * @version 2017/4/7
 * @E-mail clayanddev@163.com
 */
public class ApplicationTest {
    @Test
    public void testLeetCode() {
        List<Integer> list = new LinkedList<>(Arrays.asList(5,2,4,6,1,3,5,6,21,4,6,2));
        Collections.sort(list, (a, b) -> a.compareTo(b));
        Collections.sort(list, (a, b) ->{return a.compareTo(b);});
        Collections.sort(list, (Integer a, Integer b) ->{return a.compareTo(b);});
        System.out.println(list);
    }


}
