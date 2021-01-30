package src.util;

import src.models.User;

public class PrivilegeUtil {

    // ADMIN
    public static final int UPDATE_PRODUCT = 1;
    public static final int DELETE_PRODUCT = 1;
    public static final int SEE_ALL_ORDERS = 1;
    public static final int ADD_PRODUCT = 1;

    // COSTUMER
    public static final int CHECK_COSTUMER_PROFILE = 0;
    public static final int SEE_COSTUMER_ORDERS = 0;
    public static final int CAN_ORDER = 0;
    public static final int UPDATE_PROFILE = 0;

    public static boolean checkPrivilege(User user, int privilege){
        return user.getPrivilege() >= privilege;
    }
}
