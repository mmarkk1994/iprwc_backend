package src.util;

import src.models.User;

/**
 * Util class holding information on which action belongs to which privilege
 */
public class PrivilegeUtil {

    // ADMIN
    public static final int UPDATE_PRODUCT = 1;
    public static final int DELETE_PRODUCT = 1;
    public static final int SEE_ALL_ORDERS = 1;
    public static final int ADD_PRODUCT = 1;

    // REGULAR USER
    public static final int CHECK_USER_PROFILE = 0;
    public static final int SEE_USER_ORDERS = 0;
    public static final int CAN_ORDER = 0;
    public static final int UPDATE_PROFILE = 0;

    public static boolean checkPrivilege(User user, int privilege){
        return user.getPrivilege() >= privilege;
    }
}
