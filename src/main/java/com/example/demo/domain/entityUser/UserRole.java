package com.example.demo.domain.entityUser;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_roles")
public class UserRole {

    @EmbeddedId // ✅ Говорим Hibernate, что у таблицы нет id, а есть составной ключ
    private UserRoleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false) // ✅ Hibernate теперь понимает связь
    private User user;

    public UserRole(User user, String role) {
        this.id = new UserRoleId(user.getId(), role);
        this.user = user;
    }

    public String getRole() {
        return id.getRole();
    }
}
