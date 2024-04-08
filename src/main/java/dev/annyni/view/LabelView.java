package dev.annyni.view;

import dev.annyni.controller.LabelController;
import dev.annyni.model.Label;
import dev.annyni.model.Status;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Scanner;

/**
 * todo Document type LabelView
 */
@RequiredArgsConstructor
public class LabelView {
    private final LabelController labelController;
    private final Scanner scanner = new Scanner(System.in);

    private boolean waiting = true;

    public void printMenu(){
        while (waiting){
            System.out.println("Что бы вы хотели? Введите число.");
            System.out.println("1. Создать Label");
            System.out.println("2. Изменить Label");
            System.out.println("3. Найти Label по id");
            System.out.println("4. Найти все Label");
            System.out.println("5. Удалить Label");
            System.out.println("6. Выйти");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice){
                case 1 -> createLabel();
                case 2 -> updateLabel();
                case 3 -> getLabel();
                case 4 -> getAllLabels();
                case 5 -> deleteLabel();
                case 6 -> {
                    waiting = false;
                    System.out.println("Выход из главного меню");
                }
                default -> System.out.println("Вы ввели неверный символ!");
            }
        }
    }

    private void deleteLabel() {
        System.out.println("Введите id Label который хотите удалить: ");
        long id = scanner.nextLong();

        labelController.deleteLabelById(id);

        System.out.println("label удален!");

    }

    private void getAllLabels() {
        System.out.println("Представлены все Labels: ");
        List<Label> allLabels = labelController.getAllLabels();

        for (Label label : allLabels) {
            System.out.println(label);
        }
    }

    private void getLabel() {
        System.out.println("Есть: ");
        getAllLabels();
        System.out.println("Введите id Label которого вы хотите вывести: ");

        long id = scanner.nextLong();

        Label labelById = labelController.getLabelById(id);

        System.out.println("Label успешно найден! " + labelById);
    }

    private void updateLabel() {
        System.out.println("Есть: ");
        getAllLabels();
        System.out.println("Введите id Label имя которого вы хотите изменить: ");

        long id = scanner.nextLong();
        scanner.nextLine();

        System.out.println("Введите новое имя: ");
        String name = scanner.nextLine();
        Status status = null;

        while (waiting) {
            System.out.println("Введите новый статус для Label: ");
            System.out.println("1. Label is ACTIVE");
            System.out.println("2. Label is UNDER_REVIEW");
            System.out.println("3. Label is DELETE");
            System.out.println("4. Выйти");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> status = Status.ACTIVE;
                case 2 -> status = Status.UNDER_REVIEW;
                case 3 -> status = Status.DELETED;
                case 4 -> {
                    System.out.println("Выход!");
                    waiting = false;
                }

                default -> System.out.println("Вы ввели неверный символ!");
            }
        }

        Label newLabel = Label.builder()
            .id(id)
            .name(name)
            .status(status)
            .build();

        labelController.updateLabelById(newLabel);
        System.out.println("Label успешно изменен! " + newLabel);

    }

    private void createLabel() {
        System.out.println("Введите имя: ");
        String name =  scanner.nextLine();

        Label label = Label.builder()
            .name(name)
            .status(Status.ACTIVE)
            .build();

        labelController.createLabel(label);

        System.out.println("Label успешно создан. " + label);
    }
}
