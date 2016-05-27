package com.cotech.taxislibres.activities;

public class RowItem {

	 private String member_name;
	 private int profile_pic_id;
	 private String status;
	 private String IdTarjeta;
	 

	 public RowItem(String member_name, int profile_pic_id, String status, String idtarjeta) {

	  this.member_name = member_name;
	  this.profile_pic_id = profile_pic_id;
	  this.status = status;
	  this.IdTarjeta = idtarjeta;
	 }

	 public String getMember_name() {
	  return member_name;
	 }

	 public void setMember_name(String member_name) {
	  this.member_name = member_name;
	 }

	 public int getProfile_pic_id() {
	  return profile_pic_id;
	 }

	 public void setProfile_pic_id(int profile_pic_id) {
	  this.profile_pic_id = profile_pic_id;
	 }

	 public String getStatus() {
	  return status;
	 }

	 public void setStatus(String status) {
	  this.status = status;
	 }

	 public String getIdTarjeta() {
	  return IdTarjeta;
	 }

	 public void setIdTarjeta(String IdTarjeta) {
	  this.IdTarjeta = IdTarjeta;
	 }

	

}
