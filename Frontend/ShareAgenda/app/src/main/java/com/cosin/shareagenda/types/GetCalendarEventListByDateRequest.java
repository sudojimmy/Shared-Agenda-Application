
package types;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * GetCalendarEventListByDateRequest
 * <p>
 * get event list by calendar id and date
 * 
 */
public class GetCalendarEventListByDateRequest {

    /**
     * calendar id
     * (Required)
     * 
     */
    private String calendarId;
    /**
     * the date of the events
     * (Required)
     * 
     */
    private String date;

    /**
     * calendar id
     * (Required)
     * 
     */
    public String getCalendarId() {
        return calendarId;
    }

    /**
     * calendar id
     * (Required)
     * 
     */
    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public GetCalendarEventListByDateRequest withCalendarId(String calendarId) {
        this.calendarId = calendarId;
        return this;
    }

    /**
     * the date of the events
     * (Required)
     * 
     */
    public String getDate() {
        return date;
    }

    /**
     * the date of the events
     * (Required)
     * 
     */
    public void setDate(String date) {
        this.date = date;
    }

    public GetCalendarEventListByDateRequest withDate(String date) {
        this.date = date;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("calendarId", calendarId).append("date", date).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(calendarId).append(date).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof GetCalendarEventListByDateRequest) == false) {
            return false;
        }
        GetCalendarEventListByDateRequest rhs = ((GetCalendarEventListByDateRequest) other);
        return new EqualsBuilder().append(calendarId, rhs.calendarId).append(date, rhs.date).isEquals();
    }

}
