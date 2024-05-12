package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Setter
@Getter
public class Movie extends Item {

    private String director;
    private String actor;
}
