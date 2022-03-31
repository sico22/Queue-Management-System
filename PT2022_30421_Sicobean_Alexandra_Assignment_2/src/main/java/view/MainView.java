package view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class MainView extends JFrame{
    private JTextField nrOfClientsTextField;
    private JTextField nrOfQueuesTextField;
    private JTextField simulationEndTimeTextField;
    private JTextField clientLowerBoundTimeTextField;
    private JTextField clientUpperBoundTimeTextField;
    private JTextField serviceLowerBoundTimeTextField;
    private JTextField serviceUpperBoundTimeTextField;
    private JButton startButton;
    private JScrollPane simulationScrollPane;
    private JPanel mainPanel;
    private JTextField averageWaitingTimeTextField;
    private JTextField averageServiceTimeTextField;
    private JTextField peakHourTextField;
    private JTextArea simulationTextArea;
    private JButton shorterTimeButton;
    private JButton shorterQueueButton;

    public MainView(String title){
        super(title);

        this.simulationTextArea.setText("");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
    }


    public void setSimulationTextPane(String results){
        simulationTextArea.setText(results);
    }

    public void setAverageWaitingTimeTextField(float averageWaitingTime) {
        this.averageWaitingTimeTextField.setText(String.valueOf(averageWaitingTime));
    }

    public void setAverageServiceTimeTextField(float averageServiceTime) {
        this.averageServiceTimeTextField.setText(String.valueOf(averageServiceTime));
    }

    public void setPeakHourTextField(int peakHour) {
        this.peakHourTextField.setText(String.valueOf(peakHour));
    }

    public String getSimulationTextPane(){
        return simulationTextArea.getText();
    }

    public Integer getNrOfQueuesTextField() {
        return Integer.parseInt(nrOfQueuesTextField.getText());
    }

    public Integer getSimulationEndTimeTextField() {
        return Integer.parseInt(simulationEndTimeTextField.getText());
    }

    public Integer getClientLowerBoundTimeTextField() {
        return Integer.parseInt(clientLowerBoundTimeTextField.getText());
    }

    public Integer getClientUpperBoundTimeTextField() {
        return Integer.parseInt(clientUpperBoundTimeTextField.getText());
    }

    public Integer getServiceLowerBoundTimeTextField() {
        return Integer.parseInt(serviceLowerBoundTimeTextField.getText());
    }

    public Integer getNrOfClientsTextField() {
        return Integer.parseInt(nrOfClientsTextField.getText());
    }

    public Integer getServiceUpperBoundTimeTextField() {
        return Integer.parseInt(serviceUpperBoundTimeTextField.getText());
    }

    public void addStartListener(ActionListener listener){
        startButton.addActionListener(listener);
    }

    public void addShorterTimeListener(ActionListener listener){
        shorterTimeButton.addActionListener(listener);
    }

    public void addShorterQueueListener(ActionListener listener){
        shorterQueueButton.addActionListener(listener);
    }
}
