package base.java;

import org.junit.Test;

import static java.lang.System.out;

public class EnumTest {

    @Test
    public void testNameMethod(){
        out.println(ScenePromotionResult.NO_SEND_ADD_CART);
        out.println(ScenePromotionResult.NO_SEND_ADD_CART.toString());
        out.println(ScenePromotionResult.NO_SEND_ADD_CART.name());
        out.println(ScenePromotionResult.NO_SEND_ADD_CART.getValue());
        out.println(ScenePromotionResult.NO_SEND_ADD_CART.getName());
    }
}
