import java.util.Objects;

public final class Product {
    final String productName;
    final String units;
    double Quantity;

    public Product(String productName, double Quantity, String units) {
        this.productName = productName;
        this.Quantity = Quantity;
        this.units = units;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Product) obj;
        return Objects.equals(this.productName, that.productName) && Double.doubleToLongBits(this.Quantity) == Double.doubleToLongBits(that.Quantity) && Objects.equals(this.units, that.units);
    }

    @Override
    public String toString() {
        return "Product[" + "productName=" + productName + ", " + "Quantity=" + Quantity + ", " + "units=" + units + ']';
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, units, Quantity);
    }
}
