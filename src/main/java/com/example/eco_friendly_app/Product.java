import javax.validation.constraints.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Name is required")
    private String name;

    @Positive(message = "Price must be positive")
    private double price;

    private String description;

    @PositiveOrZero(message = "Carbon emission must be non-negative")
    private double carbonEmission;

    // Getters and setters
}