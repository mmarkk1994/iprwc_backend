package src.core;

import javax.ws.rs.core.Response;

public class HttpBody {
    private Response.Status status;
    private String message;
    private Object content;
    private String token;

    public HttpBody(){
        status = Response.Status.OK;
    }

    public void setStatus(Response.Status status) {
        this.status = status;
    }

    public void setMessage(String message) { this.message = message; }

    public void setContent(Object content) {
        this.content = content;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Response build(){
        return Response.status(status)
                .entity(this)
                .build();
    }

    public static Response createResponse(HttpBody httpBody, Response.Status status, String message, Object content){
        if(content != null){
            httpBody.setContent(content);
        }
        httpBody.setStatus(status);
        httpBody.setMessage(message);
        return httpBody.build();
    }
}
