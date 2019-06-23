
package types;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * GetAccountRequest
 * <p>
 * get account request
 * 
 */
public class GetAccountRequest {

    /**
     * account id (globally unique)
     * (Required)
     * 
     */
    private String accountId;

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

    public GetAccountRequest withAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("accountId", accountId).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(accountId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof GetAccountRequest) == false) {
            return false;
        }
        GetAccountRequest rhs = ((GetAccountRequest) other);
        return new EqualsBuilder().append(accountId, rhs.accountId).isEquals();
    }

}
