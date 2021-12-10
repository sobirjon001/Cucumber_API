public class Main {

    public static void main(String[] args) {
        int count = 19;
        int limit = 10;
        int pages = 0;
        while ( count > 0) {
            count = count - limit;
            pages++;
        }
        System.out.println("pages = " + pages);
    }
}
