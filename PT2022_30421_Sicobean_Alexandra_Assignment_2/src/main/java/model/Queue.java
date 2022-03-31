package model;

import java.util.ArrayList;

public class Queue implements Runnable{
    private final ArrayList<Client> clientsQueue;
    private final Thread thread;
    private final int index;
    private boolean isRunning;
    private int currentNrOfClients;
    private int currentServiceTime;
    private int totalServiceTime;
    private int totalNrOfClients;
    private int currentTime;
    private int totalWaitingTime;
    private int nrOfWaitingClients;

    public Queue(int index){
        this.index = index;
        this.isRunning = false;
        this.currentNrOfClients = 0;
        this.currentServiceTime = 0;
        this.clientsQueue = new ArrayList<>();
        this.totalServiceTime = 0;
        this.totalNrOfClients = 0;
        this.currentTime = 0;
        this.totalWaitingTime = 0;
        this.nrOfWaitingClients = 0;

        this.thread = new Thread(this);
        start();
    }

    public ArrayList<Client> getClientsQueue() {
        return clientsQueue;
    }

    public void setCurrentServiceTime(int currentServiceTime) {
        this.currentServiceTime = currentServiceTime;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public int getTotalWaitingTime() {
        return totalWaitingTime;
    }

    public int getNrOfWaitingClients() {
        return nrOfWaitingClients;
    }

    public int getCurrentNrOfClients(){
        return currentNrOfClients;
    }

    public int getIndex(){
        return this.index;
    }

    public int getCurrentServiceTime() {
        return currentServiceTime;
    }

    public int getTotalServiceTime() {
        return totalServiceTime;
    }

    public int getTotalNrOfClients() {
        return totalNrOfClients;
    }

    public void addClient(Client client){
        this.clientsQueue.add(client);
        this.currentNrOfClients++;
        this.currentServiceTime += client.getServiceTime();
        this.totalNrOfClients++;
        this.totalServiceTime += client.getServiceTime();
    }

    public void removeClient(Client client){
        this.clientsQueue.remove(client);
        this.currentNrOfClients--;
    }

    public String queueStatus(){
        StringBuilder status = new StringBuilder();
        if(!clientsQueue.isEmpty()){
            Client firstClient = clientsQueue.get(0);
            for(Client c : clientsQueue){
                status.append("(").append(c.getClientId()).append(", ").append(c.getArrivalTime()).append(", ").append(c.getTimeSinceService()).append("); ");
            }
            firstClient.decrementTimeSinceService();
            this.setCurrentServiceTime(this.getCurrentServiceTime() - 1);
        }
        else
            status = new StringBuilder("closed");
        return status.toString();
    }

    public void run(){
        while(isRunning || clientsQueue.size() > 0){
            if(!clientsQueue.isEmpty()) {
                Client currentClient = clientsQueue.get(0);
                this.totalWaitingTime += this.currentTime - currentClient.getArrivalTime();
                if(this.currentTime - currentClient.getArrivalTime() != 0)
                    this.nrOfWaitingClients++;
                try {
                    Thread.sleep(1000L * currentClient.getServiceTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                removeClient(currentClient);
            }
            else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void start(){
        this.isRunning = true;
        thread.start();
    }

}
