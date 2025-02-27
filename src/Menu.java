import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Menu {
    ArrayList<Person> people = new ArrayList<Person>();
    Path fileForm = Path.of("utils\\form.txt");
    List<String> questions = new ArrayList<>();
    Scanner myObj = new Scanner(System.in).useLocale(Locale.US);
    int option = 0;
    int index = 1;
    public void app() {

        do {
            showMenu();

            Scanner myOption = new Scanner((System.in));
            option = myOption.nextInt();

            switch (option) {
                case 1:
                    showForm();
                    addPerson();
                    break;
                case 2:
                    listPeople();
                    break;
                case 3:
                    createQuestion();
                    break;
                case 4:
                    removeQuestion();
                    break;
                case 5:
                    searchByUser();
                    break;
                case 6:
                    removeUser();
                    break;
                case 7:
                    updateUser();
                    break;
                case 8:
                    System.exit(0);

            }
        }
        while (option > 5 || option <= 0);
    }

    public void updateUser() {
        if(people.isEmpty()) {
            System.out.println("You need add user!!");
        }

        listPeople();
        System.out.print("User to be updated: ");
        int userNumber = myObj.nextInt();

        String name = getValidString("Enter name: ", 4, 15);
        int age = getValidInt("Enter age: ", 0, 100);
        String email = getValidString("Enter email: ", 10, 50);
        float height = getValidInt("Enter height (cm): ", 140, 300);

        Person personObj = new Person(name, age, email, height);
        people.remove(userNumber - 1);
        people.add(userNumber - 1, personObj);

        app();

    }

    public void addPerson() {
        String name = getValidString("Enter name: ", 4, 15);
        int age = getValidInt("Enter age: ", 0, 100);
        String email = getValidString("Enter email: ", 10, 50);
        float height = getValidInt("Enter height (cm): ", 140, 300);

        Person personObj = new Person(name, age, email, height);
        people.add(personObj);

        app();
    }

    private String getValidString(String prompt, int min, int max) {
        String input;
        do {
            System.out.print(prompt);
            input = myObj.nextLine();
        } while (input.length() < min || input.length() > max);
        return input;
    }

    private int getValidInt(String prompt, int min, int max) {
        int input;
        do {
            System.out.print(prompt);
            while (!myObj.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                myObj.next();
            }
            input = myObj.nextInt();
            myObj.nextLine(); // Consumir quebra de linha
        } while (input < min || input > max);
        return input;
    }


    public void removeUser() {
        if(people.isEmpty()) {
            System.out.println("You need add user!!");
        } else {
            listPeople();
            System.out.print("User to be deleted: ");
            int userNumber = myObj.nextInt();
            people.remove(userNumber - 1);
        }

    }

    public void searchByUser() {
        System.out.println("Search User by:\n" +
                "       ➜ Name\n" +
                "       ➜ Age\n" +
                "       ➜ Email  ");

        String compare = myObj.nextLine();

        if (compare.matches("\\d+")) {
            int age = Integer.parseInt(compare);
            people.stream()
                    .filter(person -> person.getAge() == age)
                    .forEach(System.out::println);
        }

        Stream<Person> streamEmail = people.stream().filter(people -> people.getEmail().contains(compare));
        streamEmail.forEach(System.out::println);

        Stream<Person> streamName = people.stream().filter(people -> people.getName().contains(compare));
        streamName.forEach(System.out::println);

        app();
    }

    public void removeQuestion() {
        showForm();
        System.out.print("Question for deletion: ");
        int numberQ = myObj.nextInt();
        while (numberQ <= 4) {
            numberQ = myObj.nextInt();
        }

        try {
            questions = Files.readAllLines(Paths.get("utils\\form.txt"));
            System.out.println(questions.remove(numberQ - 1));
            Files.write(Paths.get("utils\\form.txt"), questions);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        app();
    }

        public void showForm() {
        int i = 0;

        try {
            questions = Files.readAllLines(Paths.get("utils\\form.txt"));
            System.out.println("==================================================\n" +
                    "                 FORMULARY\n" +
                    "        Please answer the questions\n" +
                    "==================================================");
            for(String contents : questions) {
                i++;
                System.out.println("[" + i + "] ➤ " + contents);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createQuestion() {
        int numberQuestion = 5;
        String content;

        try {
            content = Files.readString(fileForm);
            showForm();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        System.out.print("[" + numberQuestion + "] ➤ ");
        String question = myObj.nextLine();
        while (question.length() < 10 || question.length() > 100) {
            question = myObj.nextLine();
        }


        numberQuestion += 1;


        try {
            FileWriter myFile = new FileWriter("utils\\form.txt");
            myFile.write(content);
            myFile.write(question);
            myFile.write("\n");
            myFile.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getCause());
        }

        app();

    }

    public void showMenu() {
        Path fileMenu = Path.of("utils\\menu.txt");
        try {
            String content = Files.readString(fileMenu);
            System.out.println(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void listPeople() {
        int i = 1;
        if(people.isEmpty()) {
            System.out.println("You ned add Users!!");
        } else {
            for(Person user : people) {
                System.out.println(i + "-" + user);
                i++;
            }
        }
        option = 0;
    }



}
