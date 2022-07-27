package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
public class CategoryItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "CATEGORY_ITEM_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

}
