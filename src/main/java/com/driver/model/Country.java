// Note: Do not write @Enumerated annotation above CountryName in this model.
package com.driver.model;


import javax.persistence.*;

@Entity
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private CountryName countryName;
    private String code;

    @ManyToOne
    @JoinColumn
    private User user;

    @OneToOne
    @JoinColumn
    private ServiceProvider serviceProvider;


    public Country() {
    }

    public Country(CountryName countryName, String code) {
        this.countryName = countryName;
        this.code = code;
    }

    public Country(
                   ServiceProvider serviceProvider,
                   User user,
                   String code,
                   CountryName countryName) {
        this.serviceProvider = serviceProvider;
        this.user = user;
        this.code = code;
        this.countryName = countryName;
    }

    public int getCountryId() {
        return id;
    }

    public void setCountryId(int countryId) {
        this.id = countryId;
    }

    public CountryName getCountryName() {
        return countryName;
    }

    public void setCountryName(CountryName countryName) {
        this.countryName = countryName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
