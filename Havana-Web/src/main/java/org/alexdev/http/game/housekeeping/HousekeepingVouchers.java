package org.alexdev.http.game.housekeeping;

public class HousekeepingVouchers {
    private String voucherCode;
    private String saleCode;
    private int credits;
    private String expiryDate;
    private boolean isSingleUse;
    private boolean allowNewUsers;

    public HousekeepingVouchers(String voucherCode, String saleCode, int credits, String expiryDate, boolean isSingleUse, boolean allowNewUsers) {
        this.voucherCode = voucherCode;
        this.saleCode = saleCode;
        this.credits = credits;
        this.expiryDate = expiryDate;
        this.isSingleUse = isSingleUse;
        this.allowNewUsers = allowNewUsers;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public String getSaleCode() {
        return saleCode;
    }

    public int getCredits() {
        return credits;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public boolean isSingleUse() {
        return isSingleUse;
    }

    public boolean isAllowNewUsers() {
        return allowNewUsers;
    }
}
