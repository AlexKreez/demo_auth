package com.example.demo.domain.entityUser;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Embeddable //Говорит Hibernate, что этот класс не является отдельной таблицей, а будет встроен в другой класс (UserRole)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleId implements Serializable {//Serializable обязательно для составных ключей в JPA

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role")
    private String role;
}
