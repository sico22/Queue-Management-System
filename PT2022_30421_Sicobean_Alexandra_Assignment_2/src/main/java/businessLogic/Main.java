package businessLogic;

import view.MainView;

public class Main {
    public static void main(String[] args) {
        MainView mainView = new MainView("Queue management system");
        mainView.setVisible(true);
        SimulationManager simulationManager = new SimulationManager(mainView);

    }
}
