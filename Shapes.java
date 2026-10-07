import java.util.Scanner;

interface Shape {
double calculateArea();
}

class Circle implements Shape {
double r;

Circle(double r) {
this.r = r;
}

public double calculateArea() {
return Math.PI * r * r;
}
}

class Triangle implements Shape {
double b, h;
Triangle(double b, double h) {
this.b = b;
this.h = h;
}

public double calculateArea() {
return 0.5 * b * h;
}
}

class Rectangle implements Shape {
double l, w;
Rectangle(double l, double w) {
this.l = l;
this.w = w;
}
public double calculateArea() {
return l * w;
}
}

class Square implements Shape {
double s;
Square(double s) {
this.s = s;
}

public double calculateArea() {
return s * s;
}
}
public class Shapes {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
Shape shape = null;

System.out.println("1. Circle");
System.out.println("2. Triangle");
System.out.println("3. Rectangle");
System.out.println("4. Square");

System.out.print("Choose shape: ");
int choice = sc.nextInt();

switch (choice) {
case 1:
System.out.print("Enter radius: ");

shape = new Circle(sc.nextDouble());
break;

case 2:
System.out.print("Enter base: ");

double b = sc.nextDouble();
System.out.print("Enter height: ");

double h = sc.nextDouble();
shape = new Triangle(b, h);
break;

case 3:
System.out.print("Enter length: ");

double l = sc.nextDouble();
System.out.print("Enter width: ");

double w = sc.nextDouble();
shape = new Rectangle(l, w);
break;

case 4:
System.out.print("Enter side: ");
shape = new Square(sc.nextDouble());
break;

default:
System.out.println("Invalid choice");
}

if (shape != null) {
System.out.println("Area = " +
shape.calculateArea());
}

sc.close();
}
}