public class Main {
    public static void main(String[] args) {
        ServerWindow server = new ServerWindow();
        new Client(server, "Vasya");
        new Client(server, "Lena");
    }

}