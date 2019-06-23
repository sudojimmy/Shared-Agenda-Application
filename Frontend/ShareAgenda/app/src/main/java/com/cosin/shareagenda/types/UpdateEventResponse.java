
package types;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * UpdateEventResponse
 * <p>
 * update event response
 * 
 */
public class UpdateEventResponse {

    /**
     * eventId is unique, provided by frontend
     * (Required)
     * 
     */
    private String eventId;

    /**
     * eventId is unique, provided by frontend
     * (Required)
     * 
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * eventId is unique, provided by frontend
     * (Required)
     * 
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public UpdateEventResponse withEventId(String eventId) {
        this.eventId = eventId;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("eventId", eventId).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(eventId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof UpdateEventResponse) == false) {
            return false;
        }
        UpdateEventResponse rhs = ((UpdateEventResponse) other);
        return new EqualsBuilder().append(eventId, rhs.eventId).isEquals();
    }

}
