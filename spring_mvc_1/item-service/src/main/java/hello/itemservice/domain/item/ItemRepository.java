package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {


    // 멀티 스레드 환경 사용할 경우 동시에 접근하면 값이 꼬일 수 있어
    // AtomicLong, ConcurrentHashMap 사용 해야함.
    private static final Map<Long, Item> store = new HashMap<>();

    private static long sequence = 0L;

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    // 값을 감싸서 반환해 ArrayList 에 값을 넣어도 store 이 변하지 않도록 안전하게.
    // 사용시 타입을 바꿔야 해서 ArrayList 로 바꿔서 반환
    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    // 정석 대로 하려면 id 를 안쓰므로
    // ItemParameterDto 이런 식으로 전용 객체를 만드는 것이 정석
    // 중복인 것 같아도 명확한게 더 좋다...!
    public void update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore() {
        store.clear();
    }

}
