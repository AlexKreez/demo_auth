package com.example.demo.domain.entityUser;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_roles")
public class UserRole {

    @EmbeddedId // Говорим что у таблицы нет id, а есть составной ключ
    private UserRoleId id;

    @ManyToOne(fetch = FetchType.LAZY)//много ролей могут принадлежать одному пользователю
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    public UserRole(User user, String role) {
        this.id = new UserRoleId(user.getId(), role);
        this.user = user;
    }

    public String getRole() {
        return id.getRole();
    }
}
