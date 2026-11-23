package model;

// An interface is a list of methods that a class PROMISES to have.
// It has no code inside the methods, only their names.
// Any account that earns interest must have these two methods.
public interface InterestBearing {

    // works out how much interest the account earns this month
    double calculateInterest();

    // adds the interest to the balance
    void payInterest();
}