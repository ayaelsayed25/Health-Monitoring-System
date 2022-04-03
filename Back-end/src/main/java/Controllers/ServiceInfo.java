package Controllers;


public class ServiceInfo{
    String service;
    String value;

    public ServiceInfo(java.lang.String service, java.lang.String value) {
        this.service = service;
        this.value = value;
    }

    public java.lang.String getService() {
        return service;
    }

    public void setService(java.lang.String service) {
        this.service = service;
    }

    public java.lang.String getValue() {
        return value;
    }

    public void setValue(java.lang.String value) {
        this.value = value;
    }
}