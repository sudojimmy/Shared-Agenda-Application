
package types;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * UpdateAccountResponse
 * <p>
 * update account response
 * 
 */
public class UpdateAccountResponse {

    /**
     * account id
     * (Required)
     * 
     */
    private String accountId;

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

    public UpdateAccountResponse withAccountId(String accountId) {
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
        if ((other instanceof UpdateAccountResponse) == false) {
            return false;
        }
        UpdateAccountResponse rhs = ((UpdateAccountResponse) other);
        return new EqualsBuilder().append(accountId, rhs.accountId).isEquals();
    }

}
