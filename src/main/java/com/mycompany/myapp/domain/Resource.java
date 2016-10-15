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
 * A Resource.
 */
@Entity
@Table(name = "resource")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "resource")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @NotNull
    @Min(value = 0)
    @Max(value = 50000)
    @Column(name = "size", nullable = false)
    private Integer size;

    @ManyToOne
    private Service service;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "resource_employee",
               joinColumns = @JoinColumn(name="resources_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="employees_id", referencedColumnName="ID"))
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(mappedBy = "resource")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BookingItem> bookingItems = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Resource name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public Resource size(Integer size) {
        this.size = size;
        return this;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Service getService() {
        return service;
    }

    public Resource service(Service service) {
        this.service = service;
        return this;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public Resource employees(Set<Employee> employees) {
        this.employees = employees;
        return this;
    }

    public Resource addEmployee(Employee employee) {
        employees.add(employee);
        employee.getResources().add(this);
        return this;
    }

    public Resource removeEmployee(Employee employee) {
        employees.remove(employee);
        employee.getResources().remove(this);
        return this;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public Set<BookingItem> getBookingItems() {
        return bookingItems;
    }

    public Resource bookingItems(Set<BookingItem> bookingItems) {
        this.bookingItems = bookingItems;
        return this;
    }

    public Resource addBookingItem(BookingItem bookingItem) {
        bookingItems.add(bookingItem);
        bookingItem.setResource(this);
        return this;
    }

    public Resource removeBookingItem(BookingItem bookingItem) {
        bookingItems.remove(bookingItem);
        bookingItem.setResource(null);
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
        Resource resource = (Resource) o;
        if(resource.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, resource.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Resource{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", size='" + size + "'" +
            '}';
    }
}
