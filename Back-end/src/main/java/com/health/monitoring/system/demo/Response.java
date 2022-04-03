import demo;

public class Response{
    ServiceInfo[] list;

    public Response(ServiceInfo[] list) {
        this.list = list;
    }

    public ServiceInfo[] getList() {
        return list;
    }

    public void setList(ServiceInfo[] list) {
        this.list = list;
    }
}