package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "categories")
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent; // 여러개의 자식은 하나의 부모를 가질 수 있음

    @OneToMany(mappedBy = "parent") // 하나의 부모에 여러개의 자식을 가질수 있음
    private List<Category> child = new ArrayList<>();

    //==연관관계 메소드==//
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }
}
