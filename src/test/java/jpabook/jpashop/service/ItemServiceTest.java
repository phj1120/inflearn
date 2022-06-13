package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.Movie;
import jpabook.jpashop.exception.NotEnoughStockException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Test
    public void 재고_수량_증가() throws Exception {
        // given
        Item item = new Movie();
        item.setName("브로커");
        item.setStockQuantity(10);

        // when
        item.addStock(5);

        // then
        assertEquals(item.getStockQuantity(), 15);
    }

    @Test
    public void 재고_수량_감소() throws Exception {
        // given
        Item item = new Movie();
        item.setName("브로커");
        item.setStockQuantity(10);

        // when
        item.removeStock(5);

        // then
        assertEquals(item.getStockQuantity(), 5);
    }

    @Test(expected = NotEnoughStockException.class)
    public void 재고_초과_감소() throws Exception {
        // given
        Item item = new Movie();
        item.setName("브로커");
        item.setStockQuantity(10);

        // when
        item.removeStock(15);

        // then
        fail("예외가 발생해야 합니다.");
    }


    @Test
    public void 상품_추가() throws Exception {
        // given
        Item item = new Movie();
        item.setName("브로커");
        item.setStockQuantity(10);

        // when
        List<Item> beforeItems = itemService.findItems();
        itemService.saveItem(item);
        List<Item> afterItems = itemService.findItems();

        // then
        assertEquals(beforeItems.size() + 1, afterItems.size());
    }

    @Test
    public void 상품_조회() throws Exception {
        // given
        Item item = new Movie();
        item.setName("브로커");
        itemService.saveItem(item);
        List<Item> findItems = itemService.findItems();
        Optional<Item> result = findItems.stream().filter(findItem -> findItem.getName().equals("브로커")).findFirst();


        // when
        Item findItem = result.get();
        long itemId = findItem.getId();
        Item findOne = itemService.findOne(itemId);

        // then
        assertEquals(findOne, findItem);
    }
}