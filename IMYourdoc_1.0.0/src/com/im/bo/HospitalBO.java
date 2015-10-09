package com.im.bo;

public class HospitalBO {
	String id;
	String hospital_name;
	String city;
	
	public HospitalBO(String id,String hospital_name,String city){
		this.id = id;
		this.hospital_name = hospital_name;
		this.city = city;
	}
	@Override
	
	public String toString() {
		return hospital_name;	//"Id: " + id + ", Name: " + name + ", code: " + code;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHospital_name() {
		return hospital_name;
	}
	public void setHospital_name(String hospital_name) {
		this.hospital_name = hospital_name;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

}
