public class Customer {
    private int id;
    private String fullName;

    public Customer(int id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public Customer(String fullName) {
        this.fullName = fullName;
    }

    public int getId() {
        return this.id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFullName(String FullName) {
        this.fullName = fullName;
    }
}