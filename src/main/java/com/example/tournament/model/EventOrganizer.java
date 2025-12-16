package com.example.tournament.model;

import javax.persistence.*;

/**
 * Entity representing an event organizer.
 */
@Entity
@Table(name = "event_organizers")
public class EventOrganizer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private String contactEmail;
    
    private String phoneNumber;
    
    // Constructors
    public EventOrganizer() {
    }
    
    public EventOrganizer(String name) {
        this.name = name;
    }
    
    public EventOrganizer(String name, String contactEmail, String phoneNumber) {
        this.name = name;
        this.contactEmail = contactEmail;
        this.phoneNumber = phoneNumber;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getContactEmail() {
        return contactEmail;
    }
    
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    @Override
    public String toString() {
        return "EventOrganizer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
