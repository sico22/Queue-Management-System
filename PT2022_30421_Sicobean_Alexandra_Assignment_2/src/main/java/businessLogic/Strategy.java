package businessLogic;

import model.Client;
import model.Queue;

import java.util.ArrayList;

public interface Strategy {

    void addClient(ArrayList<Queue> queues, Client client);
}
