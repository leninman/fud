/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bod.securefactorbod.fud.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author lmanrique
 */
@Entity
public class Response implements Serializable {

    @Id
    private int responseCode;

    private String responseDescription;

    private Customermachine customermachine;
    
    public Response(){
        
    }

    public Response(int responseCode, String responseDescription, Customermachine customermachine) {
        this.responseCode = responseCode;
        this.responseDescription = responseDescription;
        this.customermachine = customermachine;
    }
    
   

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public Customermachine getCustomermachine() {
        return customermachine;
    }

    public void setCustomermachine(Customermachine customermachine) {
        this.customermachine = customermachine;
    }
    
    
    
    

}
