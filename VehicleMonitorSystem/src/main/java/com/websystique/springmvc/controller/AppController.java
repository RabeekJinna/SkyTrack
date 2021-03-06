package com.websystique.springmvc.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.websystique.springmvc.model.Driver;
import com.websystique.springmvc.model.Payment;
import com.websystique.springmvc.model.Trip;
import com.websystique.springmvc.model.User;
import com.websystique.springmvc.model.UserProfile;
import com.websystique.springmvc.model.Vehicle;
import com.websystique.springmvc.service.DriverService;
import com.websystique.springmvc.service.PaymentService;
import com.websystique.springmvc.service.TripService;
import com.websystique.springmvc.service.UserProfileService;
import com.websystique.springmvc.service.UserService;
import com.websystique.springmvc.service.VehicleService;


@Controller
@RequestMapping("/")
@SessionAttributes("roles")
public class AppController {

	@Autowired
	HttpSession session;
	
	@Autowired
	UserService userService;
	
	@Autowired
	DriverService driverService;
	
	@Autowired
	TripService tripService;
	
	@Autowired
	VehicleService vehicleService;
	
	@Autowired
	UserProfileService userProfileService;
	
	@Autowired
	PaymentService paymentService;
	
	@Autowired
	MessageSource messageSource;

	@Autowired
	PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;
	
	@Autowired
	AuthenticationTrustResolver authenticationTrustResolver;
	
	
	/**
	 * This method will list all existing users.
	 */
	@RequestMapping(value = { "/user", "/list" }, method = RequestMethod.GET)
	public String listUsers(ModelMap model) {

		List<User> users = userService.findAllUsers();
		model.addAttribute("users", users);
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("search", true);
		model.addAttribute("menu", "Users");
		return "userslist";
	}
	
	@RequestMapping(value = { "/triplist" }, method = RequestMethod.GET)
	public String listTrips(ModelMap model) {
				
		List<Trip> trips = tripService.findAllTrips();
		model.addAttribute("trips", trips);
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("search", true);
		model.addAttribute("tab", "Track");
		model.addAttribute("menu", "Trips");
		return "trips";
				
	}
	
	@RequestMapping(value = { "/paymentlist" }, method = RequestMethod.GET)
	public String listPayments(ModelMap model) {
				
		model.addAttribute("tab", "Track");
		model.addAttribute("page", "payment");
		List<Payment> payments = paymentService.findAllPayments();
		model.addAttribute("payments", payments);
		model.addAttribute("loggedinuser", getPrincipal());
		model.addAttribute("search", true);
		model.addAttribute("menu", "Payments");
		return "payment";
				
	}
	
	
	
	@RequestMapping(value = { "/","/index" }, method = RequestMethod.GET)
	public String indexPage(ModelMap model) {
		
		List<User> users = userService.findAllUsers();
		userService.findAllUsers();
		model.addAttribute("users1", users);
		model.addAttribute("users", users);
		model.addAttribute("tab", "dashboard");
		model.addAttribute("loggedinuser", getPrincipal());
		return "index";
	    
	}
	
	

	/**
	 * This method will provide the medium to add a new user.
	 */
	@RequestMapping(value = { "/newuser" }, method = RequestMethod.GET)
	public String newUser(ModelMap model) {
		
		List<User> users = userService.findAllUsers();
		model.addAttribute("users", users);
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("create", true);
		model.addAttribute("menu", "Users");
		model.addAttribute("loggedinuser", getPrincipal());
		return "userslist";
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * saving user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/newuser" }, method = RequestMethod.POST)
	public String saveUser(@Valid User user, BindingResult result,
			ModelMap model) {

		if (result.hasErrors()) {
			return "userslist";
		}

		/*
		 * Preferred way to achieve uniqueness of field [sso] should be implementing custom @Unique annotation 
		 * and applying it on field [sso] of Model class [User].
		 * 
		 * Below mentioned peace of code [if block] is to demonstrate that you can fill custom errors outside the validation
		 * framework as well while still using internationalized messages.
		 * 
		 */
		if(!userService.isUserSSOUnique(user.getId(), user.getSsoId())){
			FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
		    result.addError(ssoError);
			return "userslist";
		}
		
		userService.saveUser(user);

		model.addAttribute("success", "User " + user.getFirstName() + " "+ user.getLastName() + " saved successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		List<User> users = userService.findAllUsers();
		model.addAttribute("users", users);
		model.addAttribute("menu", "Users");
		model.addAttribute("search", true);
		//return "success";
		return "userslist";
	}
	
	
	@RequestMapping(value = {"/newVehicle"},method = RequestMethod.POST)
	public String addVehicle(@Valid Vehicle vehicle, BindingResult result,
			ModelMap model) {
	
		if(result.hasErrors()) {
			return "vehicle";
		}else {
			vehicleService.saveDriver(vehicle);
			model.addAttribute("success", "Vehicle Model " + vehicle.getModel() + " saved successfully");
			model.addAttribute("loggedinuser" , getPrincipal());
			List<Vehicle> vehicleList = vehicleService.findAllVehicles();
			model.addAttribute("vehicle", vehicleList);
			model.addAttribute("menu", "Vehicles");
			model.addAttribute("search", true);
			return "vehicle";
		}
	}
	
	@RequestMapping(value = {"/edit-vehicle-{id}"}, method = RequestMethod.GET)
	public String editVehicle(@PathVariable String id, ModelMap model) {
		int vehicleId = Integer.parseInt(id);
		Vehicle vehicleIDValue = vehicleService.findByVehicleID(vehicleId);
		List<Vehicle> vehicles = vehicleService.findAllVehicles();
		model.addAttribute("vehicle", vehicleIDValue);
		model.addAttribute("vehicles", vehicles);
		model.addAttribute("menu", "Vehicles");
		model.addAttribute("edit", true);
		model.addAttribute("loggedinuser", getPrincipal());
		
		return "vehicle";
	}
	
	@RequestMapping (value = {"/edit-vehicle-{id}"}, method = RequestMethod.POST)
	public String updateVehicle(@Valid Vehicle vehicles, BindingResult result,
			ModelMap model, @PathVariable String id) {
		
		if (result.hasErrors()) {
			return "vehicle";
		}else {
			vehicleService.updateVehicle(vehicles);
			model.addAttribute("success", "Driver " + vehicles.getModel() + " updated successfully");
			model.addAttribute("loggedinuser", getPrincipal());
			List<Vehicle> vehicle = vehicleService.findAllVehicles();
			model.addAttribute("vehicle", vehicle);
			model.addAttribute("menu", "Vehicles");
			model.addAttribute("search", true);
			return "vehicle";
		}
		
	}
	
	@RequestMapping(value = {"/newVehicle"}, method = RequestMethod.GET)
	public String newVehicle (ModelMap model, HttpSession session) {
		
		model.addAttribute("tab", "Vehicle");
		model.addAttribute("menu","Vechile");
		model.addAttribute("page", "vehicles");
		//List<Vehicle> vehicle = vehicleService.findAllVehicles();
		Vehicle vehicle = new Vehicle();
		model.addAttribute("vehicle", vehicle);
		model.addAttribute("create", true);
		model.addAttribute("menu", "Vehicles");
		model.addAttribute("loggedinuser", getPrincipal());
		return "vehicle";
	}
	
	/**
	 * This method will provide the medium to update an existing user.
	 */
	@RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.GET)
	public String editUser(@PathVariable String ssoId, ModelMap model) {
		User user = userService.findBySSO(ssoId);
		model.addAttribute("user", user);
		model.addAttribute("edit", true);
		List<User> users = userService.findAllUsers();
		model.addAttribute("users", users);
		model.addAttribute("menu", "Users");
		model.addAttribute("loggedinuser", getPrincipal());
		
		return "userslist";
	}
	
	/**
	 * This method will be called on form submission, handling POST request for
	 * updating user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.POST)
	public String updateUser(@Valid User user, BindingResult result,
			ModelMap model, @PathVariable String ssoId) {

		if (result.hasErrors()) {
			return "userslist";
		}

		/*//Uncomment below 'if block' if you WANT TO ALLOW UPDATING SSO_ID in UI which is a unique key to a User.
		if(!userService.isUserSSOUnique(user.getId(), user.getSsoId())){
			FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
		    result.addError(ssoError);
			return "registration";
		}*/


		userService.updateUser(user);

		model.addAttribute("success", "User " + user.getFirstName() + " "+ user.getLastName() + " updated successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		List<User> users = userService.findAllUsers();
		model.addAttribute("users", users);
		model.addAttribute("search", true);
		model.addAttribute("menu", "Users");
		return "userslist";
	}

	
	/**
	 * This method will delete an user by it's SSOID value.
	 */
	@RequestMapping(value = { "/delete-user-{ssoId}" }, method = RequestMethod.GET)
	public String deleteUser(@PathVariable String ssoId) {
		userService.deleteUserBySSO(ssoId);
		return "redirect:/list";
	}
	

	/**
	 * This method will provide UserProfile list to views
	 */
	@ModelAttribute("roles")
	public List<UserProfile> initializeProfiles() {
		return userProfileService.findAll();
	}
	
	@RequestMapping(value = { "/newdriver" }, method = RequestMethod.GET)
	public String newDriver(ModelMap model) {
		Driver driver = new Driver();
		model.addAttribute("driver", driver);
		model.addAttribute("create", true);
		model.addAttribute("menu", "Drivers");
		model.addAttribute("loggedinuser", getPrincipal());
		return "driver";
	}
	
	@RequestMapping(value = { "/newdriver" }, method = RequestMethod.POST)
	public String saveDriver(@Valid Driver driver, BindingResult result,
			ModelMap model) {

		if (result.hasErrors()) {
			return "driver";
		}

		/*
		 * Preferred way to achieve uniqueness of field [sso] should be implementing custom @Unique annotation 
		 * and applying it on field [sso] of Model class [User].
		 * 
		 * Below mentioned peace of code [if block] is to demonstrate that you can fill custom errors outside the validation
		 * framework as well while still using internationalized messages.
		 * 
		 */
		/*if(!userService.isUserSSOUnique(driver.getId(), user.getSsoId())){
			FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
		    result.addError(ssoError);
			return "userslist";
		}*/
		
		driverService.saveDriver(driver);

		model.addAttribute("success", "Driver " + driver.getFullName() + " saved successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		List<Driver> drivers = driverService.findAllDrivers();
		model.addAttribute("drivers", drivers);
		model.addAttribute("menu", "Drivers");
		model.addAttribute("search", true);
		//return "success";
		return "driver";
	}
	
	@RequestMapping(value = { "/edit-driver-{id}" }, method = RequestMethod.GET)
	public String editDriver(@PathVariable String id, ModelMap model) {
		
		int driverid = Integer.parseInt(id);
		Driver driver = driverService.findById(driverid);
		List<Driver> drivers = driverService.findAllDrivers();
		model.addAttribute("driver", driver);
		model.addAttribute("drivers", drivers);
		model.addAttribute("menu", "Drivers");
		model.addAttribute("edit", true);
		model.addAttribute("loggedinuser", getPrincipal());
		return "driver";
	}
	
	/**
	 * This method will be called on form submission, handling POST request for
	 * updating user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/edit-driver-{id}" }, method = RequestMethod.POST)
	public String updateDriver(@Valid Driver driver, BindingResult result,
			ModelMap model, @PathVariable String id) {

		if (result.hasErrors()) {
			return "driver";
		}

		/*//Uncomment below 'if block' if you WANT TO ALLOW UPDATING SSO_ID in UI which is a unique key to a User.
		if(!userService.isUserSSOUnique(user.getId(), user.getSsoId())){
			FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
		    result.addError(ssoError);
			return "registration";
		}*/


		driverService.updateDriver(driver);

		model.addAttribute("success", "Driver " + driver.getFullName()  + " updated successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		List<Driver> drivers = driverService.findAllDrivers();
		model.addAttribute("drivers", drivers);
		model.addAttribute("menu", "Drivers");
		model.addAttribute("search", true);
		return "driver";
	}
	
	
	@RequestMapping(value = { "/newpayment" }, method = RequestMethod.GET)
	public String newPayment(ModelMap model, HttpSession session) {
		
		model.addAttribute("tab", "Track");
		model.addAttribute("page", "payment");
		model.addAttribute("menu", "Payments");
	    Payment payment = new Payment();
		model.addAttribute("payment", payment);
		model.addAttribute("create", true);
		model.addAttribute("loggedinuser", getPrincipal());
	//	session.setMaxInactiveInterval(2);
		return "payment";
	}
	
	@RequestMapping(value = { "/newpayment" }, method = RequestMethod.POST)
	public String newPayment(@Valid Payment payment, BindingResult result,ModelMap model) {
		
		model.addAttribute("tab", "Track");
		model.addAttribute("page", "payment");
		
		paymentService.savePayment(payment);
		Payment pmt = paymentService.findByPaymentid(payment.getPaymentsid().toString());
	
		if(pmt.getTripid1() != null && pmt.getTripid1().length()>0) {
			Trip trip = tripService.findByTripid(pmt.getTripid1());
			tripService.updateTripDetail(trip);
			}
		if(pmt.getTripid2() != null && pmt.getTripid2().length()>0) {
			Trip trip = tripService.findByTripid(pmt.getTripid2());
			tripService.updateTripDetail(trip);
			}
		if(pmt.getTripid3() != null && pmt.getTripid3().length()>0) {
			Trip trip = tripService.findByTripid(pmt.getTripid3());
			tripService.updateTripDetail(trip);
			}
		if(pmt.getTripid4() != null && pmt.getTripid4().length()>0) {
			Trip trip = tripService.findByTripid(pmt.getTripid4());
			tripService.updateTripDetail(trip);
			}
		if(pmt.getTripid5() != null && pmt.getTripid5().length()>0) {
			Trip trip = tripService.findByTripid(pmt.getTripid5());
			tripService.updateTripDetail(trip);
			}
		if(pmt.getTripid6() != null && pmt.getTripid6().length()>0) {
			Trip trip = tripService.findByTripid(pmt.getTripid6());
			tripService.updateTripDetail(trip);
			}
		if(pmt.getTripid7() != null && pmt.getTripid7().length()>0) {
			Trip trip = tripService.findByTripid(pmt.getTripid7());
			tripService.updateTripDetail(trip);
			}
		if(pmt.getTripid8() != null && pmt.getTripid8().length()>0) {
			Trip trip = tripService.findByTripid(pmt.getTripid8());
			tripService.updateTripDetail(trip);
			}
		if(pmt.getTripid9() != null && pmt.getTripid9().length()>0) {
			Trip trip = tripService.findByTripid(pmt.getTripid9());
			tripService.updateTripDetail(trip);
			}
		if(pmt.getTripid10() != null && pmt.getTripid10().length()>0) {
			Trip trip = tripService.findByTripid(pmt.getTripid10());
			tripService.updateTripDetail(trip);
			}
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Or whatever IST is supposed to be
		formatter.format(pmt.getCreatedate());
		model.addAttribute("success", "Payment Amount paid successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		List<Payment> payments = paymentService.findAllPayments();
		model.addAttribute("payments", payments);
		model.addAttribute("menu", "Payments");
		model.addAttribute("search", true);
		return "payment";
	}
	
	@RequestMapping(value = { "/edit-payment-{paymentsid}" }, method = RequestMethod.GET)
	public String editPayment(@PathVariable String paymentsid, ModelMap model) {
		
		model.addAttribute("tab", "Track");
		model.addAttribute("page", "payment");
		Payment payment = paymentService.findByPaymentid(paymentsid);
		model.addAttribute("payment", payment);
		model.addAttribute("menu", "Payments");
		model.addAttribute("edit", true);
		model.addAttribute("loggedinuser", getPrincipal());
		
		return "payment";
	}
	
	@RequestMapping(value = { "/edit-payment-{paymentsid}" }, method = RequestMethod.POST)
	public String updatePayment(@Valid Payment payment, BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {
			return "payment";
		}

		model.addAttribute("tab", "Track");
		model.addAttribute("page", "payment");
		paymentService.updatePayment(payment);

		model.addAttribute("success", "Payment " + payment.getPaymentsid()  + " updated successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		List<Payment> payments = paymentService.findAllPayments();
		model.addAttribute("payments", payments);
		model.addAttribute("menu", "Payments");
		model.addAttribute("search", true);
		return "payment";
	}
	
	@RequestMapping(value = { "/browse-payment-{paymentsid}" }, method = RequestMethod.GET)
	public String browsePayment(@PathVariable String paymentsid, ModelMap model) {
		
		model.addAttribute("tab", "Track");
		model.addAttribute("page", "payment");
		Payment payment = paymentService.findByPaymentid(paymentsid);
		model.addAttribute("payment", payment);
		model.addAttribute("menu", "Payments");
		model.addAttribute("browse", true);
		model.addAttribute("loggedinuser", getPrincipal());
		
		return "payment";
	}
	
	@RequestMapping(value = { "/newtrip" }, method = RequestMethod.GET)
	public String newTrip(ModelMap model, HttpSession session) {
		
		model.addAttribute("tab", "Track");
		model.addAttribute("menu", "Trips");
		model.addAttribute("page", "trips");
		Trip trip = new Trip();
		trip.setTemptripid(tripService.findMaxOfTripid());
		model.addAttribute("trip", trip);
		model.addAttribute("create", true);
		model.addAttribute("loggedinuser", getPrincipal());
	//	session.setMaxInactiveInterval(2);
		return "trips";
	}
	
	@RequestMapping(value = { "/browse-trip-{id}" }, method = RequestMethod.GET)
	public String browseTrip(@PathVariable String id, ModelMap model) {
		
		int tripid= Integer.parseInt(id);
		Trip trip = tripService.findById(tripid);
		model.addAttribute("trip", trip);
		model.addAttribute("menu", "Trips");
		model.addAttribute("tab", "Track");
		model.addAttribute("browse", true);
		model.addAttribute("loggedinuser", getPrincipal());
		
		return "trips";
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = { "/browse-paymenttrip-{id}" }, method = RequestMethod.GET)
	public String browseTripPayment(@PathVariable String id, ModelMap model) {
		
		Payment payment = paymentService.findByTripid(id, "tripid1");
		if(payment == null) {
			payment = paymentService.findByTripid(id, "tripid2");
		}   if (payment == null) {
			payment = paymentService.findByTripid(id, "tripid3");
		}   if (payment == null) {
			payment = paymentService.findByTripid(id, "tripid4");
		}   if (payment == null) {
			payment = paymentService.findByTripid(id, "tripid5");
		}  if (payment == null) {
			payment = paymentService.findByTripid(id, "tripid6");
		}  if (payment == null) {
			payment = paymentService.findByTripid(id, "tripid7");
		}  if (payment == null) {
			payment = paymentService.findByTripid(id, "tripid8");
		}  if (payment == null) {
			payment = paymentService.findByTripid(id, "tripid9");
		}  if (payment == null) {
			payment = paymentService.findByTripid(id, "tripid10");
		} 
		if(payment == null) {
			List<Trip> trips = tripService.findAllTrips();
			model.addAttribute("warning", "Trip Amount is not paid for TRIPID "+id+". Please check it...");
			model.addAttribute("trips", trips);
			model.addAttribute("loggedinuser", getPrincipal());
			model.addAttribute("search", true);
			model.addAttribute("menu", "Trips");
			return "trips";
		} else {
		
		model.addAttribute("tab", "Track");
		model.addAttribute("page", "payment");
		model.addAttribute("payment", payment);
		model.addAttribute("menu", "Payments");
		model.addAttribute("browse", true);	
		}
		return "payment";
	}
	
	@RequestMapping(value = { "/edit-trip-{id}" }, method = RequestMethod.GET)
	public String editTrip(@PathVariable String id, ModelMap model) {
		
		int tripid= Integer.parseInt(id);
		Trip trip = tripService.findById(tripid);
		model.addAttribute("trip", trip);
		model.addAttribute("tab", "Track");
		model.addAttribute("menu", "Trips");
		model.addAttribute("edit", true);
		model.addAttribute("loggedinuser", getPrincipal());
		
		return "trips";
	}
	
	@RequestMapping(value = { "/edit-trip-{id}" }, method = RequestMethod.POST)
	public String updateTrip(@Valid Trip trip, BindingResult result,
			ModelMap model, @PathVariable String id) {

		if (result.hasErrors()) {
			return "trips";
		}

		/*//Uncomment below 'if block' if you WANT TO ALLOW UPDATING SSO_ID in UI which is a unique key to a User.
		if(!userService.isUserSSOUnique(user.getId(), user.getSsoId())){
			FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
		    result.addError(ssoError);
			return "registration";
		}*/


		tripService.updateTrip(trip);

		model.addAttribute("success", "Trip " + trip.getTripid() + " updated successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		trip = tripService.findById(trip.getId());
		model.addAttribute("trip", trip);
		model.addAttribute("tab", "Track");
		model.addAttribute("menu", "Trips");
		model.addAttribute("browse", true);
		model.addAttribute("loggedinuser", getPrincipal());
		return "trips";
	}
	
	@RequestMapping(value = { "/newtrip" }, method = RequestMethod.POST)
	public String saveTrip(@Valid Trip trip, BindingResult result,
			ModelMap model) {

		if (result.hasErrors()) {
			return "trips";
		}

		/*
		 * Preferred way to achieve uniqueness of field [sso] should be implementing custom @Unique annotation 
		 * and applying it on field [sso] of Model class [User].
		 * 
		 * Below mentioned peace of code [if block] is to demonstrate that you can fill custom errors outside the validation
		 * framework as well while still using internationalized messages.
		 * 
		 */
		/*if(!userService.isUserSSOUnique(user.getId(), user.getSsoId())){
			FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
		    result.addError(ssoError);
			return "userslist";
		}*/
		
		tripService.saveTrip(trip);

		model.addAttribute("success", "Trip " + trip.getTripid() + " saved successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		trip = tripService.findById(trip.getId());
		model.addAttribute("tab", "Track");
		model.addAttribute("trip", trip);
		model.addAttribute("menu", "Trips");
		model.addAttribute("browse", true);
		//return "success";
		return "trips";
	}
	
	/**
	 * This method handles Access-Denied redirect.
	 */
	@RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
	public String accessDeniedPage(ModelMap model) {
		model.addAttribute("loggedinuser", getPrincipal());
		return "accessDenied";
	}

	/**
	 * This method handles login GET requests.
	 * If users is already logged-in and tries to goto login page again, will be redirected to list page.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage() {
		if (isCurrentAuthenticationAnonymous()) {
			return "login";
	    } else {
	    	return "redirect:/list";  
	    }
	}

	/**
	 * This method handles logout requests.
	 * Toggle the handlers if you are RememberMe functionality is useless in your app.
	 */
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){    
			//new SecurityContextLogoutHandler().logout(request, response, auth);
			persistentTokenBasedRememberMeServices.logout(request, response, auth);
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		return "redirect:/login?logout";
	}
	
	
	@RequestMapping(value = { "/toptab-{tab}-{menu}-{page}" }, method = {RequestMethod.GET})
	public String navPage(ModelMap model, @PathVariable("tab") String tab, @PathVariable("menu") String menu,
			@PathVariable("page") String page) {
			
			model.addAttribute("loggedinuser", getPrincipal());
			if(tab != null && tab.length() > 0)
			model.addAttribute("tab", tab);
			if(menu != null && menu.length() > 0)
			model.addAttribute("menu", menu);
			if(page != null && page.length() > 0)
			model.addAttribute("page", page);
			
			if(page.equalsIgnoreCase("userslist")){
				List<User> users = userService.findAllUsers();
				model.addAttribute("users", users);
				model.addAttribute("search", true);
			}
			if(page.equalsIgnoreCase("driver")){
				List<Driver> drivers = driverService.findAllDrivers();
				model.addAttribute("drivers", drivers);
				model.addAttribute("search", true);
			}	if(page.equalsIgnoreCase("vehicle")){
			List<Vehicle> vehicles = vehicleService.findAllVehicles();
			model.addAttribute("vehicles", vehicles);
			model.addAttribute("search", true);
		    }
			if(page.equalsIgnoreCase("companies")){
				List<Driver> drivers = driverService.findAllDrivers();
				model.addAttribute("drivers", drivers);
				model.addAttribute("search", true);
			}
			if(page.equalsIgnoreCase("trips")){
				List<Trip> trips = tripService.findAllTrips();
				model.addAttribute("trips", trips);
				model.addAttribute("search", true);
			}
			if(page.equalsIgnoreCase("payment")){
				List<Payment> payments = paymentService.findAllPayments();
				model.addAttribute("payments", payments);
				model.addAttribute("search", true);
			}
			return page;	    
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ModelAttribute("loadDrivers")
	public List<Map> initializeDrivers() {
		List<Map> driverMaps = new ArrayList<Map>();
		List<Driver> drivers = driverService.findAllDrivers();
		for (Driver driver : drivers) {
			Map driverMap = new HashMap();
			driverMap.put("fullName", driver.getFullName());
			driverMaps.add(driverMap);
		}
		return driverMaps;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ModelAttribute("loadTrips")
	public List<Map> initializeTrips() {
		List<Map> tripMaps = new ArrayList<Map>();
		List<Trip> trips = tripService.findAllTrips();
		for (Trip trip : trips) {
			Map tripMap = new HashMap();
			tripMap.put("tripid", trip.getTripid());
			tripMaps.add(tripMap);
		}
		return tripMaps;
	}
	
	
	@ModelAttribute("loadVehicles")
	public List<Map> initializeVehicles() {
		List<Map> vehicleMaps = new ArrayList<Map>();
		List<Vehicle> vehicles = vehicleService.findAllVehicles();
		for (Vehicle vehicle : vehicles) {
			Map vehicleMap = new HashMap();
			vehicleMap.put("regNo", vehicle.getRegNo());
			vehicleMaps.add(vehicleMap);
		}
		return vehicleMaps;
	}
	
	@ModelAttribute("loadUsers")
	public List<User> initializeUsers() {
		
		List<User> users = userService.findAllUsers();
		return users;
	}
	
	
	 /**
	 * This method returns the principal[user-name] of logged-in user.
	 */
	private String getPrincipal(){
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails)principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}
	
	/**
	 * This method returns true if users is already authenticated [logged-in], else false.
	 */
	private boolean isCurrentAuthenticationAnonymous() {
	    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    return authenticationTrustResolver.isAnonymous(authentication);
	}
	
	


}