/*
 * UserInputFormat.java in Presentation Logic Layer
 */
package vendingmachine;

public class UserInputFormat {

    // Common commands
    static String exit = "exit";
    static String help = "help [verbose]";
    static String register = "register <Username> <Password> <Role>";
    static String login = "login <Username> <Password>";
    static String logout = "logout";

    // Buyer commands
    static String list_products = "list products";
    static String buy_cash = "buy <ProductId>*<Quantity> [<P>*<Q> [...]] cash <Denomination>*<Quantity> [<D>*<Q> [...]] [cancel]";
    static String buy_card = "buy <ProductId>*<Quantity> [<P>*<Q> [...]] card <CardHolder> <CardNumber> [cancel]";
    static String cancel = "cancel";

    // Seller commands
    static String list_products_available = "list products available";
    static String list_products_sold = "list products sold";
    static String add_product = "add <ProductId> <ProductName> <Category> <Quantity> <Price>";
    static String del_product = "del <ProductId>";

    // Cashier commands
    static String list_change = "list change";
    static String list_transactions_done = "list transactions done";
    static String update_change = "update <Denomination> <Quantity>";

    // Owner commands
    static String list_accounts = "list accounts";
    static String list_transactions_cancelled = "list transactions cancelled";
    static String add_account = "add account <Username> <Password> <Role>";
    static String del_account = "del account <Username> <Password> <Role>";

    // Eech element's regular expressions
    static String Username = "[A-Za-z]+";
    static String Password = "[A-Za-z0-9]+";
    static String Role = "(buyer|seller|cashier|owner)";
    static String ProductId = "[a-z]{1,3}";
    static String ProductName = "[A-Za-z]+";
    static String Category = "[A-Za-z]+";
    static String Quantity = "[0-9]+";
    static String Price = "[0-9]+\\.[0-9]{2}";
    static String Denomination = "(\\$100|\\$50|\\$20|\\$10|\\$5|\\$2|\\$1|50c|20c|10c|5c)";
    static String Dollors = "(\\$100|\\$50|\\$20|\\$10|\\$5|\\$2|\\$1)";
    static String Cents = "(50c|20c|10c|5c)";
    static String CardHolder = "[A-Z][a-z]+";
    static String CardNumber = "[0-9]{5}";
}
