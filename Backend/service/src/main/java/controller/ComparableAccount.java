package controller;
import types.Account;



public class ComparableAccount implements ComparableAccountInterface {
    Account account;
    int rank;

    public ComparableAccount(Account passAccount, int passRank){
        this.account = passAccount;
        this.rank = passRank;
    }

    @Override
    public int compareTo(ComparableAccount comparableAccount) {
        int compareRank = comparableAccount.rank;

        return this.rank - compareRank;
    }

}