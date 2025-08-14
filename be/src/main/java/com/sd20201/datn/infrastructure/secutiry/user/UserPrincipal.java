package com.sd20201.datn.infrastructure.secutiry.user;

import com.sd20201.datn.entity.Customer;
import com.sd20201.datn.entity.Staff;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@ToString
public class UserPrincipal implements UserDetails, OAuth2User {

    @Getter
    private final String id;

    private final String username;

    @Getter
    private final String email;

    private String password;

    private final Collection<? extends GrantedAuthority> authorities;

    @Setter
    private Map<String, Object> attributes;

    public UserPrincipal(String id,String username,String password, String email, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
    }

    public static UserPrincipal create(Staff staff, List<String> roles) {
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        log.info("Creating user principal for staff. authorities staff : {}", staff.toString());

        return new UserPrincipal(
                staff.getId(),
                staff.getAccount().getUsername(),
                staff.getAccount().getPassword(),
                staff.getEmail(),
                authorities
        );
    }

    public static UserPrincipal create(Customer customer, List<String> roles) {
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        log.info("Creating user principal for customer. authorities customer : {}", customer.toString());

        return new UserPrincipal(
                customer.getId(),
                customer.getAccount().getUsername(),
                customer.getAccount().getPassword(),
                customer.getEmail(),
                authorities
        );
    }

    public static UserPrincipal create(Staff staff,Map<String, Object> attributes, List<String> roles) {
        UserPrincipal userPrincipal = UserPrincipal.create(staff, roles);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    public static UserPrincipal create(Customer customer, Map<String, Object> attributes, List<String> roles) {
        UserPrincipal userPrincipal = UserPrincipal.create(customer, roles);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
