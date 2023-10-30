package com.kkoch.admin.domain.notice;

import com.kkoch.admin.domain.TimeBaseEntity;
import com.kkoch.admin.domain.admin.Admin;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Builder
    private Notice(String title, String content, boolean active, Admin admin) {
        this.title = title;
        this.content = content;
        this.active = active;
        this.admin = admin;
    }

    //== 비즈니스 로직 ==//
    public void edit(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void remove() {
        this.active = false;
    }
}
