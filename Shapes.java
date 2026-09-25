interface Shapes {
    public static void main(String[] args) {
        
    }
    void CalculateArea();
}

class circle implements Shapes {
    public void calculate () {
        System.out.println("circle");
    }
}

class rectangle implements Shapes {
    public void print() {
        System.out.println("rectangle");

    }
}
