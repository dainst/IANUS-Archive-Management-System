package de.ianus.metadata.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="Address")
public class Address extends MDEntry{

	@Column(name="entityId")
	private Long entityId; // the id of a institution (extension of a MetadataEntity)
	
	@Column(name="street")
	private String street;
	
	@Column(name="zipCode")
	private String zipCode;
	
	@Column(name="city")
	private String city;
	
	@Column(name="country")
	private String country;

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
