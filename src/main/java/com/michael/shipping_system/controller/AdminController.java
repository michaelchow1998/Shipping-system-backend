package com.michael.shipping_system.controller;

import com.michael.shipping_system.model.Location;
import com.michael.shipping_system.model.Order;
import com.michael.shipping_system.model.User;
import com.michael.shipping_system.requestValid.RequestChangeLocation;
import com.michael.shipping_system.service.LocationService;
import com.michael.shipping_system.service.OrderService;
import com.michael.shipping_system.service.TrackingService;
import com.michael.shipping_system.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;



@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminController {

    @Autowired
    private  UserService userService;
    @Autowired
    private  TrackingService trackingService;
    @Autowired
    private  OrderService orderService;
    @Autowired
    private  LocationService locationService;

    //Admin ROLE: CREATE location
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/locations")
    public ResponseEntity <Location> addLocation(@RequestBody @Valid Location location) throws Exception{
        List<Location> locations = locationService.getAllLocation();
        boolean contain = false;
        for (Location l : locations){
            if (l.getName().equals(location.getName())) {
                contain = true;
                break;
            }
        }
        if(contain){
            throw new IllegalAccessException("Location name already used");
        }else {
            location = locationService.addLocation(location);
            return ResponseEntity.status(HttpStatus.CREATED).body(location);
        }
    }

    //Admin ROLE: READ User information
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("users/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username){
        User user = userService.getUser(username);
        log.info(user.getEmail());
        if (user.getId() != null){
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //Admin ROLE: READ all Users information
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> users = userService.getAllUser();
        if (users != null){
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //Admin ROLE: READ all Orders information
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrder(){
        List<Order> orders = orderService.getAllOrders();
        if (orders != null){
            return ResponseEntity.status(HttpStatus.OK).body(orders);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //Admin ROLE: READ Location by location id
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/locations/{locationId}")
    public ResponseEntity <Location> getLocation(@PathVariable Integer locationId){
        Location location = locationService.getLocationById(locationId);
        if (location.getName() != null){
            return ResponseEntity.status(HttpStatus.OK).body(location);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //Admin ROLE: READ all Location information
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/locations")
    public ResponseEntity <List<Location>> getAllLocation(){
        List<Location> locations = locationService.getAllLocation();
        if (locations != null){
            return ResponseEntity.status(HttpStatus.OK).body(locations);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    ////Admin ROLE: Update Location state by location id
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/locations/{locationId}")
    public ResponseEntity<Location> updateLocationState(
            @PathVariable Integer locationId,
            @RequestBody RequestChangeLocation req

    ){
        Location location = locationService.updateLocationState( locationId , req );
        log.info("{}",location.getName());
        return ResponseEntity.status(HttpStatus.OK).body(location);

    }



    //Admin ROLE: Delete User by username
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/users/{username}")
    public void deleteUser(
            @PathVariable String username){
        userService.deleteUser(username);
        log.info("{} deleted in user db", username);
    }

    //Admin ROLE: Delete Order by search id
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/orders/{searchId}")
    public void deleteOrder(
            @PathVariable String searchId){
        trackingService.deleteDetails(searchId);
        orderService.deleteOrder(searchId);
        log.info("{} deleted in order and tracking detail db", searchId);
    }

    //Admin ROLE: Delete Location by location id
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/locations/{locationId}")
    public void deleteLocation(

            @PathVariable Integer locationId){
        locationService.deleteLocation(locationService.getLocationById(locationId));
        log.info("{} deleted in locationId db", locationId);
    }

}
