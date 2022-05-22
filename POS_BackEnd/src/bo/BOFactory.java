package bo;

import bo.custom.impl.CustomerBOImpl;
import bo.custom.impl.ItemBOImpl;
import bo.custom.impl.OrderBOImpl;

/**
 * @author : M-Prageeth
 * @created : 22/05/2022 - 10:11 AM
 **/
public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {

    }

    public static BOFactory getBoFactory() {
        if (boFactory == null) {
            boFactory = new BOFactory();
        }
        return boFactory;
    }

    public SuperBO getBO(BOTypes types) {
        switch (types) {
            case CUSTOMER:
                return new CustomerBOImpl();
            case ITEM:
                return new ItemBOImpl();
            case ORDER:
                return new OrderBOImpl();
            default:
                return null;
        }
    }

    public enum BOTypes {
        CUSTOMER, ITEM, ORDER
    }
}
