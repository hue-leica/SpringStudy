package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional // readOnly를 오버라이드함 -> 조회빼고 나머지는 쓸 수 있어야 하기 때문!
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    /* 변경 감지를 할수 있게 하려면 해당 식별자를 가진 객체를 영속상태로 만들어야 한다
       1) find로 찾아와서 영속성 컨텍스트에서 관리하게 한다
       2) 객체를 수정하면 Dirty Checking이 일어나서 수정이 됨 */
    @Transactional
    public void updateItem(Long id, String name, int price, int stockQuantity){
        Item findItem = itemRepository.findOne(id);
        // setter를 쓰지 않고 찾아온 객체에 파라미터로 값을 바로바로 지정하는 것이 베스트!
        // 만약 변경되는 필드가 너무 많다면 DTO로 하자!
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
}
