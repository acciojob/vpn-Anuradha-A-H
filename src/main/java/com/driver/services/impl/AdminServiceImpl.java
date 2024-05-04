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
            ServiceProvider serviceProvider = new ServiceProvider();
            serviceProvider.setName(providerName);
            admin.getServiceProviders().add(serviceProvider);
            return adminRepository1.save(admin);
        } else {
            throw new RuntimeException("Admin not found with id: " + adminId);
        }

    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{


        Optional<ServiceProvider> optionalServiceProvider = serviceProviderRepository1.findById(serviceProviderId);
        if (optionalServiceProvider.isPresent()) {
            ServiceProvider serviceProvider = optionalServiceProvider.get();

            // Check if the provided country name is valid
//            if (!isValidCountryName(countryName)) {
//                throw new IllegalArgumentException("Country not found");
//            }

            // Create a new Country object based on the provided country name
            Country country = new Country();
            country.setCountryName(countryName);
            country.setCode(generateCountryCode(countryName));

            // Add the country to the service provider's country list
            serviceProvider.getCountryList().add(country);

            // Save the updated service provider
            return serviceProviderRepository1.save(serviceProvider);
        } else {
            throw new RuntimeException("Service provider not found with id: " + serviceProviderId);
        }
    }

    // Utility method to check if the provided country name is valid
//    private boolean isValidCountryName(String countryName) {
//        // Check if the country name is one of the specified options
//        return countryName.matches("(?i)ind|aus|usa|chi|jpn");
//    }

    // Utility method to generate country code based on the country name
    private String generateCountryCode(String countryName) {
        try {
            // Convert the country name to uppercase to match the enum values
            String uppercaseCountryName = countryName.toUpperCase();

            // Find the corresponding CountryName enum value
            CountryName countryEnum = CountryName.valueOf(uppercaseCountryName);

            // Retrieve the code using the toCode() method of the enum
            return countryEnum.toCode();
        } catch (IllegalArgumentException e) {
            // If the provided country name does not match any enum value, throw an error
            throw new IllegalArgumentException("Country not found ");
        }
    }


}
