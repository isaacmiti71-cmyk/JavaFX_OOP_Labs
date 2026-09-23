abstract class Report {
    protected String title;

    public Report(String title) {
        this.title = title;
    }

    void printTitle() {
        System.out.println(title);
    }
    
    abstract void generate();
}

class SalesReport extends Report {
    SalesReport() {
       super("Sales");
    }
    void generate() {
       System.out.println("Generating sales report");
    }
}

public class Main {
    
    public static void main(String[] args) {
    
        SalesReport report = new
    SalesReport();

        report.printTitle();

        report.generate();
    }
}