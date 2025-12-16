package com.example.tournament.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a venue where matches can be held.
 */
@Entity
@Table(name = "venues")
public class Venue {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private String location;
    
    private Integer capacity;
    
    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    private List<TimeSlot> bookings = new ArrayList<>();
    
    @Transient
    private ObservableList<TimeSlot> observableBookings;
    
    // Constructors
    public Venue() {
    }
    
    public Venue(String name, String location) {
        this.name = name;
        this.location = location;
    }
    
    public Venue(String name, String location, Integer capacity) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
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
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    
    public List<TimeSlot> getBookings() {
        return bookings;
    }
    
    public void setBookings(List<TimeSlot> bookings) {
        this.bookings = bookings;
        syncObservableBookings();
    }
    
    public ObservableList<TimeSlot> getObservableBookings() {
        if (observableBookings == null) {
            observableBookings = FXCollections.observableArrayList(bookings);
        }
        return observableBookings;
    }
    
    /**
     * Checks if the venue is available during a specific time slot.
     */
    public boolean isAvailable(TimeSlot requestedSlot) {
        for (TimeSlot booking : bookings) {
            if (booking.overlapsWith(requestedSlot)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Books a time slot at this venue.
     */
    public boolean bookTimeSlot(TimeSlot timeSlot) {
        if (isAvailable(timeSlot)) {
            bookings.add(timeSlot);
            syncObservableBookings();
            return true;
        }
        return false;
    }
    
    /**
     * Synchronizes the observable list with the persisted list.
     */
    private void syncObservableBookings() {
        if (observableBookings != null) {
            observableBookings.setAll(bookings);
        }
    }
    
    @PostLoad
    @PostPersist
    @PostUpdate
    private void syncAfterDatabaseOperation() {
        syncObservableBookings();
    }
    
    @Override
    public String toString() {
        return "Venue{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
