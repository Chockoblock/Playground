package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 1, max = 20)
    @Column(name = "first_line_of_address", length = 20)
    private String firstLineOfAddress;

    @Size(min = 2, max = 20)
    @Column(name = "second_line_of_address", length = 20)
    private String secondLineOfAddress;

    @Size(min = 11, max = 15)
    @Pattern(regexp = "[0-9]*$")
    @Column(name = "contact_number", length = 15)
    private String contactNumber;

    @Size(min = 2, max = 20)
    @Column(name = "name", length = 20)
    private String name;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Photo> photos = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BookingItem> bookingItems = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstLineOfAddress() {
        return firstLineOfAddress;
    }

    public Customer firstLineOfAddress(String firstLineOfAddress) {
        this.firstLineOfAddress = firstLineOfAddress;
        return this;
    }

    public void setFirstLineOfAddress(String firstLineOfAddress) {
        this.firstLineOfAddress = firstLineOfAddress;
    }

    public String getSecondLineOfAddress() {
        return secondLineOfAddress;
    }

    public Customer secondLineOfAddress(String secondLineOfAddress) {
        this.secondLineOfAddress = secondLineOfAddress;
        return this;
    }

    public void setSecondLineOfAddress(String secondLineOfAddress) {
        this.secondLineOfAddress = secondLineOfAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public Customer contactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
        return this;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getName() {
        return name;
    }

    public Customer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public Customer photos(Set<Photo> photos) {
        this.photos = photos;
        return this;
    }

    public Customer addPhoto(Photo photo) {
        photos.add(photo);
        photo.setCustomer(this);
        return this;
    }

    public Customer removePhoto(Photo photo) {
        photos.remove(photo);
        photo.setCustomer(null);
        return this;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public Customer reviews(Set<Review> reviews) {
        this.reviews = reviews;
        return this;
    }

    public Customer addReview(Review review) {
        reviews.add(review);
        review.setCustomer(this);
        return this;
    }

    public Customer removeReview(Review review) {
        reviews.remove(review);
        review.setCustomer(null);
        return this;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public Set<BookingItem> getBookingItems() {
        return bookingItems;
    }

    public Customer bookingItems(Set<BookingItem> bookingItems) {
        this.bookingItems = bookingItems;
        return this;
    }

    public Customer addBookingItem(BookingItem bookingItem) {
        bookingItems.add(bookingItem);
        bookingItem.setCustomer(this);
        return this;
    }

    public Customer removeBookingItem(BookingItem bookingItem) {
        bookingItems.remove(bookingItem);
        bookingItem.setCustomer(null);
        return this;
    }

    public void setBookingItems(Set<BookingItem> bookingItems) {
        this.bookingItems = bookingItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        if(customer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + id +
            ", firstLineOfAddress='" + firstLineOfAddress + "'" +
            ", secondLineOfAddress='" + secondLineOfAddress + "'" +
            ", contactNumber='" + contactNumber + "'" +
            ", name='" + name + "'" +
            '}';
    }
}
