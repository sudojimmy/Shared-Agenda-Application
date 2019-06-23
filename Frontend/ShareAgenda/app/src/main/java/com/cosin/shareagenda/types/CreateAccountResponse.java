
package types;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * CreateAccountRequest
 * <p>
 * create account response
 * 
 */
public class CreateAccountResponse {

    /**
     * account id
     * (Required)
     * 
     */
    private String accountId;
    /**
     * calendar id
     * (Required)
     * 
     */
    private String calendarId;

    /**
     * account id
     * (Required)
     * 
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * account id
     * (Required)
     * 
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public CreateAccountResponse withAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

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

    public CreateAccountResponse withCalendarId(String calendarId) {
        this.calendarId = calendarId;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("accountId", accountId).append("calendarId", calendarId).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(calendarId).append(accountId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CreateAccountResponse) == false) {
            return false;
        }
        CreateAccountResponse rhs = ((CreateAccountResponse) other);
        return new EqualsBuilder().append(calendarId, rhs.calendarId).append(accountId, rhs.accountId).isEquals();
    }

}
