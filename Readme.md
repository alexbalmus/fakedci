# Fake DCI

### A compromise that aims to keep the Use Case orientation of DCI when OOP is de-emphasized
If you are new to Data-Context-Interaction:

* [Official DCI documentation](https://fulloo.info/)
* [Official DCI Article](https://fulloo.info/Documents/ArtimaDCI.html)
* [Unofficial example in Java that aims to be closer to the goals of DCI](https://github.com/alexbalmus/dci_java_playground)

### A bit of context
Unfortunately, in recent years we've seen a tendency of de-emphasis (or sometimes even bashing) of OOP.
This is true at least in the area of web/enterprise applications. While there's more than one cause for the phenomenon,
the misuse of OOP over time (which DCI aims to fix) has certainly had its influence. 
[This other example](https://github.com/alexbalmus/dci_java_playground) aims to provide a Java implementation
that's closer to the DCI philosophy, however it may be met with resistance.
In case of Java web apps, it's quit common to see the approach of having simple ("anemic") JPA entities, with the 
business logic being housed in stateless service methods. This becomes especially problematic when the services are
entity-aligned (rather than use case aligned), leading to difficulties identifying functionality implementation and
service classes that become ever-so-large (because the same entity may be used in several use cases).
In such situations, we may still be able to keep some of the benefits of DCI - namely the use case orientation - even though 
we loose the object enrichment capabilities.

### Approach taken
First and foremost, we need to let go of entity-aligned (large) services and switch the focus on use cases. 
Therefore, we will have miniature (I won't use the word "micro") domain services that only contain the business logic 
associated with an entity in a given use case. The Spring framework is most commonly encountered in Java web apps
and so the @Service annotation is usually used. In this example however, I propose defining new @Component based stereotypes
that clearly express the relation to DCI concepts. For example:

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Component
    public @interface DciRole
    {
        @AliasFor(
            annotation = Component.class
        )
        String value() default "";
    }
https://github.com/alexbalmus/fakedci/blob/master/src/main/java/com/alexbalmus/fakedci/dcibankaccounts/stereotypes/DciRole.java
    
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Component
    public @interface DciContext
    {
        @AliasFor(
            annotation = Component.class
        )
        String value() default "";
    }
https://github.com/alexbalmus/fakedci/blob/master/src/main/java/com/alexbalmus/fakedci/dcibankaccounts/stereotypes/DciContext.java

Now, assuming we have an entity of type Account (or a subclass), we will annotate the "role" miniature domain services 
and define them like so:

    @DciRole
    public class Account_DestinationRole<A extends Account>
    {
        public void receive(A destination, final Double amount)
        {
            destination.increaseBalanceBy(amount);
        }
    }
https://github.com/alexbalmus/fakedci/blob/master/src/main/java/com/alexbalmus/fakedci/dcibankaccounts/usecases/moneytransfer/Account_DestinationRole.java

    @DciRole
    @RequiredArgsConstructor
    public class Account_SourceRole<A extends Account>
    {
        private final Account_DestinationRole<A> accountDestinationRole;
        String INSUFFICIENT_FUNDS = "Insufficient funds.";
    
        void transfer(final A source, final A destination, final Double amount)
        {
            if (source.getBalance() < amount)
            {
                throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
            }
            source.decreaseBalanceBy(amount);
            accountDestinationRole.receive(destination, amount);
        }
    }
https://github.com/alexbalmus/fakedci/blob/master/src/main/java/com/alexbalmus/fakedci/dcibankaccounts/usecases/moneytransfer/Account_SourceRole.java

These "roles" will be played by Account (or subtype) entities inside the MoneyTransferContext (use case):

    @DciContext
    @RequiredArgsConstructor
    public class MoneyTransferContext<A extends Account>
    {
        private final Account_SourceRole<A> accountSourceRole;
    
        public void execute(final A source, final A destination, final Double amount)
        {
            accountSourceRole.transfer(source, destination, amount);
        }
    }
https://github.com/alexbalmus/fakedci/blob/master/src/main/java/com/alexbalmus/fakedci/dcibankaccounts/usecases/moneytransfer/MoneyTransferContext.java

As mentioned, this approach is to be viewed as a compromise that looses some benefits of both DCI and OOP,
and should only be used in situations where the more [DCI-savvy approach](https://github.com/alexbalmus/dci_java_playground) is met with resistance.