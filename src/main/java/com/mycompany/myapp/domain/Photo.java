package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Photo.
 */
@Entity
@Table(name = "photo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "photo")
public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "location")
    private String location;

    @Column(name = "is_profile_photo")
    private Boolean isProfilePhoto;

    @Column(name = "tag")
    private String tag;

    @ManyToOne
    private Review review;

    @ManyToOne
    private Merchant merchant;

    @ManyToOne
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public Photo location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean isIsProfilePhoto() {
        return isProfilePhoto;
    }

    public Photo isProfilePhoto(Boolean isProfilePhoto) {
        this.isProfilePhoto = isProfilePhoto;
        return this;
    }

    public void setIsProfilePhoto(Boolean isProfilePhoto) {
        this.isProfilePhoto = isProfilePhoto;
    }

    public String getTag() {
        return tag;
    }

    public Photo tag(String tag) {
        this.tag = tag;
        return this;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Review getReview() {
        return review;
    }

    public Photo review(Review review) {
        this.review = review;
        return this;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public Photo merchant(Merchant merchant) {
        this.merchant = merchant;
        return this;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Photo customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Photo photo = (Photo) o;
        if(photo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, photo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Photo{" +
            "id=" + id +
            ", location='" + location + "'" +
            ", isProfilePhoto='" + isProfilePhoto + "'" +
            ", tag='" + tag + "'" +
            '}';
    }
}
