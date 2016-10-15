package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Service.
 */
@Entity
@Table(name = "service")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "service")
public class Service implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "monday_is_open")
    private Boolean mondayIsOpen;

    @Column(name = "tuesday_is_open")
    private Boolean tuesdayIsOpen;

    @Column(name = "wednesday_is_open")
    private Boolean wednesdayIsOpen;

    @Column(name = "thursday_is_open")
    private Boolean thursdayIsOpen;

    @Column(name = "friday_is_open")
    private Boolean fridayIsOpen;

    @Column(name = "saturday_is_open")
    private Boolean saturdayIsOpen;

    @Column(name = "sunday_is_open")
    private Boolean sundayIsOpen;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @NotNull
    @Size(min = 2, max = 256)
    @Column(name = "description", length = 256, nullable = false)
    private String description;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "service_type", length = 20, nullable = false)
    private String serviceType;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private ZonedDateTime endTime;

    @ManyToOne
    private Merchant merchant;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "service_holiday",
               joinColumns = @JoinColumn(name="services_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="holidays_id", referencedColumnName="ID"))
    private Set<Holiday> holidays = new HashSet<>();

    @OneToMany(mappedBy = "service")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Offer> offers = new HashSet<>();

    @OneToMany(mappedBy = "service")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Resource> resources = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isMondayIsOpen() {
        return mondayIsOpen;
    }

    public Service mondayIsOpen(Boolean mondayIsOpen) {
        this.mondayIsOpen = mondayIsOpen;
        return this;
    }

    public void setMondayIsOpen(Boolean mondayIsOpen) {
        this.mondayIsOpen = mondayIsOpen;
    }

    public Boolean isTuesdayIsOpen() {
        return tuesdayIsOpen;
    }

    public Service tuesdayIsOpen(Boolean tuesdayIsOpen) {
        this.tuesdayIsOpen = tuesdayIsOpen;
        return this;
    }

    public void setTuesdayIsOpen(Boolean tuesdayIsOpen) {
        this.tuesdayIsOpen = tuesdayIsOpen;
    }

    public Boolean isWednesdayIsOpen() {
        return wednesdayIsOpen;
    }

    public Service wednesdayIsOpen(Boolean wednesdayIsOpen) {
        this.wednesdayIsOpen = wednesdayIsOpen;
        return this;
    }

    public void setWednesdayIsOpen(Boolean wednesdayIsOpen) {
        this.wednesdayIsOpen = wednesdayIsOpen;
    }

    public Boolean isThursdayIsOpen() {
        return thursdayIsOpen;
    }

    public Service thursdayIsOpen(Boolean thursdayIsOpen) {
        this.thursdayIsOpen = thursdayIsOpen;
        return this;
    }

    public void setThursdayIsOpen(Boolean thursdayIsOpen) {
        this.thursdayIsOpen = thursdayIsOpen;
    }

    public Boolean isFridayIsOpen() {
        return fridayIsOpen;
    }

    public Service fridayIsOpen(Boolean fridayIsOpen) {
        this.fridayIsOpen = fridayIsOpen;
        return this;
    }

    public void setFridayIsOpen(Boolean fridayIsOpen) {
        this.fridayIsOpen = fridayIsOpen;
    }

    public Boolean isSaturdayIsOpen() {
        return saturdayIsOpen;
    }

    public Service saturdayIsOpen(Boolean saturdayIsOpen) {
        this.saturdayIsOpen = saturdayIsOpen;
        return this;
    }

    public void setSaturdayIsOpen(Boolean saturdayIsOpen) {
        this.saturdayIsOpen = saturdayIsOpen;
    }

    public Boolean isSundayIsOpen() {
        return sundayIsOpen;
    }

    public Service sundayIsOpen(Boolean sundayIsOpen) {
        this.sundayIsOpen = sundayIsOpen;
        return this;
    }

    public void setSundayIsOpen(Boolean sundayIsOpen) {
        this.sundayIsOpen = sundayIsOpen;
    }

    public String getName() {
        return name;
    }

    public Service name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Service description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceType() {
        return serviceType;
    }

    public Service serviceType(String serviceType) {
        this.serviceType = serviceType;
        return this;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public Service startTime(ZonedDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public Service endTime(ZonedDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public Service merchant(Merchant merchant) {
        this.merchant = merchant;
        return this;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Set<Holiday> getHolidays() {
        return holidays;
    }

    public Service holidays(Set<Holiday> holidays) {
        this.holidays = holidays;
        return this;
    }

    public Service addHoliday(Holiday holiday) {
        holidays.add(holiday);
        holiday.getServices().add(this);
        return this;
    }

    public Service removeHoliday(Holiday holiday) {
        holidays.remove(holiday);
        holiday.getServices().remove(this);
        return this;
    }

    public void setHolidays(Set<Holiday> holidays) {
        this.holidays = holidays;
    }

    public Set<Offer> getOffers() {
        return offers;
    }

    public Service offers(Set<Offer> offers) {
        this.offers = offers;
        return this;
    }

    public Service addOffer(Offer offer) {
        offers.add(offer);
        offer.setService(this);
        return this;
    }

    public Service removeOffer(Offer offer) {
        offers.remove(offer);
        offer.setService(null);
        return this;
    }

    public void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }

    public Set<Resource> getResources() {
        return resources;
    }

    public Service resources(Set<Resource> resources) {
        this.resources = resources;
        return this;
    }

    public Service addResource(Resource resource) {
        resources.add(resource);
        resource.setService(this);
        return this;
    }

    public Service removeResource(Resource resource) {
        resources.remove(resource);
        resource.setService(null);
        return this;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Service service = (Service) o;
        if(service.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, service.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Service{" +
            "id=" + id +
            ", mondayIsOpen='" + mondayIsOpen + "'" +
            ", tuesdayIsOpen='" + tuesdayIsOpen + "'" +
            ", wednesdayIsOpen='" + wednesdayIsOpen + "'" +
            ", thursdayIsOpen='" + thursdayIsOpen + "'" +
            ", fridayIsOpen='" + fridayIsOpen + "'" +
            ", saturdayIsOpen='" + saturdayIsOpen + "'" +
            ", sundayIsOpen='" + sundayIsOpen + "'" +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", serviceType='" + serviceType + "'" +
            ", startTime='" + startTime + "'" +
            ", endTime='" + endTime + "'" +
            '}';
    }
}
