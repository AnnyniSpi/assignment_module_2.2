package dev.annyni;

import dev.annyni.view.MainView;

/**
 * todo Document type ${NAME}
 */
public class Main {
    public static void main(String[] args) {
        ProjectFactory projectFactory = new ProjectFactory();
        MainView mainView = new MainView(projectFactory);

        mainView.statProject();
    }
}