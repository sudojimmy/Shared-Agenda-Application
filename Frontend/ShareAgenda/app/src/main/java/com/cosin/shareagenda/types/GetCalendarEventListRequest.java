
package types;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * GetCalendarEventListRequest
 * <p>
 * get event list by calendar id
 * 
 */
public class GetCalendarEventListRequest {

    /**
     * calendar id
     * (Required)
     * 
     */
    private String calendarId;

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

    public GetCalendarEventListRequest withCalendarId(String calendarId) {
        this.calendarId = calendarId;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("calendarId", calendarId).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(calendarId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof GetCalendarEventListRequest) == false) {
            return false;
        }
        GetCalendarEventListRequest rhs = ((GetCalendarEventListRequest) other);
        return new EqualsBuilder().append(calendarId, rhs.calendarId).isEquals();
    }

}
