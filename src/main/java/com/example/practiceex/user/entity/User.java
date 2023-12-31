package com.example.practiceex.user.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor // 생성자를 직접 만들어주었기 때문에 기초 생성자가 필수라서 추가
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    private String nickname;
    private String introduction;

    public User(String email, String password, UserRoleEnum role) {
        this.email = email;
        this.password = password;
        this.role = role;
        nickname = email.substring(0, email.indexOf('@'));
        introduction = "I'm " + nickname + " from ITWASSUMMER:D";
    }

    public void editUserInfo(String nickname, String introduction) {
        this.nickname = nickname;
        this.introduction = introduction;
    }

    public void editPassword(String newPassword) {
        this.password = newPassword;
    }
}
