package com.timeco.application.model.user;

import com.timeco.application.model.cart.Cart;
import com.timeco.application.model.order.Coupon;
import com.timeco.application.model.order.Wallet;
import com.timeco.application.model.role.Role;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name =  "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    private String phoneNumber;

    private String password;

    private boolean isBlocked;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))

    private Collection<Role> roles;


    @OneToOne(mappedBy = "user" , cascade= CascadeType.ALL)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> address = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_coupons",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id"))
    private Set<Coupon> coupons = new HashSet<>();


    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Wallet wallet;
    public User() {

    }

    public User(Long id, String firstName, String lastName, String email, String phoneNumber, String password, boolean isBlocked, Collection<Role> roles, Cart cart, List<Address> address, Set<Coupon> coupons, Wallet wallet) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.isBlocked = isBlocked;
        this.roles = roles;
        this.cart = cart;
        this.address = address;
        this.coupons = coupons;
        this.wallet = wallet;
    }
    public User(String firstName, String lastName, String email, String phoneNumber, String password, Collection<Role> roles) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber=phoneNumber;
        this.password = password;
        this.roles = roles;


    }

//    public User(String firstName, String lastName, String email, String phoneNumber, String password, boolean isBlocked, Collection<Role> roles, Cart cart,List<Address> address) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.phoneNumber = phoneNumber;
//        this.password = password;
//        this.isBlocked = isBlocked;
//        this.roles = roles;
//        this.cart = cart;
//        this.address=address;
//    }

    public User(Set<Coupon> coupons) {
        this.coupons = coupons;
    }

//    public <T> User(String firstName, String lastName, String email, String phoneNumber, String encode, List<T> roleUser) {
//    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Collection<Role> getRoles() {
        return roles;
    }
    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

//    public User(Long id, String firstName, String lastName, String email, String phoneNumber, String password, boolean isBlocked, Collection<Role> roles) {
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.phoneNumber = phoneNumber;
//        this.password = password;
//        this.isBlocked = isBlocked;
//        this.roles = roles;
//
//    }


    public User(String firstName, String lastName, String email, String phoneNumber, String password, boolean isBlocked, Collection<Role> roles, Cart cart, List<Address> address, Set<Coupon> coupons) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.isBlocked = isBlocked;
        this.roles = roles;
        this.cart = cart;
        this.address = address;
        this.coupons = coupons;
    }

    public Set<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(Set<Coupon> coupons) {
        this.coupons = coupons;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}