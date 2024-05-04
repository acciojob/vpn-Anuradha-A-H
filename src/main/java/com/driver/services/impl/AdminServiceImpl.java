package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password) {
        Admin admin  = new Admin();
        admin.setPassword(password);
        admin.setUsername(username);
        return adminRepository1.save(admin);
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {

        Optional<Admin> optionalAdmin = adminRepository1.findById(adminId);
        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();

            // Create a new ServiceProvider
            ServiceProvider serviceProvider = new ServiceProvider();
            serviceProvider.setName(providerName);

            // Add the service provider to the admin's list
            admin.getServiceProviders().add(serviceProvider);

            // Save the admin
            return adminRepository1.save(admin);
        } else {
            throw new RuntimeException("Admin not found with id: " + adminId);
        }

    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{


        // Find the service provider by serviceProviderId
        Optional<ServiceProvider> optionalServiceProvider = serviceProviderRepository1.findById(serviceProviderId);
        if (optionalServiceProvider.isPresent()) {
            ServiceProvider serviceProvider = optionalServiceProvider.get();

            if (!isValidCountryName(countryName)) {
                throw new IllegalArgumentException("Invalid country name: " + countryName);
            }

            // Create a new Country object for the given countryName
            Country country = new Country();
            country.setCountryName(CountryName.valueOf(countryName.toUpperCase())); // Assuming countryName is in uppercase
            country.setCode(country.getCountryName().toCode()); // Set the country code based on the enum value

            // Add the country to the service provider's country list
            serviceProvider.getCountryList().add(country);

            // Set the service provider for the country
            country.setServiceProvider(serviceProvider);

            // Save the updated service provider
            return serviceProviderRepository1.save(serviceProvider);
        } else {
            throw new RuntimeException("Service provider not found with id: " + serviceProviderId);
        }
    }

    private boolean isValidCountryName(String countryName) {
        try {
            CountryName.valueOf(countryName.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
