package com.kkoch.admin.domain.plant;

import com.kkoch.admin.domain.TimeBaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Category extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> children = new ArrayList<>();

    @Builder
    private Category(String name, int level, boolean active, Category parent) {
        this.name = name;
        this.level = level;
        this.active = active;
        this.parent = parent;
    }

    public Category createCategory(String name) {
        Category child = Category.builder()
                .name(name)
                .level(this.level + 1)
                .active(true)
                .parent(this)
                .build();
        this.children.add(child);
        return child;
    }

    public void changeCategory(String name) {
        this.name = name;
    }

    public void remove() {
        this.active = false;
    }
}
