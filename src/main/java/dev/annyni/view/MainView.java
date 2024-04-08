package dev.annyni.view;

import dev.annyni.ProjectFactory;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

/**
 * todo Document type MainView
 */
@RequiredArgsConstructor
public class MainView {

    private final ProjectFactory projectFactory;
    private final Scanner scanner = new Scanner(System.in);

    public void statProject(){
        System.out.println("Выберите:");
        System.out.println("1. Label");
        System.out.println("2. Writer");
        System.out.println("3. Post");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice){
            case 1 -> projectFactory.runLabel();
            case 2 -> projectFactory.runWriter();
            case 3 -> projectFactory.runPost();
            default -> System.out.println("Вы ввели неверный символ!");
        }
    }
}
