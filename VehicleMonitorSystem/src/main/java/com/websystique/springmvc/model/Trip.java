package com.websystique.springmvc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "TRIPS")
public class Trip implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotEmpty
	@Column(name = "TRIPID", unique = true, nullable = false)
	private String tripid;
	
	@Column(name = "TRIPTIME")
	private String triptime;
	
	@Column(name = "FOLLOWUP")
	private String followup;
	
	@Column(name = "TRIPFROM")
	private String tripfrom;
	
	@Column(name="TRIPDATE", nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date tripdate;
	
	@Column(name = "TRIPTO")
	private String tripto;
	
	@Column(name="BOOKINGS")
	private String bookings;
	
	@Column(name="TRIPTYPE")
	private String triptype;
	
	@Column(name="CUSTOMERNAME")
	private String  customername;
	
	@Column(name="CUSTOMERPHONE")
	private String  customerphone;
	
	@Column(name="PICKLOCATION")
	private String pickup;
	
	@Column(name="DROPLOCATION")
	private String drop;
	
	@Column(name="TRIPDRIVER")
	private String tripdriver;
	
	@Column(name="TRIPVEHICLE")
	private String tripvehicle;
	
	@Column(name="DRIVERPHONE")
	private String driverphone;
	
	@Column(name="OPENKM")
	private double openkm;
	
	@Column(name="CLOSEKM")
	private double closekm;
	
	@Column(name="AMOUNT")
	private double amount;
	
	@Column(name="COMMENTS")
	private String comments;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTripid() {
		return tripid;
	}

	public void setTripid(String tripid) {
		this.tripid = tripid;
	}

	public String getTripto() {
		return tripto;
	}

	public void setTripto(String tripto) {
		this.tripto = tripto;
	}
	
	

	public String getTripfrom() {
		return tripfrom;
	}

	public void setTripfrom(String tripfrom) {
		this.tripfrom = tripfrom;
	}

	public String getBookings() {
		return bookings;
	}

	public void setBookings(String bookings) {
		this.bookings = bookings;
	}

	public String getTriptype() {
		return triptype;
	}

	public void setTriptype(String triptype) {
		this.triptype = triptype;
	}

	public String getCustomername() {
		return customername;
	}

	public String getFollowup() {
		return followup;
	}

	public void setFollowup(String followup) {
		this.followup = followup;
	}

	public String getTriptime() {
		return triptime;
	}

	public void setTriptime(String triptime) {
		this.triptime = triptime;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getCustomerphone() {
		return customerphone;
	}

	public Date getTripdate() {
		return tripdate;
	}

	public void setTripdate(Date tripdate) {
		this.tripdate = tripdate;
	}

	public void setCustomerphone(String customerphone) {
		this.customerphone = customerphone;
	}

	public String getPickup() {
		return pickup;
	}

	public void setPickup(String pickup) {
		this.pickup = pickup;
	}

	public String getDrop() {
		return drop;
	}

	public void setDrop(String drop) {
		this.drop = drop;
	}

	public String getTripdriver() {
		return tripdriver;
	}

	public void setTripdriver(String tripdriver) {
		this.tripdriver = tripdriver;
	}

	public String getTripvehicle() {
		return tripvehicle;
	}

	public void setTripvehicle(String tripvehicle) {
		this.tripvehicle = tripvehicle;
	}

	public String getDriverphone() {
		return driverphone;
	}

	public void setDriverphone(String driverphone) {
		this.driverphone = driverphone;
	}

	public double getOpenkm() {
		return openkm;
	}

	public void setOpenkm(double openkm) {
		this.openkm = openkm;
	}

	public double getClosekm() {
		return closekm;
	}

	public void setClosekm(double closekm) {
		this.closekm = closekm;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "Trip [id=" + id + ", tripid=" + tripid + ", tripfrom=" + tripfrom + ", tripto=" + tripto + 
				", bookings="+bookings+", triptype="+triptype+", customername="+customername+", pickup="+pickup+
				", drop="+drop+", tripdriver="+tripdriver+", tripvehicle="+tripvehicle+", driverphone="+driverphone+
				", openkm="+openkm+", closekm="+closekm+", amount="+amount+", comments="+comments+
				"]";
	}
}