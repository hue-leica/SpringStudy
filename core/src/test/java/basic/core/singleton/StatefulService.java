package basic.core.singleton;

public class StatefulService {

    private int price; // 상태를 유지하는 필드
    public void order(String name, int price){
        System.out.println("name  = " + name + ", price = "+price);
        this.price = price; // 여기에서 필드에 직접 값을 삽입해버림; 문제!
    }

    public int getPrice(){
        return price;
    }
}
