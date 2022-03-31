package model;

public class Client {
    private final int clientId;
    private final int arrivalTime;
    private final int serviceTime;
    private int timeSinceService;

    public Client(int clientId, int arrivalTime, int serviceTime){
        this.clientId = clientId;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.timeSinceService = serviceTime;
    }

    public void decrementTimeSinceService(){
        this.timeSinceService--;
    }

    public int getTimeSinceService(){
        return timeSinceService;
    }

    public int getClientId() {
        return clientId;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }
}
