package src.service;

import src.WebshopApplication;
import src.models.Order;
import src.models.User;
import src.models.OrderItems;
import src.core.HttpBody;
import src.core.JwtHelper;
import src.db.OrderDAO;
import src.util.MessageUtil;
import src.util.PrivilegeUtil;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private OrderDAO orderDAO;

    public OrderService() {
        orderDAO = WebshopApplication.jdbiCon.onDemand(OrderDAO.class);
        orderDAO.createTable();
    }

    public Response getAllOrders(User authUser) {
        HttpBody httpBody = new HttpBody();

        JwtHelper.renewToken(httpBody, authUser);

        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.SEE_ALL_ORDERS)) {
            return HttpBody.createResponse(httpBody, Response.Status.BAD_REQUEST, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        ArrayList<Order> orders = orderDAO.getOrders();
        return HttpBody.createResponse(httpBody, Response.Status.OK, MessageUtil.ORDERS_FOUND, orders);
    }

    public Response createOrder(User authUser, List<OrderItems> orderItems) {
        HttpBody httpBody = new HttpBody();

        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.CAN_ORDER)) {
            return HttpBody.createResponse(httpBody, Response.Status.BAD_REQUEST, MessageUtil.ORDER_CREATED_FAILED, null);
        }

        JwtHelper.renewToken(httpBody, authUser);

        boolean created = false;

        for(OrderItems orderItem: orderItems) {
            created = this.orderDAO.createOrder(orderItem.getProductId(), orderItem.getUserId());
        }
        return created ? HttpBody.createResponse(httpBody, Response.Status.OK, MessageUtil.ORDER_CREATED, null) :
                HttpBody.createResponse(httpBody, Response.Status.BAD_REQUEST, MessageUtil.ORDER_CREATED_FAILED, null);
    }

    public Response getOrdersFromUser(User authUser, int id) {
        HttpBody httpBody = new HttpBody();

        JwtHelper.renewToken(httpBody, authUser);

        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.SEE_COSTUMER_ORDERS)) {
            return HttpBody.createResponse(httpBody, Response.Status.BAD_REQUEST, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        ArrayList<Order> orders = orderDAO.getOrdersFromUser(id);
        return HttpBody.createResponse(httpBody, Response.Status.OK, MessageUtil.ORDERS_FOUND, orders);
    }
}
