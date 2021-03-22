package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@DiscriminatorColumn(name = "dtype")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;
    private int stockQuantity;
    @ManyToMany
    @JoinTable(name = "category_item",
                joinColumns = @JoinColumn(name = "item_id"),
                inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories = new ArrayList<>();

    // 방법 1: service에서 재고를 증/감 하는 비즈니스 로직이 있는 것
    // 방법 2: Entity자체에서 재고를 증/감 하는 비즈니스 로직을 가지는 것
    // 두 방법중 Entity자체에 필드를 관리하는 메서드를 두는 것이 응집력이 좋은 것이기에 Entity에 작성
    // 이러한 방식을 '도메인 모델 패턴' 이라고 한다 (service계층은 단순히 요청을 위임하는 역할만 한다)
    // == 비즈니스 로직 == //
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){
            /* Exception을 직접 만들 수도 있음 */
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity =  restStock;
    }
}
