package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setConnected(false); // Set default connected state
        user.setOriginalIp(""); // Original IP will be set later
        user.setMaskedIp(null); // Masked IP initially null

        // Create a new Country object for the user's original country
        Country country = new Country();
//        country.setCountryName(CountryName.valueOf(countryName.toUpperCase())); // Assuming countryName is in uppercase
//        country.setCode(); // Set the country code based on enum value
        user.setOriginalCountry(country);

        // Save the user object
        User us =  userRepository3.save(user);
        user.setOriginalIp(country.getCode()+"."+us.getId());
        return us;

    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        Optional<User> optionalUser = userRepository3.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Find the service provider
            Optional<ServiceProvider> optionalProvider = serviceProviderRepository3.findById(serviceProviderId);
            if (optionalProvider.isPresent()) {
                ServiceProvider serviceProvider = optionalProvider.get();

                // Add the service provider to the user's list
                user.getServiceProviderList().add(serviceProvider);

                // Update the user
                return userRepository3.save(user);
            } else {
                throw new RuntimeException("Service provider not found with id: " + serviceProviderId);
            }
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }
}
