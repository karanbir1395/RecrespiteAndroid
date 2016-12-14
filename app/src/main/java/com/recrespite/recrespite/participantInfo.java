    package com.recrespite.recrespite;

    import java.util.ArrayList;
    import java.util.HashMap;

    /**
     * Created by Navpreet on 2016-11-16.
     * this class gets and sets the user information everytime user signs in
     */

    public class participantInfo {
        private static String username,phone,email,eventName,location,eventId,userFirstName,userLastName,diagnosis,gender,programOfInterest,notes,region;
        private  static ArrayList<HashMap<String,String>>participant = new ArrayList<>();
        private  static ArrayList<HashMap<String,String>>participantAge=new ArrayList<>();
        public String getUsername(){
            return  this.username;
        }
        public void setUsername(String username){
            this.username=username;
        }
        public String getRegion(){
            return  this.region;
        }
        public void setRegion(String region){
            this.region=region;
        }
        public String getPhone(){
            return  this.phone;
        }
        public void setPhone(String phone){
            this.phone=phone;
        }
        public  String getEmail(){
        return this.email;
    }
        public void setEmail(String email){
            this.email=email;
        }
        public  String getLocation(){
            return  this.location;
        }
        public void setLocation(String location){
            this.location=location;
        }
        public ArrayList<HashMap<String,String>> getParticipant(){
           return this.participant;
        }
        public  void setParticipant(HashMap<String,String>participant){
           this.participant.add(participant);
        }
        public ArrayList<HashMap<String,String>>getParticipantAge(){
            return this.participantAge;
        }
        public  void setParticipantAge(HashMap<String,String>participantAge){
            this.participantAge.add(participantAge);
        }
        public  String getEventName(){
            return  this.eventName;
        }
        public void setEventName(String eventName){
            this.eventName=eventName;
        }
        public  String getEventId(){
            return  this.eventId;
        }
        public void setEventId(String eventId){
            this.eventId=eventId;
        }
        public  void deleteParticipant(Object o){
            participant.remove(o);

        }
       public  void deleteParticipantAge(Object o){
        participantAge.remove(o);
    }
        public  String getUserFirstName(){
            return  this.userFirstName;
        }
        public void setUserFirstName(String userFirstName){
            this.userFirstName=userFirstName;
        }
        public  String getUserLastName(){
            return  this.userLastName;
        }
        public void setUserLastName(String userLastName){
            this.userLastName=userLastName;
        }

        public  String getDiagnosis(){
            return  this.diagnosis;
        }
        public void setDiagnosis(String Diagnosis){
            this.diagnosis=diagnosis;
        }
        public  String getGender(){
            return  this.gender;
        }
        public void setGender(String gender){
            this.gender=gender;
        }
        public  String getProgramOfInterest(){
            return  this.programOfInterest;
        }
        public void setProgramOfInterest(String programOfInterest){
            this.programOfInterest=programOfInterest;
        }
        public  String getNotes(){
            return  this.notes;
        }
        public void setNotes(String notes){
            this.notes=notes;
        }
    }
