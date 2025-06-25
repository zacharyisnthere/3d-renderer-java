import java.util.Scanner;

;public class HelloWorld {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username");

        String userName = scanner.nextLine();
        System.out.println("Username: " + userName);
        System.out.println("Hello, World");
    }
}