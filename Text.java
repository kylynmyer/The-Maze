import java.awt.*;
public class Text {
    private String text;
    private int totalSteps;
    private int currentStep;
    private static int stepCount = 0;
    private static int totalTutorialSteps = 2;
    private String taskDescription;
    private String objective;

    public void setText(String text) {
        this.text = text;
    }
    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }
    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }
    public String getText() {
        return text;
    }
    public int getTotalSteps() {
        return totalSteps;
    }
    public int getCurrentStep() {
        return currentStep;
    }
    public String getTaskDescription() {
        return taskDescription;
    }
    public void updateStep() {
        stepCount++;
        if (stepCount > totalTutorialSteps) {
            stepCount = totalTutorialSteps;
        }
    }
    public String getStepCount() {
        return "(Step " + stepCount + "/" + totalTutorialSteps + ")";
    }
    public void newTaskWCount(String taskDescription, int totalSteps) {
        this.taskDescription = taskDescription;
        this.totalSteps = totalSteps;
        stepCount = 0;
    }
    public String isTaskCompleted() {
        if (stepCount >= totalSteps) {
            stepCount = totalSteps;
            return "Task Completed!";
        }
        return "Task In Progress";
    }
    public Boolean whereToStart(int x, int y) {
        if (x == 0 && y == 0) {
            return true;
        }
        return false;
    }
    public void extraDescription(String objective) {
        this.objective = objective;
    }
    public void draw(Graphics g, int x, int y) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString(text, x, y);
        g.drawString(getStepCount(), x, y + 20);
    }
    public void drawTaskDescription(Graphics g, int x, int y) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString(taskDescription, x, y);
    }
    public void drawObjective(Graphics g, int x, int y) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString(objective, x, y);
    }
    public void textFadeOut(boolean f, Graphics g) {
        double dim = 1.0;
        if(dim < 1.0){
            Graphics2D g2 = (Graphics2D) g.create();
            int alpha = (int)(255 * (1.0 - dim));
            g2.setColor(new Color(0, 0, 0, alpha));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
        if(f && dim > 0.0){
            dim = Math.max(0.0, dim - 0.1);
        }
    }
}
