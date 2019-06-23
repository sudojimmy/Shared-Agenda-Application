
package types;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Account
 * <p>
 * account object describes user information
 * 
 */
public class Account {

    /**
     * account id (globally unique)
     * (Required)
     * 
     */
    private String accountId;
    /**
     * account nickname entered by user (repeatable)
     * (Required)
     * 
     */
    private String nickname;
    /**
     * user description
     * 
     */
    private String description;
    /**
     * calendar Id
     * (Required)
     * 
     */
    private String calendarId;

    /**
     * account id (globally unique)
     * (Required)
     * 
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * account id (globally unique)
     * (Required)
     * 
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Account withAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    /**
     * account nickname entered by user (repeatable)
     * (Required)
     * 
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * account nickname entered by user (repeatable)
     * (Required)
     * 
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Account withNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    /**
     * user description
     * 
     */
    public String getDescription() {
        return description;
    }

    /**
     * user description
     * 
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public Account withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * calendar Id
     * (Required)
     * 
     */
    public String getCalendarId() {
        return calendarId;
    }

    /**
     * calendar Id
     * (Required)
     * 
     */
    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public Account withCalendarId(String calendarId) {
        this.calendarId = calendarId;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("accountId", accountId).append("nickname", nickname).append("description", description).append("calendarId", calendarId).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(nickname).append(accountId).append(description).append(calendarId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Account) == false) {
            return false;
        }
        Account rhs = ((Account) other);
        return new EqualsBuilder().append(nickname, rhs.nickname).append(accountId, rhs.accountId).append(description, rhs.description).append(calendarId, rhs.calendarId).isEquals();
    }

}
