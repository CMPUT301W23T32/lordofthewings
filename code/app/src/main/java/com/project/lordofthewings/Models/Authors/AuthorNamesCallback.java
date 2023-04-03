package com.project.lordofthewings.Models.Authors;

import java.util.List;
import java.util.Map;

/**
 * Interface to handle the callback functions for the Authors of a QRCode Object
 */
public interface AuthorNamesCallback {

    /**
     * Method to handle the callback for the Authors received from the database
     *
     */
    void onAuthorNamesReceived();

    /**
     * Method to handle the callback for Author verification from the database
     *
     */
    void checkQRCodeOwner();
}
