package de.lhind.internship.mini.project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuestProfile {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int id;
    private String address;
    private String dateOfBirth;
    private String nationality;
    private String preferredLanguage;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Guest guest;
}
