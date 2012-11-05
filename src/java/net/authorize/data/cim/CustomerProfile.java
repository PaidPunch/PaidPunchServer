package net.authorize.data.cim;

import java.util.ArrayList;

import net.authorize.data.xml.Address;

public class CustomerProfile {

   String merchantCustomerId;
     String description;
     String email;
     String customerProfileId;
     ArrayList<Address> shipToAddressList = new ArrayList<Address>();

    private CustomerProfile() { }

    /**
     * Create a new CustomerProfile object
     *
     * @return CustomerProfile
     */
    public static CustomerProfile createCustomerProfile() {
    	return new CustomerProfile();
    }

    /**
     * Get the customer profile id.
     *
	 * @return the id
	 */
	public String getCustomerProfileId() {
		return customerProfileId;
	}

	/**
	 * Set the customer profile id.
	 *
	 * @param id the id to set
	 */
	public void setCustomerProfileId(String id) {
           // System.out.println("Profile id "+id);
		this.customerProfileId = id;
	}

	/**
     * Gets the value of the merchantCustomerId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMerchantCustomerId() {
        return merchantCustomerId;
    }

    /**
     * Sets the value of the merchantCustomerId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMerchantCustomerId(String value) {
      //  System.out.println("Merchant Customer id "+value);
        this.merchantCustomerId = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDescription(String value) {
       // System.out.println("Profile Description "+value);
        this.description = value;
    }

    /**
     * Gets the value of the email property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEmail(String value) {
      //  System.out.println("Profile email "+value);
        this.email = value;
    }

	/**
	 * @return the shipToAddressList
	 */
	public ArrayList<Address> getShipToAddressList() {
		return shipToAddressList;
	}

	/**
	 * @param shipToAddressList the shipToAddressList to set
	 */
	public void setShipToAddressList(ArrayList<Address> shipToAddressList) {
		this.shipToAddressList = shipToAddressList;
	}

	/**
	 * Add an address to the ship to list.
	 *
	 * @param shipTo
	 */
	public void addShipToAddress(Address shipTo) {
		if(this.shipToAddressList == null) {
			this.shipToAddressList = new ArrayList<Address>();
		}

		this.shipToAddressList.add(shipTo);
	}

	/**
	 * Return the first (perhaps only) address in the list.
	 *
	 * @return Address
	 */
	public Address getShipToAddress() {

		Address retval = null;
		if(this.shipToAddressList != null &&
				this.shipToAddressList.size() > 0) {

			retval = this.shipToAddressList.get(0);
		}

		return retval;
	}
}
