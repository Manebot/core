package io.manebot.user;

import java.util.Date;

public interface UserBan {

    /**
     * Gets the user that was banned.
     * @return banned user instance.
     */
    User getUser();

    /**
     * Gets the reason for the user being banned.
     * @return ban reason.
     */
    String getReason();

    /**
     * Gets the user that created this ban record, null if no reason was supplied.
     * @return ban reason.
     */
    User getBanningUser();

    /**
     * Gets the date that the ban was created.
     * @return Date instance.
     */
    Date getDate();

    /**
     * Gets the date that the ban ends, null if there is no end.
     * @return Ban end.
     */
    Date getEnd();

    /**
     * Finds if the ban has been pardoned.
     * @return true if the ban was pardoned, false otherwise.
     */
    boolean isPardoned();

    /**
     * Pardons the ban.
     */
    void pardon() throws SecurityException;

}
