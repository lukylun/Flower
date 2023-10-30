package com.kkoch.admin.domain.admin;

import com.kkoch.admin.domain.TimeBaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long id;

    @Column(unique = true, nullable = false, updatable = false, length = 20)
    private String loginId;

    @Column(nullable = false, length = 60)
    private String loginPw;

    @Column(nullable = false, updatable = false, length = 20)
    private String name;

    @Column(unique = true, nullable = false, length = 13)
    private String tel;

    @Column(nullable = false, length = 2)
    private String position;

    @Column(nullable = false)
    private boolean active;

    @Builder
    private Admin(Long id, String loginId, String loginPw, String name, String tel, String position, boolean active) {
        this.id = id;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.name = name;
        this.tel = tel;
        this.position = position;
        this.active = active;
    }

    //== 연관관계 편의 메서드 ==//
    public static Admin toEntity(Long id) {
        Admin admin = Admin
                .builder()
                .build();
        admin.id = id;
        return admin;
    }

    //== 비즈니스 로직 ==//
    public void edit(String loginPw, String tel) {
        this.loginPw = loginPw;
        this.tel = tel;

    }

    public void remove() {
        this.active = false;
    }
}
