
package types;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * GetAccountResponse
 * <p>
 * get account response
 * 
 */
public class GetAccountResponse {

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

    public GetAccountResponse withAccountId(String accountId) {
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

    public GetAccountResponse withNickname(String nickname) {
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

    public GetAccountResponse withDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("accountId", accountId).append("nickname", nickname).append("description", description).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(nickname).append(accountId).append(description).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof GetAccountResponse) == false) {
            return false;
        }
        GetAccountResponse rhs = ((GetAccountResponse) other);
        return new EqualsBuilder().append(nickname, rhs.nickname).append(accountId, rhs.accountId).append(description, rhs.description).isEquals();
    }

}
