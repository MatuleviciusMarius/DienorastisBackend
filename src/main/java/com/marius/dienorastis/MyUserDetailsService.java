package com.marius.dienorastis;

import com.marius.dienorastis.dao.UserRepo;
import com.marius.dienorastis.models.MyUserDetails;
import com.marius.dienorastis.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;
    private User currentUser;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByEmail(email);

        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + email));
        currentUser = user.get();
        return user.map(MyUserDetails::new).get();
    }

    public void registerUser (User user){
        userRepo.save(user);
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
