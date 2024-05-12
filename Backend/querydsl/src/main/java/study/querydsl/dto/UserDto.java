package study.querydsl.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private String name;
    private int age;
    private int maxAge;

    public UserDto(String name, int age, int maxAge) {
        this.name = name;
        this.age = age;
        this.maxAge = maxAge;
    }
}
