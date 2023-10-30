package com.kkoch.admin.domain.plant;

import com.kkoch.admin.domain.TimeBaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Plant extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plant_id")
    private Long id;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code")
    private Category code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type")
    private Category type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "name")
    private Category name;

    @Builder
    private Plant(boolean active, Category code, Category type, Category name) {
        this.active = active;
        this.code = code;
        this.type = type;
        this.name = name;
    }
}
