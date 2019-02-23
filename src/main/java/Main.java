import impls.*;

public class Main {
    public static void main(String[] args) {
        TUIImpl tui = new TUIImpl(new FunctionalityImpl(new PasswordValidatorImpl(),
                                                        new PasswordGeneratorImpl(),
                                                        new UserDAOImpl()));
        tui.execute();
    }
}
