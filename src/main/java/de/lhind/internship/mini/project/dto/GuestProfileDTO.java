package de.lhind.internship.mini.project.dto;

import java.time.LocalDate;

public class GuestProfileDTO {

    private Long id;
    private String address;
    private LocalDate dateOfBirth;
    private String nationality;
    private String preferredLanguage;

    public GuestProfileDTO() {}

    public GuestProfileDTO(Long id, String address, LocalDate dateOfBirth, String nationality, String preferredLanguage) {
        this.id = id;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.preferredLanguage = preferredLanguage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }
}
