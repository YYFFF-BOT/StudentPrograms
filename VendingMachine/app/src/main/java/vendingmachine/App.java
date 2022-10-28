/*
 * Main App class
 */
package vendingmachine;
import java.util.*;

public class App {
    private UserInterface UI;

    public App() {
        this.UI = new UserInterface();
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        this.UI.run(sc);
    }

    public static void main(String[] args) {
        App app = new App();
        app.run();

    }
}
