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
 * A Merchant.
 */
@Entity
@Table(name = "merchant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "merchant")
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "line_one_of_address", length = 20, nullable = false)
    private String lineOneOfAddress;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "second_line_of_address", length = 20, nullable = false)
    private String secondLineOfAddress;

    @NotNull
    @Size(min = 2, max = 7)
    @Column(name = "postcode", length = 7, nullable = false)
    private String postcode;

    @NotNull
    @Size(min = 2, max = 255)
    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @OneToMany(mappedBy = "merchant")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "merchant")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Service> services = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Merchant name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLineOneOfAddress() {
        return lineOneOfAddress;
    }

    public Merchant lineOneOfAddress(String lineOneOfAddress) {
        this.lineOneOfAddress = lineOneOfAddress;
        return this;
    }

    public void setLineOneOfAddress(String lineOneOfAddress) {
        this.lineOneOfAddress = lineOneOfAddress;
    }

    public String getSecondLineOfAddress() {
        return secondLineOfAddress;
    }

    public Merchant secondLineOfAddress(String secondLineOfAddress) {
        this.secondLineOfAddress = secondLineOfAddress;
        return this;
    }

    public void setSecondLineOfAddress(String secondLineOfAddress) {
        this.secondLineOfAddress = secondLineOfAddress;
    }

    public String getPostcode() {
        return postcode;
    }

    public Merchant postcode(String postcode) {
        this.postcode = postcode;
        return this;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getDescription() {
        return description;
    }

    public Merchant description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public Merchant reviews(Set<Review> reviews) {
        this.reviews = reviews;
        return this;
    }

    public Merchant addReview(Review review) {
        reviews.add(review);
        review.setMerchant(this);
        return this;
    }

    public Merchant removeReview(Review review) {
        reviews.remove(review);
        review.setMerchant(null);
        return this;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public Set<Service> getServices() {
        return services;
    }

    public Merchant services(Set<Service> services) {
        this.services = services;
        return this;
    }

    public Merchant addService(Service service) {
        services.add(service);
        service.setMerchant(this);
        return this;
    }

    public Merchant removeService(Service service) {
        services.remove(service);
        service.setMerchant(null);
        return this;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Merchant merchant = (Merchant) o;
        if(merchant.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, merchant.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Merchant{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", lineOneOfAddress='" + lineOneOfAddress + "'" +
            ", secondLineOfAddress='" + secondLineOfAddress + "'" +
            ", postcode='" + postcode + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
