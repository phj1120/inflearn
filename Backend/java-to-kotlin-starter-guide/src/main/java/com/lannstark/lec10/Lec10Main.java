package com.lannstark.lec10;

public class Lec10Main {

  public static void main(String[] args) {
    new JavaDerived(300);
  }
}

class JavaBase {
  public JavaBase() {
    System.out.println("Base Constructor");
    System.out.println(getNumber());  // 오버라이드된 메서드 호출
  }

  public int getNumber() {
    return 100;
  }
}

class JavaDerived extends JavaBase {
  private int number;

  public JavaDerived(int number) {
    super();
    System.out.println("Base Constructor");
    this.number = number;  // 이 시점에서야 number = 300
  }

  @Override
  public int getNumber() {
    return number;  // 아직 초기화 안 됨!
  }
}
