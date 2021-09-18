package org.thebreak.roombooking.common.util;

import static org.junit.jupiter.api.Assertions.*;

class BookingUtilsTest {

    @org.junit.jupiter.api.Test
    void isEmail() {
        String email = "luka.bacic278@gmail.com";
        System.out.println(BookingUtils.isEmail(email));
    }
}