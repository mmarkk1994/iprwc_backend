package src.service;

import src.WebshopApplication;
import src.models.Order;
import src.models.User;
import src.models.OrderItems;
import src.core.Body;
import src.core.JwtHelper;
import src.db.OrderDAO;
import src.util.MessageUtil;
import src.util.PrivilegeUtil;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

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
        Body body = new Body();

        JwtHelper.renewAuthToken(body, authUser);

        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.SEE_ALL_ORDERS)) {
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        try {
            ArrayList<Order> orders = orderDAO.getOrders();
            return Body.createResponse(body, Response.Status.OK, MessageUtil.ORDERS_FOUND, orders);
        } catch (UnableToExecuteStatementException e) {
            e.printStackTrace();
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.SOMETHING_WENT_WRONG, null);
        }
    }

    public Response getUserOrders(User authUser, int id) {
        Body body = new Body();

        JwtHelper.renewAuthToken(body, authUser);

        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.SEE_USER_ORDERS)) {
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }
        try {
            ArrayList<Order> orders = orderDAO.getUserOrders(id);
            return Body.createResponse(body, Response.Status.OK, MessageUtil.ORDERS_FOUND, orders);
        } catch (UnableToExecuteStatementException e) {
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.SOMETHING_WENT_WRONG, null);
        }
    }

    public Response createOrder(User authUser, List<OrderItems> orderItems) {
        Body body = new Body();

        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.CAN_ORDER)) {
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.ORDER_CREATED_FAILED, null);
        }

        JwtHelper.renewAuthToken(body, authUser);

        boolean created = false;
        try {
            for(OrderItems orderItem: orderItems) {
                created = this.orderDAO.createOrder(orderItem.getProductId(), orderItem.getUserId());
            }
            return created ? Body.createResponse(body, Response.Status.OK, MessageUtil.ORDER_CREATED, null) :
                    Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.ORDER_CREATED_FAILED, null);
        } catch (UnableToExecuteStatementException e) {
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.SOMETHING_WENT_WRONG, null);
        }
    }
}
