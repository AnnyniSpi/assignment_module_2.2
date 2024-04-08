package dev.annyni.view;

import dev.annyni.controller.LabelController;
import dev.annyni.controller.PostController;
import dev.annyni.controller.WriterController;
import dev.annyni.model.Label;
import dev.annyni.model.Post;
import dev.annyni.model.Status;
import dev.annyni.model.Writer;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * todo Document type PostView
 */
@RequiredArgsConstructor
public class PostView {
    private final WriterController writerController;
    private final PostController postController;
    private final LabelController labelController;
    private final Scanner scanner = new Scanner(System.in);

    private boolean waiting = true;

    public void printMenu(){
        while (waiting){
            System.out.println("Что бы вы хотели? Введите число.");
            System.out.println("1. Создать Post");
            System.out.println("2. Изменить Post");
            System.out.println("3. Найти Post по id");
            System.out.println("4. Найти все Posts");
            System.out.println("5. Удалить Post");
            System.out.println("6. Добавить Label в Post");
            System.out.println("7. Выйти");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice){
                case 1 -> createPost();
                case 2 -> updatePost();
                case 3 -> getPost();
                case 4 -> getAllPosts();
                case 5 -> deletePost();
                case 6 -> {
                    System.out.println("Выход из главного меню");
                    waiting = false;
                }
                default -> System.out.println("Вы ввели неверный символ!");
            }
        }
    }

    private void deletePost() {
        System.out.println("Введите id Post который хотите удалить: ");
        long postId = scanner.nextLong();

        postController.deletePostById(postId);
        System.out.println("Post удален!");

    }

    private void getAllPosts() {
        List<Post> posts = postController.getAllPosts();
        for (Post post : posts) {
            System.out.println(post);
        }
    }

    private void getPost() {
        System.out.println("Есть: ");
        getAllPosts();
        System.out.println("Введите id Writer которого вы хотите вывести: ");

        long postId = scanner.nextLong();
        scanner.nextLine();

        Post post = postController.getPostById(postId);
        System.out.println("Post успешно найден! " + post);
    }

    private void updatePost() {
        System.out.println("Есть: ");
        getAllPosts();
        System.out.println("Введите id Post которого вы хотите изменить: ");

        long postId = scanner.nextLong();
        Post post = postController.getPostById(postId);

        System.out.println("Введите новый контент: ");
        String content = scanner.nextLine();
        Status status = Status.ACTIVE;

        List<Label> labels = getLabels();

        Map<Long, Label> labelMap = post.getLabels().stream()
            .collect(Collectors.toMap(Label::getId, label -> label));

        System.out.println("Есть: ");
        for(Label label : post.getLabels()){
            System.out.println(label.getId() + ". " + label.getName());
        }

        List<Label> deleteLabels = getDeleteLabels(post.getLabels());

        for (Label deleteLabel : deleteLabels) {
            labelController.deleteLabelById(deleteLabel.getId());
        }

        for (Label label : labels) {
            if (label.getId() != null){
                labelController.createLabel(label);
            }
        }

        post.getLabels().removeIf(deleteLabels::contains);
        post.getLabels().retainAll(labelMap.values());
        post.getLabels().addAll(labels);

        post.setContent(content);
        post.setUpdated(new Date());
        post.setStatus(status);

        postController.updatePost(post);
        System.out.println("Post успешно изменен! " + post);
    }

    private void createPost() {
        Writer writer = getWriter();

        System.out.println("Введите content: ");
        String content = scanner.nextLine();
        Status status = Status.ACTIVE;

        List<Label> labels = getLabels();

        for (Label label : labels) {
            labelController.createLabel(label);
        }

        Post post = Post.builder()
            .content(content)
            .created(new Date())
            .updated(new Date())
            .status(status)
            .labels(labels)
            .writer(writer)
            .build();

        postController.createPost(post);
        System.out.println("Post успешно создан. " + post);
    }

    private Writer getWriter(){
        System.out.println("Выберите Writer по ID: ");
        Long writerId = scanner.nextLong();
        scanner.nextLine();

        Writer writer = writerController.getWriterById(writerId);
        System.out.println("Выбранный Writer: " + writer.getFirstName() + " " + writer.getLastName() + " - " + writer.getStatus());
        return writer;
    }

    private List<Label> getLabels() {
        List<Label> labels = new ArrayList<>();

        boolean flag = true;

        while (flag){
            System.out.println("Добавить Label?");
            System.out.println("1. Да");
            System.out.println("2. Нет");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice){
                case 1 -> {
                    System.out.println("Введите название Label: ");
                    String name = scanner.nextLine();

                    Label label = Label.builder()
                        .name(name)
                        .status(Status.ACTIVE)
                        .build();

                    labels.add(label);
                }
                case 2 -> flag = false;
                default -> System.out.println("Вы ввели неверный символ!");
            }
        }

        return labels;
    }

    private List<Label> getDeleteLabels(List<Label> labels) {
        List<Label> deleteLabels = new ArrayList<>();

        boolean flag = true;

        while (flag){
            System.out.println("Хотите удалить Label?");
            System.out.println("1. Да");
            System.out.println("2. Нет");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice){
                case 1 -> {
                    System.out.println("Введите id Label который хотите удалить:");
                    long labelId = scanner.nextLong();

                    Label label = labels.stream()
                        .filter(l -> l.getId().equals(labelId))
                        .findFirst()
                        .orElse(null);

                    if (label != null){
                        deleteLabels.add(label);
                    } else {
                        System.out.println("Label с id " + labelId + " не найден.");
                    }
                }
                case 2 -> flag = false;
                default -> System.out.println("Вы ввели неверный символ!");
            }
        }

        return deleteLabels;
    }

}
