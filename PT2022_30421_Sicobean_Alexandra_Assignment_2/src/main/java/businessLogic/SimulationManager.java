package businessLogic;

import model.Client;
import model.Queue;
import view.MainView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class SimulationManager implements Runnable{
    private int numberOfClients;
    private int numberOfQueues;
    private int simulationEndTime;
    private int arrivalClientTimeLowerBound;
    private int arrivalClientTimeUpperBound;
    private int serviceTimeLowerBound;
    private int serviceTimeUpperBound;
    private int peakHourNrOfClients;
    private int peakHour;
    private final MainView mainView;
    private final ArrayList<Client> clientsList;
    private final ArrayList<Queue> queuesList;
    private Thread thread;
    private int time;
    boolean shorterQueue;

    public SimulationManager(MainView mainView){
        this.mainView = mainView;
        this.time = 0;
        this.clientsList = new ArrayList<>();
        this.queuesList = new ArrayList<>();
        this.shorterQueue = true;
        this.peakHour = 0;
        this.peakHourNrOfClients = 0;
        mainView.addStartListener(new StartButtonListener());
        mainView.addShorterQueueListener(new ShorterQueueListener());
        mainView.addShorterTimeListener(new ShorterTimeListener());

    }

    public void startSimulation(){
        for(int i = 1; i <= numberOfQueues; i++){
            Queue newQueue = new Queue(i);
            this.queuesList.add(newQueue);
        }

        this.time = 0;
        this.thread = new Thread(this);
        this.thread.start();

    }

    public void generateNRandomTasks(){
        for(int i = 1; i <= this.numberOfClients; i++){
            int newArrivalTime = (int)Math.floor(Math.random() * (arrivalClientTimeUpperBound - arrivalClientTimeLowerBound + 1) + arrivalClientTimeLowerBound);
            int newServiceTime = (int)Math.floor(Math.random() * (serviceTimeUpperBound - serviceTimeLowerBound + 1) + serviceTimeLowerBound);
            Client newClient = new Client(i, newArrivalTime, newServiceTime);
            clientsList.add(newClient);
        }

        clientsList.sort(Comparator.comparingInt(Client::getArrivalTime));
    }

    public Queue ShortestQueueStrategy(int time){
        int min = 100000;
        int nrOfClients = 1;
        Queue shortestQueue = new Queue(1);
        for(Queue q : queuesList){
            if(q.getCurrentNrOfClients() < min){
                min = q.getCurrentNrOfClients();
                shortestQueue = q;
            }
            nrOfClients += q.getCurrentNrOfClients();
            q.setCurrentTime(time);
        }
        if(nrOfClients > peakHourNrOfClients){
            peakHourNrOfClients = nrOfClients + 1;
            peakHour = time;
        }

        return shortestQueue;
    }

    public Queue ShortestTimeStrategy(int time){
        int min = 100000;
        int nrOfClients = 1;
        Queue shortestQueue = new Queue(1);
        for(Queue q : queuesList){
            if(q.getCurrentServiceTime() < min){
                min = q.getCurrentServiceTime();
                shortestQueue = q;
            }
            q.setCurrentTime(time);
            nrOfClients += q.getCurrentNrOfClients();

        }
        if(nrOfClients > peakHourNrOfClients) {
            peakHourNrOfClients = nrOfClients + 1;
            peakHour = time;
        }
        return shortestQueue;
    }

    public void computeAverageServiceTime(){
        float averageServiceTime = 0;
        int usedQueues = numberOfQueues;
        for(Queue q : queuesList){
            if(q.getTotalNrOfClients() != 0)
                averageServiceTime += (1.0 *q.getTotalServiceTime() / q.getTotalNrOfClients());
            else
                usedQueues--;
        }
        if(usedQueues != 0)
            averageServiceTime = averageServiceTime / usedQueues;
        this.mainView.setAverageServiceTimeTextField(averageServiceTime);
    }

    public void computeAverageWaitingTime(){
        float averageWaitingTime = 0;
        int usedQueues = numberOfQueues;
        for(Queue q : queuesList){
            if(q.getTotalNrOfClients() != 0) {
                if (q.getNrOfWaitingClients() != 0)
                    averageWaitingTime += (1.0 * q.getTotalWaitingTime() / q.getNrOfWaitingClients());
            }
            else
                usedQueues--;
        }
        if(usedQueues != 0)
            averageWaitingTime = averageWaitingTime / usedQueues;
        this.mainView.setAverageWaitingTimeTextField(averageWaitingTime);
    }

    public boolean allQueuesAreEmpty(){
        for(Queue q : queuesList){
            if(!q.getClientsQueue().isEmpty())
                return false;
        }
        return true;
    }

    @Override
    public void run() {

        while(time <= simulationEndTime && (!clientsList.isEmpty() || !allQueuesAreEmpty())){
            ArrayList<Client> clientsToBeRemoved = new ArrayList<>();
            for(Client c : clientsList){
                if(c.getArrivalTime() == time){
                    if(shorterQueue)
                        ShortestQueueStrategy(time).addClient(c);
                    else
                        ShortestTimeStrategy(time).addClient(c);
                    clientsToBeRemoved.add(c);
                }
            }
            clientsList.removeAll(clientsToBeRemoved);
            for(Queue q : queuesList){
                q.setCurrentTime(time);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            printLog(time);
            time++;
        }
        computeAverageServiceTime();
        computeAverageWaitingTime();
        mainView.setPeakHourTextField(peakHour);
    }

    public String waitingClientsStatus(){
        StringBuilder status = new StringBuilder();
        for(Client c : clientsList){
            status.append("(").append(c.getClientId()).append(", ").append(c.getArrivalTime()).append(", ").append(c.getServiceTime()).append("); ");
        }
        return status.toString();
    }

    public void printLog(int time){

        mainView.setSimulationTextPane(mainView.getSimulationTextPane() + "Time " + time + "\n" + "Waiting clients: " + waitingClientsStatus() + "\n");
        for(Queue q : queuesList){
            mainView.setSimulationTextPane(mainView.getSimulationTextPane() + "Queue " + q.getIndex() + ": " + q.queueStatus() + "\n");
        }
        mainView.setSimulationTextPane(mainView.getSimulationTextPane() + "\n");
    }

    public void printTxt(String text){
        File file = new File("log.txt");
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert fw != null;
        PrintWriter pw = new PrintWriter(fw);
        pw.println(text);
        pw.close();
    }

    private class StartButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            numberOfClients = mainView.getNrOfClientsTextField();
            numberOfQueues = mainView.getNrOfQueuesTextField();
            simulationEndTime = mainView.getSimulationEndTimeTextField();
            arrivalClientTimeLowerBound = mainView.getClientLowerBoundTimeTextField();
            arrivalClientTimeUpperBound = mainView.getClientUpperBoundTimeTextField();
            serviceTimeLowerBound = mainView.getServiceLowerBoundTimeTextField();
            serviceTimeUpperBound = mainView.getServiceUpperBoundTimeTextField();
            generateNRandomTasks();
            startSimulation();
            printTxt(mainView.getSimulationTextPane());
        }
    }

    private class ShorterTimeListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            shorterQueue = false;
        }
    }

    private class ShorterQueueListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            shorterQueue = true;
        }
    }
}
