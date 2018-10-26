package kioskmode.com.epoptia.pojo;

public class NetworkStatus {

    private int status;//-1 network off, 1 network low, 2 network full

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
