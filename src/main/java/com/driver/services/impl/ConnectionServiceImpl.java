package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception{
        // Find the user by userId
        Optional<User> optionalUser = userRepository2.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Check if the user is already connected to any service provider
            if (user.getConnected()) {
                throw new RuntimeException("Already connected");
            }

            // Check if the countryName corresponds to the user's original country
            if (user.getOriginalCountry().getCountryName().toString().equalsIgnoreCase(countryName)) {
                return user; // No action required, return the user as it is
            }

            // Find suitable service provider to connect to the given country
            List<ServiceProvider> suitableProviders = user.getServiceProviderList().stream()
                    .filter(provider -> provider.getCountryList().stream()
                            .anyMatch(country -> country.getCountryName().toString().equalsIgnoreCase(countryName)))
                    .sorted(Comparator.comparing(ServiceProvider::getId))
                    .collect(Collectors.toList());

            if (suitableProviders.isEmpty()) {
                throw new RuntimeException("Unable to connect");
            }

            // Connect to the first suitable provider
            ServiceProvider serviceProvider = suitableProviders.get(0);
            String maskedIp = serviceProvider.getName() + "." + serviceProvider.getId() + "." + user.getId();
            user.setMaskedIp(maskedIp);
            user.setConnected(true);
            return userRepository2.save(user);
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }
    @Override
    public User disconnect(int userId) throws Exception {
        // Find the user by userId
        Optional<User> optionalUser = userRepository2.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Check if the user is already disconnected
            if (!user.getConnected()) {
                throw new RuntimeException("Already disconnected");
            }

            // Disconnect the user
            user.setMaskedIp(null);
            user.setConnected(false);
            return userRepository2.save(user);
        } else {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
        return new User();
    }
}
